package net.noliaware.yumi_agent.commun.data.remote

import net.noliaware.yumi_agent.commun.ApiConstants.CONNECT
import net.noliaware.yumi_agent.commun.ApiConstants.DELETE_INBOX_MESSAGE
import net.noliaware.yumi_agent.commun.ApiConstants.DELETE_OUTBOX_MESSAGE
import net.noliaware.yumi_agent.commun.ApiConstants.GET_ACCOUNT
import net.noliaware.yumi_agent.commun.ApiConstants.GET_ALERT_LIST
import net.noliaware.yumi_agent.commun.ApiConstants.GET_BACK_OFFICE_SIGN_IN_CODE
import net.noliaware.yumi_agent.commun.ApiConstants.GET_INBOX_MESSAGE
import net.noliaware.yumi_agent.commun.ApiConstants.GET_INBOX_MESSAGE_LIST
import net.noliaware.yumi_agent.commun.ApiConstants.GET_OUTBOX_MESSAGE
import net.noliaware.yumi_agent.commun.ApiConstants.GET_OUTBOX_MESSAGE_LIST
import net.noliaware.yumi_agent.commun.ApiConstants.INIT
import net.noliaware.yumi_agent.commun.ApiConstants.SEND_MESSAGE
import net.noliaware.yumi_agent.commun.ApiConstants.SET_PRIVACY_POLICY_READ_STATUS
import net.noliaware.yumi_agent.commun.ApiParameters.SALT_STRING
import net.noliaware.yumi_agent.commun.ApiParameters.TIMESTAMP
import net.noliaware.yumi_agent.commun.ApiParameters.TOKEN
import net.noliaware.yumi_agent.commun.data.remote.dto.ResponseDTO
import net.noliaware.yumi_agent.feature_alerts.data.remote.dto.AlertsDTO
import net.noliaware.yumi_agent.feature_auth.data.remote.dto.BOSignInDTO
import net.noliaware.yumi_agent.feature_auth.data.remote.dto.UpdatePrivacyPolicyResponseDTO
import net.noliaware.yumi_agent.feature_login.data.remote.dto.AccountDataDTO
import net.noliaware.yumi_agent.feature_login.data.remote.dto.InitDTO
import net.noliaware.yumi_agent.feature_message.data.remote.dto.DeleteInboxMessageDTO
import net.noliaware.yumi_agent.feature_message.data.remote.dto.DeleteOutboxMessageDTO
import net.noliaware.yumi_agent.feature_message.data.remote.dto.InboxMessageDTO
import net.noliaware.yumi_agent.feature_message.data.remote.dto.InboxMessagesDTO
import net.noliaware.yumi_agent.feature_message.data.remote.dto.OutboxMessageDTO
import net.noliaware.yumi_agent.feature_message.data.remote.dto.OutboxMessagesDTO
import net.noliaware.yumi_agent.feature_message.data.remote.dto.SentMessageDTO
import net.noliaware.yumi_agent.feature_profile.data.remote.dto.UserAccountDTO
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path

interface RemoteApi {

    @FormUrlEncoded
    @POST("$INIT/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchInitData(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<InitDTO>

    @FormUrlEncoded
    @POST("$CONNECT/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchAccountDataForPassword(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<AccountDataDTO>

    @FormUrlEncoded
    @POST("$SET_PRIVACY_POLICY_READ_STATUS/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun updatePrivacyPolicyReadStatus(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<UpdatePrivacyPolicyResponseDTO>

    @FormUrlEncoded
    @POST("$GET_BACK_OFFICE_SIGN_IN_CODE/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchBackOfficeSignInCode(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<BOSignInDTO>

    @FormUrlEncoded
    @POST("$GET_ACCOUNT/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchAccount(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<UserAccountDTO>

    @FormUrlEncoded
    @POST("$GET_INBOX_MESSAGE_LIST/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchInboxMessages(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<InboxMessagesDTO>

    @FormUrlEncoded
    @POST("$GET_INBOX_MESSAGE/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchInboxMessageForId(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<InboxMessageDTO>

    @FormUrlEncoded
    @POST("$GET_OUTBOX_MESSAGE_LIST/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchOutboxMessages(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<OutboxMessagesDTO>

    @FormUrlEncoded
    @POST("$GET_OUTBOX_MESSAGE/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchOutboxMessageForId(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<OutboxMessageDTO>

    @FormUrlEncoded
    @POST("$SEND_MESSAGE/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun sendMessage(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<SentMessageDTO>

    @FormUrlEncoded
    @POST("$DELETE_INBOX_MESSAGE/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun deleteInboxMessageForId(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<DeleteInboxMessageDTO>

    @FormUrlEncoded
    @POST("$DELETE_OUTBOX_MESSAGE/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun deleteOutboxMessageForId(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<DeleteOutboxMessageDTO>

    @FormUrlEncoded
    @POST("$GET_ALERT_LIST/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchAlertList(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<AlertsDTO>
}