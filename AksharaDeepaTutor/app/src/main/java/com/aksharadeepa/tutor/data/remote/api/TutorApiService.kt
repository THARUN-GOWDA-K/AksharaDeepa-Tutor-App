package com.aksharadeepa.tutor.data.remote.api

import com.aksharadeepa.tutor.data.remote.dto.AiTipRequest
import com.aksharadeepa.tutor.data.remote.dto.AiTipResponse
import com.aksharadeepa.tutor.data.remote.dto.AuthResponse
import com.aksharadeepa.tutor.data.remote.dto.ForgotPasswordRequest
import com.aksharadeepa.tutor.data.remote.dto.LoginRequest
import com.aksharadeepa.tutor.data.remote.dto.ProgressResponse
import com.aksharadeepa.tutor.data.remote.dto.ProgressUpdateRequest
import com.aksharadeepa.tutor.data.remote.dto.ProfileResponse
import com.aksharadeepa.tutor.data.remote.dto.ProfileUpdateRequest
import com.aksharadeepa.tutor.data.remote.dto.QuizHistoryResponse
import com.aksharadeepa.tutor.data.remote.dto.QuizSubmitRequest
import com.aksharadeepa.tutor.data.remote.dto.QuizSubmitResponse
import com.aksharadeepa.tutor.data.remote.dto.SignupRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface TutorApiService {
    @POST("signup")
    suspend fun signup(@Body request: SignupRequest): AuthResponse

    @POST("login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest)

    @GET("questions")
    suspend fun getQuestions(@Query("chapter_id") chapterId: String)

    @POST("submit-quiz")
    suspend fun submitQuiz(@Body request: QuizSubmitRequest): QuizSubmitResponse

    @GET("quiz-history")
    suspend fun getQuizHistory(@Query("user_id") userId: String): QuizHistoryResponse

    @GET("progress")
    suspend fun getProgress(
        @Query("user_id") userId: String,
        @Query("subject") subject: String
    ): ProgressResponse

    @POST("update-progress")
    suspend fun updateProgress(@Body request: ProgressUpdateRequest): ProgressResponse

    @POST("ai-study-tip")
    suspend fun getAiStudyTip(@Body request: AiTipRequest): AiTipResponse

    @GET("profile")
    suspend fun getProfile(@Query("user_id") userId: String): ProfileResponse

    @PUT("profile")
    suspend fun updateProfile(
        @Query("user_id") userId: String,
        @Body request: ProfileUpdateRequest
    ): ProfileResponse
}
