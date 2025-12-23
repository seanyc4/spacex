package com.seancoyle.core.ui.designsystem

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.seancoyle.core.ui.NotificationState
import com.seancoyle.core.ui.NotificationType
import com.seancoyle.core.ui.UiComponentType
import com.seancoyle.core.ui.designsystem.dialog.ErrorDialog
import com.seancoyle.core.ui.designsystem.dialog.InfoDialog

@Composable
fun DisplayNotification(
    error: NotificationState?,
    onDismissNotification: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    error?.let {
        when (it.uiComponentType) {
            UiComponentType.Dialog -> {

                when (it.notificationType) {
                    NotificationType.Success -> {
                    //TODO("Create Success Dialog")
                    }

                    NotificationType.Error -> {
                        ErrorDialog(
                            message = it.message.resolve(),
                            onDismissNotification = onDismissNotification
                        )
                    }

                    NotificationType.Info -> {
                        InfoDialog(
                            message = it.message.resolve(),
                            onDismissNotification = onDismissNotification
                        )
                    }
                }
            }

            UiComponentType.Snackbar -> {
                when (it.notificationType) {
                    NotificationType.Success -> {
                        SnackBar(
                            hostState = snackbarHostState,
                            message = error.message.resolve()
                        )
                    }

                    NotificationType.Error -> {
                        SnackBar(
                            hostState = snackbarHostState,
                            message = error.message.resolve()
                        )
                    }

                    NotificationType.Info -> {
                        SnackBar(
                            hostState = snackbarHostState,
                            message = error.message.resolve()
                        )
                    }
                }
            }
            //TODO( "Create different Snackbars for different types of notifications")
            else -> Unit // Do nothing for MessageDisplayType.None
        }
    }
}

@Composable
fun SnackBar(
    hostState: SnackbarHostState?,
    message: String
) {
    LaunchedEffect(message) {
        hostState?.showSnackbar(message)
    }
}