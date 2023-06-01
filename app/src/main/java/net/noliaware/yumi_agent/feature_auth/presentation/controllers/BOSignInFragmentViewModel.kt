package net.noliaware.yumi_agent.feature_auth.presentation.controllers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import net.noliaware.yumi_agent.commun.presentation.EventsHelper
import net.noliaware.yumi_agent.feature_auth.data.repository.AuthRepository
import net.noliaware.yumi_agent.feature_auth.domain.model.BOSignIn
import net.noliaware.yumi_agent.feature_auth.domain.model.TimerState
import javax.inject.Inject

@HiltViewModel
class BOSignInFragmentViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val ONE_SECOND = 1000L

    val eventsHelper = EventsHelper<BOSignIn>()
    private var _timerStateFlow = MutableStateFlow(TimerState())
    val timerStateFlow: StateFlow<TimerState> = _timerStateFlow

    init {
        callGetBackOfficeSignInCode()
    }

    private fun callGetBackOfficeSignInCode() {
        viewModelScope.launch {
            repository.getBackOfficeSignInCode().onEach { result ->
                eventsHelper.handleResponse(result)
            }.launchIn(this)
        }
    }

    fun startTimerWithPeriod(totalSeconds: Int) {
        viewModelScope.launch {
            initTimer(totalSeconds).launchIn(this)
        }
    }

    private fun initTimer(totalSeconds: Int): Flow<TimerState> =
        (totalSeconds - 1 downTo 0).asFlow() // Emit total - 1 because the first was emitted onStart
            .onEach { delay(ONE_SECOND) } // Each second later emit a number
            .onStart { _timerStateFlow.emit(TimerState(totalSeconds)) } // Emit total seconds immediately
            .conflate() // In case the operation onTick takes some time, conflate keeps the time ticking separately
            .transform { remainingSeconds: Int ->
                _timerStateFlow.emit(TimerState(remainingSeconds))
            }
}