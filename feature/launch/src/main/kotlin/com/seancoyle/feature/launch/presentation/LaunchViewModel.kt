package com.seancoyle.feature.launch.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.seancoyle.feature.launch.domain.model.LaunchQuery
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.domain.model.LaunchStatus
import com.seancoyle.feature.launch.domain.usecase.component.LaunchesComponent
import com.seancoyle.feature.launch.presentation.state.LaunchesEvents
import com.seancoyle.feature.launch.presentation.state.LaunchesScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import timber.log.Timber
import androidx.paging.map
import com.seancoyle.feature.launch.presentation.model.LaunchUi
import com.seancoyle.feature.launch.presentation.model.LaunchUiMapper
import com.seancoyle.feature.launch.presentation.state.PagingEvents
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import com.seancoyle.core.ui.NotificationState
import com.seancoyle.core.ui.NotificationType
import com.seancoyle.core.ui.StringResource
import com.seancoyle.core.ui.UiComponentType

private const val TAG = "LaunchViewModel"

@OptIn(SavedStateHandleSaveableApi::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class LaunchViewModel @Inject constructor(
    private val launchesComponent: LaunchesComponent,
    private val uiMapper: LaunchUiMapper,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var screenState by savedStateHandle.saveable { mutableStateOf(LaunchesScreenState()) }
        private set

    private val _notificationEvents = Channel<NotificationState>(Channel.BUFFERED)
    val notificationEvents = _notificationEvents.receiveAsFlow()

    private val _pagingEvents = Channel<PagingEvents>(Channel.BUFFERED)
    val pagingEvents = _pagingEvents.receiveAsFlow()

    init {
        viewModelScope.launch {
            Timber.tag(TAG).d("screenState before init: $screenState")
            restoreFilterAndOrderState()
            Timber.tag(TAG).d("screenState after init: $screenState")
        }
    }

    val launchQueryState: StateFlow<LaunchQuery> = snapshotFlow {
        screenState.query to screenState.order
    }.map { (query, order) ->
        LaunchQuery(
            query = query,
            order = order
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, LaunchQuery())

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
                query = result.query
            )
        }
    }

    fun onEvent(event: LaunchesEvents) = viewModelScope.launch {
        when (event) {
            is LaunchesEvents.DismissFilterDialogEvent -> displayFilterDialog(false)
            is LaunchesEvents.DisplayFilterDialogEvent -> displayFilterDialog(true)
            is LaunchesEvents.NewSearchEvent -> newSearch()
            is LaunchesEvents.PullToRefreshEvent -> onPullToRefresh()
            is LaunchesEvents.RetryFetchEvent -> onRetryFetch()
            is LaunchesEvents.UpdateFilterStateEvent -> setLaunchFilterState(
                order = event.order,
                launchStatus = event.launchStatus,
                query = event.query
            )
        }
    }

    private suspend fun newSearch() {
        saveLaunchPreferences(order = getOrderState())
        displayFilterDialog(false)
    }

    private fun getOrderState() = screenState.order

    private fun clearQueryParameters() {
        setLaunchFilterState(
            query = "",
            order = Order.ASC,
            launchStatus = LaunchStatus.ALL
        )
    }

    private suspend fun onPullToRefresh() {
        clearQueryParameters()
        setRefreshing(true)
        _pagingEvents.send(PagingEvents.Refresh)
    }

    private suspend fun onRetryFetch() {
        _pagingEvents.send(PagingEvents.Retry)
    }

    fun setRefreshing(isRefreshing: Boolean) {
        screenState = screenState.copy(isRefreshing = isRefreshing)
        Timber.tag(TAG).d("Updated isRefreshing: $isRefreshing")
    }

    private fun setLaunchFilterState(
        query: String,
        order: Order,
        launchStatus: LaunchStatus
    ) {
        screenState = screenState.copy(
            query = query,
            order = order,
            launchStatus = launchStatus
        )
        Timber.tag(TAG).d("Updated filterState: order=$order, status=$launchStatus, year=$query")
    }

    private fun displayFilterDialog(isDisplayed: Boolean) {
        screenState = screenState.copy(isFilterDialogVisible = isDisplayed)
        Timber.tag(TAG).d("Updated filterState.isVisible: $isDisplayed")
    }

    fun updateScrollPosition(position: Int) {
        screenState = screenState.copy(scrollPosition = position)
        Timber.tag(TAG).d("Updated scrollState.scrollPosition: $position")
    }

    private suspend fun saveLaunchPreferences(order: Order) {
        launchesComponent.saveLaunchPreferencesUseCase(order)
    }

    fun emitErrorNotification(errorMessage: StringResource) = viewModelScope.launch {
        _notificationEvents.send(
            NotificationState(
                message = errorMessage,
                uiComponentType = UiComponentType.Snackbar,
                notificationType = NotificationType.Error
            )
        )
    }

    fun clearNotification() = viewModelScope.launch {
        _notificationEvents.send(
            NotificationState(
                message = StringResource.Text(""),
                uiComponentType = UiComponentType.None,
                notificationType = NotificationType.Info
            )
        )
    }
}
