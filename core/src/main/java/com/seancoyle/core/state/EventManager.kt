package com.seancoyle.core.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.seancoyle.core.testing.EspressoIdlingResource
import com.seancoyle.core.util.printLogDebug


class EventManager {

    private val activeEvents: HashMap<String, Event> = HashMap()

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()

    val loading: LiveData<Boolean>
            get() = _loading

    fun getActiveJobNames(): MutableSet<String>{
        return activeEvents.keys
    }

    fun clearActiveEventCounter(){
        printLogDebug("DCM", "Clear active state events")
        EspressoIdlingResource.clear()
        activeEvents.clear()
        syncNumActiveEvents()
    }

    fun addEvent(event: Event){
        EspressoIdlingResource.increment()
        activeEvents[event.eventName()] = event
        syncNumActiveEvents()
    }

    fun removeEvent(event: Event?){
        printLogDebug("DCM", "remove state event: ${event?.eventName()}")
        event?.let {
            EspressoIdlingResource.decrement()
        }
        activeEvents.remove(event?.eventName())
        syncNumActiveEvents()
    }

    fun isEventActive(event: Event): Boolean{
        printLogDebug("DCM sem", "is state event active? " +
                "${activeEvents.containsKey(event.eventName())}")
        return activeEvents.containsKey(event.eventName())
    }

    private fun syncNumActiveEvents(){
        var shouldDisplayProgressBar = false
        for(stateEvent in activeEvents.values){
            if(stateEvent.shouldDisplayProgressBar()){
                shouldDisplayProgressBar = true
            }
        }
        _loading.value = shouldDisplayProgressBar
    }
}

















