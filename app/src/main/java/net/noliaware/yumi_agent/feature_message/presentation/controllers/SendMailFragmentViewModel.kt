package net.noliaware.yumi_agent.feature_message.presentation.controllers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.noliaware.yumi_agent.commun.DOMAIN_NAME
import net.noliaware.yumi_agent.commun.MESSAGE
import net.noliaware.yumi_agent.commun.presentation.EventsHelper
import net.noliaware.yumi_agent.feature_message.data.repository.MessageRepository
import net.noliaware.yumi_agent.feature_message.domain.model.Message
import javax.inject.Inject

@HiltViewModel
class SendMailFragmentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: MessageRepository
) : ViewModel() {

    val message get() = savedStateHandle.get<Message>(MESSAGE)
    val domainName get() = savedStateHandle.get<String>(DOMAIN_NAME)
    val messageSentEventsHelper = EventsHelper<Boolean>()

    fun callSendMessage(
        recipients: List<String>? = null,
        subject: String? = null,
        messagePriority: Int? = null,
        messageId: String? = null,
        messageBody: String
    ) {
        viewModelScope.launch {
            repository.sendMessage(recipients, subject, messagePriority, messageId, messageBody)
                .onEach { result ->
                    messageSentEventsHelper.handleResponse(result)
                }.launchIn(this)
        }
    }
}