package com.seancoyle.core.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.core.di.MainDispatcher
import com.seancoyle.core.state.DataChannelManager
import com.seancoyle.core.state.DataState
import com.seancoyle.core.state.Event
import com.seancoyle.core.state.MessageType
import com.seancoyle.core.state.Response
import com.seancoyle.core.state.StateMessage
import com.seancoyle.core.state.UIComponentType
import com.seancoyle.core.util.GenericErrors
import com.seancoyle.core.util.printLogDebug
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

@FlowPreview
@ExperimentalCoroutinesApi
abstract class BaseViewModel<UiState>
constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState : MutableStateFlow<UiState> by lazy {
        MutableStateFlow(initNewUiState())
    }

    val uiState: StateFlow<UiState>
        get() = _uiState

    private val dataChannelManager: DataChannelManager<UiState> = object : DataChannelManager<UiState>(
        ioDispatcher = ioDispatcher,
        mainDispatcher = mainDispatcher
    ) {

        override fun setUpdatedState(data: UiState) {
            this@BaseViewModel.setUpdatedState(data)
        }
    }

    val loading: LiveData<Boolean> = dataChannelManager.loading

    val stateMessage: LiveData<StateMessage?>
        get() = dataChannelManager.messageStack.stateMessage

    fun getMessageStackSize(): Int {
        return dataChannelManager.messageStack.size
    }

    fun setupChannel() = dataChannelManager.setupChannel()

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
                    message = GenericErrors.INVALID_STATE_EVENT,
                    uiComponentType = UIComponentType.None,
                    messageType = MessageType.Error
                ),
                event = event
            )
        )
    }

    fun launchJob(
        event: Event,
        jobFunction: Flow<DataState<UiState>?>
    ) = dataChannelManager.launchJob(event, jobFunction)

    fun getCurrentStateOrNew(): UiState {
        return uiState.value ?: initNewUiState()
    }

    fun setState(viewState: UiState) {
        _uiState.value = viewState
    }

    fun clearStateMessage(index: Int = 0) {
        printLogDebug("BaseViewModel", "clearStateMessage")
        dataChannelManager.clearStateMessage(index)
    }

    fun clearActiveEvents() = dataChannelManager.clearActiveEventCounter()

    fun clearAllStateMessages() = dataChannelManager.clearAllStateMessages()

    fun printStateMessages() = dataChannelManager.printStateMessages()

    fun cancelActiveJobs() = dataChannelManager.cancelJobs()

    abstract fun initNewUiState(): UiState

}








