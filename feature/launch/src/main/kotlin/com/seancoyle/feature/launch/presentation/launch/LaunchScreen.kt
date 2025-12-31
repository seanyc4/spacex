package com.seancoyle.feature.launch.presentation.launch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.core.ui.components.progress.CircularProgressBar
import com.seancoyle.feature.launch.domain.model.LaunchStatus
import com.bumptech.glide.integration.compose.GlideImage
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.draw.clip
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi

@Composable
fun LaunchScreen(
    launchId: String,
    launchesType: LaunchesType,
    snackbarHostState: SnackbarHostState,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
) {
    val viewModel: LaunchViewModel =
        hiltViewModel<LaunchViewModel, LaunchViewModel.Factory> { factory ->
            factory.create(
                launchId = launchId,
                launchType = launchesType
            )
        }

    val launchState by viewModel.launchState.collectAsStateWithLifecycle()

    LaunchScreen(
        launchState = launchState,
        snackbarHostState = snackbarHostState,
        windowSizeClass = windowSizeClass,
        modifier = modifier
    )
}

@Composable
private fun LaunchScreen(
    launchState: LaunchUiState,
    snackbarHostState: SnackbarHostState,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (launchState) {
            is LaunchUiState.Loading -> {
                CircularProgressBar()
            }

            is LaunchUiState.Success -> {
                LaunchContent(launch = launchState.launch)
            }

            is LaunchUiState.Error -> {
                /*ErrorScreen(
                    message = launchState.message,
                    onRetry = { *//* TODO: Implement retry *//* }
                )*/
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun LaunchContent(
    launch: LaunchUI,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GlideImage(
                model = launch.image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Text(
                text = launch.missionName,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            // Launch date and status chips
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AssistChip(
                    onClick = {},
                    label = { Text(text = launch.launchDate) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                )
                StatusChip(status = launch.status)
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Launch ID (subtle)
            Text(
                text = "ID: ${launch.id}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Composable
private fun StatusChip(status: LaunchStatus) {
    val labelColor: Pair<String, Color> = when (status) {
        LaunchStatus.SUCCESS -> Pair("Success", Color(0xFF4CAF50))
        LaunchStatus.FAILED -> Pair("Failed", Color(0xFFF44336))
        LaunchStatus.TBD -> Pair("TBD", Color(0xFF9E9E9E))
        LaunchStatus.GO -> Pair("Go", Color(0xFF2196F3))
        LaunchStatus.TBC -> Pair("TBC", Color(0xFFFFC107))
        LaunchStatus.ALL -> Pair("Unknown", Color(0xFFBDBDBD))
    }
    val (label, color) = labelColor
    AssistChip(
        onClick = {},
        label = { Text(text = label) },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = color.copy(alpha = 0.15f),
            labelColor = color
        ),
        shape = CircleShape
    )
}
