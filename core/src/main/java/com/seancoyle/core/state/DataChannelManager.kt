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
abstract class DataChannelManager<ViewState>(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) {

    private var channelScope: CoroutineScope? = null
    private val eventManager: EventManager = EventManager()
    val messageStack = MessageStack()
    val loading = eventManager.loading

    fun setupChannel() {
        cancelJobs()
    }

    abstract fun setUpdatedState(data: ViewState)

    fun launchJob(
        event: Event,
        jobFunction: Flow<DataState<ViewState>?>
    ) {
        if (canExecuteNewEvent(event)) {
            printLogDebug("DCM", "launching job: ${event.eventName()}")
            addEvent(event)
            jobFunction
                .onEach { dataState ->
                    dataState?.let {
                        withContext(mainDispatcher) {
                            dataState.data?.let { data ->
                                setUpdatedState(data)
                            }
                            dataState.stateMessage?.let { stateMessage ->
                                handleNewStateMessage(stateMessage)
                            }
                            dataState.event?.let { stateEvent ->
                                removeEvent(stateEvent)
                            }
                        }
                    }
                }
                .launchIn(getChannelScope(ioDispatcher))
        }
    }

    private fun canExecuteNewEvent(event: Event): Boolean {
        // If a job is already active, do not allow duplication
        if (isJobAlreadyActive(event)) {
            return false
        }
        // Check the top of the stack, if a dialog is showing, do not allow new StateEvents
        if (!isMessageStackEmpty()) {
            if (messageStack[0].response.uiComponentType == UIComponentType.Dialog) {
                return false
            }
        }
        return true
    }

    private fun isMessageStackEmpty(): Boolean {
        return messageStack.isStackEmpty()
    }

    private fun handleNewStateMessage(stateMessage: StateMessage) {
        appendStateMessage(stateMessage)
    }

    private fun appendStateMessage(stateMessage: StateMessage) {
        messageStack.add(stateMessage)
    }

    fun clearStateMessage(index: Int = 0) {
        printLogDebug("DataChannelManager", "clear state message")
        messageStack.removeAt(index)
    }

    fun clearAllStateMessages() = messageStack.clear()

    fun printStateMessages() {
        for (message in messageStack) {
            printLogDebug("DCM", "${message.response.message}")
        }
    }

    fun getActiveJobs() = eventManager.getActiveJobNames()

    fun clearActiveEventCounter() = eventManager.clearActiveEventCounter()

    private fun addEvent(event: Event) = eventManager.addEvent(event)

    private fun removeEvent(event: Event?) = eventManager.removeEvent(event)

    private fun isEventActive(event: Event) = eventManager.isEventActive(event)

    private fun isJobAlreadyActive(event: Event): Boolean {
        return isEventActive(event)
    }

    private fun getChannelScope(ioDispatcher: CoroutineDispatcher): CoroutineScope {
        return channelScope ?: setupNewChannelScope(CoroutineScope(ioDispatcher))
    }

    private fun setupNewChannelScope(coroutineScope: CoroutineScope): CoroutineScope {
        channelScope = coroutineScope
        return channelScope as CoroutineScope
    }

    fun cancelJobs() {
        if (channelScope != null) {
            if (channelScope?.isActive == true) {
                channelScope?.cancel()
            }
            channelScope = null
        }
        clearActiveEventCounter()
    }

}























