package com.seancoyle.core.presentation

data class NotificationState(
    val message: StringResource,
    val notificationUiType: NotificationUiType,
    val notificationType: NotificationType
)

sealed class NotificationUiType {
    data object Snackbar : NotificationUiType()
    data object Dialog : NotificationUiType()
    data object None: NotificationUiType()
}

sealed class NotificationType{
    data object Success: NotificationType()
    data object Error: NotificationType()
    data object Info: NotificationType()
    data object None: NotificationType()
}