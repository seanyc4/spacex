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
import com.seancoyle.feature.launch.domain.model.Family

@Composable
internal fun RocketFamilyCard(
    family: Family,
    modifier: Modifier = Modifier
) {
    AppCard.Subtle(modifier = modifier.fillMaxWidth()) {
        family.name?.let { name ->
            AppText.titleSmall(
                text = name,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colors.primary
            )
        }

        family.description?.let { desc ->
            AppText.bodySmall(
                text = desc,
                color = AppTheme.colors.secondary
            )
        }

        family.maidenFlight?.let { maidenFlight ->
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
                    text = stringResource(R.string.maiden_flight_date, maidenFlight),
                    color = AppTheme.colors.secondary
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = paddingMedium),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            family.totalLaunchCount?.let {
                MiniStatItem(
                    value = it.toString(),
                    label = stringResource(R.string.launches),
                    icon = Icons.Default.Star
                )
            }

            family.successfulLaunches?.let {
                MiniStatItem(
                    value = it.toString(),
                    label = stringResource(R.string.successful),
                    icon = Icons.Default.CheckCircle,
                    iconTint = AppColors.success
                )
            }

            family.active?.let { isActive ->
                MiniStatItem(
                    value = if (isActive) stringResource(R.string.yes) else stringResource(R.string.no),
                    label = stringResource(R.string.active),
                    icon = Icons.Default.CheckCircle,
                    iconTint = if (isActive) AppColors.success else AppTheme.colors.secondary
                )
            }
        }
    }
}

@PreviewDarkLightMode
@Composable
private fun RocketFamilyCardPreview() {
    AppTheme {
        RocketFamilyCard(
            family = Family(
                id = 1,
                name = "Falcon 9",
                manufacturer = null,
                description = "A two-stage rocket designed and manufactured by SpaceX",
                active = true,
                maidenFlight = "June 4, 2010",
                totalLaunchCount = 200,
                consecutiveSuccessfulLaunches = 150,
                successfulLaunches = 195,
                failedLaunches = 2,
                pendingLaunches = 5,
                attemptedLandings = 180,
                successfulLandings = 175,
                failedLandings = 5,
                consecutiveSuccessfulLandings = 100
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}
