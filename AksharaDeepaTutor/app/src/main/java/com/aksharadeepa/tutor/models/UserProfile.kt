package com.aksharadeepa.tutor.models

data class UserProfile(
    val userId: String,
    val displayName: String,
    val email: String,
    val photoUrl: String? = null,
    val grade: String = "10",
    val school: String? = null,
    val streakCount: Int = 0
)
