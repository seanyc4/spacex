package com.seancoyle.core.presentation

data class NotificationState(
    val message: StringResource,
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