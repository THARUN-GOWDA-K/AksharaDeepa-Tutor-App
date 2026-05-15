package com.aksharadeepa.tutor.data.remote.dto

data class ProgressUpdateRequest(
    val userId: String,
    val subject: String,
    val completedChapters: Int,
    val totalChapters: Int,
    val updatedAt: Long,
    val completedChapterIds: List<Long> = emptyList(),
    val completionDates: Map<String, String> = emptyMap()
)

data class ProgressResponse(
    val userId: String,
    val subject: String,
    val completedChapters: Int,
    val totalChapters: Int,
    val updatedAt: Long,
    val completedChapterIds: List<Long> = emptyList(),
    val completionDates: Map<String, String> = emptyMap()
)
