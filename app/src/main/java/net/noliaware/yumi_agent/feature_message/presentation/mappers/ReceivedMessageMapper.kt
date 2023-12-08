package net.noliaware.yumi_agent.feature_message.presentation.mappers

import android.content.Context
import net.noliaware.yumi_agent.R
import net.noliaware.yumi_agent.feature_message.domain.model.Message
import javax.inject.Inject

class ReceivedMessageMapper @Inject constructor() : MessageMapper {
    override fun resolveMail(
        context: Context,
        message: Message
    ) = "${context.getString(R.string.mail_from)} ${message.messageSender}"
}