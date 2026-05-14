package com.aksharadeepa.tutor.data.remote.dto

data class ProfileResponse(
    val userId: String,
    val displayName: String,
    val email: String,
    val photoUrl: String? = null,
    val grade: String = "10",
    val school: String? = null,
    val streakCount: Int = 0
)

data class ProfileUpdateRequest(
    val displayName: String? = null,
    val photoUrl: String? = null,
    val school: String? = null,
    val grade: String? = null
)
