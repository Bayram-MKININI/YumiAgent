package net.noliaware.yumi_agent.feature_auth.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.noliaware.yumi_agent.commun.ApiConstants.GET_BACK_OFFICE_SIGN_IN_CODE
import net.noliaware.yumi_agent.commun.ApiConstants.SET_PRIVACY_POLICY_READ_STATUS
import net.noliaware.yumi_agent.commun.data.remote.RemoteApi
import net.noliaware.yumi_agent.commun.domain.model.SessionData
import net.noliaware.yumi_agent.commun.util.Resource
import net.noliaware.yumi_agent.commun.util.currentTimeInMillis
import net.noliaware.yumi_agent.commun.util.generateToken
import net.noliaware.yumi_agent.commun.util.getCommonWSParams
import net.noliaware.yumi_agent.commun.util.handleRemoteCallError
import net.noliaware.yumi_agent.commun.util.handleSessionWithNoFailure
import net.noliaware.yumi_agent.commun.util.randomString
import net.noliaware.yumi_agent.feature_auth.domain.model.BOSignIn
import net.noliaware.yumi_agent.feature_auth.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: RemoteApi,
    private val sessionData: SessionData
) : AuthRepository {

    override fun updatePrivacyPolicyReadStatus(): Flow<Resource<Boolean>> = flow {

        emit(Resource.Loading())

        try {
            val timestamp = currentTimeInMillis()
            val randomString = randomString()

            val remoteData = api.updatePrivacyPolicyReadStatus(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp = timestamp,
                    methodName = SET_PRIVACY_POLICY_READ_STATUS,
                    randomString = randomString
                ),
                params = getCommonWSParams(sessionData, SET_PRIVACY_POLICY_READ_STATUS)
            )

            val sessionNoFailure = handleSessionWithNoFailure(
                session = remoteData.session,
                sessionData = sessionData,
                tokenKey = SET_PRIVACY_POLICY_READ_STATUS,
                appMessage = remoteData.message,
                error = remoteData.error
            )

            if (sessionNoFailure) {
                emit(
                    Resource.Success(
                        data = remoteData.data?.result == 1,
                        appMessage = remoteData.message?.toAppMessage()
                    )
                )
            }

        } catch (ex: Exception) {
            handleRemoteCallError(ex)
        }
    }

    override fun getBackOfficeSignInCode(): Flow<Resource<BOSignIn>> = flow {

        emit(Resource.Loading())

        try {
            val timestamp = currentTimeInMillis()
            val randomString = randomString()

            val remoteData = api.fetchBackOfficeSignInCode(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp = timestamp,
                    methodName = GET_BACK_OFFICE_SIGN_IN_CODE,
                    randomString = randomString
                ),
                params = getCommonWSParams(sessionData, GET_BACK_OFFICE_SIGN_IN_CODE)
            )

            val sessionNoFailure = handleSessionWithNoFailure(
                session = remoteData.session,
                sessionData = sessionData,
                tokenKey = GET_BACK_OFFICE_SIGN_IN_CODE,
                appMessage = remoteData.message,
                error = remoteData.error
            )

            if (sessionNoFailure) {

                remoteData.data?.toBOSignIn()?.let { boSignIn ->
                    emit(
                        Resource.Success(
                            data = boSignIn,
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