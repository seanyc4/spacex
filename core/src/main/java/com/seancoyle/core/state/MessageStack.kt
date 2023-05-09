package com.seancoyle.core.state

import kotlinx.coroutines.flow.StateFlow

interface MessageStack {

    val stateMessage: StateFlow<StateMessage?>
    fun isStackEmpty(): Boolean

    fun addAll(elements: Collection<StateMessage>): Boolean

    fun add(element: StateMessage): Boolean

    fun removeAt(index: Int): StateMessage

    fun getMessageAt(index: Int): StateMessage?

    fun clear()

    fun getSize(): Int

    fun getAllMessages(): List<StateMessage>

    fun peek(): StateMessage?
}