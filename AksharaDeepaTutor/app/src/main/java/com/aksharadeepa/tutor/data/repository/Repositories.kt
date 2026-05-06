package com.aksharadeepa.tutor.data.repository

import com.aksharadeepa.tutor.data.database.ChapterDao
import com.aksharadeepa.tutor.data.database.GoalDao
import com.aksharadeepa.tutor.data.database.QuizDao
import com.aksharadeepa.tutor.data.model.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChapterRepository @Inject constructor(
    private val chapterDao: ChapterDao
) {
    fun getChaptersBySubject(subject: String) = chapterDao.getChaptersBySubject(subject)
    fun getAllChapters() = chapterDao.getAllChapters()
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
