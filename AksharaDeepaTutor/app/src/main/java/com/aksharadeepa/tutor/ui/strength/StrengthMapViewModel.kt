package com.aksharadeepa.tutor.ui.strength

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aksharadeepa.tutor.data.local.entities.Chapter
import com.aksharadeepa.tutor.data.local.entities.QuizAttempt
import com.aksharadeepa.tutor.data.repository.AiRepository
import com.aksharadeepa.tutor.data.repository.ChapterRepository
import com.aksharadeepa.tutor.data.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class StrengthMapViewModel @Inject constructor(
    private val chapterRepository: ChapterRepository,
    private val quizRepository: QuizRepository,
    private val aiRepository: AiRepository
) : ViewModel() {

    private val subjects = listOf("SCIENCE", "MATH", "SOCIAL")

    val allChapters = chapterRepository.getAllChapters()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val allAttempts = quizRepository.getAllAttempts()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val strengthUiState: StateFlow<StrengthUiState> = combine(allChapters, allAttempts) { chapters, attempts ->
        val subjectStrengths = subjects.map { subject ->
            buildSubjectStrength(subject, chapters, attempts)
        }
        StrengthUiState(subjects = subjectStrengths)
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        StrengthUiState.empty()
    )

    private val _aiResponse = MutableStateFlow<String?>(null)
    val aiResponse: StateFlow<String?> = _aiResponse

    private val _isLoadingAi = MutableStateFlow(false)
    val isLoadingAi: StateFlow<Boolean> = _isLoadingAi

    fun askAi(question: String) {
        if (question.isBlank()) {
            _aiResponse.value = "Ask a question to get personalized advice."
            return
        }

        _isLoadingAi.value = true
        _aiResponse.value = null

        viewModelScope.launch {
            val strengthDataStr = buildStrengthPrompt(strengthUiState.value.subjects)
            val weakChapters = findWeakChapters(allChapters.value, allAttempts.value)
            val strongChapters = findStrongChapters(allChapters.value, allAttempts.value)

            val prompt = "Student asks: '$question'.\nHere is their strength data:\n$strengthDataStr\nWeak chapters: $weakChapters\nStrong chapters: $strongChapters\nRespond as a personal study coach, keep it encouraging and actionable."

            val result = aiRepository.getAiTips(prompt, topic = "Strength Map")
            _aiResponse.value = result.fold(
                onSuccess = { response ->
                    if (response.fallback) {
                        buildOfflineCoachTips(question, weakChapters, strongChapters)
                    } else {
                        response.tips.joinToString("\n")
                    }
                },
                onFailure = { buildOfflineCoachTips(question, weakChapters, strongChapters) }
            )
            _isLoadingAi.value = false
        }
    }

    private fun buildOfflineCoachTips(question: String, weakChapters: String, strongChapters: String): String {
        val weakTip = if (weakChapters != "None") {
            "Focus first on: $weakChapters."
        } else {
            "Pick one topic you find hardest and start there."
        }
        val strongTip = if (strongChapters != "None") {
            "Use $strongChapters to build confidence, then switch back to weak areas."
        } else {
            "After practice, quiz yourself to confirm improvement."
        }
        return listOf(
            "Plan two short sessions (15-20 min) and end with a 5-question quiz.",
            weakTip,
            strongTip,
            "Question: $question"
        ).joinToString("\n")
    }

    private fun buildSubjectStrength(
        subject: String,
        chapters: List<Chapter>,
        attempts: List<QuizAttempt>
    ): SubjectStrength {
        val subChapters = chapters.filter { it.subject == subject }
        val completedCount = subChapters.count { it.isCompleted }
        val completionPercent = if (subChapters.isNotEmpty()) {
            ((completedCount.toFloat() / subChapters.size) * 100f).roundToInt()
        } else {
            0
        }

        val subAttempts = attempts.filter { att -> subChapters.any { it.id == att.chapterId } }
        val totalScore = subAttempts.sumOf { it.score }
        val maxScore = subAttempts.sumOf { it.totalQuestions }
        val quizPercent = if (maxScore > 0) {
            ((totalScore.toFloat() / maxScore) * 100f).roundToInt()
        } else {
            0
        }

        return SubjectStrength(
            subject = subject,
            quizPercent = quizPercent,
            completionPercent = completionPercent,
            hasAttempts = maxScore > 0
        )
    }

    private fun buildStrengthPrompt(subjects: List<SubjectStrength>): String {
        return subjects.joinToString(separator = "\n") { strength ->
            val attemptLabel = if (strength.hasAttempts) "Avg ${strength.quizPercent}%" else "Not attempted"
            "Subject ${strength.subject}: $attemptLabel, Coverage ${strength.completionPercent}%."
        }
    }

    private fun findWeakChapters(chapters: List<Chapter>, attempts: List<QuizAttempt>): String {
        val weak = chapters.filter { ch ->
            val lastAtt = attempts.filter { it.chapterId == ch.id }.maxByOrNull { it.attemptedAt }
            lastAtt != null && lastAtt.totalQuestions > 0 &&
                (lastAtt.score * 100 / lastAtt.totalQuestions) < 60
        }
        return weak.joinToString { it.chapterName }.ifEmpty { "None" }
    }

    private fun findStrongChapters(chapters: List<Chapter>, attempts: List<QuizAttempt>): String {
        val strong = chapters.filter { ch ->
            val lastAtt = attempts.filter { it.chapterId == ch.id }.maxByOrNull { it.attemptedAt }
            lastAtt != null && lastAtt.totalQuestions > 0 &&
                (lastAtt.score * 100 / lastAtt.totalQuestions) >= 80
        }
        return strong.joinToString { it.chapterName }.ifEmpty { "None" }
    }
}

data class StrengthUiState(
    val subjects: List<SubjectStrength>
) {
    val performanceScores: List<Float> = subjects.map {
        val score = if (it.hasAttempts) it.quizPercent else it.completionPercent
        score / 100f
    }

    companion object {
        fun empty(): StrengthUiState = StrengthUiState(
            subjects = listOf(
                SubjectStrength("SCIENCE", 0, 0, false),
                SubjectStrength("MATH", 0, 0, false),
                SubjectStrength("SOCIAL", 0, 0, false)
            )
        )
    }
}

data class SubjectStrength(
    val subject: String,
    val quizPercent: Int,
    val completionPercent: Int,
    val hasAttempts: Boolean
)
