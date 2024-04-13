package com.seancoyle.launch.implementation.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.seancoyle.core.ui.StringResource

@ExperimentalMaterialApi
@Composable
fun LaunchErrorScreen(
    modifier: Modifier = Modifier,
    errorMessage: StringResource,
    retryAction: (() -> Unit)? = null
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 50.dp)
    ) {
        item {
            Text(
                text = errorMessage.asString(),
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.error,
                modifier = modifier.fillMaxWidth()
            )
        }

        retryAction?.let { action ->
            item {
                Spacer(modifier = modifier.height(20.dp))

                Button(onClick = action) {
                    Text("Retry")
                }
            }
        }
    }
}