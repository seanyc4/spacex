package com.seancoyle.feature.launch.presentation.launch.components.rocket

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppColors
import com.seancoyle.core.ui.designsystem.theme.AppTextStyles
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingMedium
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingSmall
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.R

@Composable
internal fun MiniStatItem(
    value: String,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    iconTint: Color = AppTheme.colors.onSurface
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(paddingSmall)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(20.dp)
        )
        AppText.titleSmall(
            text = value,
            fontWeight = FontWeight.Bold,
            color = AppTheme.colors.onSurface,
            textAlign = TextAlign.Center
        )
        AppText.bodySmall(
            text = label,
            color = AppTheme.colors.secondary,
            textAlign = TextAlign.Center,
            fontSize = AppTextStyles.labelSmall.fontSize,
            isSelectable = false
        )
    }
}

@Composable
internal fun StatItem(
    value: String,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    valueColor: Color = AppTheme.colors.onSurface
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(paddingSmall)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = valueColor,
            modifier = Modifier.size(24.dp)
        )
        AppText.headlineSmall(
            text = value,
            fontWeight = FontWeight.Bold,
            color = valueColor,
            textAlign = TextAlign.Center
        )
        AppText.bodySmall(
            text = label,
            color = AppTheme.colors.secondary,
            textAlign = TextAlign.Center,
            isSelectable = false
        )
    }
}

@Composable
internal fun InfoChip(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(paddingSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AppTheme.colors.primary,
            modifier = Modifier.size(16.dp)
        )
        AppText.bodySmall(
            text = "$label: ",
            color = AppTheme.colors.secondary
        )
        AppText.bodySmall(
            text = value,
            fontWeight = FontWeight.Bold,
            color = AppTheme.colors.primary
        )
    }
}

@PreviewDarkLightMode
@Composable
private fun MiniStatItemPreview() {
    AppTheme {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingMedium),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MiniStatItem(
                value = "12",
                label = stringResource(R.string.launches),
                icon = Icons.Default.Star
            )
            MiniStatItem(
                value = "10",
                label = stringResource(R.string.successful),
                icon = Icons.Default.CheckCircle,
                iconTint = AppColors.success
            )
        }
    }
}

@PreviewDarkLightMode
@Composable
private fun StatItemPreview() {
    AppTheme {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingMedium),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(
                value = "100",
                label = stringResource(R.string.total_launches),
                icon = Icons.Default.Star
            )
            StatItem(
                value = "5",
                label = stringResource(R.string.failed),
                icon = Icons.Default.CheckCircle,
                valueColor = AppColors.error
            )
        }
    }
}
