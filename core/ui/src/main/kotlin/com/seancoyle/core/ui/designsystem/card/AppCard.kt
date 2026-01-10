package com.seancoyle.core.ui.designsystem.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppColors
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens.cornerRadiusLarge
import com.seancoyle.core.ui.designsystem.theme.Dimens.cornerRadiusMedium
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingLarge
import com.seancoyle.core.ui.designsystem.theme.Dimens.verticalArrangementSpacingLarge
import com.seancoyle.core.ui.designsystem.theme.Dimens.verticalArrangementSpacingMedium
import com.seancoyle.core.ui.designsystem.theme.Dimens.verticalArrangementSpacingSmall
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode

object AppCard {

    /**
     * Primary elevated card with surface background and shadow.
     * Use for main content sections.
     */
    @Composable
    fun Primary(
        modifier: Modifier = Modifier,
        elevation: Dp = 2.dp,
        testTag: String? = null,
        content: @Composable ColumnScope.() -> Unit
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = paddingLarge)
                .then(
                    if (testTag != null) {
                        Modifier.testTag(testTag)
                    } else {
                        Modifier
                    }
                ),
            colors = CardDefaults.cardColors(
                containerColor = AppTheme.colors.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = elevation),
            shape = RoundedCornerShape(cornerRadiusLarge)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(verticalArrangementSpacingLarge),
                modifier = Modifier.padding(20.dp),
                content = content
            )
        }
    }

    /**
     * Tinted card with semi-transparent colored background.
     * Use for highlighting content or status-related cards.
     *
     * @param tintColor The color to use for the tint (will be applied with alpha)
     * @param tintAlpha The alpha value for the tint color (default 0.1f)
     */
    @Composable
    fun Tinted(
        modifier: Modifier = Modifier,
        tintColor: Color = AppTheme.colors.primary,
        tintAlpha: Float = 0.1f,
        testTag: String? = null,
        content: @Composable ColumnScope.() -> Unit
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .then(
                    if (testTag != null) {
                        Modifier.testTag(testTag)
                    } else {
                        Modifier
                    }
                ),
            colors = CardDefaults.cardColors(
                containerColor = tintColor.copy(alpha = tintAlpha)
            ),
            shape = RoundedCornerShape(cornerRadiusMedium),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(verticalArrangementSpacingMedium),
                content = content
            )
        }
    }

    /**
     * Subtle card with surface variant background.
     * Use for secondary or nested content.
     */
    @Composable
    fun Subtle(
        modifier: Modifier = Modifier,
        alpha: Float = 0.5f,
        testTag: String? = null,
        content: @Composable ColumnScope.() -> Unit
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .then(
                    if (testTag != null) {
                        Modifier.testTag(testTag)
                    } else {
                        Modifier
                    }
                ),
            colors = CardDefaults.cardColors(
                containerColor = AppTheme.colors.surfaceVariant.copy(alpha = alpha)
            ),
            shape = RoundedCornerShape(cornerRadiusMedium)
        ) {
            Column(
                modifier = Modifier.padding(paddingLarge),
                verticalArrangement = Arrangement.spacedBy(verticalArrangementSpacingSmall),
                content = content
            )
        }
    }

    @Composable
    fun Success(
        modifier: Modifier = Modifier,
        testTag: String? = null,
        content: @Composable ColumnScope.() -> Unit
    ) {
        Tinted(
            modifier = modifier,
            tintColor = AppColors.success,
            testTag = testTag,
            content = content
        )
    }

    @Composable
    fun Error(
        modifier: Modifier = Modifier,
        testTag: String? = null,
        content: @Composable ColumnScope.() -> Unit
    ) {
        Tinted(
            modifier = modifier,
            tintColor = AppTheme.colors.error,
            testTag = testTag,
            content = content
        )
    }

    @Composable
    fun Warning(
        modifier: Modifier = Modifier,
        testTag: String? = null,
        content: @Composable ColumnScope.() -> Unit
    ) {
        Tinted(
            modifier = modifier,
            tintColor = AppColors.warning,
            testTag = testTag,
            content = content
        )
    }
}

@PreviewDarkLightMode
@Composable
private fun AppCardPrimaryPreview() {
    AppTheme {
        AppCard.Primary(
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                AppText.titleMedium(
                    text = "Primary Card",
                    color = AppTheme.colors.onSurface
                )
                AppText.bodyMedium(
                    text = "This is the primary elevated card style",
                    color = AppTheme.colors.secondary
                )
            }
        }
    }
}

@PreviewDarkLightMode
@Composable
private fun AppCardTintedPreview() {
    AppTheme {
        AppCard.Tinted(
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                AppText.titleMedium(
                    text = "Tinted Card",
                    color = AppTheme.colors.primary
                )
                AppText.bodyMedium(
                    text = "This is the tinted card style",
                    color = AppTheme.colors.secondary
                )
            }
        }
    }
}

@PreviewDarkLightMode
@Composable
private fun AppCardSubtlePreview() {
    AppTheme {
        AppCard.Subtle(
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                AppText.titleMedium(
                    text = "Subtle Card",
                    color = AppTheme.colors.primary
                )
                AppText.bodyMedium(
                    text = "This is the Subtle card style",
                    color = AppTheme.colors.secondary
                )
            }
        }
    }
}

@PreviewDarkLightMode
@Composable
private fun AppCardSuccessPreview() {
    AppTheme {
        AppCard.Success(
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                AppText.titleMedium(
                    text = "Success Card",
                    color = AppColors.success
                )
                AppText.bodyMedium(
                    text = "Operation completed successfully",
                    color = AppTheme.colors.secondary
                )
            }
        }
    }
}

@PreviewDarkLightMode
@Composable
private fun AppCardErrorPreview() {
    AppTheme {
        AppCard.Error(
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                AppText.titleMedium(
                    text = "Error Card",
                    color = AppColors.error
                )
                AppText.bodyMedium(
                    text = "Something went wrong",
                    color = AppTheme.colors.secondary
                )
            }
        }
    }
}

@PreviewDarkLightMode
@Composable
private fun AppCardWarningPreview() {
    AppTheme {
        AppCard.Warning(
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                AppText.titleMedium(
                    text = "Warning Card",
                    color = AppColors.error
                )
                AppText.bodyMedium(
                    text = "Something went wrong",
                    color = AppTheme.colors.secondary
                )
            }
        }
    }
}
