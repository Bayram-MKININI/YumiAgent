package net.noliaware.yumi_agent.feature_auth.domain.model

data class BOSignIn(
    val expiryDelayInSeconds: Int,
    val signInCode: String
)