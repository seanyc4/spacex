package com.seancoyle.core.ui.designsystem.dialog

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seancoyle.core.ui.R
import com.seancoyle.core.ui.designsystem.theme.AppTheme

@Composable
fun ErrorDialog(
    message: String,
    onDismissNotification: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismissNotification,
        confirmButton = {
            TextButton(onClick = onDismissNotification) {
                Text(text = stringResource(id = R.string.text_ok))
            }
        },
        icon = {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        },
        title = {
            Text(
                text = stringResource(id = R.string.text_error),
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    )
}

@Composable
fun InfoDialog(
    message: String,
    onDismissNotification: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismissNotification,
        confirmButton = {
            TextButton(onClick = onDismissNotification) {
                Text(text = stringResource(id = R.string.text_ok))
            }
        },
        icon = {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text(
                text = stringResource(id = R.string.text_info),
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(name = "Error Dialog - Light Mode")
@Composable
private fun ErrorDialogLightPreview() {
    AppTheme(isDarkTheme = false) {
        ErrorDialog(
            message = "An error occurred while loading the data. Please try again later.",
            onDismissNotification = {}
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(name = "Error Dialog - Dark Mode")
@Composable
private fun ErrorDialogDarkPreview() {
    AppTheme(isDarkTheme = true) {
        ErrorDialog(
            message = "An error occurred while loading the data. Please try again later.",
            onDismissNotification = {}
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(name = "Info Dialog - Light Mode")
@Composable
private fun InfoDialogLightPreview() {
    AppTheme(isDarkTheme = false) {
        InfoDialog(
            message = "This is an informational message to help you understand the feature better.",
            onDismissNotification = {}
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(name = "Info Dialog - Dark Mode")
@Composable
private fun InfoDialogDarkPreview() {
    AppTheme(isDarkTheme = true) {
        InfoDialog(
            message = "This is an informational message to help you understand the feature better.",
            onDismissNotification = {}
        )
    }
}
