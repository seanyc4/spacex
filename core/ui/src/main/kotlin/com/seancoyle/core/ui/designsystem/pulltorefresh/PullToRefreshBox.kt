package com.seancoyle.core.ui.designsystem.pulltorefresh

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import com.seancoyle.core.ui.R
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppColors
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingXLarge
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RefreshableContent(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    state: PullToRefreshState = rememberPullToRefreshState(),
    content: @Composable () -> Unit
) {
    val refreshStateDesc = if (isRefreshing) {
        stringResource(R.string.refreshing)
    } else {
        stringResource(R.string.pull_to_refresh)
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier.semantics {
            stateDescription = refreshStateDesc
            if (isRefreshing) {
                liveRegion = LiveRegionMode.Polite
            }
        },
        state = state,
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = isRefreshing,
                containerColor = AppColors.White,
                color = AppColors.Black,
                state = state,
            )
        },
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewDarkLightMode
@Composable
private fun RefreshableContentPreview() {
    AppTheme {
        val state = rememberPullToRefreshState()

        runBlocking {
            state.snapTo(1f)
        }

        RefreshableContent(
            state = state,
            isRefreshing = true,
            onRefresh = {
            },
            content = {
                LazyColumn {
                    items(10) {
                        AppText.bodyMedium(
                            text = "Item #$it",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(paddingXLarge)
                        )
                    }
                }
            }
        )
    }
}
