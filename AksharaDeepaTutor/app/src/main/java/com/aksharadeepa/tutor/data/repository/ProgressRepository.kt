package com.aksharadeepa.tutor.data.repository

import com.aksharadeepa.tutor.data.remote.api.TutorApiService
import com.aksharadeepa.tutor.data.remote.dto.ProgressResponse
import com.aksharadeepa.tutor.data.remote.dto.ProgressUpdateRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProgressRepository @Inject constructor(
    private val apiService: TutorApiService
) {
    suspend fun syncProgress(request: ProgressUpdateRequest): Result<ProgressResponse> {
        return runCatching {
            apiService.updateProgress(request)
        }
    }
}
