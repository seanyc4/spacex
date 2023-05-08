package com.seancoyle.core.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MessageStack{

    private val _stateMessage = MutableStateFlow<StateMessage?>(null)
    val stateMessage: StateFlow<StateMessage?> = _stateMessage.asStateFlow()

    private val messages = mutableListOf<StateMessage>()

    val size: Int
        get() = messages.size

    val allMessages: List<StateMessage>
        get() = messages.toList()

    fun isStackEmpty(): Boolean {
        return messages.isEmpty()
    }

    fun addAll(elements: Collection<StateMessage>): Boolean {
        elements.forEach { add(it) }
        return true
    }

    fun add(element: StateMessage): Boolean {
        if (messages.contains(element)) { // prevent duplicate errors added to stack
            return false
        }
        val transaction = messages.add(element)
        if (messages.size == 1) {
            _stateMessage.value = element
        }
        return transaction
    }

    fun removeAt(index: Int): StateMessage {
        return try {
            val transaction = messages.removeAt(index)
            if (messages.isNotEmpty()) {
                _stateMessage.value = messages[0]
            } else {
                _stateMessage.value = null
            }
            transaction
        } catch (e: IndexOutOfBoundsException) {
            _stateMessage.value = null
            e.printStackTrace()
            StateMessage(
                Response(
                    message = "does nothing",
                    uiComponentType = UIComponentType.None,
                    messageType = MessageType.None
                )
            )
        }
    }

    fun getMessageAt(index: Int): StateMessage? {
        return if (index >= 0 && index < messages.size) {
            messages[index]
        } else {
            null
        }
    }

    fun clear() {
        messages.clear()
        _stateMessage.value = null
    }
}