package net.noliaware.yumi_agent.feature_message.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SentMessageDTO(
    @Json(name = "sentMessageId")
    val messageId: String
)