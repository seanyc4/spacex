package com.seancoyle.core.state

import com.seancoyle.core.di.IODispatcher
import com.seancoyle.core.di.MainDispatcher
import com.seancoyle.core.util.printLogDebug
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext

@FlowPreview
@ExperimentalCoroutinesApi
abstract class EventExecutor<UiState>(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    val messageStack: MessageStack
) {

    private var channelScope: CoroutineScope? = null
    private val eventManager: EventManager = EventManager()
    val loading = eventManager.loading

    fun cancelCurrentJobs() = cancelJobs()

    abstract fun setUpdatedState(data: UiState)

    fun launchJob(
        event: Event,
        jobFunction: Flow<DataState<UiState>?>
    ) {
        if (canExecuteNewEvent(event)) {
            printLogDebug("EventExecutor", "launching job: ${event.eventName()}")
            eventManager.addEvent(event)
            jobFunction
                .onEach { dataState ->
                    dataState?.let {
                        withContext(mainDispatcher) {
                            it.data?.let(::setUpdatedState)
                            it.stateMessage?.let(messageStack::add)
                            it.event?.let(eventManager::removeEvent)
                        }
                    }
                }
                .launchIn(getChannelScope(ioDispatcher))
        }
    }

    private fun canExecuteNewEvent(event: Event): Boolean {
        return !eventManager.isEventActive(event) &&
                messageStack.isStackEmpty() ||
                messageStack.peek()?.response?.uiComponentType != UIComponentType.Dialog
    }

    private fun handleNewStateMessage(stateMessage: StateMessage) {
        appendStateMessage(stateMessage)
    }

    private fun appendStateMessage(stateMessage: StateMessage) {
        messageStack.add(stateMessage)
    }

    fun clearStateMessage(index: Int = 0) {
        printLogDebug("EventExecutor", "clear state message")
        messageStack.removeAt(index)
    }

    fun clearAllStateMessages() = messageStack.clear()

    fun getActiveJobs() = eventManager.getActiveEventNames()

    fun clearActiveEventCounter() = eventManager.clearActiveEventCounter()

    fun printStateMessages() {
        messageStack.getAllMessages().forEach { message ->
            printLogDebug("EventExecutor", "${message.response.message}")
        }
    }

    private fun getChannelScope(ioDispatcher: CoroutineDispatcher): CoroutineScope {
        return channelScope ?: CoroutineScope(ioDispatcher).also { channelScope = it }
    }

    fun cancelJobs() {
        channelScope?.takeIf { it.isActive }?.cancel()
        channelScope = null
        eventManager.clearActiveEventCounter()
    }
}