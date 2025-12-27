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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.seancoyle.core.ui.designsystem.switch.Switch
import com.seancoyle.core.domain.Order
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.Dimens
import com.seancoyle.feature.launch.implementation.domain.model.LaunchStatus
import com.seancoyle.feature.launch.implementation.R
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchesEvents
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchesEvents.DismissFilterDialogEvent
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchesEvents.NewSearchEvent
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchesEvents.UpdateFilterStateEvent
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchesScreenState

@Composable
internal fun LaunchFilterDialog(
    currentFilterState: LaunchesScreenState,
    onEvent: (LaunchesEvents) -> Unit,
    isLandScape: Boolean,
    modifier: Modifier = Modifier
) {
    var localQuery by remember { mutableStateOf(currentFilterState.query) }
    var localOrder by remember { mutableStateOf(currentFilterState.order) }
    var localLaunchStatus by remember { mutableStateOf(currentFilterState.launchStatus) }

    AlertDialog(
        onDismissRequest = { onEvent(DismissFilterDialogEvent) },
        title = { AppText.headlineMedium(stringResource(R.string.filter_options)) },
        text = {
            if (isLandScape) {
                LandscapeDialogContent(
                    query = localQuery,
                    onQueryChange = { localQuery = it },
                    order = localOrder,
                    onOrderChange = { localOrder = it },
                    launchStatus = localLaunchStatus,
                    onLaunchStatusChange = { localLaunchStatus = it },
                    modifier = modifier
                )
            } else {
                PortraitDialogContent(
                    query = localQuery,
                    onQueryChange = { localQuery = it },
                    order = localOrder,
                    onOrderChange = { localOrder = it },
                    launchStatus = localLaunchStatus,
                    onLaunchStatusChange = { localLaunchStatus = it },
                    modifier = modifier
                )
            }
        },
        confirmButton = {
            ConfirmButton {
                onEvent(
                    UpdateFilterStateEvent(
                        order = localOrder,
                        launchStatus = localLaunchStatus,
                        query = localQuery
                    )
                )
                onEvent(NewSearchEvent)
            }
        },
        dismissButton = {
            DismissButton { onEvent(DismissFilterDialogEvent) }
        }
    )
}

@Composable
private fun PortraitDialogContent(
    query: String,
    onQueryChange: (String) -> Unit,
    order: Order,
    onOrderChange: (Order) -> Unit,
    launchStatus: LaunchStatus,
    onLaunchStatusChange: (LaunchStatus) -> Unit,
    modifier: Modifier
) {
    Column {
        AppText.bodyLarge(stringResource(R.string.search))
        QueryInputField(query = query, onQueryChange = onQueryChange)
        AppText.bodyLarge(
            text = stringResource(R.string.launch_status),
            modifier = modifier.padding(top = Dimens.dp16)
        )
        RadioGroup(
            selectedLaunchStatus = launchStatus,
            onLaunchStatusSelected = onLaunchStatusChange
        )
        Spacer(modifier = Modifier.height(Dimens.dp16))
        OrderSwitch(order = order, onOrderChange = onOrderChange)
    }
}

@Composable
private fun LandscapeDialogContent(
    query: String,
    onQueryChange: (String) -> Unit,
    order: Order,
    onOrderChange: (Order) -> Unit,
    launchStatus: LaunchStatus,
    onLaunchStatusChange: (LaunchStatus) -> Unit,
    modifier: Modifier
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            AppText.bodyLarge(stringResource(R.string.filter_by_year))
            QueryInputField(query = query, onQueryChange = onQueryChange)
            Spacer(modifier = Modifier.height(16.dp))
            OrderSwitch(order = order, onOrderChange = onOrderChange)
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 6.dp)
        ) {
            AppText.bodyLarge(text = stringResource(R.string.launch_status))
            RadioGroup(
                selectedLaunchStatus = launchStatus,
                onLaunchStatusSelected = onLaunchStatusChange
            )
        }
    }
}

@Composable
fun QueryInputField(
    query: String,
    onQueryChange: (String) -> Unit,
    maxChar: Int = 16
) {
    OutlinedTextField(
        value = query,
        onValueChange = { if (it.length <= maxChar) onQueryChange(it) },
        label = { Text(stringResource(R.string.query)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
fun OrderSwitch(
    order: Order,
    onOrderChange: (Order) -> Unit
) {
    AppText.bodyLarge(text = stringResource(R.string.asc_desc))
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
                AppText.bodyMedium(
                    text = option.name,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
private fun DismissButton(onEvent: (LaunchesEvents) -> Unit) {
    Button(onClick = { onEvent(DismissFilterDialogEvent) }) {
        Text(stringResource(R.string.text_cancel))
    }
}

@Composable
private fun ConfirmButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(stringResource(R.string.text_search))
    }
}
