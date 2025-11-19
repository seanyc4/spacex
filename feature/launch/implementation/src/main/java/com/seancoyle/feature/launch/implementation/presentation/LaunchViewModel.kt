package com.seancoyle.feature.launch.implementation.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.AppStringResource
import com.seancoyle.core.domain.Order
import com.seancoyle.core.ui.NotificationState
import com.seancoyle.core.ui.NotificationType
import com.seancoyle.core.ui.UiComponentType
import com.seancoyle.core.ui.extensions.asStringResource
import com.seancoyle.feature.launch.api.LaunchConstants.PAGINATION_PAGE_SIZE
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.implementation.presentation.model.UIErrors
import com.seancoyle.feature.launch.implementation.domain.usecase.component.LaunchesComponent
import com.seancoyle.feature.launch.implementation.presentation.model.LinksUi
import com.seancoyle.feature.launch.implementation.presentation.state.BottomSheetUiState
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchEvents
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchEvents.*
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchesFilterState
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchesScrollState
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchesUiState
import com.seancoyle.feature.launch.implementation.presentation.state.PaginationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.Lazy
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
internal class LaunchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val launchesComponent: LaunchesComponent,
    private val appStringResource: Lazy<AppStringResource>
) : ViewModel() {

    private val _uiState: MutableStateFlow<LaunchesUiState> = MutableStateFlow(LaunchesUiState.Loading)
    var uiState = _uiState.asStateFlow()

    private val _filterState = MutableStateFlow(LaunchesFilterState())
    var filterState = _filterState.asStateFlow()

    private val _scrollState = MutableStateFlow(LaunchesScrollState())
    var scrollState = _scrollState.asStateFlow()

    private val _bottomSheetState = MutableStateFlow(BottomSheetUiState())
    var bottomSheetState = _bottomSheetState.asStateFlow()

    private val _linkEvent = MutableSharedFlow<String>(replay = 0)
    var linkEvent = _linkEvent.asSharedFlow()

    private val _errorEvent = MutableSharedFlow<UIErrors>(replay = 0)
    var errorEvent = _errorEvent.asSharedFlow()

    fun init() {
        if (_uiState.value is LaunchesUiState.Loading) {
            restoreFilterAndOrderState()
            restoreStateOnProcessDeath()
            loadDataOnAppLaunchOrRestore()
        }
    }

    private fun loadDataOnAppLaunchOrRestore() {
        if (getScrollPositionState() != 0) {
            // Restoring state from cache data
            onEvent(CreateMergedLaunchesEvent)
        } else {
            // Fresh app launch - get data from network
            onEvent(GetSpaceXDataEvent)
        }
    }

    private fun restoreStateOnProcessDeath() {
        savedStateHandle.get<LaunchesScrollState>(LAUNCH_LIST_STATE_KEY)?.let { scrollState ->
            this._scrollState.value = scrollState
        }
    }

    private fun restoreFilterAndOrderState() {
        viewModelScope.launch {
            val result = launchesComponent.getLaunchPreferencesUseCase()
            setLaunchFilterState(
                order = result.order,
                launchStatus = result.launchStatus,
                year = result.launchYear
            )
        }
    }

    fun onEvent(event: LaunchEvents) {
        viewModelScope.launch {
            when (event) {
                is CreateMergedLaunchesEvent -> mergedLaunchesCacheUseCase()
                is DismissBottomSheetEvent -> dismissBottomSheet()
                is DismissFilterDialogEvent -> displayFilterDialog(false)
                is DisplayFilterDialogEvent -> displayFilterDialog(true)
                is DismissNotificationEvent -> dismissNotification()
                is HandleLaunchClickEvent -> handleLaunchClick(event.links)
                is LoadNextPageEvent -> loadNextPage(event.page)
                is NewSearchEvent -> newSearch()
                is NotificationEvent -> updateNotificationState(event)
                is OpenLinkEvent -> openLink(event.url)
                is PaginateLaunchesCacheEvent -> paginateLaunchesCacheUseCase()
                is SaveScrollPositionEvent -> setScrollPositionState(event.position)
                is SetFilterStateEvent -> setLaunchFilterState(
                    order = event.order,
                    launchStatus = event.launchStatus,
                    year = event.launchYear
                )
                is SwipeToRefreshEvent -> swipeToRefresh()
                is GetSpaceXDataEvent -> getSpaceXDataUseCase()
            }
        }
    }

    private fun updateNotificationState(event: NotificationEvent) {
        _uiState.update { currentState ->
            currentState.isSuccess { it.copy(notificationState = event.notificationState) }
        }
    }

    private suspend fun paginateLaunchesCacheUseCase() {
        launchesComponent.paginateLaunchesCacheUseCase(
            year = getSearchYearState(),
            order = getOrderState(),
            launchFilter = getLaunchStatusState(),
            page = getPageState()
        ).collect { result ->
            when (result) {
                is LaunchResult.Success -> {
                    // Pagination - We append the next 30 rows to the current state as a new list
                    // This triggers a recompose and keeps immutability
                    _uiState.update { currentState ->
                        val updatedLaunches = result.data.map { it.toUiModel(appStringResource) }
                        currentState.isSuccess {
                            val paginatedLaunches = it.launches + (updatedLaunches)
                            it.copy(
                                launches = paginatedLaunches,
                                paginationState = PaginationState.None
                            )
                        }
                    }
                }

                is LaunchResult.Error -> {
                    _uiState.update { currentState ->
                        currentState.isSuccess { it.copy(paginationState = PaginationState.Error) }
                    }
                }
            }
        }
    }

    private suspend fun getSpaceXDataUseCase() {
        launchesComponent.getSpaceXDataUseCase()
            .onStart { _uiState.update { LaunchesUiState.Loading } }
            .collect { result ->
                when (result) {
                    is LaunchResult.Success -> onEvent(CreateMergedLaunchesEvent)
                    is LaunchResult.Error -> {
                        _uiState.update {
                            LaunchesUiState.Error(
                                errorNotificationState = NotificationState(
                                    message = result.error.asStringResource(),
                                    uiComponentType = UiComponentType.Dialog,
                                    notificationType = NotificationType.Error
                                )
                            )
                        }
                    }
                }
            }
    }

    private suspend fun mergedLaunchesCacheUseCase() {
        launchesComponent.createMergedLaunchesCacheUseCase(
            year = getSearchYearState(),
            order = getOrderState(),
            launchFilter = getLaunchStatusState(),
            page = getPageState()
        ).distinctUntilChanged()
            .collect { result ->
                when (result) {
                    is LaunchResult.Success -> {
                        _uiState.update {
                            LaunchesUiState.Success(
                                launches = result.data.map { it.toUiModel(appStringResource) },
                                paginationState = PaginationState.None
                            )
                        }
                    }

                    is LaunchResult.Error -> {
                        _uiState.update {
                            LaunchesUiState.Error(
                                errorNotificationState = NotificationState(
                                    message = result.error.asStringResource(),
                                    uiComponentType = UiComponentType.Snackbar,
                                    notificationType = NotificationType.Error
                                )
                            )
                        }
                    }
                }
            }
    }

    private suspend fun openLink(link: String) {
        _linkEvent.emit(link)
    }

    private fun LaunchesUiState.isSuccess(updateState: (LaunchesUiState.Success) -> LaunchesUiState): LaunchesUiState {
        return if (this is LaunchesUiState.Success) {
            updateState(this)
        } else {
            this
        }
    }

    private fun dismissNotification() {
        _uiState.update { currentState ->
            currentState.isSuccess {
                it.copy(notificationState = null)
            }
        }
    }

    private fun clearListState() {
        _uiState.update { currentState ->
            currentState.isSuccess { it.copy(launches = emptyList()) }
        }
    }

    private suspend fun newSearch() {
        clearListState()
        resetPageState()
        newSearchEvent()
        displayFilterDialog(false)
    }

    private fun loadNextPage(position: Int) {
        if ((position + 1) >= (getPageState() * PAGINATION_PAGE_SIZE)) {
            incrementPage()
            onEvent(PaginateLaunchesCacheEvent)
        }
    }

    private fun getScrollPositionState() = _scrollState.value.scrollPosition
    private fun getPageState() = _scrollState.value.page
    private fun getSearchYearState() = _filterState.value.launchYear
    private fun getOrderState() = _filterState.value.order
    private fun getLaunchStatusState() = _filterState.value.launchStatus

    private fun clearQueryParameters() {
        clearListState()
        setLaunchFilterState(
            order = Order.DESC,
            launchStatus = LaunchStatus.ALL,
            year = ""
        )
        resetPageState()
    }

    private fun swipeToRefresh() {
        clearQueryParameters()
        clearListState()
        onEvent(GetSpaceXDataEvent)
    }

    private fun setLaunchFilterState(
        order: Order,
        launchStatus: LaunchStatus,
        year: String
    ) {
        _filterState.update { currentState ->
            currentState.copy(
                order = order,
                launchStatus = launchStatus,
                launchYear = year
            )
        }
    }

    private fun displayFilterDialog(isDisplayed: Boolean) {
        _filterState.update { currentState -> currentState.copy(isVisible = isDisplayed) }
    }

    private fun resetPageState() {
        _scrollState.update { currentState -> currentState.copy(page = 1) }
    }

    private fun setPageState(pageNum: Int) {
        _scrollState.update { currentState -> currentState.copy(page = pageNum) }
    }

    private fun setScrollPositionState(position: Int) {
        _scrollState.update { currentState -> currentState.copy(scrollPosition = position) }
    }

    private fun incrementPage() {
        val incrementedPage = _scrollState.value.page + 1
        _scrollState.update { currentState -> currentState.copy(page = incrementedPage) }
        setPageState(incrementedPage)
    }

    fun saveState() {
        savedStateHandle[LAUNCH_LIST_STATE_KEY] = _scrollState.value
    }

    private suspend fun saveLaunchPreferences(
        order: Order,
        launchStatus: LaunchStatus,
        launchYear: String
    ) {
        launchesComponent.saveLaunchPreferencesUseCase(
            order = order,
            launchStatus = launchStatus,
            launchYear = launchYear
        )
    }

    private suspend fun newSearchEvent() {
        onEvent(CreateMergedLaunchesEvent)
        saveLaunchPreferences(
            order = getOrderState(),
            launchStatus = getLaunchStatusState(),
            launchYear = getSearchYearState()
        )
    }

    private suspend fun handleLaunchClick(links: LinksUi?) {
        val bottomSheetLinks = links.getLinks()

        if (bottomSheetLinks.isNotEmpty()) {
            _bottomSheetState.update { currentState ->
                currentState.copy(
                    isVisible = true,
                    bottomSheetLinks = bottomSheetLinks
                )

            }
        } else {
            _errorEvent.emit(UIErrors.NO_LINKS)
        }
    }

    private fun dismissBottomSheet() {
        _bottomSheetState.update { currentState ->
            currentState.copy(isVisible = false)
        }
    }

    private companion object {
        const val LAUNCH_LIST_STATE_KEY = "launches.list.position.key"
    }
}
