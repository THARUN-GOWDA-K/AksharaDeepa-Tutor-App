package com.aksharadeepa.tutor.data.repository

import com.aksharadeepa.tutor.data.local.dao.ChapterDao
import com.aksharadeepa.tutor.data.local.dao.GoalDao
import com.aksharadeepa.tutor.data.local.dao.QuizDao
import com.aksharadeepa.tutor.data.local.entities.Chapter
import com.aksharadeepa.tutor.data.local.entities.DailyProgress
import com.aksharadeepa.tutor.data.local.entities.QuizAttempt
import com.aksharadeepa.tutor.data.local.entities.UserAnswer
import com.aksharadeepa.tutor.data.local.entities.StreakData
import javax.inject.Inject
import javax.inject.Singleton

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
