package net.noliaware.yumi_agent.feature_message.presentation.mappers

import android.content.Context
import net.noliaware.yumi_agent.R
import net.noliaware.yumi_agent.commun.presentation.mappers.PriorityMapper
import net.noliaware.yumi_agent.commun.util.parseTimeString
import net.noliaware.yumi_agent.commun.util.parseToShortDate
import net.noliaware.yumi_agent.feature_message.domain.model.Message
import net.noliaware.yumi_agent.feature_message.presentation.views.MessageItemView.MessageItemViewAdapter
import javax.inject.Inject

class ReceivedMessageMapper @Inject constructor() : MessageMapper {

    override fun mapMessage(
        context: Context,
        message: Message
    ) = MessageItemViewAdapter(
        priorityIconRes = PriorityMapper().mapPriorityIcon(message.messagePriority),
        subject = if (message.messageType.isNullOrEmpty()) {
            message.messageSubject
        } else {
            "${message.messageType} ${message.messageSubject}"
        },
        time = context.getString(
            R.string.date_short,
            parseToShortDate(message.messageDate),
            parseTimeString(message.messageTime)
        ),
        mail = "${context.getString(R.string.mail_from)} ${message.messageSender}",
        body = message.messagePreview.orEmpty()
    )
}
