package net.noliaware.yumi_agent.feature_auth.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import net.noliaware.yumi_agent.feature_auth.domain.model.BOSignIn

@JsonClass(generateAdapter = true)
data class BOSignInDTO(
    @Json(name = "expiryDelayInSeconds")
    val expiryDelayInSeconds: Int,
    @Json(name = "signInCode")
    val signInCode: String
) {
    fun toBOSignIn() = BOSignIn(
        expiryDelayInSeconds = expiryDelayInSeconds,
        signInCode = signInCode
    )
}