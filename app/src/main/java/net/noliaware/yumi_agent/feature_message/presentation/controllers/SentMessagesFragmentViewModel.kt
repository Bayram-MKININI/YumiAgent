package net.noliaware.yumi_agent.feature_message.presentation.controllers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import net.noliaware.yumi_agent.feature_message.domain.repository.MessageRepository
import javax.inject.Inject

@HiltViewModel
class SentMessagesFragmentViewModel @Inject constructor(
    private val messageRepository: MessageRepository
) : ViewModel() {
    fun getMessages() = messageRepository.getSentMessageList().cachedIn(viewModelScope)
}