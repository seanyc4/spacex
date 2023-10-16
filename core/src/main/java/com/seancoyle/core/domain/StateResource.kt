package com.seancoyle.core.domain

data class StateMessage(val response: Response)

data class Response(
    val message: String,
    val messageDisplayType: MessageDisplayType,
    val messageType: MessageType
)

sealed class MessageDisplayType {
    data object Snackbar : MessageDisplayType()
    data object Dialog : MessageDisplayType()
    data object None: MessageDisplayType()
}

sealed class MessageType{
    data object Success: MessageType()
    data object Error: MessageType()
    data object Info: MessageType()
    data object None: MessageType()
}

interface StateMessageCallback{
    fun removeMessageFromStack()
}