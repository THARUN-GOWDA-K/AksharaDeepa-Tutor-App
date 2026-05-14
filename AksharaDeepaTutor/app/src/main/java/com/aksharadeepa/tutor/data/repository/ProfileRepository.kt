package com.aksharadeepa.tutor.data.repository

import com.aksharadeepa.tutor.models.UserProfile
import com.aksharadeepa.tutor.utils.await
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun getProfile(userId: String): Result<UserProfile> {
        return runCatching {
            val doc = firestore.collection("users").document(userId).get().await()
            val data = doc.data.orEmpty()
            UserProfile(
                userId = userId,
                displayName = data["display_name"] as? String ?: "",
                email = data["email"] as? String ?: "",
                photoUrl = data["photo_url"] as? String,
                grade = data["grade"] as? String ?: "10",
                school = data["school"] as? String,
                streakCount = (data["streak_count"] as? Number)?.toInt() ?: 0
            )
        }.recover {
            UserProfile(
                userId = userId,
                displayName = "Student",
                email = "",
                photoUrl = null,
                grade = "10",
                school = "",
                streakCount = 0
            )
        }
    }

    suspend fun updateProfile(profile: UserProfile): Result<Unit> {
        return runCatching {
            val payload = mapOf(
                "display_name" to profile.displayName,
                "email" to profile.email,
                "photo_url" to profile.photoUrl,
                "grade" to profile.grade,
                "school" to profile.school,
                "streak_count" to profile.streakCount
            )
            firestore.collection("users").document(profile.userId).set(payload).await()
        }
    }
}
