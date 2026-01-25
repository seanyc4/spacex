package com.seancoyle.core.ui.components.progress

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import com.seancoyle.core.ui.R
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode

const val TAG_LOADING = "loading"

@Composable
fun CircularProgressBar(
    modifier: Modifier = Modifier
) {
    val loadingDesc = stringResource(R.string.loading)

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag(TAG_LOADING)
            .semantics {
                contentDescription = loadingDesc
                liveRegion = LiveRegionMode.Polite
            },
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
