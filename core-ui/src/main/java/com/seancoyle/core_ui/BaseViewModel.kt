package com.seancoyle.core_ui

import androidx.lifecycle.ViewModel
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.core.di.MainDispatcher
import com.seancoyle.core.domain.DataState
import com.seancoyle.core.domain.Event
import com.seancoyle.core.domain.EventExecutor
import com.seancoyle.core.domain.MessageDisplayType
import com.seancoyle.core.domain.MessageStack
import com.seancoyle.core.domain.MessageType
import com.seancoyle.core.domain.Response
import com.seancoyle.core.domain.StateMessage
import com.seancoyle.core.domain.UsecaseResponses
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

abstract class BaseViewModel<UiState>(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val messageStack: MessageStack
) : ViewModel() {

    private val _uiState : MutableStateFlow<UiState> by lazy {
        MutableStateFlow(initNewUIState())
    }

    val uiState: StateFlow<UiState>
        get() = _uiState

    private val eventExecutor: EventExecutor<UiState> = object : EventExecutor<UiState>(
        ioDispatcher = ioDispatcher,
        mainDispatcher = mainDispatcher,
        messageStack = messageStack
    ) {

        override fun setUpdatedState(data: UiState) {
            this@BaseViewModel.setUpdatedState(data)
        }
    }

    val loading: StateFlow<Boolean> = eventExecutor.loading

    val stateMessage: StateFlow<StateMessage?>
        get() = eventExecutor.messageStack.stateMessage

    fun getMessageStackSize(): Int {
        return eventExecutor.messageStack.getSize()
    }

    fun cancelCurrentJobs() = eventExecutor.cancelCurrentJobs()

    abstract fun setUpdatedState(data: UiState)

    abstract fun setEvent(event: Event)

    fun emitStateMessageEvent(
        stateMessage: StateMessage,
        event: Event
    ) = flow {
        emit(
            DataState.error<UiState>(
                response = stateMessage.response,
                event = event
            )
        )
    }

    fun emitInvalidEvent(event: Event) = flow {
        emit(
            DataState.error<UiState>(
                response = Response(
                    message = UsecaseResponses.INVALID_STATE_EVENT,
                    messageDisplayType = MessageDisplayType.None,
                    messageType = MessageType.Error
                ),
                event = event
            )
        )
    }

    fun launchJob(
        event: Event,
        jobFunction: Flow<DataState<UiState>?>
    ) = eventExecutor.launchJob(event, jobFunction)

    fun getCurrentStateOrNew(): UiState {
        return uiState.value ?: initNewUIState()
    }

    fun setState(uiState: UiState) {
        _uiState.value = uiState
    }

    fun clearStateMessage(index: Int = 0) {
        eventExecutor.clearStateMessage(index)
    }

    fun clearActiveEvents() = eventExecutor.clearActiveEventCounter()

    fun clearAllStateMessages() = eventExecutor.clearAllStateMessages()

    fun printStateMessages() = eventExecutor.printStateMessages()

    fun cancelActiveJobs() = eventExecutor.cancelJobs()

    abstract fun initNewUIState(): UiState

}