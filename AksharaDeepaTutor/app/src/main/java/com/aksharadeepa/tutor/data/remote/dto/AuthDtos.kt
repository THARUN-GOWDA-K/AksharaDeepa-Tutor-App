package com.aksharadeepa.tutor.data.remote.dto

data class SignupRequest(
    val email: String,
    val password: String,
    val displayName: String
)

data class LoginRequest(
    val idToken: String
)

data class ForgotPasswordRequest(
    val email: String
)

data class AuthResponse(
    val uid: String,
    val email: String,
    val displayName: String
)
