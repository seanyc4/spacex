package com.seancoyle.feature.launch.presentation.launches.filter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import com.seancoyle.core.test.testags.LaunchesTestTags
import com.seancoyle.core.ui.designsystem.buttons.ButtonPrimary
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingMedium
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.R
import com.seancoyle.feature.launch.presentation.launch.model.LaunchStatus
import com.seancoyle.feature.launch.presentation.launch.model.getLaunchStatusColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheetRoute(
    currentQuery: String,
    currentStatus: LaunchStatus,
    onApplyFilters: (FilterResult) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<FilterBottomSheetViewModel, FilterBottomSheetViewModel.Factory>(
        creationCallback = { factory ->
            factory.create(
                initialQuery = currentQuery,
                initialStatus = currentStatus
            )
        }
    )

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(Unit) {
        viewModel.filterResult.collect { result ->
            onApplyFilters(result)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.dismissEvent.collect {
            onDismiss()
        }
    }

    FilterBottomSheet(
        state = viewModel.state,
        sheetState = sheetState,
        onEvent = viewModel::onEvent,
        onDismiss = onDismiss,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    state: FilterBottomSheetState,
    sheetState: SheetState,
    onEvent: (FilterBottomSheetEvent) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val windowSizeClass = currentWindowAdaptiveInfo(supportLargeAndXLargeWidth = true).windowSizeClass
    val isExpandedScreen = windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = AppTheme.colors.surface,
        contentColor = AppTheme.colors.onSurface,
        dragHandle = { FilterBottomSheetDragHandle() },
        shape = RoundedCornerShape(
            topStart = Dimens.cornerRadiusLarge,
            topEnd = Dimens.cornerRadiusLarge
        ),
        modifier = modifier.testTag(LaunchesTestTags.FILTER_DIALOG)
    ) {
        if (isExpandedScreen) {
            ExpandedFilterContent(
                state = state,
                onEvent = onEvent,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
            )
        } else {
            CompactFilterContent(
                state = state,
                onEvent = onEvent,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
            )
        }
    }
}

@Composable
private fun FilterBottomSheetDragHandle() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        // Drag indicator
        Box(
            modifier = Modifier
                .width(32.dp)
                .height(4.dp)
                .padding(vertical = 0.dp)
                .then(
                    Modifier.semantics { contentDescription = "Drag handle" }
                )
        ) {
            Canvas(
                modifier = Modifier
                    .width(32.dp)
                    .height(4.dp)
            ) {
                drawRoundRect(
                    color = Color.Gray.copy(alpha = 0.4f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(2.dp.toPx())
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

/**
 * Compact layout for phones (portrait)
 * Single column, stacked sections
 */
@Composable
internal fun CompactFilterContent(
    state: FilterBottomSheetState,
    onEvent: (FilterBottomSheetEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Dimens.paddingXLarge)
            .padding(bottom = Dimens.paddingXLarge)
    ) {
        FilterHeader(
            hasActiveFilters = state.hasActiveFilters,
            activeFilterCount = state.activeFilterCount,
            onClearAll = { onEvent(FilterBottomSheetEvent.ClearAllFilters) }
        )

        Spacer(modifier = Modifier.height(Dimens.paddingXLarge))

        SearchSection(
            query = state.query,
            onQueryChanged = { onEvent(FilterBottomSheetEvent.QueryChanged(it)) },
            onClear = { onEvent(FilterBottomSheetEvent.QueryChanged("")) }
        )

        if (state.recentSearches.isNotEmpty()) {
            Spacer(modifier = Modifier.height(Dimens.paddingMedium))
            RecentSearchesSection(
                recentSearches = state.recentSearches,
                onSearchSelected = { onEvent(FilterBottomSheetEvent.RecentSearchSelected(it)) }
            )
        }

        Spacer(modifier = Modifier.height(Dimens.paddingXLarge))
        HorizontalDivider(color = AppTheme.colors.outlineVariant)
        Spacer(modifier = Modifier.height(Dimens.paddingXLarge))

        StatusFilterSection(
            selectedStatus = state.selectedStatus,
            onStatusSelected = { onEvent(FilterBottomSheetEvent.StatusSelected(it)) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        FilterActionButtons(
            hasActiveFilters = state.hasActiveFilters,
            onApply = { onEvent(FilterBottomSheetEvent.ApplyFilters) },
            onDismiss = { onEvent(FilterBottomSheetEvent.Dismiss) }
        )
    }
}

/**
 * Expanded layout for tablets/landscape
 * Two columns side by side
 */
@Composable
internal fun ExpandedFilterContent(
    state: FilterBottomSheetState,
    onEvent: (FilterBottomSheetEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(bottom = Dimens.paddingXLarge)
    ) {
        FilterHeader(
            hasActiveFilters = state.hasActiveFilters,
            activeFilterCount = state.activeFilterCount,
            onClearAll = { onEvent(FilterBottomSheetEvent.ClearAllFilters) }
        )

        Spacer(modifier = Modifier.height(Dimens.paddingXLarge))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Left column: Search
            Column(modifier = Modifier.weight(1f)) {
                SearchSection(
                    query = state.query,
                    onQueryChanged = { onEvent(FilterBottomSheetEvent.QueryChanged(it)) },
                    onClear = { onEvent(FilterBottomSheetEvent.QueryChanged("")) }
                )

                if (state.recentSearches.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(paddingMedium))
                    RecentSearchesSection(
                        recentSearches = state.recentSearches,
                        onSearchSelected = { onEvent(FilterBottomSheetEvent.RecentSearchSelected(it)) }
                    )
                }
            }

            // Right column: Status filters
            Column(modifier = Modifier.weight(1f)) {
                StatusFilterSection(
                    selectedStatus = state.selectedStatus,
                    onStatusSelected = { onEvent(FilterBottomSheetEvent.StatusSelected(it)) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        FilterActionButtons(
            hasActiveFilters = state.hasActiveFilters,
            onApply = { onEvent(FilterBottomSheetEvent.ApplyFilters) },
            onDismiss = { onEvent(FilterBottomSheetEvent.Dismiss) }
        )
    }
}

@Composable
private fun FilterHeader(
    hasActiveFilters: Boolean,
    activeFilterCount: Int,
    onClearAll: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AppText.headlineMedium(
                text = stringResource(R.string.filter_options),
                color = AppTheme.colors.onSurface
            )
            if (activeFilterCount > 0) {
                Spacer(modifier = Modifier.width(8.dp))
                FilterCountBadge(count = activeFilterCount)
            }
        }

        AnimatedVisibility(visible = hasActiveFilters) {
            TextButton(onClick = onClearAll) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    tint = AppTheme.colors.error,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                AppText.labelMedium(
                    text = stringResource(R.string.text_clear_all),
                    color = AppTheme.colors.error
                )
            }
        }
    }
}

@Composable
private fun FilterCountBadge(count: Int) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .then(
                Modifier.semantics {
                    contentDescription = "$count active filters"
                }
            ),
        contentAlignment = Alignment.Center
    ) {
       Canvas(modifier = Modifier.size(24.dp)) {
            drawCircle(color = Color(0xFF4CAF50).copy(alpha = 0.2f))
        }
        AppText.labelSmall(
            text = count.toString(),
            color = Color(0xFF4CAF50)
        )
    }
}

@Composable
private fun SearchSection(
    query: String,
    onQueryChanged: (String) -> Unit,
    onClear: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column {
        AppText.titleSmall(
            text = stringResource(R.string.search),
            color = AppTheme.colors.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(Dimens.paddingMedium))
        OutlinedTextField(
            value = query,
            onValueChange = { if (it.length <= 32) onQueryChanged(it) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag(LaunchesTestTags.FILTER_SEARCH),
            placeholder = {
                AppText.bodyMedium(
                    text = stringResource(R.string.mission_name),
                    color = AppTheme.colors.onSurfaceVariant.copy(alpha = 0.6f)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = AppTheme.colors.onSurfaceVariant
                )
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = onClear) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.text_cancel),
                            tint = AppTheme.colors.onSurfaceVariant
                        )
                    }
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AppTheme.colors.primary,
                unfocusedBorderColor = AppTheme.colors.outlineVariant,
                cursorColor = AppTheme.colors.primary
            ),
            shape = RoundedCornerShape(Dimens.cornerRadiusMedium)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RecentSearchesSection(
    recentSearches: List<String>,
    onSearchSelected: (String) -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = Dimens.paddingSmall)
        ) {
            Icon(
                imageVector = Icons.Default.History,
                contentDescription = stringResource(R.string.recent_searches),
                modifier = Modifier.size(16.dp),
                tint = AppTheme.colors.onSurfaceVariant.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.width(4.dp))
            AppText.labelSmall(
                text = stringResource(R.string.recent_searches),
                color = AppTheme.colors.onSurfaceVariant.copy(alpha = 0.6f)
            )
        }
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            recentSearches.forEach { search ->
                SuggestionChip(
                    onClick = { onSearchSelected(search) },
                    label = {
                        AppText.labelSmall(
                            text = search,
                            color = AppTheme.colors.onSurfaceVariant
                        )
                    },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = AppTheme.colors.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    border = null,
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun StatusFilterSection(
    selectedStatus: LaunchStatus,
    onStatusSelected: (LaunchStatus) -> Unit
) {
    Column {
        AppText.titleSmall(
            text = stringResource(R.string.launch_status),
            color = AppTheme.colors.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(paddingMedium))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            getLaunchStatusColors().forEach { (status, color) ->
                StatusFilterChip(
                    status = status,
                    isSelected = selectedStatus == status,
                    accentColor = color,
                    onSelected = { onStatusSelected(status) }
                )
            }
        }
    }
}

@Composable
private fun StatusFilterChip(
    status: LaunchStatus,
    isSelected: Boolean,
    accentColor: Color?,
    onSelected: () -> Unit
) {
    val chipColor = accentColor ?: AppTheme.colors.primary

    FilterChip(
        selected = isSelected,
        onClick = onSelected,
        label = {
            AppText.labelMedium(
                text = status.abbrev,
                color = if (isSelected) {
                    AppTheme.colors.onPrimary
                } else {
                    AppTheme.colors.onSurfaceVariant
                }
            )
        },
        leadingIcon = if (isSelected) {
            {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = AppTheme.colors.onPrimary
                )
            }
        } else null,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = AppTheme.colors.surfaceVariant.copy(alpha = 0.5f),
            selectedContainerColor = chipColor,
            labelColor = AppTheme.colors.onSurfaceVariant,
            selectedLabelColor = AppTheme.colors.onPrimary
        ),
        border = FilterChipDefaults.filterChipBorder(
            borderColor = AppTheme.colors.outlineVariant,
            selectedBorderColor = chipColor,
            enabled = true,
            selected = isSelected
        ),
        modifier = Modifier
            .testTag(LaunchesTestTags.FILTER_STATUS_CHIP + "_${status.name}")
            .semantics { contentDescription = "Filter by ${status.label}" }
    )
}

@Composable
private fun FilterActionButtons(
    hasActiveFilters: Boolean,
    onApply: () -> Unit,
    onDismiss: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Cancel button
        TextButton(
            onClick = onDismiss,
            modifier = Modifier.weight(1f)
        ) {
            AppText.bodyLarge(
                text = stringResource(R.string.text_cancel),
                color = AppTheme.colors.onSurfaceVariant
            )
        }

        // Apply button
        ButtonPrimary(
            text = if (hasActiveFilters) {
                stringResource(R.string.text_apply_filters)
            } else {
                stringResource(R.string.text_search)
            },
            onClick = onApply,
            modifier = Modifier.weight(1f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewDarkLightMode
@Composable
private fun FilterBottomSheetPreview() {
    AppTheme {
        CompactFilterContent(
            state = FilterBottomSheetState(
                query = "Falcon",
                selectedStatus = LaunchStatus.SUCCESS,
                recentSearches = listOf("Starlink", "Dragon")
            ),
            onEvent = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewDarkLightMode
@Composable
private fun FilterBottomSheetExpandedPreview() {
    AppTheme {
        ExpandedFilterContent(
            state = FilterBottomSheetState(
                query = "",
                selectedStatus = LaunchStatus.ALL
            ),
            onEvent = {}
        )
    }
}

@PreviewDarkLightMode
@Composable
private fun StatusFilterChipPreview() {
    AppTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatusFilterChip(
                status = LaunchStatus.SUCCESS,
                isSelected = true,
                accentColor = Color(0xFF4CAF50),
                onSelected = {}
            )
            StatusFilterChip(
                status = LaunchStatus.FAILED,
                isSelected = false,
                accentColor = Color(0xFFF44336),
                onSelected = {}
            )
        }
    }
}

@PreviewDarkLightMode
@Composable
private fun FilterHeaderPreview() {
    AppTheme {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            FilterHeader(
                hasActiveFilters = false,
                activeFilterCount = 0,
                onClearAll = {}
            )
            FilterHeader(
                hasActiveFilters = true,
                activeFilterCount = 2,
                onClearAll = {}
            )
        }
    }
}

@PreviewDarkLightMode
@Composable
private fun FilterCountBadgePreview() {
    AppTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterCountBadge(count = 1)
            FilterCountBadge(count = 3)
            FilterCountBadge(count = 5)
        }
    }
}

@PreviewDarkLightMode
@Composable
private fun SearchSectionPreview() {
    AppTheme {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            SearchSection(
                query = "",
                onQueryChanged = {},
                onClear = {}
            )
            SearchSection(
                query = "Falcon 9",
                onQueryChanged = {},
                onClear = {}
            )
        }
    }
}

@PreviewDarkLightMode
@Composable
private fun RecentSearchesSectionPreview() {
    AppTheme {
        RecentSearchesSection(
            recentSearches = listOf("Starlink", "Dragon", "Falcon Heavy"),
            onSearchSelected = {}
        )
    }
}

@PreviewDarkLightMode
@Composable
private fun StatusFilterSectionPreview() {
    AppTheme {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            StatusFilterSection(
                selectedStatus = LaunchStatus.ALL,
                onStatusSelected = {}
            )
            StatusFilterSection(
                selectedStatus = LaunchStatus.SUCCESS,
                onStatusSelected = {}
            )
        }
    }
}

@PreviewDarkLightMode
@Composable
private fun FilterActionButtonsPreview() {
    AppTheme {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            FilterActionButtons(
                hasActiveFilters = false,
                onApply = {},
                onDismiss = {}
            )
            FilterActionButtons(
                hasActiveFilters = true,
                onApply = {},
                onDismiss = {}
            )
        }
    }
}

@PreviewDarkLightMode
@Composable
private fun FilterBottomSheetDragHandlePreview() {
    AppTheme {
        FilterBottomSheetDragHandle()
    }
}

@PreviewDarkLightMode
@Composable
private fun CompactFilterContentEmptyStatePreview() {
    AppTheme {
        CompactFilterContent(
            state = FilterBottomSheetState(
                query = "",
                selectedStatus = LaunchStatus.ALL,
                recentSearches = emptyList()
            ),
            onEvent = {}
        )
    }
}

@PreviewDarkLightMode
@Composable
private fun ExpandedFilterContentWithFiltersPreview() {
    AppTheme {
        ExpandedFilterContent(
            state = FilterBottomSheetState(
                query = "SpaceX",
                selectedStatus = LaunchStatus.GO,
                recentSearches = listOf("Falcon", "Dragon")
            ),
            onEvent = {}
        )
    }
}

