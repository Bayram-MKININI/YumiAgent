package net.noliaware.yumi_agent.feature_message.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import net.noliaware.yumi_agent.commun.ApiConstants.GET_OUTBOX_MESSAGE_LIST
import net.noliaware.yumi_agent.commun.ApiParameters.LIMIT
import net.noliaware.yumi_agent.commun.ApiParameters.LIST_PAGE_SIZE
import net.noliaware.yumi_agent.commun.ApiParameters.OFFSET
import net.noliaware.yumi_agent.commun.data.remote.RemoteApi
import net.noliaware.yumi_agent.commun.domain.model.SessionData
import net.noliaware.yumi_agent.commun.util.ErrorType
import net.noliaware.yumi_agent.commun.util.PaginationException
import net.noliaware.yumi_agent.commun.util.currentTimeInMillis
import net.noliaware.yumi_agent.commun.util.generateToken
import net.noliaware.yumi_agent.commun.util.getCommonWSParams
import net.noliaware.yumi_agent.commun.util.handlePagingSourceError
import net.noliaware.yumi_agent.commun.util.randomString
import net.noliaware.yumi_agent.commun.util.resolvePaginatedListErrorIfAny
import net.noliaware.yumi_agent.feature_message.domain.model.Message

class OutboxMessagePagingSource(
    private val api: RemoteApi,
    private val sessionData: SessionData
) : PagingSource<Int, Message>() {

    override fun getRefreshKey(state: PagingState<Int, Message>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Message> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPage = params.key ?: 0

            val timestamp = currentTimeInMillis()
            val randomString = randomString()

            val remoteData = api.fetchOutboxMessages(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp = timestamp,
                    methodName = GET_OUTBOX_MESSAGE_LIST,
                    randomString = randomString
                ),
                params = generateGetMessagesListParams(nextPage, GET_OUTBOX_MESSAGE_LIST)
            )

            val errorType = resolvePaginatedListErrorIfAny(
                session = remoteData.session,
                sessionData = sessionData,
                tokenKey = GET_OUTBOX_MESSAGE_LIST
            )

            if (errorType != ErrorType.RECOVERABLE_ERROR) {
                throw PaginationException(errorType)
            }

            val messageRank = remoteData.data?.messageDTOList?.lastOrNull()?.messageRank ?: nextPage

            val moreItemsAvailable = remoteData.data?.messageDTOList?.lastOrNull()?.let { messageDTO ->
                if (messageDTO.messageRank != null && messageDTO.messageCount != null) {
                    messageDTO.messageRank < messageDTO.messageCount
                } else {
                    false
                }
            }

            val canLoadMore = moreItemsAvailable == true

            return LoadResult.Page(
                data = remoteData.data?.messageDTOList?.map { it.toMessage() }.orEmpty(),
                prevKey = null,// Only paging forward.
                nextKey = if (canLoadMore) messageRank else null
            )
        } catch (ex: Exception) {
            return handlePagingSourceError(ex)
        }
    }

    private fun generateGetMessagesListParams(offset: Int, tokenKey: String) = mutableMapOf(
        LIMIT to LIST_PAGE_SIZE.toString(),
        OFFSET to offset.toString()
    ).also {
        it.plusAssign(getCommonWSParams(sessionData, tokenKey))
    }.toMap()
}