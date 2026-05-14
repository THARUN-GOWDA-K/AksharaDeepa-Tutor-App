package com.aksharadeepa.tutor.data.remote.dto

data class ProgressUpdateRequest(
    val userId: String,
    val subject: String,
    val completedChapters: Int,
    val totalChapters: Int,
    val updatedAt: Long
)

data class ProgressResponse(
    val userId: String,
    val subject: String,
    val completedChapters: Int,
    val totalChapters: Int,
    val updatedAt: Long
)
