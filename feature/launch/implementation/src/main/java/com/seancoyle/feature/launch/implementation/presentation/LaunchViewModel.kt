package com.seancoyle.feature.launch.implementation.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.seancoyle.core.common.coroutines.stateIn
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.AppStringResource
import com.seancoyle.core.domain.Order
import com.seancoyle.core.ui.NotificationState
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.implementation.presentation.model.UIErrors
import com.seancoyle.feature.launch.implementation.domain.usecase.component.LaunchesComponent
import com.seancoyle.feature.launch.implementation.domain.usecase.launch.PaginationResult
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.Lazy
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

private const val TAG = "LaunchViewModel"

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
internal class LaunchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val launchesComponent: LaunchesComponent,
    private val appStringResource: Lazy<AppStringResource>
) : ViewModel() {

    var scrollState by savedStateHandle.saveable { mutableStateOf(LaunchesScrollState()) }
        private set

    var filterState by savedStateHandle.saveable { mutableStateOf(LaunchesFilterState()) }
        private set

    private val _paginationState = MutableStateFlow<PaginationState>(PaginationState.Idle)
    val paginationState = _paginationState.asStateFlow()

    private val _notificationState = MutableStateFlow<NotificationState?>(null)
    val notificationState = _notificationState.asStateFlow()

    private val _bottomSheetState = MutableStateFlow(BottomSheetUiState())
    val bottomSheetState = _bottomSheetState.asStateFlow()

    private val _linkEvent = MutableSharedFlow<String>(replay = 0)
    val linkEvent = _linkEvent.asSharedFlow()

    private val _errorEvent = MutableSharedFlow<UIErrors>(replay = 0)
    val errorEvent = _errorEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            restoreFilterAndOrderState()
            loadNextPage()
        }
    }

    val feedState: StateFlow<LaunchesUiState> =
        launchesComponent.observeLaunchesUseCase()
            .map { result ->
                when (result) {
                    is LaunchResult.Success -> {
                        LaunchesUiState.Success(
                            launches = result.data.map { launch ->
                                launch.toUiModel(appStringResource)
                            }
                        )
                    }
                    is LaunchResult.Error -> {
                        LaunchesUiState.Error()
                    }
                }
            }
            .stateIn(
                scope = viewModelScope,
                initialValue = LaunchesUiState.Loading
            )

    private fun loadNextPage() {

        viewModelScope.launch {
            launchesComponent.paginateLaunchesUseCase()
                .onStart {
                    shouldLoadNextPage()
                    _paginationState.update { PaginationState.Loading }
                }
                .collect { result ->
                when (result) {
                    is LaunchResult.Success -> {
                        when (result.data) {
                            is PaginationResult.Success -> {
                                _paginationState.update { PaginationState.Idle }
                            }
                            is PaginationResult.EndReached -> {
                                _paginationState.update { PaginationState.EndReached }
                            }
                        }
                    }
                    is LaunchResult.Error -> {
                        _paginationState.update { PaginationState.Error }
                    }
                }
            }
        }
    }

    private fun shouldLoadNextPage() {
        if (_paginationState.value == PaginationState.Loading ||
            _paginationState.value == PaginationState.EndReached) {
            Timber.tag(TAG).d("Pagination blocked: state = ${_paginationState.value}")
            return
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
                is DismissBottomSheetEvent -> dismissBottomSheet()
                is DismissFilterDialogEvent -> displayFilterDialog(false)
                is DisplayFilterDialogEvent -> displayFilterDialog(true)
                is DismissNotificationEvent -> dismissNotification()
                is HandleLaunchClickEvent -> handleLaunchClick(event.links)
                is LoadNextPageEvent -> loadNextPage()
                is NewSearchEvent -> newSearch()
                is NotificationEvent -> updateNotificationState(event)
                is OpenLinkEvent -> openLink(event.url)
                is SaveScrollPositionEvent -> setScrollPositionState(event.position)
                is SetFilterStateEvent -> setLaunchFilterState(
                    order = event.order,
                    launchStatus = event.launchStatus,
                    year = event.launchYear
                )
                is SwipeToRefreshEvent -> swipeToRefresh()
            }
        }
    }

    private fun updateNotificationState(event: NotificationEvent) {
        _notificationState.update { event.notificationState }
    }

    private suspend fun openLink(link: String) {
        _linkEvent.emit(link)
    }

    private fun dismissNotification() {
        _notificationState.update { null }
    }

    private suspend fun newSearch() {
        saveLaunchPreferences(
            order = getOrderState(),
            launchStatus = getLaunchStatusState(),
            launchYear = getSearchYearState()
        )
        displayFilterDialog(false)
        // TODO: Implement search/filter logic in data layer
    }

    private fun getSearchYearState() = filterState.launchYear
    private fun getOrderState() = filterState.order
    private fun getLaunchStatusState() = filterState.launchStatus

    private fun clearQueryParameters() {
        setLaunchFilterState(
            order = Order.DESC,
            launchStatus = LaunchStatus.ALL,
            year = ""
        )
    }

    private fun swipeToRefresh() {
        clearQueryParameters()
        // Reset pagination state to allow fetching again
        _paginationState.update { PaginationState.Idle }
        loadNextPage()
    }

    private fun setLaunchFilterState(
        order: Order,
        launchStatus: LaunchStatus,
        year: String
    ) {
        filterState = filterState.copy(
            order = order,
            launchStatus = launchStatus,
            launchYear = year
        )
        Timber.tag(TAG).d("Updated filterState: order=$order, status=$launchStatus, year=$year")
    }

    private fun displayFilterDialog(isDisplayed: Boolean) {
        filterState = filterState.copy(isVisible = isDisplayed)
        Timber.tag(TAG).d("Updated filterState.isVisible: $isDisplayed")
    }

    private fun setScrollPositionState(position: Int) {
        scrollState = scrollState.copy(scrollPosition = position)
        Timber.tag(TAG).d("Updated scrollState.scrollPosition: $position")
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

}
