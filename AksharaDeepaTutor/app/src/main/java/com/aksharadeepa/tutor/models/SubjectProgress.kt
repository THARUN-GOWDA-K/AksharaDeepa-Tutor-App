package com.aksharadeepa.tutor.models

data class SubjectProgress(
    val subject: String,
    val completedChapters: Int,
    val totalChapters: Int,
    val quizScorePercent: Int
)
