package com.seancoyle.core_ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleCoroutineScope
import com.seancoyle.core.domain.MessageDisplayType
import com.seancoyle.core.domain.Response
import com.seancoyle.core_ui.R
import kotlinx.coroutines.launch

@Composable
fun DisplayErrorAlert(
    error: Response,
    displayError: Boolean,
    dismissAlert: (Boolean) -> Unit
) {
    when (error.messageDisplayType) {

        MessageDisplayType.Dialog -> {
            Dialog(
                message = error.message,
                openDialog = displayError,
                displayAlertChanged = dismissAlert
            )
        }

        MessageDisplayType.Snackbar -> {
            //SnackBar()
        }

        else -> {
            MessageDisplayType.None
        }
    }
}


@Composable
fun Dialog(
    message: String,
    openDialog: Boolean,
    displayAlertChanged: (Boolean) -> Unit
) {
    if (openDialog) {
        AlertDialog(
            modifier = Modifier,
            onDismissRequest = { displayAlertChanged(false) },
            icon = { Icon(imageVector = Icons.Filled.Info, contentDescription = "Info") },
            title = {
                Text(text = stringResource(id = R.string.text_error))
            },
            text = {
                Text(message)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        displayAlertChanged(false)
                    }
                ) {
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
}

@Composable
fun SnackBar(
    hostState: SnackbarHostState,
    coroutineScope: LifecycleCoroutineScope
) {
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = hostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // this is how you show a Snackbar
                    coroutineScope.launch {
                        hostState.showSnackbar("Hello!")
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Create,
                    contentDescription = "Create"
                )
            }
        },
        content = { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                // Screen content
            }
        }
    )
}