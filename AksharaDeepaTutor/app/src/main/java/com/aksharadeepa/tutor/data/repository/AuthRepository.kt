package com.aksharadeepa.tutor.data.repository

import com.aksharadeepa.tutor.utils.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun signUp(name: String, email: String, password: String): Result<String> {
        return runCatching {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = authResult.user ?: error("User not created")
            val profile = UserProfileChangeRequest.Builder().setDisplayName(name).build()
            user.updateProfile(profile).await()
            user.uid
        }
    }

    suspend fun login(email: String, password: String): Result<String> {
        return runCatching {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            authResult.user?.uid ?: error("Login failed")
        }
    }

    suspend fun sendPasswordReset(email: String): Result<Unit> {
        return runCatching {
            firebaseAuth.sendPasswordResetEmail(email).await()
        }
    }

    fun currentUserId(): String? = firebaseAuth.currentUser?.uid

    fun signOut() {
        firebaseAuth.signOut()
    }
}
