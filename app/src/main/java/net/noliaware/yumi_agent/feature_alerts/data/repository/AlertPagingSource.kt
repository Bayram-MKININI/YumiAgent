package net.noliaware.yumi_agent.feature_alerts.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import net.noliaware.yumi_agent.commun.ApiConstants.GET_ALERT_LIST
import net.noliaware.yumi_agent.commun.ApiParameters.LIMIT
import net.noliaware.yumi_agent.commun.ApiParameters.TIMESTAMP_OFFSET
import net.noliaware.yumi_agent.commun.data.remote.RemoteApi
import net.noliaware.yumi_agent.commun.domain.model.SessionData
import net.noliaware.yumi_agent.commun.util.PaginationException
import net.noliaware.yumi_agent.commun.util.ServiceError.ErrNone
import net.noliaware.yumi_agent.commun.util.currentTimeInMillis
import net.noliaware.yumi_agent.commun.util.generateToken
import net.noliaware.yumi_agent.commun.util.getCommonWSParams
import net.noliaware.yumi_agent.commun.util.handlePagingSourceError
import net.noliaware.yumi_agent.commun.util.randomString
import net.noliaware.yumi_agent.commun.util.resolvePaginatedListErrorIfAny
import net.noliaware.yumi_agent.feature_alerts.domain.model.Alert

class AlertPagingSource(
    private val api: RemoteApi,
    private val sessionData: SessionData
) : PagingSource<Long, Alert>() {

    override fun getRefreshKey(
        state: PagingState<Long, Alert>
    ) = null

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Alert> {
        try {
            val offsetTimestamp = params.key ?: 0

            val timestamp = currentTimeInMillis()
            val randomString = randomString()

            val remoteData = api.fetchAlertList(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp = timestamp,
                    methodName = GET_ALERT_LIST,
                    randomString = randomString
                ),
                params = generateGetAlertsListParams(
                    timestamp = offsetTimestamp,
                    loadSize = params.loadSize,
                    tokenKey = GET_ALERT_LIST
                )
            )

            val serviceError = resolvePaginatedListErrorIfAny(
                session = remoteData.session,
                sessionData = sessionData,
                tokenKey = GET_ALERT_LIST
            )

            if (serviceError !is ErrNone) {
                throw PaginationException(serviceError)
            }

            val lastAlertTimestamp = remoteData.data?.alertDTOList?.lastOrNull()?.alertTimestamp ?: offsetTimestamp

            val moreItemsAvailable = remoteData.data?.alertDTOList?.lastOrNull()?.let { alertDTO ->
                alertDTO.alertRank < alertDTO.alertCount
            }

            return LoadResult.Page(
                data = remoteData.data?.alertDTOList?.map { it.toAlert() }.orEmpty(),
                prevKey = null,// Only paging forward.
                nextKey = if (moreItemsAvailable == true) lastAlertTimestamp else null
            )
        } catch (ex: Exception) {
            return handlePagingSourceError(ex)
        }
    }

    private fun generateGetAlertsListParams(
        timestamp: Long,
        loadSize: Int,
        tokenKey: String
    ) = mutableMapOf(
        TIMESTAMP_OFFSET to timestamp.toString(),
        LIMIT to loadSize.toString()
    ).also {
        it += getCommonWSParams(sessionData, tokenKey)
    }.toMap()
}