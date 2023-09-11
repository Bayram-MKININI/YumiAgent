package net.noliaware.yumi_agent.feature_message.presentation.controllers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import net.noliaware.yumi_agent.commun.Args.DOMAIN_NAME
import javax.inject.Inject

@HiltViewModel
class MessagingFragmentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val domainName get() = savedStateHandle.get<String>(DOMAIN_NAME)
}