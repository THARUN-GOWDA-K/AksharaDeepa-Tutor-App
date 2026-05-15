package com.aksharadeepa.tutor.network

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

data class AnthropicMessage(
    val role: String,
    val content: String
)

data class AnthropicRequest(
    val model: String = "claude-3-haiku-20240307",
    val maxTokens: Int = 1024,
    val messages: List<AnthropicMessage>
)

data class AnthropicContent(
    val type: String,
    val text: String
)

data class AnthropicResponse(
    val id: String,
    val type: String,
    val role: String,
    val content: List<AnthropicContent>
)

interface AnthropicApiService {
    @Headers(
        "anthropic-version: 2023-06-01",
        "content-type: application/json"
    )
    @POST("v1/messages")
    suspend fun getCompletion(
        @Header("x-api-key") apiKey: String,
        @Body request: AnthropicRequest
    ): AnthropicResponse
}
