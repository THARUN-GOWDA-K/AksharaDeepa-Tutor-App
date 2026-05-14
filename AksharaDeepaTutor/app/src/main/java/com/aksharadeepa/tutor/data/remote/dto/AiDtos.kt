package com.aksharadeepa.tutor.data.remote.dto

data class AiTipRequest(
    val prompt: String,
    val userId: String? = null,
    val topic: String? = null
)

data class AiTipResponse(
    val tips: List<String>,
    val fallback: Boolean = false
)
