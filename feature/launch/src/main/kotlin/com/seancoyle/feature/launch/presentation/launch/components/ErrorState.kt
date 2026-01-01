package com.seancoyle.feature.launch.presentation.launch.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.seancoyle.core.ui.designsystem.buttons.ButtonPrimary
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.R

@Composable
internal fun ErrorState(
    message: String,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .semantics { contentDescription = "Error loading launch: $message" },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = AppTheme.colors.error
            )
            AppText.titleLarge(
                text = "Unable to load launch",
                color = AppTheme.colors.onBackground
            )
            AppText.bodyMedium(
                text = message,
                color = AppTheme.colors.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(Dimens.dp8))
            ButtonPrimary(
                text = stringResource(R.string.retry),
                onClick = onRetry,
                modifier = Modifier.semantics {
                    contentDescription = "Retry loading launch"
                }
            )

        }
    }
}

@PreviewDarkLightMode
@Composable
private fun ErrorStatePreview() {
    AppTheme {
        ErrorState(
            message = "Network connection error. Please try again.",
            onRetry = {}
        )
    }
}
