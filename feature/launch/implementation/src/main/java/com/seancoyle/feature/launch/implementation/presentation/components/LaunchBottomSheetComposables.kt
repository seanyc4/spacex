package com.seancoyle.feature.launch.implementation.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.seancoyle.feature.launch.implementation.R

@Composable
fun LaunchBottomSheetDivider(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(MaterialTheme.colorScheme.primary)
    )
}

@Composable
fun LaunchBottomSheetHeader(
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = R.string.links),
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(bottom = dimensionResource(id = R.dimen.default_bottom_sheet_margin))
    )
}

@Composable
fun LaunchBottomSheetTitle(
    name: String,
    actionLinkClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = name,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.secondary,
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.default_bottom_sheet_margin))
            .focusable(enabled = true)
            .clickable { actionLinkClicked() }
    )
}

@Composable
fun LaunchBottomSheetExitButton(
    actionExitClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = actionExitClicked,
        modifier = modifier
            .fillMaxWidth()
            .height(height = 60.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Text(
            text = stringResource(id = R.string.text_cancel),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.error
        )
    }
    Spacer(modifier = modifier.height(50.dp))
}
