package net.noliaware.yumi_agent.feature_profile.data.repository

import kotlinx.coroutines.flow.Flow
import net.noliaware.yumi_agent.commun.util.Resource
import net.noliaware.yumi_agent.feature_profile.domain.model.UserProfile

interface ProfileRepository {
    fun getUserProfile(): Flow<Resource<UserProfile>>
}