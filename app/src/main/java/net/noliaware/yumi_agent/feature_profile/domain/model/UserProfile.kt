package net.noliaware.yumi_agent.feature_profile.domain.model

import java.io.Serializable

data class UserProfile(
    val login: String?,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String?,
    val cellPhoneNumber: String?,
    val email: String?,
    val agentJob: String?,
    val service: String?,
    val userCount: Int,
    val retailerCount: Int,
    val partnerCount: Int,
    val contributorCount: Int,
    val messageBoxUsagePercentage: Int
) : Serializable