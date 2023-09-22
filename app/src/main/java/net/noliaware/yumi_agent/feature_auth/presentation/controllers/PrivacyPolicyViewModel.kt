package net.noliaware.yumi_agent.feature_auth.presentation.controllers
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.noliaware.yumi_agent.commun.presentation.EventsHelper
import net.noliaware.yumi_agent.feature_auth.domain.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class PrivacyPolicyViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    val privacyPolicyStatusEventsHelper = EventsHelper<Boolean>()

    fun callUpdatePrivacyPolicyReadStatus() {
        viewModelScope.launch {
            repository.updatePrivacyPolicyReadStatus().onEach { result ->
                privacyPolicyStatusEventsHelper.handleResponse(result)
            }.launchIn(this)
        }
    }
}