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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun Dialog(
    displayAlertChanged: (Boolean) -> Unit
) {
    AlertDialog(
        modifier = Modifier,
        onDismissRequest = { displayAlertChanged(false) },
        icon = { Icon(imageVector = Icons.Filled.Info, contentDescription = "Info") },
        title = {
            Text(text = "Title")
        },
        text = {
            Text(
                "This area typically contains the supportive text " +
                        "which presents the details regarding the Dialog's purpose."
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    displayAlertChanged(false)
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    displayAlertChanged(false)
                }
            ) {
                Text("Dismiss")
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
    hostState: SnackbarHostState,
    coroutineScope: LifecycleCoroutineScope
){
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