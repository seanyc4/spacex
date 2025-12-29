package com.seancoyle.core.ui

data class NotificationState(
    val message: StringResource,
    val uiComponentType: UiComponentType,
    val notificationType: NotificationType
)

sealed interface UiComponentType {
    data object Snackbar : UiComponentType
    data object Dialog : UiComponentType
    data object None : UiComponentType
}

sealed interface NotificationType {
    data object Success : NotificationType
    data object Error : NotificationType
    data object Info : NotificationType
}