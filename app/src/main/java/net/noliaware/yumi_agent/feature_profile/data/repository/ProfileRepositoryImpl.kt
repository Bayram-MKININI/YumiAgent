package net.noliaware.yumi_agent.feature_profile.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.noliaware.yumi_agent.commun.ApiConstants.GET_ACCOUNT
import net.noliaware.yumi_agent.commun.data.remote.RemoteApi
import net.noliaware.yumi_agent.commun.domain.model.SessionData
import net.noliaware.yumi_agent.commun.util.Resource
import net.noliaware.yumi_agent.commun.util.currentTimeInMillis
import net.noliaware.yumi_agent.commun.util.generateToken
import net.noliaware.yumi_agent.commun.util.getCommonWSParams
import net.noliaware.yumi_agent.commun.util.handleRemoteCallError
import net.noliaware.yumi_agent.commun.util.handleSessionWithNoFailure
import net.noliaware.yumi_agent.commun.util.randomString
import net.noliaware.yumi_agent.feature_profile.domain.model.UserProfile
import net.noliaware.yumi_agent.feature_profile.domain.repository.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: RemoteApi,
    private val sessionData: SessionData
) : ProfileRepository {

    override fun getUserProfile(): Flow<Resource<UserProfile>> = flow {

        emit(Resource.Loading())

        try {
            val timestamp = currentTimeInMillis()
            val randomString = randomString()

            val remoteData = api.fetchAccount(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp = timestamp,
                    methodName = GET_ACCOUNT,
                    randomString = randomString
                ),
                params = getCommonWSParams(sessionData, GET_ACCOUNT)
            )

            val sessionNoFailure = handleSessionWithNoFailure(
                session = remoteData.session,
                sessionData = sessionData,
                tokenKey = GET_ACCOUNT,
                appMessage = remoteData.message,
                error = remoteData.error
            )

            if (sessionNoFailure) {

                remoteData.data?.userProfileDTO?.toUserProfile()?.let { userProfile ->
                    emit(
                        Resource.Success(
                            data = userProfile,
                            appMessage = remoteData.message?.toAppMessage()
                        )
                    )
                }
            }

        } catch (ex: Exception) {
            handleRemoteCallError(ex)
        }
    }
}