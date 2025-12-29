package com.seancoyle.core.ui.designsystem.theme

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    showBackground = true,
    name = "Light mode",
    backgroundColor = 0xFFFFFFFF,
    uiMode = UI_MODE_NIGHT_NO
)
@Preview(
    showBackground = true,
    name = "Dark mode",
    backgroundColor = 0xFF000001,
    uiMode = UI_MODE_NIGHT_YES
)

annotation class PreviewDarkLightMode
