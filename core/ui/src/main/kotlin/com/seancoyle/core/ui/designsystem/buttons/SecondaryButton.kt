package com.seancoyle.core.ui.designsystem.buttons

import android.R.attr.disabledAlpha
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens.cornerRadiusXSmall
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingMedium
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingSmall
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode

@Composable
fun ButtonSecondary(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    return Button(
        modifier = modifier
            .defaultMinSize(minHeight = 48.dp)
            .focusable(enabled)
            .semantics {
                role = Role.Button
                if (!enabled) {
                    disabled()
                }
            },
        colors = ButtonDefaults.textButtonColors(
            containerColor = AppTheme.colors.onSurface,
            disabledContainerColor = AppTheme.colors.onSurface.copy(alpha = disabledAlpha.toFloat()),
            disabledContentColor = AppTheme.colors.onSurface.copy(alpha = disabledAlpha.toFloat()),
            contentColor = AppTheme.colors.onSurface
        ),
        enabled = enabled,
        shape = RoundedCornerShape(cornerRadiusXSmall),
        onClick = onClick
    ) {
        AppText.bodyLarge(
            text = text,
            color = AppTheme.colors.onPrimary.copy(alpha = if (enabled) 1f else disabledAlpha.toFloat()),
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
                .padding(
                    horizontal = paddingMedium,
                    vertical = paddingSmall
                ),

            )
    }
}

@PreviewDarkLightMode
@Composable
private fun Preview() {
    ButtonSecondary(
        text = "Secondary Button",
        onClick = { }
    )
}

@PreviewDarkLightMode
@Composable
private fun PreviewDisabled() {
    ButtonSecondary(
        enabled = false,
        text = "Secondary Button",
        onClick = { }
    )
}
