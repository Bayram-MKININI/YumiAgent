package net.noliaware.yumi_agent.feature_profile.presentation.controllers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.noliaware.yumi_agent.commun.presentation.EventsHelper
import net.noliaware.yumi_agent.feature_profile.domain.model.UserProfile
import net.noliaware.yumi_agent.feature_profile.domain.repository.ProfileRepository
import javax.inject.Inject

@HiltViewModel
class UserProfileFragmentViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    val eventsHelper = EventsHelper<UserProfile>()

    init {
        callGetUserProfile()
    }

    private fun callGetUserProfile() {
        viewModelScope.launch {
            profileRepository.getUserProfile().onEach { result ->
                eventsHelper.handleResponse(result)
            }.launchIn(this)
        }
    }
}