package com.seancoyle.launch.implementation.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.seancoyle.core.domain.Order
import com.seancoyle.launch.api.domain.model.LaunchStatus
import com.seancoyle.launch.implementation.R
import com.seancoyle.launch.implementation.presentation.state.LaunchFilterState

private val maxChar = 4

@Composable
fun FilterDialog(
    filterState: LaunchFilterState,
    year: (String) -> Unit,
    launchStatus: (LaunchStatus) -> Unit,
    order: (Order) -> Unit,
    onDismiss: (Boolean) -> Unit,
    newSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { onDismiss(false) },
        title = { Text(stringResource(R.string.filter_options)) },
        text = {
            Column {
                Text(stringResource(R.string.filter_by_year))

                OutlinedTextField(
                    value = filterState.year,
                    onValueChange = { if (it.length <= maxChar) year(it) },
                    label = { Text(stringResource(R.string.year)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Text(
                    text = stringResource(R.string.launch_status),
                    modifier = modifier.padding(top = dimensionResource(R.dimen.default_view_margin))
                )

                RadioGroup(
                    selectedLaunchStatus = filterState.launchStatus,
                    onLaunchStatusSelected = launchStatus
                )

                Text(
                    text = stringResource(R.string.asc_desc),
                    modifier = modifier.padding(top = dimensionResource(R.dimen.small_view_margins_8dp))
                )

                Switch(
                    checked = filterState.order == Order.ASC,
                    onCheckedChange = { newValue -> order(if (newValue) Order.ASC else Order.DESC) }
                )
            }
        },

        confirmButton = {
            Button(onClick = { newSearch() }) {
                Text(stringResource(R.string.text_search))
            }
        },

        dismissButton = {
            Button(onClick = { onDismiss(false) }) {
                Text(stringResource(R.string.text_cancel))
            }
        }
    )
}

@Composable
fun RadioGroup(
    selectedLaunchStatus: LaunchStatus,
    onLaunchStatusSelected: (LaunchStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        val options = listOf(
            LaunchStatus.ALL,
            LaunchStatus.SUCCESS,
            LaunchStatus.FAILED,
            LaunchStatus.UNKNOWN
        )
        options.forEach { option ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedLaunchStatus == option,
                    onClick = { onLaunchStatusSelected(option) }
                )
                Text(
                    text = option.name,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}