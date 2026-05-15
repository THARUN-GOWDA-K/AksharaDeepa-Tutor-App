package com.aksharadeepa.tutor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aksharadeepa.tutor.data.repository.AuthRepository
import com.aksharadeepa.tutor.data.repository.ChapterRepository
import com.aksharadeepa.tutor.data.repository.LocalDataRepository
import com.aksharadeepa.tutor.data.repository.ProfileRepository
import com.aksharadeepa.tutor.data.repository.QuizRepository
import com.aksharadeepa.tutor.data.local.entities.Chapter
import com.aksharadeepa.tutor.data.local.entities.QuizAttempt
import com.aksharadeepa.tutor.models.UserProfile
import com.aksharadeepa.tutor.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val localDataRepository: LocalDataRepository,
    private val chapterRepository: ChapterRepository,
    private val quizRepository: QuizRepository
) : ViewModel() {
    private val _profileState = MutableStateFlow<UiState<UserProfile>>(UiState.Idle)
    val profileState: StateFlow<UiState<UserProfile>> = _profileState

    private val chaptersFlow = chapterRepository.getAllChapters()
    private val attemptsFlow = quizRepository.getAllAttempts()

    val profileStats: StateFlow<ProfileStats> = combine(chaptersFlow, attemptsFlow) { chapters, attempts ->
        buildProfileStats(chapters, attempts)
    }.stateIn(viewModelScope, SharingStarted.Lazily, ProfileStats())

    val recentAttempts: StateFlow<List<RecentAttempt>> = combine(chaptersFlow, attemptsFlow) { chapters, attempts ->
        buildRecentAttempts(chapters, attempts)
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun loadProfile() {
        val userId = authRepository.currentUserId() ?: return
        viewModelScope.launch {
            _profileState.value = UiState.Loading
            val result = profileRepository.getProfile(userId)
            _profileState.value = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Profile load failed") }
            )
        }
    }

    fun updateProfile(profile: UserProfile) {
        viewModelScope.launch {
            _profileState.value = UiState.Loading
            val result = profileRepository.updateProfile(profile)
            _profileState.value = result.fold(
                onSuccess = { UiState.Success(profile) },
                onFailure = { UiState.Error(it.message ?: "Profile update failed") }
            )
        }
    }

    fun signOut() {
        viewModelScope.launch {
            runCatching { localDataRepository.clearUserData() }
            authRepository.signOut()
        }
    }

    private fun buildProfileStats(
        chapters: List<Chapter>,
        attempts: List<QuizAttempt>
    ): ProfileStats {
        val totalChapters = chapters.size
        val completedChapters = chapters.count { it.isCompleted }
        val completionPercent = if (totalChapters > 0) {
            (completedChapters * 100) / totalChapters
        } else {
            0
        }

        val totalQuestions = attempts.sumOf { it.totalQuestions }
        val totalScore = attempts.sumOf { it.score }
        val averageScorePercent = if (totalQuestions > 0) {
            (totalScore * 100) / totalQuestions
        } else {
            0
        }

        val chapterById = chapters.associateBy { it.id }
        val subjectScores = attempts
            .mapNotNull { attempt -> chapterById[attempt.chapterId]?.subject?.let { subject -> subject to attempt } }
            .groupBy({ it.first }, { it.second })
            .mapValues { (_, subjectAttempts) ->
                val subjectQuestions = subjectAttempts.sumOf { it.totalQuestions }
                val subjectScore = subjectAttempts.sumOf { it.score }
                if (subjectQuestions > 0) (subjectScore * 100) / subjectQuestions else 0
            }

        val strongestSubject = subjectScores.maxByOrNull { it.value }?.key
        val weakestSubject = subjectScores.minByOrNull { it.value }?.key

        return ProfileStats(
            completedChapters = completedChapters,
            totalChapters = totalChapters,
            completionPercent = completionPercent,
            quizzesTaken = attempts.size,
            averageScorePercent = averageScorePercent,
            strongestSubject = strongestSubject,
            weakestSubject = weakestSubject
        )
    }

    private fun buildRecentAttempts(
        chapters: List<Chapter>,
        attempts: List<QuizAttempt>
    ): List<RecentAttempt> {
        val chapterById = chapters.associateBy { it.id }
        return attempts
            .sortedByDescending { it.attemptedAt }
            .take(3)
            .map { attempt ->
                val percent = if (attempt.totalQuestions > 0) {
                    (attempt.score * 100) / attempt.totalQuestions
                } else {
                    0
                }
                RecentAttempt(
                    chapterName = chapterById[attempt.chapterId]?.chapterName ?: "Chapter ${attempt.chapterId}",
                    scorePercent = percent,
                    attemptedAt = attempt.attemptedAt
                )
            }
    }
}

data class ProfileStats(
    val completedChapters: Int = 0,
    val totalChapters: Int = 0,
    val completionPercent: Int = 0,
    val quizzesTaken: Int = 0,
    val averageScorePercent: Int = 0,
    val strongestSubject: String? = null,
    val weakestSubject: String? = null
)

data class RecentAttempt(
    val chapterName: String,
    val scorePercent: Int,
    val attemptedAt: Long
)
