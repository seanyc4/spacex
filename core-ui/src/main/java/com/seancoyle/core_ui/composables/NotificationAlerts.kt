package com.seancoyle.core_ui.composables

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
import com.seancoyle.core.domain.MessageDisplayType
import com.seancoyle.core.domain.Response
import com.seancoyle.core_ui.R

@Composable
fun DisplayErrorAlert(
    error: Response?,
    onDismiss: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    error?.let {
        when (it.messageDisplayType) {
            MessageDisplayType.Dialog -> {
                ErrorDialog(
                    message = it.message,
                    onDismiss = onDismiss
                )
            }

            MessageDisplayType.Snackbar -> {
                SnackBar(
                    hostState = snackbarHostState,
                    message = error.message
                )
            }

            else -> Unit // Do nothing for MessageDisplayType.None
        }
    }
}

@Composable
fun ErrorDialog(
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(id = R.string.text_error)) },
        text = { Text(message) },
        icon = { Icon(imageVector = Icons.Filled.Info, contentDescription = "Info") },
        confirmButton = {
            TextButton(onClick = onDismiss) {
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