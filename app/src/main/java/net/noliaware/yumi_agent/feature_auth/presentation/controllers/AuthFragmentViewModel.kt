package net.noliaware.yumi_agent.feature_auth.presentation.controllers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import net.noliaware.yumi_agent.commun.ACCOUNT_DATA
import net.noliaware.yumi_agent.feature_login.domain.model.AccountData
import javax.inject.Inject

@HiltViewModel
class AuthFragmentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val accountData get() = savedStateHandle.get<AccountData>(ACCOUNT_DATA)
}