package net.noliaware.yumi_agent.feature_profile.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import net.noliaware.yumi_agent.feature_profile.domain.model.UserProfile

@JsonClass(generateAdapter = true)
data class UserProfileDTO(
    @Json(name = "login")
    val login: String?,
    @Json(name = "firstName")
    val firstName: String,
    @Json(name = "lastName")
    val lastName: String,
    @Json(name = "phoneNumber")
    val phoneNumber: String?,
    @Json(name = "cellPhoneNumber")
    val cellPhoneNumber: String?,
    @Json(name = "email")
    val email: String?,
    @Json(name = "agentJob")
    val agentJob: String?,
    @Json(name = "organizationUnit")
    val organizationUnit: String?,
    @Json(name = "userCount")
    val userCount: Int,
    @Json(name = "retailerCount")
    val retailerCount: Int,
    @Json(name = "partnerCount")
    val partnerCount: Int,
    @Json(name = "contributorCount")
    val contributorCount: Int,
    @Json(name = "messageBoxUsagePercentage")
    val messageBoxUsagePercentage: Int
) {
    fun toUserProfile() = UserProfile(
        login = login,
        firstName = firstName,
        lastName = lastName,
        phoneNumber = phoneNumber,
        cellPhoneNumber = cellPhoneNumber,
        email = email,
        agentJob = agentJob,
        service = organizationUnit,
        userCount = userCount,
        retailerCount = retailerCount,
        partnerCount = partnerCount,
        contributorCount = contributorCount,
        messageBoxUsagePercentage = messageBoxUsagePercentage
    )
}