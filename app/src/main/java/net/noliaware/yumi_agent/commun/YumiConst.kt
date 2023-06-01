package net.noliaware.yumi_agent.commun

import net.noliaware.yumi_agent.BuildConfig

const val BASE_URL = "https://api.noliaware.net/yumi/agent/"
const val INIT = "init"
const val CONNECT = "connect"
const val GET_ACCOUNT = "getAccount"
const val GET_BACK_OFFICE_SIGN_IN_CODE = "getBackOfficeSignInCode"
const val GET_ALERT_LIST = "getAlertList"
const val GET_INBOX_MESSAGE_LIST = "getInboxMessageList"
const val GET_INBOX_MESSAGE = "getInboxMessage"
const val GET_OUTBOX_MESSAGE_LIST = "getOutboxMessageList"
const val GET_OUTBOX_MESSAGE = "getOutboxMessage"
const val SEND_MESSAGE = "sendMessage"
const val DELETE_INBOX_MESSAGE = "delInboxMessage"
const val DELETE_OUTBOX_MESSAGE = "delOutboxMessage"

const val LOGIN = "login"
const val PASSWORD = "password"
const val APP_VERSION = "appVersion"
const val DEVICE_ID = "deviceId"
const val PUSH_TOKEN = "devicePushToken"
const val DEVICE_TYPE = "deviceType"
const val DEVICE_OS = "deviceOS"
const val DEVICE_UUID = "deviceUUID"
const val DEVICE_LABEL = "deviceLabel"
const val SESSION_ID = "sessionId"
const val SESSION_TOKEN = "sessionToken"
const val TIMESTAMP = "timestamp"
const val SALT_STRING = "saltString"
const val TOKEN = "token"
const val KEYBOARD = "keyboard"
const val ENCRYPTION_VECTOR = "encryption_vector"
const val LIMIT = "limit"
const val LIST_PAGE_SIZE = 20
const val OFFSET = "offset"
const val MESSAGE_ID = "messageId"
const val MESSAGE_TO = "messageTo"
const val MESSAGE_PRIORITY = "messagePriority"
const val MESSAGE_SUBJECT = "messageSubject"
const val MESSAGE_BODY = "messageBody"
const val MESSAGE = "message"
const val MESSAGES = "messages"
const val ALERTS = "alerts"
const val TIMESTAMP_OFFSET = "timestampOffset"

const val ACCOUNT_DATA = "account_data"

const val MESSAGE_INBOX_DAT = "message_inbox_dat"
const val MESSAGE_OUTBOX_DAT = "message_outbox_dat"

const val DATA_SHOULD_REFRESH = "dataShouldRefresh"

const val ACTION_PUSH_DATA = BuildConfig.APPLICATION_ID + ".action.PUSH"
const val PUSH_TITLE = "title"
const val PUSH_BODY = "body"

const val KEY_CURRENT_VERSION = "android_force_update_current_version"
const val KEY_FORCE_UPDATE_REQUIRED = "android_force_update_required"
const val KEY_FORCE_UPDATE_URL = "android_force_update_store_url"

//FRAGMENT TAGS
const val BO_SIGN_IN_FRAGMENT_TAG = "bo_sign_in_fragment"
const val READ_MESSAGE_FRAGMENT_TAG = "read_message_fragment"
const val SEND_MESSAGES_FRAGMENT_TAG = "send_messages_fragment"

const val GOLDEN_RATIO = 1.6180339887
const val ONE_HOUR = 3600L