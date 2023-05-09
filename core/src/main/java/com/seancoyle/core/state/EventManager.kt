package com.seancoyle.core.state

import com.seancoyle.core.testing.EspressoIdlingResource
import com.seancoyle.core.util.printLogDebug
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class EventManager {

    private val activeEvents = mutableMapOf<String, Event>()

    private val _loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loading: StateFlow<Boolean>
        get() = _loading

    fun getActiveEventNames(): Set<String> = activeEvents.keys

    fun clearActiveEvents() {
        printLogDebug("EventExecutor", "Clear active state events")
        EspressoIdlingResource.clear()
        activeEvents.clear()
        updateLoadingStatusBasedOnActiveEvents()
    }

    fun addEvent(event: Event) {
        EspressoIdlingResource.increment()
        activeEvents[event.eventName()] = event
        updateLoadingStatusBasedOnActiveEvents()
    }

    fun removeEvent(event: Event?) {
        event?.let {
            printLogDebug("EventExecutor", "remove state event: ${it.eventName()}")
            EspressoIdlingResource.decrement()
            activeEvents.remove(it.eventName())
            updateLoadingStatusBasedOnActiveEvents()
        }
    }

    fun isEventActive(event: Event) = activeEvents.containsKey(event.eventName())

    private fun updateLoadingStatusBasedOnActiveEvents() {
        _loading.value = activeEvents.values.any { it.shouldDisplayProgressBar() }
    }
}