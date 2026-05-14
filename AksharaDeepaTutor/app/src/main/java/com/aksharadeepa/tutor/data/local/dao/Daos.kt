package com.aksharadeepa.tutor.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aksharadeepa.tutor.data.local.entities.Chapter
import com.aksharadeepa.tutor.data.local.entities.DailyProgress
import com.aksharadeepa.tutor.data.local.entities.QuizAttempt
import com.aksharadeepa.tutor.data.local.entities.QuizQuestion
import com.aksharadeepa.tutor.data.local.entities.StreakData
import com.aksharadeepa.tutor.data.local.entities.UserAnswer
import kotlinx.coroutines.flow.Flow

@Dao
interface ChapterDao {
    @Query("SELECT * FROM chapters WHERE subject = :subject ORDER BY orderIndex ASC")
    fun getChaptersBySubject(subject: String): Flow<List<Chapter>>

    @Query("SELECT * FROM chapters")
    fun getAllChapters(): Flow<List<Chapter>>

    @Query("SELECT * FROM chapters WHERE isCompleted = 1 AND completion_date = :date")
    fun getChaptersCompletedOnDate(date: String): Flow<List<Chapter>>

    @Update
    suspend fun updateChapter(chapter: Chapter)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapters(chapters: List<Chapter>)

    @Query("SELECT COUNT(*) FROM chapters")
    suspend fun getChapterCount(): Int
}

@Dao
interface QuizDao {
    @Query("SELECT * FROM quiz_questions WHERE chapterId = :chapterId")
    suspend fun getQuestionsForChapter(chapterId: Long): List<QuizQuestion>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuizQuestion>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttempt(attempt: QuizAttempt): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserAnswers(answers: List<UserAnswer>)

    @Query("SELECT * FROM quiz_attempts WHERE chapterId = :chapterId ORDER BY attemptedAt DESC LIMIT 1")
    fun getLastAttemptForChapter(chapterId: Long): Flow<QuizAttempt?>

    @Query("SELECT * FROM quiz_attempts")
    fun getAllAttempts(): Flow<List<QuizAttempt>>
}

@Dao
interface GoalDao {
    @Query("SELECT * FROM daily_progress WHERE date = :date")
    fun getDailyProgress(date: String): Flow<DailyProgress?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateDailyProgress(progress: DailyProgress)

    @Query("SELECT * FROM streak_data WHERE id = 1")
    fun getStreakData(): Flow<StreakData?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateStreakData(streakData: StreakData)
}
