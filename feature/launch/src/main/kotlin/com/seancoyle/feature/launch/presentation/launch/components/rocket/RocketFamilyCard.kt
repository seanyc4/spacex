package com.seancoyle.feature.launch.presentation.launch.components.rocket

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.seancoyle.core.ui.designsystem.card.AppCard
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppColors
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingMedium
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.R
import com.seancoyle.feature.launch.presentation.launch.components.previewData
import com.seancoyle.feature.launch.presentation.launch.model.RocketFamilyUI

@Composable
internal fun RocketFamilyCard(
    family: RocketFamilyUI,
    modifier: Modifier = Modifier
) {
    AppCard.Subtle(modifier = modifier.fillMaxWidth()) {
        AppText.titleSmall(
            text = family.name,
            fontWeight = FontWeight.Bold,
            color = AppTheme.colors.primary
        )

        AppText.bodySmall(
            text = family.description,
            color = AppTheme.colors.secondary
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = AppTheme.colors.onSurface,
                modifier = Modifier.size(16.dp)
            )
            AppText.bodySmall(
                text = stringResource(R.string.maiden_flight_date, family.maidenFlight),
                color = AppTheme.colors.secondary
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = paddingMedium),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MiniStatItem(
                value = family.totalLaunches,
                label = stringResource(R.string.launches),
                icon = Icons.Default.Star
            )

            MiniStatItem(
                value = family.successfulLaunches,
                label = stringResource(R.string.successful),
                icon = Icons.Default.CheckCircle,
                iconTint = AppColors.success
            )

            MiniStatItem(
                value = family.active,
                label = stringResource(R.string.active),
                icon = Icons.Default.CheckCircle,
                iconTint = if (family.active == "Active") AppColors.success else AppTheme.colors.secondary
            )
        }
    }
}

@PreviewDarkLightMode
@Composable
private fun RocketFamilyCardPreview() {
    AppTheme {
        RocketFamilyCard(
            family = previewData().rocket.configuration.families.first()
        )
    }
}
