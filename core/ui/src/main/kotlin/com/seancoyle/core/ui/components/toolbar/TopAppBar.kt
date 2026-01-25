package com.seancoyle.core.ui.components.toolbar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import com.seancoyle.core.ui.R
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingLarge
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingMedium
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        tonalElevation = 4.dp
    ) {
        // Hides TopAppBar when there is limited space i.e mobile landscape mode
        val windowSizeClass = currentWindowAdaptiveInfo(supportLargeAndXLargeWidth = true).windowSizeClass
        val showTopAppBar = windowSizeClass.isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND)
        if (!showTopAppBar) {
            return@Surface
        }

        TopAppBar(
            modifier = modifier,
            title = {
                Row(
                    modifier = Modifier.padding(start = paddingMedium),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    AppText.headlineLarge(
                        text = stringResource(id = R.string.app_name),
                        color = AppTheme.colors.primary,
                        modifier = Modifier.semantics { heading() }
                    )
                    Spacer(modifier = Modifier.width(paddingLarge))
                    Icon(
                        imageVector = Icons.Default.RocketLaunch,
                        contentDescription = null, // Decorative - app branding
                        tint = AppTheme.colors.primary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                actionIconContentColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.secondary
            ),
            actions = {
                IconButton(
                    onClick = { onClick() },
                    modifier = Modifier
                        .padding(end = paddingMedium)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(id = R.string.search_btn_content_desc),
                        tint = AppTheme.colors.primary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            },
        )
    }
}

@PreviewDarkLightMode
@Composable
fun TopAppBarPreview() {
    AppTheme {
        TopAppBar(onClick = {})
    }
}
