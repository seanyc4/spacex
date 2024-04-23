package com.seancoyle.core.ui.composables

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.seancoyle.core.ui.NotificationState
import com.seancoyle.core.ui.NotificationType
import com.seancoyle.core.ui.R
import com.seancoyle.core.ui.UiComponentType

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
fun ErrorDialog(
    message: String,
    onDismissNotification: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissNotification,
        title = { Text(text = stringResource(id = R.string.text_error)) },
        text = { Text(message) },
        icon = { Icon(imageVector = Icons.Filled.Info, contentDescription = "Info") },
        confirmButton = {
            TextButton(onClick = onDismissNotification) {
                Text(text = stringResource(id = R.string.text_ok))
            }
        },
        shape = RoundedCornerShape(12.dp),
        containerColor = MaterialTheme.colorScheme.errorContainer,
        iconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        textContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        tonalElevation = 1.dp
    )
}

@Composable
fun InfoDialog(
    message: String,
    onDismissNotification: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissNotification,
        title = { Text(text = stringResource(id = R.string.text_info)) },
        text = { Text(message) },
        icon = { Icon(imageVector = Icons.Filled.Info, contentDescription = "Info") },
        confirmButton = {
            TextButton(onClick = onDismissNotification) {
                Text(text = stringResource(id = R.string.text_ok))
            }
        },
        shape = RoundedCornerShape(12.dp),
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        iconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        textContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        tonalElevation = 1.dp
    )
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