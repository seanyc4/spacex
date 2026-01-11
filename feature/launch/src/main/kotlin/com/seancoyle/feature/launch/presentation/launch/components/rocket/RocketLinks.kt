package com.seancoyle.feature.launch.presentation.launch.components.rocket

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens.cornerRadiusMedium
import com.seancoyle.core.ui.designsystem.theme.Dimens.horizontalArrangementSpacingMedium
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingXLarge
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.R

@Composable
internal fun LinkButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.primary.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(cornerRadiusMedium)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingXLarge),
            horizontalArrangement = Arrangement.spacedBy(horizontalArrangementSpacingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AppTheme.colors.onSurface,
                modifier = Modifier.size(24.dp)
            )
            AppText.bodyLarge(
                text = text,
                color = AppTheme.colors.onSurface,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f),
                isSelectable = false
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = AppTheme.colors.onSurface,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@PreviewDarkLightMode
@Composable
private fun LinkButtonPreview() {
    AppTheme {
        LinkButton(
            text = stringResource(R.string.rocket_on_wikipedia),
            icon = Icons.Default.Info,
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
