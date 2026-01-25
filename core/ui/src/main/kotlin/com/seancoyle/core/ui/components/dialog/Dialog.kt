package com.seancoyle.core.ui.components.dialog

import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.seancoyle.core.ui.R
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode

@Composable
fun ErrorDialog(
    message: String,
    onDismissNotification: () -> Unit,
    modifier: Modifier = Modifier
) {
    val errorTitle = stringResource(id = R.string.text_error)

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
                contentDescription = null, // Decorative - error context is in title
                tint = MaterialTheme.colorScheme.error
            )
        },
        title = {
            Text(
                text = errorTitle,
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
        modifier = modifier.semantics {
            paneTitle = errorTitle
            liveRegion = LiveRegionMode.Polite
        }
    )
}

@Composable
fun InfoDialog(
    message: String,
    onDismissNotification: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infoTitle = stringResource(id = R.string.text_info)

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
                contentDescription = null, // Decorative - info context is in title
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text(
                text = infoTitle,
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
        modifier = modifier.semantics {
            paneTitle = infoTitle
            liveRegion = LiveRegionMode.Polite
        }
    )
}

@PreviewDarkLightMode
@Composable
private fun ErrorDialogPreview() {
    AppTheme {
        ErrorDialog(
            message = "An error occurred while loading the data. Please try again later.",
            onDismissNotification = {}
        )
    }
}

@PreviewDarkLightMode
@Composable
private fun InfoDialogPreview() {
    AppTheme {
        InfoDialog(
            message = "This is an informational message to help you understand the feature better.",
            onDismissNotification = {}
        )
    }
}
