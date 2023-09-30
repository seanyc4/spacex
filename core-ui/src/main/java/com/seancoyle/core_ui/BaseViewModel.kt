package com.seancoyle.core_ui

import androidx.lifecycle.ViewModel
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.core.di.MainDispatcher
import com.seancoyle.core.domain.DataState
import com.seancoyle.core.domain.Event
import com.seancoyle.core.domain.StateMessage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

abstract class BaseViewModel<UiState>(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState : MutableStateFlow<UiState> by lazy {
        MutableStateFlow(initNewUIState())
    }

    val uiState: StateFlow<UiState>
        get() = _uiState


   // val loading: StateFlow<Boolean> = eventExecutor.loading


  //  abstract fun setUpdatedState(data: UiState)

    abstract fun setEvent(event: Event)

    fun emitStateMessageEvent(
        stateMessage: StateMessage
    ) = flow {
        emit(
            DataState.error<UiState>(
                response = stateMessage.response
            )
        )
    }

    fun getCurrentStateOrNew(): UiState {
        return uiState.value ?: initNewUIState()
    }

    fun setState(uiState: UiState) {
        _uiState.value = uiState
    }

    abstract fun initNewUIState(): UiState

}