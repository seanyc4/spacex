package com.seancoyle.core.ui.designsystem.switch

import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode

@Composable
fun Switch(
    checked: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit = {},
) {
    Switch(
        checked = checked,
        enabled = enabled,
        onCheckedChange = {
            onCheckedChange(it)
        },
        modifier = modifier,
        colors = SwitchDefaults.colors(
            checkedThumbColor = AppTheme.colors.primary,
            checkedTrackColor = AppTheme.colors.primary,
            uncheckedThumbColor = AppTheme.colors.outline,
            uncheckedTrackColor = AppTheme.colors.outline
        ),
    )
}

@PreviewDarkLightMode
@Composable
private fun PreviewChecked() {
    Switch(
        checked = true,
        onCheckedChange = {}
    )
}

@PreviewDarkLightMode
@Composable
private fun PreviewUnchecked() {
    Switch(
        checked = false,
        onCheckedChange = {}
    )
}
