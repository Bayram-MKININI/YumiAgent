package net.noliaware.yumi_agent.feature_login.domain.model

import java.io.Serializable

data class AccountData(
    val helloMessage: String = "",
    val userName: String = "",
    val lastConnectionTimestamp: Long = 0,
    val newAlertCount: Int = 0,
    val newMessageCount: Int = 0,
    val domainName: String?
) : Serializable