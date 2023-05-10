package com.seancoyle.core.domain

data class StateMessage(val response: Response)

data class Response(
    val message: String?,
    val messageDisplayType: MessageDisplayType,
    val messageType: MessageType
)

sealed class MessageDisplayType {
    object Toast : MessageDisplayType()
    object Dialog : MessageDisplayType()
    object None: MessageDisplayType()
}

sealed class MessageType{
    object Success: MessageType()
    object Error: MessageType()
    object Info: MessageType()
    object None: MessageType()
}

interface StateMessageCallback{
    fun removeMessageFromStack()
}