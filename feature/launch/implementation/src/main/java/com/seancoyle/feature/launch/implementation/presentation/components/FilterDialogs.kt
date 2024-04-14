package com.seancoyle.feature.launch.implementation.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchesFilterState
import com.seancoyle.launch.implementation.R

@Composable
fun FilterDialog(
    currentFilterState: LaunchesFilterState,
    updateFilterState: (Order, LaunchStatus, String) -> Unit,
    onDismiss: (Boolean) -> Unit,
    newSearch: () -> Unit,
    isLandScape: Boolean,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { onDismiss(false) },
        title = { Text(stringResource(R.string.filter_options)) },
        text = {
            if (isLandScape){
                LandscapeDialogContent(currentFilterState, updateFilterState, modifier)
            } else {
                PortraitDialogContent(currentFilterState, updateFilterState, modifier)
            }
        },

        confirmButton = {
            ConfirmButton(newSearch)
        },

        dismissButton = {
            DismissButton(onDismiss)
        }
    )
}

@Composable
private fun PortraitDialogContent(
    filterState: LaunchesFilterState,
    updateFilterState: (Order, LaunchStatus, String) -> Unit,
    modifier: Modifier
) {
    Column {
        Text(stringResource(R.string.filter_by_year))

        YearInputField(year = filterState.launchYear, onYearChange = { year ->
            updateFilterState(filterState.order, filterState.launchStatus, year)
        })

        Text(
            text = stringResource(R.string.launch_status),
            modifier = modifier.padding(top = dimensionResource(R.dimen.default_view_margin))
        )

        RadioGroup(
            selectedLaunchStatus = filterState.launchStatus,
            onLaunchStatusSelected = { status ->
                updateFilterState(filterState.order, status, filterState.launchYear)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OrderSwitch(order = filterState.order, onOrderChange = { order ->
            updateFilterState(order, filterState.launchStatus, filterState.launchYear)
        })
    }
}

@Composable
private fun LandscapeDialogContent(
    filterState: LaunchesFilterState,
    updateFilterState: (Order, LaunchStatus, String) -> Unit,
    modifier: Modifier
) {
    Row(modifier = modifier.fillMaxWidth()) {

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            Text(stringResource(R.string.filter_by_year))

            YearInputField(year = filterState.launchYear, onYearChange = { year ->
                updateFilterState(filterState.order, filterState.launchStatus, year)
            })

            Spacer(modifier = Modifier.height(16.dp))

            OrderSwitch(order = filterState.order, onOrderChange = { order ->
                updateFilterState(order, filterState.launchStatus, filterState.launchYear)
            })
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 6.dp)
        ) {
            Text(text = stringResource(R.string.launch_status))
            RadioGroup(
                selectedLaunchStatus = filterState.launchStatus,
                onLaunchStatusSelected = { status ->
                    updateFilterState(filterState.order, status, filterState.launchYear)
                }
            )
        }
    }
}

@Composable
private fun DismissButton(onDismiss: (Boolean) -> Unit) {
    Button(onClick = { onDismiss(false) }) {
        Text(stringResource(R.string.text_cancel))
    }
}

@Composable
private fun ConfirmButton(newSearch: () -> Unit) {
    Button(onClick = { newSearch() }) {
        Text(stringResource(R.string.text_search))
    }
}

@Composable
fun YearInputField(
    year: String,
    onYearChange: (String) -> Unit,
    maxChar: Int = 4
) {
    OutlinedTextField(
        value = year,
        onValueChange = { if (it.length <= maxChar) onYearChange(it) },
        label = { Text(stringResource(R.string.year)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
fun OrderSwitch(
    order: Order,
    onOrderChange: (Order) -> Unit
) {
    Text(text = stringResource(R.string.asc_desc))
    Switch(
        checked = order == Order.ASC,
        onCheckedChange = { newValue -> onOrderChange(if (newValue) Order.ASC else Order.DESC) }
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
                    onClick = { onLaunchStatusSelected(option) },
                    modifier = modifier.size(40.dp)
                )
                Text(
                    text = option.name,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}