package net.noliaware.yumi_agent.feature_message.presentation.mappers

import android.content.Context
import net.noliaware.yumi_agent.feature_message.domain.model.Message
import net.noliaware.yumi_agent.feature_message.presentation.views.MessageItemView.MessageItemViewAdapter

interface MessageMapper {
    fun mapMessage(
        context: Context,
        message: Message
    ): MessageItemViewAdapter
}