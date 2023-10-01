package com.seancoyle.launch.implementation.presentation

import androidx.lifecycle.ViewModel
import com.seancoyle.core.domain.Event
import com.seancoyle.launch.api.presentation.LaunchUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel: ViewModel() {

    protected val _uiState : MutableStateFlow<LaunchUiState.LaunchState> by lazy {
        MutableStateFlow(initNewUIState())
    }

    val uiState: StateFlow<LaunchUiState.LaunchState>
        get() = _uiState

    abstract fun setEvent(event: Event)

    protected open fun getCurrentState(): LaunchUiState.LaunchState {
        return uiState.value
    }

    fun setState(uiState: LaunchUiState.LaunchState) {
        _uiState.value = uiState
    }

    abstract fun initNewUIState(): LaunchUiState.LaunchState

    // val loading: StateFlow<Boolean> = eventExecutor.loading


    //  abstract fun setUpdatedState(data: UiState)

    /*fun emitStateMessageEvent(
        stateMessage: StateMessage
    ) = flow {
        emit(
            Result.error<UiState>(
                response = stateMessage.response
            )
        )
    }*/

}