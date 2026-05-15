package com.aksharadeepa.tutor.ui.quiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aksharadeepa.tutor.data.local.entities.*
import com.aksharadeepa.tutor.data.repository.AiRepository
import com.aksharadeepa.tutor.data.repository.AuthRepository
import com.aksharadeepa.tutor.data.repository.ChapterRepository
import com.aksharadeepa.tutor.data.repository.QuizRepository
import com.aksharadeepa.tutor.data.repository.QuizRemoteRepository
import com.aksharadeepa.tutor.data.remote.dto.QuizAnswerDto
import com.aksharadeepa.tutor.data.remote.dto.QuizSubmitRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val chapterRepository: ChapterRepository,
    private val quizRepository: QuizRepository,
    private val aiRepository: AiRepository,
    private val authRepository: AuthRepository,
    private val quizRemoteRepository: QuizRemoteRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val allChapters = chapterRepository.getAllChapters()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val allAttempts = quizRepository.getAllAttempts()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _currentChapter = MutableStateFlow<Chapter?>(null)
    val currentChapter: StateFlow<Chapter?> = _currentChapter

    private val _questions = MutableStateFlow<List<QuizQuestion>>(emptyList())
    val questions: StateFlow<List<QuizQuestion>> = _questions

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex

    private val _timer = MutableStateFlow(30)
    val timer: StateFlow<Int> = _timer

    private val _userAnswers = MutableStateFlow<List<UserAnswer>>(emptyList())
    val userAnswers: StateFlow<List<UserAnswer>> = _userAnswers

    private val _quizState = MutableStateFlow<QuizState>(QuizState.NotStarted)
    val quizState: StateFlow<QuizState> = _quizState

    private val _aiTipsUiState = MutableStateFlow<AiTipsUiState>(AiTipsUiState.Idle)
    val aiTipsUiState: StateFlow<AiTipsUiState> = _aiTipsUiState

    private val _uiEvents = MutableSharedFlow<String>()
    val uiEvents = _uiEvents.asSharedFlow()

    init {
        val chapterIdArg = savedStateHandle.get<Long?>("chapterId")
        if (chapterIdArg != null && chapterIdArg <= 0L) {
            viewModelScope.launch {
                _uiEvents.emit("Invalid chapter. Please try again.")
            }
        }
    }

    suspend fun startQuiz(chapter: Chapter): Boolean {
        if (chapter.id <= 0L) {
            _uiEvents.emit("Invalid chapter. Please try again.")
            return false
        }

        val allQuestions = quizRepository.getQuestionsForChapter(chapter.id)
        if (allQuestions.isEmpty()) {
            _quizState.value = QuizState.NotStarted
            _uiEvents.emit("No questions available for this chapter yet.")
            return false
        }

        _currentChapter.value = chapter
        val seed = dailySeed(chapter.id)
        _questions.value = allQuestions.shuffled(Random(seed)).take(5)
        _currentQuestionIndex.value = 0
        _userAnswers.value = emptyList()
        _quizState.value = QuizState.InProgress
        _aiTipsUiState.value = AiTipsUiState.Idle
        startTimer()
        return true
    }

    private fun dailySeed(chapterId: Long): Int {
        val calendar = Calendar.getInstance()
        val daySeed = calendar.get(Calendar.YEAR) * 1000 + calendar.get(Calendar.DAY_OF_YEAR)
        return (chapterId * 1000L + daySeed).toInt()
    }

    private fun startTimer() {
        viewModelScope.launch {
            if (_questions.value.isEmpty()) return@launch
            _timer.value = 30
            while (_timer.value > 0 && _quizState.value == QuizState.InProgress) {
                delay(1000)
                if (_quizState.value == QuizState.InProgress) {
                    _timer.value -= 1
                }
            }
            if (_timer.value == 0 && _quizState.value == QuizState.InProgress) {
                submitAnswer("")
            }
        }
    }

    fun submitAnswer(selectedOption: String) {
        if (_quizState.value != QuizState.InProgress) return
        val questionIndex = _currentQuestionIndex.value
        if (questionIndex !in _questions.value.indices) return
        val currentQ = _questions.value[questionIndex]
        val isCorrect = selectedOption == currentQ.correctOption
        
        val newAnswers = _userAnswers.value.toMutableList()
        newAnswers.add(UserAnswer(
            attemptId = 0,
            questionId = currentQ.id,
            selectedOption = selectedOption,
            isCorrect = isCorrect
        ))
        _userAnswers.value = newAnswers
        _quizState.value = QuizState.Answered(isCorrect)
    }

    fun nextQuestion() {
        if (_currentQuestionIndex.value < _questions.value.size - 1) {
            _currentQuestionIndex.value += 1
            _quizState.value = QuizState.InProgress
            startTimer()
        } else {
            finishQuiz()
        }
    }

    private fun finishQuiz() {
        _quizState.value = QuizState.Finished
        viewModelScope.launch {
            val chapter = _currentChapter.value ?: return@launch
            if (_questions.value.isEmpty()) return@launch
            val score = _userAnswers.value.count { it.isCorrect }
            val attempt = QuizAttempt(
                chapterId = chapter.id,
                score = score,
                totalQuestions = _questions.value.size,
                attemptedAt = System.currentTimeMillis()
            )
            quizRepository.saveQuizAttempt(attempt, _userAnswers.value)
            val userId = authRepository.currentUserId()
            if (userId != null) {
                val answers = _userAnswers.value.map { answer ->
                    QuizAnswerDto(
                        questionId = answer.questionId.toString(),
                        selectedOption = answer.selectedOption,
                        isCorrect = answer.isCorrect
                    )
                }
                val request = QuizSubmitRequest(
                    userId = userId,
                    chapterId = chapter.id.toString(),
                    score = score,
                    totalQuestions = _questions.value.size,
                    answers = answers,
                    attemptedAt = attempt.attemptedAt
                )
                quizRemoteRepository.submitQuiz(request)
            }
        }
    }

    fun getAiStudyTips() {
        val chapter = _currentChapter.value ?: return
        if (_aiTipsUiState.value is AiTipsUiState.Loading) return
        val wrongAnswers = _userAnswers.value.filter { !it.isCorrect }
        val score = _userAnswers.value.count { it.isCorrect }
        val wrongQuestionsText = wrongAnswers.mapNotNull { ans ->
            _questions.value.find { it.id == ans.questionId }?.questionText
        }.joinToString(", ")

        viewModelScope.launch {
            _aiTipsUiState.value = AiTipsUiState.Loading
            val prompt = "The student scored $score/${_questions.value.size} on the chapter '${chapter.chapterName}' (${chapter.subject}). The questions they got wrong were: $wrongQuestionsText. Give 3 short, friendly, encouraging study tips tailored to these specific weak areas. Keep each tip under 2 sentences."

            val result = aiRepository.getAiTips(prompt, topic = chapter.chapterName)
            _aiTipsUiState.value = result.fold(
                onSuccess = { response ->
                    val tipsText = if (response.fallback) {
                        buildOfflineTips(score, _questions.value.size, wrongQuestionsText, chapter.chapterName)
                    } else {
                        formatTips(response.tips)
                    }
                    AiTipsUiState.Success(tipsText)
                },
                onFailure = {
                    _uiEvents.emit("Using offline tips. Connect to the internet for AI tips.")
                    AiTipsUiState.Success(buildOfflineTips(score, _questions.value.size, wrongQuestionsText, chapter.chapterName))
                }
            )
        }
    }

    private fun formatTips(tips: List<String>): String {
        return tips.joinToString("\n") { "• $it" }
    }

    private fun buildOfflineTips(score: Int, total: Int, wrongQuestionsText: String, chapterName: String): String {
        val accuracy = if (total > 0) (score * 100 / total) else 0
        val focusText = if (wrongQuestionsText.isNotBlank()) {
            val preview = if (wrongQuestionsText.length > 120) {
                wrongQuestionsText.take(120) + "..."
            } else {
                wrongQuestionsText
            }
            "Revisit the missed questions: $preview."
        } else {
            "Revisit the tricky parts of $chapterName and write a short summary."
        }
        val tips = listOf(
            "Accuracy: $accuracy%. Review $chapterName for 15 minutes, then try similar problems.",
            focusText,
            "Practice 5 new questions and explain each answer in your own words."
        )
        return formatTips(tips)
    }
}

sealed class QuizState {
    object NotStarted : QuizState()
    object InProgress : QuizState()
    data class Answered(val isCorrect: Boolean) : QuizState()
    object Finished : QuizState()
}

sealed class AiTipsUiState {
    object Idle : AiTipsUiState()
    object Loading : AiTipsUiState()
    data class Success(val tips: String) : AiTipsUiState()
    data class Error(val message: String) : AiTipsUiState()
}
