package com.aksharadeepa.tutor.ui.strength

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aksharadeepa.tutor.BuildConfig
import com.aksharadeepa.tutor.data.model.Chapter
import com.aksharadeepa.tutor.data.model.QuizAttempt
import com.aksharadeepa.tutor.data.repository.ChapterRepository
import com.aksharadeepa.tutor.data.repository.QuizRepository
import com.aksharadeepa.tutor.network.AnthropicApiService
import com.aksharadeepa.tutor.network.AnthropicMessage
import com.aksharadeepa.tutor.network.AnthropicRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StrengthViewModel @Inject constructor(
    private val chapterRepository: ChapterRepository,
    private val quizRepository: QuizRepository,
    private val anthropicApi: AnthropicApiService
) : ViewModel() {

    val allChapters = chapterRepository.getAllChapters()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val allAttempts = quizRepository.getAllAttempts()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _aiResponse = MutableStateFlow<String?>(null)
    val aiResponse: StateFlow<String?> = _aiResponse

    private val _isLoadingAi = MutableStateFlow(false)
    val isLoadingAi: StateFlow<Boolean> = _isLoadingAi

    fun askAi(question: String, chapters: List<Chapter>, attempts: List<QuizAttempt>) {
        _isLoadingAi.value = true
        _aiResponse.value = null
        viewModelScope.launch {
            try {
                val strengthDataStr = StringBuilder()
                val subjects = listOf("SCIENCE", "MATH", "SOCIAL")
                for (sub in subjects) {
                    val subChapters = chapters.filter { it.subject == sub }
                    val subAttempts = attempts.filter { att -> subChapters.any { it.id == att.chapterId } }
                    val totalScore = subAttempts.sumOf { it.score }
                    val maxScore = subAttempts.sumOf { it.totalQuestions }
                    val avg = if (maxScore > 0) (totalScore * 100) / maxScore else 0
                    strengthDataStr.append("Subject $sub: Avg $avg%.\n")
                }
                
                val weakChapters = chapters.filter { ch ->
                    val lastAtt = attempts.filter { it.chapterId == ch.id }.maxByOrNull { it.attemptedAt }
                    lastAtt != null && (lastAtt.score * 100 / lastAtt.totalQuestions) < 60
                }.joinToString { it.chapterName }

                val strongChapters = chapters.filter { ch ->
                    val lastAtt = attempts.filter { it.chapterId == ch.id }.maxByOrNull { it.attemptedAt }
                    lastAtt != null && (lastAtt.score * 100 / lastAtt.totalQuestions) >= 80
                }.joinToString { it.chapterName }

                val prompt = "Student asks: '$question'.\nHere is their strength data:\n$strengthDataStr\nWeak chapters: $weakChapters\nStrong chapters: $strongChapters\nRespond as a personal study coach, keep it encouraging and actionable."

                val response = anthropicApi.getCompletion(
                    apiKey = BuildConfig.ANTHROPIC_API_KEY,
                    request = AnthropicRequest(
                        messages = listOf(AnthropicMessage(role = "user", content = prompt))
                    )
                )
                _aiResponse.value = response.content.firstOrNull()?.text ?: "No response."
            } catch (e: Exception) {
                _aiResponse.value = "Failed to connect to AI Coach."
            } finally {
                _isLoadingAi.value = false
            }
        }
    }
}
