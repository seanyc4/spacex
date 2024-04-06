package com.seancoyle.launch.implementation.presentation.state

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.core.domain.DataResult
import com.seancoyle.core.domain.Order
import com.seancoyle.core.presentation.NotificationState
import com.seancoyle.core.presentation.NotificationType
import com.seancoyle.core.presentation.NotificationUiType
import com.seancoyle.core.presentation.StringResource
import com.seancoyle.core.presentation.asStringResource
import com.seancoyle.core_datastore.AppDataStore
import com.seancoyle.launch.api.LaunchConstants.PAGINATION_PAGE_SIZE
import com.seancoyle.launch.api.domain.model.Company
import com.seancoyle.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.launch.api.domain.model.LaunchStatus
import com.seancoyle.launch.api.domain.usecase.LaunchesComponent
import com.seancoyle.launch.implementation.R
import com.seancoyle.launch.implementation.presentation.state.LaunchEvents.GetCompanyApiAndCacheEvent
import com.seancoyle.launch.implementation.presentation.state.LaunchEvents.GetLaunchesApiAndCacheEvent
import com.seancoyle.launch.implementation.presentation.state.LaunchEvents.PaginateLaunchesCacheEvent
import com.seancoyle.launch.implementation.presentation.state.LaunchEvents.SortAndFilterLaunchesEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LaunchViewModel @Inject constructor(
    private val launchesComponent: LaunchesComponent,
    private val appDataStoreManager: AppDataStore,
    private val savedStateHandle: SavedStateHandle,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState: MutableStateFlow<LaunchUiState> = MutableStateFlow(LaunchUiState.Loading)
    val uiState: StateFlow<LaunchUiState> = _uiState

    private val _launchFilterState = MutableStateFlow(LaunchFilterState())
    val launchFilterState: StateFlow<LaunchFilterState> = _launchFilterState

    private val _launchListState = MutableStateFlow(LaunchesListState())
    val launchesListState: StateFlow<LaunchesListState> = _launchListState

    init {
        restoreFilterAndOrderState()
        restoreStateOnProcessDeath()
        loadDataOnAppLaunchOrRestore()
    }

    private fun loadDataOnAppLaunchOrRestore() {
        if (getScrollPositionState() != 0) {
            // Restoring state from cache data
            setEvent(SortAndFilterLaunchesEvent)
        } else {
            // Fresh app launch - get data from network
            setEvent(GetCompanyApiAndCacheEvent)
        }
    }

    private fun restoreStateOnProcessDeath() {
        savedStateHandle.get<LaunchesListState>(LAUNCH_LIST_STATE_KEY)?.let { listState ->
            _launchListState.value = listState
        }
    }

    private fun restoreFilterAndOrderState() {
        viewModelScope.launch(ioDispatcher) {
            val filterString = appDataStoreManager.readStringValue(LAUNCH_FILTER_KEY) ?: LaunchStatus.ALL.name
            val orderString = appDataStoreManager.readStringValue(LAUNCH_ORDER_KEY) ?: Order.DESC.name
            setLaunchOrderState(Order.valueOf(orderString))
            setLaunchFilterState(LaunchStatus.valueOf(filterString))
        }
    }

    fun setEvent(event: LaunchEvents) {
        viewModelScope.launch {
            when (event) {

                is SortAndFilterLaunchesEvent -> {
                    launchesComponent.createMergedLaunchesCacheUseCase(
                        year = getSearchYearState(),
                        order = getOrderState(),
                        launchFilter = getFilterState(),
                        page = getPageState()
                    ).distinctUntilChanged()
                        .collect { result ->
                            when (result) {
                                is DataResult.Success -> {
                                    _uiState.value = LaunchUiState.Success(
                                        launches = result.data,
                                        paginationState = PaginationState.None
                                    )
                                }

                                is DataResult.Error -> {
                                    _uiState.value = LaunchUiState.Error(
                                        errorNotificationState = NotificationState(
                                            message = result.error.asStringResource(),
                                            notificationUiType = NotificationUiType.Snackbar,
                                            notificationType = NotificationType.Error
                                        )
                                    )
                                }
                            }
                        }
                }

                is PaginateLaunchesCacheEvent -> {
                    launchesComponent.sortAndFilterLaunchesCacheUseCase(
                        year = getSearchYearState(),
                        order = getOrderState(),
                        launchFilter = getFilterState(),
                        page = getPageState()
                    ).collect { result ->
                        when (result) {
                            is DataResult.Success -> {
                                // Pagination - We append the next 30 rows to the current state as a new list
                                // This triggers a recompose and keeps immutability
                                _uiState.update {
                                    val currentLaunches = it.launches

                                    val allLaunches = result.data?.let { newLaunches ->
                                        currentLaunches + newLaunches
                                    } ?: currentLaunches

                                    it.copy(
                                        launches = allLaunches,
                                        paginationState = PaginationState.None
                                    )
                                }
                            }

                            is DataResult.Error -> {
                                _uiState.update { it.copy(paginationState = PaginationState.Error) }
                            }
                        }
                    }
                }

                is GetCompanyApiAndCacheEvent -> {
                    launchesComponent.getCompanyApiAndCacheUseCase()
                        .onStart { _uiState.value = LaunchUiState.Loading }
                        .collect { result ->
                            when (result) {
                                is DataResult.Success -> setEvent(GetLaunchesApiAndCacheEvent)
                                is DataResult.Error -> {
                                    _uiState.value = LaunchUiState.Error(
                                        errorNotificationState = NotificationState(
                                            message = result.error.asStringResource(),
                                            notificationUiType = NotificationUiType.Dialog,
                                            notificationType = NotificationType.Error
                                        )
                                    )
                                }
                            }
                        }
                }

                is GetLaunchesApiAndCacheEvent -> {
                    launchesComponent.getLaunchesApiAndCacheUseCase()
                        .onStart { _uiState.value = LaunchUiState.Loading }
                        .collect { result ->
                            when (result) {
                                is DataResult.Success -> setEvent(SortAndFilterLaunchesEvent)
                                is DataResult.Error -> {
                                    _uiState.value = LaunchUiState.Error(
                                        errorNotificationState = NotificationState(
                                            message = result.error.asStringResource(),
                                            notificationUiType = NotificationUiType.Dialog,
                                            notificationType = NotificationType.Error
                                        )
                                    )
                                }
                            }
                        }
                }

                is LaunchEvents.NotificationEvent -> {
                    _uiState.update { it.copy(notificationState = event.notificationState) }
                }

                else -> {}
            }
        }
    }

    fun buildCompanySummary(company: Company) =
        StringResource.AndroidStringResource(
            R.string.company_info,
            arrayOf(
                company.name,
                company.founder,
                company.founded,
                company.employees,
                company.launchSites,
                company.valuation
            )
        )


    fun getLaunchStatusIcon(status: LaunchStatus): Int = when (status) {
        LaunchStatus.SUCCESS -> R.drawable.ic_launch_success
        LaunchStatus.FAILED -> R.drawable.ic_launch_fail
        LaunchStatus.UNKNOWN -> R.drawable.ic_launch_unknown
        LaunchStatus.ALL -> throw IllegalArgumentException("LaunchStatus.ALL is not supported here")
    }

    fun getLaunchDateText(status: LaunchDateStatus): Int {
        return when (status) {
            LaunchDateStatus.PAST -> R.string.days_since_now
            LaunchDateStatus.FUTURE -> R.string.days_from_now
        }
    }

    private fun MutableStateFlow<LaunchUiState>.update(updateState: (LaunchUiState.Success) -> LaunchUiState.Success) {
        val state = this.value
        if (state is LaunchUiState.Success) {
            this.value = updateState(state)
        }
    }

    fun dismissError() {
        _uiState.update {
            it.copy(notificationState = null)
        }
    }

    fun clearListState() {
        _uiState.update { it.copy(launches = emptyList()) }
    }

    fun newSearch() {
        resetPageState()
        newSearchEvent()
    }

    fun nextPage(position: Int) {
        if ((position + 1) >= (getPageState() * PAGINATION_PAGE_SIZE)) {
            incrementPage()
            setEvent(PaginateLaunchesCacheEvent)
        }
    }

    private fun getScrollPositionState() = launchesListState.value.scrollPosition
    fun getPageState() = launchesListState.value.page
    private fun getSearchYearState() = launchFilterState.value.year
    fun getOrderState() = launchFilterState.value.order
    fun getIsDialogFilterDisplayedState() = launchFilterState.value.isDialogFilterDisplayed
    fun getFilterState(): LaunchStatus = launchFilterState.value.launchStatus

    fun clearQueryParameters() {
        clearListState()
        setYearState("")
        setLaunchFilterState(LaunchStatus.ALL)
        resetPageState()
    }

    private fun updateFilterState(update: LaunchFilterState.() -> LaunchFilterState) {
        _launchFilterState.value = _launchFilterState.value.update()
    }

    private fun updateListState(update: LaunchesListState.() -> LaunchesListState) {
        _launchListState.value = _launchListState.value.update()
    }

    fun setYearState(year: String) {
        updateFilterState { copy(year = year) }
    }

    fun setLaunchOrderState(order: Order) {
        updateFilterState { copy(order = order) }
        saveOrderToDatastore(order)
    }

    fun setLaunchFilterState(filter: LaunchStatus) {
        updateFilterState { copy(launchStatus = filter) }
        saveFilterToDataStore(filter)
    }

    fun setDialogFilterDisplayedState(isDisplayed: Boolean) {
        updateFilterState { copy(isDialogFilterDisplayed = isDisplayed) }
    }

    private fun resetPageState() {
        updateListState { copy(page = 1) }
    }

    private fun setPageState(pageNum: Int) {
        updateListState { copy(page = pageNum) }
    }

    fun setScrollPositionState(position: Int) {
        updateListState { copy(scrollPosition = position) }
    }

    private fun incrementPage() {
        val incrementedPage = launchesListState.value.page + 1
        updateListState { copy(page = incrementedPage) }
        setPageState(incrementedPage)
    }

    fun saveState() {
        savedStateHandle[LAUNCH_LIST_STATE_KEY] = _launchListState.value
    }

    private fun saveOrderToDatastore(order: Order) {
        viewModelScope.launch(ioDispatcher) {
            appDataStoreManager.setStringValue(LAUNCH_ORDER_KEY, order.name)
        }
    }

    private fun saveFilterToDataStore(filter: LaunchStatus) {
        viewModelScope.launch(ioDispatcher) {
            appDataStoreManager.setStringValue(LAUNCH_FILTER_KEY, filter.name)
        }
    }

    fun newSearchEvent() {
        setEvent(SortAndFilterLaunchesEvent)
    }

    companion object {
        // Datastore Files:
        private const val LAUNCH_DATASTORE_KEY: String = "com.seancoyle.spacex.launch"

        // Datastore Keys
        private const val LAUNCH_ORDER_KEY = "$LAUNCH_DATASTORE_KEY.LAUNCH_ORDER"
        private const val LAUNCH_FILTER_KEY = "$LAUNCH_DATASTORE_KEY.LAUNCH_FILTER"
        private const val LAUNCH_LIST_STATE_KEY = "$LAUNCH_DATASTORE_KEY.state.list.key"
    }

}