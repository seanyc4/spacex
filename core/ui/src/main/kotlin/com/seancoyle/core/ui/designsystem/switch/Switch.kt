package com.seancoyle.core.ui.designsystem.switch

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode

@Composable
fun Switch(
    checked: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
    onCheckedChange: (Boolean) -> Unit = {},
) {
    val stateDescription = if (checked) "On" else "Off"
    val accessibilityLabel = label?.let { "$it, $stateDescription" } ?: stateDescription

    Switch(
        checked = checked,
        enabled = enabled,
        onCheckedChange = {
            onCheckedChange(it)
        },
        modifier = modifier.semantics {
            contentDescription = accessibilityLabel
            toggleableState = if (checked) ToggleableState.On else ToggleableState.Off
        },
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
