package com.seancoyle.core.ui.components.progress

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.progressSemantics
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode

const val TAG_LOADING = "loading"

@Composable
fun CircularProgressBar(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .progressSemantics()
            .fillMaxSize()
            .testTag(TAG_LOADING),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@PreviewDarkLightMode
@Composable
private fun CircularProgressBarLightPreview() {
    AppTheme {
        CircularProgressBar()
    }
}
