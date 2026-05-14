package com.aksharadeepa.tutor.data.remote.dto

data class QuizAnswerDto(
    val questionId: String,
    val selectedOption: String,
    val isCorrect: Boolean
)

data class QuizSubmitRequest(
    val userId: String,
    val chapterId: String,
    val score: Int,
    val totalQuestions: Int,
    val answers: List<QuizAnswerDto>,
    val attemptedAt: Long
)

data class QuizSubmitResponse(
    val attemptId: String,
    val score: Int,
    val totalQuestions: Int
)

data class QuizHistoryItem(
    val attemptId: String,
    val chapterId: String,
    val score: Int,
    val totalQuestions: Int,
    val attemptedAt: Long
)

data class QuizHistoryResponse(
    val userId: String,
    val items: List<QuizHistoryItem>
)
