package net.noliaware.yumi_agent.feature_auth.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdatePrivacyPolicyResponseDTO(
    @Json(name = "result")
    val result: Int
)