package com.seancoyle.core.ui.components.notification

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.seancoyle.core.ui.NotificationState
import com.seancoyle.core.ui.NotificationType
import com.seancoyle.core.ui.UiComponentType
import com.seancoyle.core.ui.components.dialog.ErrorDialog
import com.seancoyle.core.ui.components.dialog.InfoDialog

/**
 * Handles the display of notifications based on the [NotificationState].
 *
 * This composable orchestrates different notification types (Dialog/Snackbar) and
 * notification categories (Success/Error/Info) to display the appropriate UI component.
 *
 * @param notification The current notification state to display, or null if no notification
 * @param onDismissNotification Callback invoked when the notification is dismissed
 * @param snackbarHostState The SnackbarHostState for displaying snackbar notifications
 */
@Composable
fun NotificationHandler(
    notification: NotificationState,
    onDismissNotification: () -> Unit,
    snackbarHostState: SnackbarHostState
) {

    when (notification.uiComponentType) {
        UiComponentType.Dialog -> {
            when (notification.notificationType) {
                NotificationType.Success -> {
                    // TODO: Create Success Dialog
                }

                NotificationType.Error -> {
                    ErrorDialog(
                        message = notification.message.resolve(),
                        onDismissNotification = onDismissNotification
                    )
                }

                NotificationType.Info -> {
                    InfoDialog(
                        message = notification.message.resolve(),
                        onDismissNotification = onDismissNotification
                    )
                }
            }
        }

        UiComponentType.Snackbar -> {
            SnackbarNotification(
                hostState = snackbarHostState,
                message = notification.message.resolve(),
                onDismissNotification = onDismissNotification
            )
        }

        UiComponentType.None -> Unit
    }
}

@Composable
private fun SnackbarNotification(
    hostState: SnackbarHostState,
    message: String,
    onDismissNotification: () -> Unit
) {
    LaunchedEffect(message) {
        hostState.showSnackbar(message)
        onDismissNotification()
    }
}
