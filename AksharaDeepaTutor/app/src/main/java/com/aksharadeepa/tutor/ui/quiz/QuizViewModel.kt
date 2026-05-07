package com.aksharadeepa.tutor.ui.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aksharadeepa.tutor.BuildConfig
import com.aksharadeepa.tutor.data.model.*
import com.aksharadeepa.tutor.data.repository.ChapterRepository
import com.aksharadeepa.tutor.data.repository.QuizRepository
import com.aksharadeepa.tutor.network.AnthropicApiService
import com.aksharadeepa.tutor.network.AnthropicMessage
import com.aksharadeepa.tutor.network.AnthropicRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.util.Calendar
import kotlin.random.Random

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val chapterRepository: ChapterRepository,
    private val quizRepository: QuizRepository,
    private val anthropicApi: AnthropicApiService
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

    private val _aiTips = MutableStateFlow<String?>(null)
    val aiTips: StateFlow<String?> = _aiTips

    private val _isLoadingAi = MutableStateFlow(false)
    val isLoadingAi: StateFlow<Boolean> = _isLoadingAi

    fun startQuiz(chapter: Chapter) {
        viewModelScope.launch {
            _currentChapter.value = chapter
            val allQuestions = quizRepository.getQuestionsForChapter(chapter.id)
            val seed = dailySeed(chapter.id)
            _questions.value = allQuestions.shuffled(Random(seed)).take(5)
            _currentQuestionIndex.value = 0
            _userAnswers.value = emptyList()
            _quizState.value = QuizState.InProgress
            _aiTips.value = null
            startTimer()
        }
    }

    private fun dailySeed(chapterId: Long): Int {
        val calendar = Calendar.getInstance()
        val daySeed = calendar.get(Calendar.YEAR) * 1000 + calendar.get(Calendar.DAY_OF_YEAR)
        return (chapterId * 1000L + daySeed).toInt()
    }

    private fun startTimer() {
        viewModelScope.launch {
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
        val currentQ = _questions.value[_currentQuestionIndex.value]
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
            val score = _userAnswers.value.count { it.isCorrect }
            val attempt = QuizAttempt(
                chapterId = _currentChapter.value!!.id,
                score = score,
                totalQuestions = _questions.value.size,
                attemptedAt = System.currentTimeMillis()
            )
            quizRepository.saveQuizAttempt(attempt, _userAnswers.value)
        }
    }

    fun getAiStudyTips() {
        val chapter = _currentChapter.value ?: return
        val wrongAnswers = _userAnswers.value.filter { !it.isCorrect }
        val score = _userAnswers.value.count { it.isCorrect }
        val wrongQuestionsText = wrongAnswers.mapNotNull { ans ->
            _questions.value.find { it.id == ans.questionId }?.questionText
        }.joinToString(", ")

        _isLoadingAi.value = true
        viewModelScope.launch {
            try {
                val prompt = "The student scored $score/${_questions.value.size} on the chapter '${chapter.chapterName}' (${chapter.subject}). The questions they got wrong were: $wrongQuestionsText. Give 3 short, friendly, encouraging study tips tailored to these specific weak areas. Keep each tip under 2 sentences."

                val apiKey = BuildConfig.ANTHROPIC_API_KEY.trim()
                if (apiKey.isEmpty()) {
                    delay(1000)
                    _aiTips.value = "Here are some tips based on your performance:\n1. Review the key concepts for this chapter.\n2. Practice more problems on the topics you missed.\n3. Take short breaks to retain information better! You've got this!"
                } else {
                    val response = anthropicApi.getCompletion(
                        apiKey = apiKey,
                        request = AnthropicRequest(
                            messages = listOf(AnthropicMessage(role = "user", content = prompt))
                        )
                    )
                    _aiTips.value = response.content.firstOrNull()?.text ?: "No tips available."
                }
            } catch (e: Exception) {
                _aiTips.value = "Failed to load AI tips. Please check your internet connection."
            } finally {
                _isLoadingAi.value = false
            }
        }
    }
}

sealed class QuizState {
    object NotStarted : QuizState()
    object InProgress : QuizState()
    data class Answered(val isCorrect: Boolean) : QuizState()
    object Finished : QuizState()
}
