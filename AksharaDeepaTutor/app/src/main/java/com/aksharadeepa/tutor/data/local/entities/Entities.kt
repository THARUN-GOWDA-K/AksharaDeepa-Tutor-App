package com.aksharadeepa.tutor.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

enum class Subject {
    SCIENCE, MATH, SOCIAL
}

@Entity(tableName = "chapters")
data class Chapter(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val subject: String,
    val chapterName: String,
    val importantConcepts: String = "",
    val isCompleted: Boolean = false,
    @ColumnInfo(name = "completion_date") val completionDate: String? = null,
    val orderIndex: Int
)

@Entity(
    tableName = "quiz_questions",
    foreignKeys = [
        ForeignKey(
            entity = Chapter::class,
            parentColumns = ["id"],
            childColumns = ["chapterId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class QuizQuestion(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val chapterId: Long,
    val questionText: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctOption: String,
    val explanation: String
)

@Entity(
    tableName = "quiz_attempts",
    foreignKeys = [
        ForeignKey(
            entity = Chapter::class,
            parentColumns = ["id"],
            childColumns = ["chapterId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class QuizAttempt(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val chapterId: Long,
    val score: Int,
    val totalQuestions: Int,
    val attemptedAt: Long
)

@Entity(
    tableName = "user_answers",
    foreignKeys = [
        ForeignKey(
            entity = QuizAttempt::class,
            parentColumns = ["id"],
            childColumns = ["attemptId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = QuizQuestion::class,
            parentColumns = ["id"],
            childColumns = ["questionId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserAnswer(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val attemptId: Long,
    val questionId: Long,
    val selectedOption: String,
    val isCorrect: Boolean
)

@Entity(tableName = "daily_progress")
data class DailyProgress(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val chaptersStudiedCount: Int,
    val goalTarget: Int,
    val goalMet: Boolean
)

@Entity(tableName = "streak_data")
data class StreakData(
    @PrimaryKey val id: Int = 1,
    val currentStreak: Int,
    val lastGoalMetDate: String
)
