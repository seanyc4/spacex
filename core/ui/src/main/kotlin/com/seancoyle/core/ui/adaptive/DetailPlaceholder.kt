package com.seancoyle.core.ui.adaptive

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.seancoyle.core.ui.R
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingLarge
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode

/**
 * A placeholder composable to display when no detail item is selected in a two-pane layout.
 * This provides visual feedback to the user that they should select an item from the list.
 */
@Composable
fun DetailPlaceholder(
    modifier: Modifier = Modifier,
    message: Int = R.string.select_item_placeholder
) {
    val message = stringResource(message)
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .semantics {
                contentDescription = message
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Icon(
            imageVector = Icons.Default.RocketLaunch,
            contentDescription = stringResource(R.string.rocket_icon_desc),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = paddingLarge),

            )
        AppText.bodyLarge(
            text = message,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@PreviewDarkLightMode
@Composable
fun DetailPlaceholderPreview() {
    AppTheme {
        DetailPlaceholder()
    }
}
