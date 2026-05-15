package com.aksharadeepa.tutor.data.repository

import com.aksharadeepa.tutor.data.remote.api.TutorApiService
import com.aksharadeepa.tutor.data.remote.dto.AiTipRequest
import com.aksharadeepa.tutor.data.remote.dto.AiTipResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiRepository @Inject constructor(
    private val apiService: TutorApiService
) {
    suspend fun getAiTips(prompt: String, userId: String? = null, topic: String? = null): Result<AiTipResponse> {
        return runCatching {
            apiService.getAiStudyTip(AiTipRequest(prompt = prompt, userId = userId, topic = topic))
        }
    }
}
