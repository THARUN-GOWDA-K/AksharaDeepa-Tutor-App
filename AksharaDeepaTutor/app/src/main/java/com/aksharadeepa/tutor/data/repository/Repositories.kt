package com.aksharadeepa.tutor.data.repository

import com.aksharadeepa.tutor.data.local.dao.ChapterDao
import com.aksharadeepa.tutor.data.local.dao.GoalDao
import com.aksharadeepa.tutor.data.local.dao.QuizDao
import com.aksharadeepa.tutor.data.local.entities.Chapter
import com.aksharadeepa.tutor.data.local.entities.DailyProgress
import com.aksharadeepa.tutor.data.local.entities.QuizAttempt
import com.aksharadeepa.tutor.data.local.entities.UserAnswer
import com.aksharadeepa.tutor.data.local.entities.StreakData
import com.aksharadeepa.tutor.data.remote.api.TutorApiService
import com.aksharadeepa.tutor.data.remote.dto.QuizHistoryResponse
import com.aksharadeepa.tutor.data.remote.dto.QuizSubmitRequest
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first
import android.util.Log

@Singleton
class ChapterRepository @Inject constructor(
    private val chapterDao: ChapterDao
) {
    fun getChaptersBySubject(subject: String) = chapterDao.getChaptersBySubject(subject)
    fun getAllChapters() = chapterDao.getAllChapters()
    fun getChaptersCompletedOnDate(date: String) = chapterDao.getChaptersCompletedOnDate(date)
    suspend fun updateChapter(chapter: Chapter) = chapterDao.updateChapter(chapter)
}

@Singleton
class QuizRepository @Inject constructor(
    private val quizDao: QuizDao
) {
    suspend fun getQuestionsForChapter(chapterId: Long) = quizDao.getQuestionsForChapter(chapterId)
    suspend fun saveQuizAttempt(attempt: QuizAttempt, answers: List<UserAnswer>) {
        val attemptId = quizDao.insertAttempt(attempt)
        quizDao.insertUserAnswers(answers.map { it.copy(attemptId = attemptId) })
    }
    fun getLastAttemptForChapter(chapterId: Long) = quizDao.getLastAttemptForChapter(chapterId)
    fun getAllAttempts() = quizDao.getAllAttempts()
}

@Singleton
class GoalRepository @Inject constructor(
    private val goalDao: GoalDao
) {
    fun getDailyProgress(date: String) = goalDao.getDailyProgress(date)
    suspend fun updateDailyProgress(progress: DailyProgress) = goalDao.insertOrUpdateDailyProgress(progress)
    fun getStreakData() = goalDao.getStreakData()
    suspend fun updateStreakData(streakData: StreakData) = goalDao.updateStreakData(streakData)
}

@Singleton
class LocalDataRepository @Inject constructor(
    private val chapterDao: ChapterDao,
    private val quizDao: QuizDao,
    private val goalDao: GoalDao
) {
    suspend fun clearUserData() {
        quizDao.clearUserAnswers()
        quizDao.clearQuizAttempts()
        chapterDao.resetChapterCompletion()
        goalDao.clearDailyProgress()
        goalDao.clearStreakData()
    }
}

@Singleton
class QuizRemoteRepository @Inject constructor(
    private val apiService: TutorApiService
) {
    suspend fun submitQuiz(request: QuizSubmitRequest): Result<Unit> {
        return runCatching { apiService.submitQuiz(request) }
    }

    suspend fun getQuizHistory(userId: String): Result<QuizHistoryResponse> {
        return runCatching { apiService.getQuizHistory(userId) }
    }
}

@Singleton
class UserSyncRepository @Inject constructor(
    private val apiService: TutorApiService,
    private val chapterDao: ChapterDao,
    private val quizDao: QuizDao,
    private val goalDao: GoalDao
) {
    suspend fun syncFromBackend(userId: String) {
        quizDao.clearUserAnswers()
        quizDao.clearQuizAttempts()
        chapterDao.resetChapterCompletion()

        val subjects = listOf("SCIENCE", "MATH", "SOCIAL")
        val completedIds = mutableSetOf<Long>()
        val completionDates = mutableMapOf<String, String>()

        subjects.forEach { subject ->
            runCatching { apiService.getProgress(userId, subject) }
                .onFailure { Log.e("UserSync", "Error fetching progress for $subject", it) }
                .getOrNull()?.let { progress ->
                completedIds.addAll(progress.completedChapterIds)
                completionDates.putAll(progress.completionDates)
            }
        }

        val chapters = runCatching { chapterDao.getAllChapters().first() }.getOrNull().orEmpty()
        chapters.forEach { chapter ->
            val isCompleted = completedIds.contains(chapter.id)
            val completionDate = completionDates[chapter.id.toString()]
            if (chapter.isCompleted != isCompleted || chapter.completionDate != completionDate) {
                chapterDao.updateChapter(
                    chapter.copy(isCompleted = isCompleted, completionDate = completionDate)
                )
            }
        }

        runCatching { apiService.getQuizHistory(userId) }
            .onFailure { Log.e("UserSync", "Error fetching quiz history", it) }
            .getOrNull()?.let { history ->
            val attempts = history.items.mapNotNull { item ->
                val chapterId = item.chapterId.toLongOrNull() ?: return@mapNotNull null
                QuizAttempt(
                    chapterId = chapterId,
                    score = item.score,
                    totalQuestions = item.totalQuestions,
                    attemptedAt = item.attemptedAt
                )
            }
            if (attempts.isNotEmpty()) {
                quizDao.insertAttempts(attempts)
            }
        }
    }
}
