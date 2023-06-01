package net.noliaware.yumi_agent.feature_login.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import net.noliaware.yumi_agent.feature_login.domain.model.AccountData

@JsonClass(generateAdapter = true)
data class AccountDataDTO(
    @Json(name = "welcomeMessage")
    val helloMessage: String = "",
    @Json(name = "welcomeUser")
    val userName: String = "",
    @Json(name = "lastConnectionTimestamp")
    val lastConnectionTimestamp: Long = 0,
    @Json(name = "encryptionVector")
    val encryptionVector: String = "",
    @Json(name = "newAlertCount")
    val newAlertCount: Int = 0,
    @Json(name = "newMessageCount")
    val newMessageCount: Int = 0
) {
    fun toAccountData() = AccountData(
        helloMessage = helloMessage,
        userName = userName,
        lastConnectionTimestamp = lastConnectionTimestamp,
        newAlertCount = newAlertCount,
        newMessageCount = newMessageCount
    )
}