package com.seancoyle.core.ui.designsystem.buttons

import android.R.attr.disabledAlpha
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens.cornerRadiusXXSmall
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingMedium
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingSmall
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode

@Composable
fun ButtonPrimary(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    return Button(
        modifier = modifier
            .focusable(true),
        colors = ButtonDefaults.textButtonColors(
            containerColor = AppTheme.colors.primary,
            disabledContainerColor = AppTheme.colors.primary.copy(alpha = disabledAlpha.toFloat()),
            disabledContentColor = AppTheme.colors.onPrimary.copy(alpha = disabledAlpha.toFloat()),
            contentColor = AppTheme.colors.onPrimary
        ),
        enabled = enabled,
        shape = RoundedCornerShape(cornerRadiusXXSmall),
        onClick = onClick
    ) {
        AppText.bodyMedium(
            text,
            color = AppTheme.colors.onPrimary.copy(alpha = if (enabled) 1f else disabledAlpha.toFloat()),
            modifier = Modifier
                .padding(
                    horizontal = paddingMedium,
                    vertical = paddingSmall
                )
        )
    }
}

@PreviewDarkLightMode
@Composable
private fun Preview() {
    ButtonPrimary(
        text = "Primary Button",
        onClick = { }
    )
}

@PreviewDarkLightMode
@Composable
private fun PreviewDisabled() {
    ButtonPrimary(
        enabled = false,
        text = "Primary Button",
        onClick = { }
    )
}
