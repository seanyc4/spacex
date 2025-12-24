package com.seancoyle.feature.launch.implementation.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.seancoyle.feature.launch.implementation.domain.model.LaunchQuery
import com.seancoyle.core.domain.Order
import com.seancoyle.core.ui.NotificationState
import com.seancoyle.feature.launch.implementation.domain.model.LaunchStatus
import com.seancoyle.feature.launch.implementation.domain.usecase.component.LaunchesComponent
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchesEvents
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchesEvents.*
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchesScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import androidx.paging.map
import com.seancoyle.feature.launch.implementation.presentation.model.LaunchUi
import com.seancoyle.feature.launch.implementation.presentation.model.LaunchUiMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow

private const val TAG = "LaunchViewModel"

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
internal class LaunchViewModel @Inject constructor(
    private val launchesComponent: LaunchesComponent,
    private val uiMapper: LaunchUiMapper,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var screenState by savedStateHandle.saveable { mutableStateOf(LaunchesScreenState()) }
        private set

    private val _notificationEvents = Channel<NotificationState>(Channel.BUFFERED)
    val notificationEvents = _notificationEvents.receiveAsFlow()

    private val _refreshEvent = Channel<Unit>(Channel.BUFFERED)
    val refreshEvent = _refreshEvent.receiveAsFlow()

    private val _launchQueryState = MutableStateFlow(LaunchQuery())
    val launchQueryState = _launchQueryState.asStateFlow()

    init {
        viewModelScope.launch {
            Timber.tag(TAG).d("screenState before init: $screenState")
            restoreFilterAndOrderState()
            Timber.tag(TAG).d("screenState after init: $screenState")
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val feedState: Flow<PagingData<LaunchUi>> = launchQueryState
        .flatMapLatest { launchQuery ->
            Timber.tag(TAG).d("Creating new pager for query: $launchQuery")
            launchesComponent.observeLaunchesUseCase(launchQuery)
                .map { pagingData ->
                    pagingData.map { launch ->
                        uiMapper(launch)
                    }
                }
        }
        .cachedIn(viewModelScope)

    private fun restoreFilterAndOrderState() {
        viewModelScope.launch {
            val result = launchesComponent.getLaunchPreferencesUseCase()
            setLaunchFilterState(
                order = result.order,
                launchStatus = result.launchStatus,
                query = result.launchYear
            )
        }
    }

    fun onEvent(event: LaunchesEvents) = viewModelScope.launch {
        when (event) {
            is DismissFilterDialogEvent -> displayFilterDialog(false)
            is DisplayFilterDialogEvent -> displayFilterDialog(true)
            is NewSearchEvent -> newSearch()
            is UpdateScrollPositionEvent -> setScrollPositionState(event.position)
            is UpdateFilterStateEvent -> setLaunchFilterState(
                order = event.order,
                launchStatus = event.launchStatus,
                query = event.launchYear
            )
            is SwipeToRefreshEvent -> swipeToRefresh()
        }
    }

    private suspend fun newSearch() {
        saveLaunchPreferences(order = getOrderState())
        displayFilterDialog(false)
        _launchQueryState.value = LaunchQuery(
            query = getQueryState(),
            order = getOrderState()
        )
    }

    private fun getQueryState() = screenState.query
    private fun getOrderState() = screenState.order

    private fun clearQueryParameters() {
        setLaunchFilterState(
            order = Order.ASC,
            launchStatus = LaunchStatus.ALL,
            query = ""
        )
    }

    private suspend fun swipeToRefresh() {
        clearQueryParameters()
        _refreshEvent.send(Unit)
    }

    private fun setLaunchFilterState(
        order: Order,
        launchStatus: LaunchStatus,
        query: String
    ) {
        screenState = screenState.copy(
            order = order,
            launchStatus = launchStatus,
            query = query
        )
        _launchQueryState.value = LaunchQuery(
            query = query,
            order = order
        )
        Timber.tag(TAG).d("Updated filterState: order=$order, status=$launchStatus, year=$query")
    }

    private fun displayFilterDialog(isDisplayed: Boolean) {
        screenState = screenState.copy(isVisible = isDisplayed)
        Timber.tag(TAG).d("Updated filterState.isVisible: $isDisplayed")
    }

    private fun setScrollPositionState(position: Int) {
        screenState = screenState.copy(scrollPosition = position)
        Timber.tag(TAG).d("Updated scrollState.scrollPosition: $position")
    }

    private suspend fun saveLaunchPreferences(order: Order) {
        launchesComponent.saveLaunchPreferencesUseCase(order)
    }

}
