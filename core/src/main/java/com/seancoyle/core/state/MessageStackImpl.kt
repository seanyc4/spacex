package com.seancoyle.core.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class MessageStackImpl @Inject constructor() : MessageStack {

    private val _stateMessage = MutableStateFlow<StateMessage?>(null)
    override val stateMessage: StateFlow<StateMessage?> = _stateMessage.asStateFlow()

    private val messages = mutableListOf<StateMessage>()

    override fun getSize(): Int {
        return messages.size
    }

    override fun getAllMessages(): List<StateMessage> {
        return messages.toList()
    }

    override fun isStackEmpty(): Boolean {
        return messages.isEmpty()
    }

    override fun addAll(elements: Collection<StateMessage>): Boolean {
        elements.forEach { add(it) }
        return true
    }

    override fun add(element: StateMessage): Boolean {
        if (messages.contains(element)) { // prevent duplicate errors added to stack
            return false
        }
        val transaction = messages.add(element)
        if (messages.size == 1) {
            _stateMessage.value = element
        }
        return transaction
    }

    override fun removeAt(index: Int): StateMessage {
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

    override fun getMessageAt(index: Int): StateMessage? {
        return if (index >= 0 && index < messages.size) {
            messages[index]
        } else {
            null
        }
    }

    override fun clear() {
        messages.clear()
        _stateMessage.value = null
    }
}