package com.seancoyle.feature.launch.implementation.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.seancoyle.core.domain.Order
import com.seancoyle.core.ui.NotificationState
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.implementation.presentation.model.UIErrors
import com.seancoyle.feature.launch.implementation.domain.usecase.component.LaunchesComponent
import com.seancoyle.feature.launch.implementation.presentation.model.LinksUi
import com.seancoyle.feature.launch.implementation.presentation.state.BottomSheetUiState
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchEvents
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchEvents.*
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchesScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import androidx.paging.map
import com.seancoyle.feature.launch.implementation.presentation.model.LaunchUi
import com.seancoyle.feature.launch.implementation.presentation.model.LaunchUiMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val TAG = "LaunchViewModel"

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
internal class LaunchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val launchesComponent: LaunchesComponent,
    private val mapToUi: LaunchUiMapper,
) : ViewModel() {

    var screenState by savedStateHandle.saveable { mutableStateOf(LaunchesScreenState()) }
        private set

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
            Timber.tag(TAG).d("screenState before init: $screenState")
            restoreFilterAndOrderState()
            Timber.tag(TAG).d("screenState after init: $screenState")
        }
    }

    val feedState: Flow<PagingData<LaunchUi>> = launchesComponent.observeLaunchesUseCase()
        .map { pagingData ->
            pagingData.map { launch ->
                mapToUi(launch)
            }
        }
        .cachedIn(viewModelScope)

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

    fun onEvent(event: LaunchEvents) = viewModelScope.launch {
        when (event) {
            is DismissBottomSheetEvent -> dismissBottomSheet()
            is DismissFilterDialogEvent -> displayFilterDialog(false)
            is DisplayFilterDialogEvent -> displayFilterDialog(true)
            is DismissNotificationEvent -> dismissNotification()
            is HandleLaunchClickEvent -> handleLaunchClick(event.links)
            is NewSearchEvent -> newSearch()
            is NotificationEvent -> updateNotificationState(event)
            is OpenLinkEvent -> openLink(event.url)
            is UpdateScrollPositionEvent -> setScrollPositionState(event.position)
            is UpdateFilterStateEvent -> setLaunchFilterState(
                order = event.order,
                launchStatus = event.launchStatus,
                year = event.launchYear
            )
            is SwipeToRefreshEvent -> swipeToRefresh()
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

    private fun getSearchYearState() = screenState.launchYear
    private fun getOrderState() = screenState.order
    private fun getLaunchStatusState() = screenState.launchStatus

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
        //loadData()
    }

    private fun setLaunchFilterState(
        order: Order,
        launchStatus: LaunchStatus,
        year: String
    ) {
        screenState = screenState.copy(
            order = order,
            launchStatus = launchStatus,
            launchYear = year
        )
        Timber.tag(TAG).d("Updated filterState: order=$order, status=$launchStatus, year=$year")
    }

    private fun displayFilterDialog(isDisplayed: Boolean) {
        screenState = screenState.copy(isVisible = isDisplayed)
        Timber.tag(TAG).d("Updated filterState.isVisible: $isDisplayed")
    }

    private fun setScrollPositionState(position: Int) {
        screenState = screenState.copy(scrollPosition = position)
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
