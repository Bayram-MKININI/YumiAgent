package net.noliaware.yumi_agent.feature_auth.data.repository

import kotlinx.coroutines.flow.Flow
import net.noliaware.yumi_agent.commun.util.Resource
import net.noliaware.yumi_agent.feature_auth.domain.model.BOSignIn

interface AuthRepository {
    fun getBackOfficeSignInCode(): Flow<Resource<BOSignIn>>
}