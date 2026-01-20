package com.seancoyle.core.ui.components.error

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.seancoyle.core.ui.R
import com.seancoyle.core.ui.StringResource
import com.seancoyle.core.ui.designsystem.buttons.ButtonPrimary
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingXLarge
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    message: StringResource? = null,
    onRetry: () -> Unit,
) {
    val errorMessage = message?.resolve() ?: stringResource(R.string.network_connection_failed)
    val errorDescription = stringResource(R.string.error_occurred, errorMessage)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingXLarge)
            .semantics {
                contentDescription = errorDescription
                liveRegion = LiveRegionMode.Polite
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Error,
            contentDescription = stringResource(R.string.error_icon_desc),
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        AppText.headlineLarge(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        ButtonPrimary(
            text = stringResource(R.string.retry),
            onClick = onRetry
        )
    }

}

@PreviewDarkLightMode
@Composable
private fun ErrorScreenPreview() {
    AppTheme {
        ErrorScreen(
            message = null,
            onRetry = {}
        )
    }
}
