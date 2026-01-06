package com.seancoyle.feature.launch.presentation.launch.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens.cornerRadiusLarge
import com.seancoyle.core.ui.designsystem.theme.Dimens.horizontalArrangementSpacingLarge
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingXLarge
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.R
import com.seancoyle.feature.launch.presentation.LaunchesTestTags

@Composable
internal fun SectionCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = paddingXLarge)
            .semantics { testTag = LaunchesTestTags.SECTION_CARD },
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(cornerRadiusLarge)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            content = content
        )
    }
}

@Composable
internal fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    val sectionDescription = stringResource(R.string.section_desc, text)

    AppText.titleLarge(
        text = text,
        fontWeight = FontWeight.Bold,
        color = AppTheme.colors.primary,
        modifier = modifier.semantics {
            contentDescription = sectionDescription
            testTag = LaunchesTestTags.SECTION_TITLE
        }
    )
}

@Composable
internal fun DetailRow(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    valueColor: Color = AppTheme.colors.onSurface
) {
    val detailDescription = stringResource(R.string.detail_row_desc, label, value)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .semantics { testTag = LaunchesTestTags.DETAIL_ROW },
        horizontalArrangement = Arrangement.spacedBy(horizontalArrangementSpacingLarge)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AppTheme.colors.onSurface,
            modifier = Modifier.size(20.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            AppText.labelMedium(
                text = label,
                color = AppTheme.colors.secondary
            )
            AppText.bodyLarge(
                text = value,
                color = valueColor,
                modifier = Modifier.semantics { contentDescription = detailDescription }
            )
        }
    }
}

@PreviewDarkLightMode
@Composable
private fun SectionCardPreview() {
    AppTheme {
        SectionCard {
            SectionTitle(text = stringResource(R.string.example_section))
            Spacer(modifier = Modifier.height(16.dp))
            AppText.bodyMedium(
                text = stringResource(R.string.example_content),
                color = AppTheme.colors.onSurface
            )
        }
    }
}
