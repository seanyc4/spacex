package com.seancoyle.feature.launch.presentation.launches

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.seancoyle.core.common.coroutines.stateIn
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.feature.launch.domain.model.LaunchesQuery
import com.seancoyle.feature.launch.domain.usecase.component.LaunchesComponent
import com.seancoyle.feature.launch.presentation.LaunchUiMapper
import com.seancoyle.feature.launch.presentation.launch.model.LaunchStatus
import com.seancoyle.feature.launch.presentation.launches.model.LaunchesUi
import com.seancoyle.feature.launch.presentation.launches.state.LaunchesEvents
import com.seancoyle.feature.launch.presentation.launches.state.LaunchesState
import com.seancoyle.feature.launch.presentation.launches.state.PagingEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

private const val TAG = "LaunchViewModel"

@OptIn(SavedStateHandleSaveableApi::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class LaunchesViewModel @Inject constructor(
    private val launchesComponent: LaunchesComponent,
    private val uiMapper: LaunchUiMapper,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var screenState by savedStateHandle.saveable { mutableStateOf(LaunchesState()) }
        private set

    private val _upcomingPagingEvents = Channel<PagingEvents>(Channel.BUFFERED)
    val upcomingPagingEvents = _upcomingPagingEvents.receiveAsFlow()

    private val _pastPagingEvents = Channel<PagingEvents>(Channel.BUFFERED)
    val pastPagingEvents = _pastPagingEvents.receiveAsFlow()

    val launchesQueryState: StateFlow<LaunchesQuery> = snapshotFlow {
        val query = screenState.query
        val status = screenState.launchStatus
        LaunchesQuery(
            query = query,
            status = if (status == LaunchStatus.ALL) null else status
        )
    }.stateIn(viewModelScope, LaunchesQuery())

    val upcomingLaunchesFlow: Flow<PagingData<LaunchesUi>> = launchesQueryState
        .flatMapLatest { launchesQuery ->
            Timber.tag(TAG).d("Creating new pager for query: $launchesQuery")
            launchesComponent.observeUpcomingLaunches(launchesQuery)
                .map { pagingData ->
                    pagingData.map { launch ->
                        uiMapper.mapToLaunchesUi(launch)
                    }
                }
        }
        .cachedIn(viewModelScope)

    val pastLaunchesFlow: Flow<PagingData<LaunchesUi>> = launchesQueryState
        .flatMapLatest { launchesQuery ->
            Timber.tag(TAG).d("Creating new pager for query: $launchesQuery")
            launchesComponent.observePastLaunches(launchesQuery)
                .map { pagingData ->
                    pagingData.map { launch ->
                        uiMapper.mapToLaunchesUi(launch)
                    }
                }
        }
        .cachedIn(viewModelScope)

    fun onEvent(event: LaunchesEvents) = viewModelScope.launch {
        when (event) {
            is LaunchesEvents.DismissFilterDialogEvent -> displayFilterDialog(false)
            is LaunchesEvents.DisplayFilterDialogEvent -> displayFilterDialog(true)
            is LaunchesEvents.PullToRefreshEvent -> onPullToRefresh()
            is LaunchesEvents.RetryFetchEvent -> onRetryFetch()
            is LaunchesEvents.TabSelectedEvent -> onTabSelected(event.launchesType)
            is LaunchesEvents.UpdateFilterStateEvent -> setLaunchFilterState(
                launchStatus = event.launchStatus,
                query = event.query
            )
        }
    }

    private fun clearQueryParameters() {
        setLaunchFilterState(
            query = "",
            launchStatus = LaunchStatus.ALL
        )
    }

    private suspend fun onPullToRefresh() {
        clearQueryParameters()
        setRefreshing(true)
        when (screenState.launchesType) {
            LaunchesType.UPCOMING -> _upcomingPagingEvents.send(PagingEvents.Refresh)
            LaunchesType.PAST -> _pastPagingEvents.send(PagingEvents.Refresh)
        }
    }

    private suspend fun onRetryFetch() {
        when (screenState.launchesType) {
            LaunchesType.UPCOMING -> _upcomingPagingEvents.send(PagingEvents.Retry)
            LaunchesType.PAST -> _pastPagingEvents.send(PagingEvents.Retry)
        }
    }

    fun setRefreshing(isRefreshing: Boolean) {
        screenState = screenState.copy(isRefreshing = isRefreshing)
        Timber.tag(TAG).d("Updated isRefreshing: $isRefreshing")
    }

    private fun setLaunchFilterState(
        query: String,
        launchStatus: LaunchStatus,
        launchesType: LaunchesType = screenState.launchesType
    ) {
        screenState = screenState.copy(
            query = query,
            launchStatus = launchStatus,
            launchesType = launchesType
        )
        Timber.tag(TAG)
            .d("Updated filterState: status=$launchStatus, query=$query, launchType=$launchesType")
    }

    private fun onTabSelected(launchesType: LaunchesType) {
        screenState = screenState.copy(launchesType = launchesType)
        Timber.tag(TAG).d("Tab selected: $launchesType (no pager invalidation)")
    }

    private fun displayFilterDialog(isDisplayed: Boolean) {
        screenState = screenState.copy(isFilterDialogVisible = isDisplayed)
        Timber.tag(TAG).d("Updated filterState.isVisible: $isDisplayed")
    }

    fun updateScrollPosition(launchesType: LaunchesType, position: Int) {
        screenState = when (launchesType) {
            LaunchesType.UPCOMING -> {
                screenState.copy(upcomingScrollPosition = position)
            }

            LaunchesType.PAST -> {
                screenState.copy(pastScrollPosition = position)
            }
        }
        Timber.tag(TAG).d("Updated scrollPosition for $launchesType: $position")
    }

}
