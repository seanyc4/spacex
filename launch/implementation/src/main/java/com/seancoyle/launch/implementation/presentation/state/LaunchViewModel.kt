package com.seancoyle.launch.implementation.presentation.state

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seancoyle.core.common.di.IODispatcher
import com.seancoyle.core.common.result.DataResult
import com.seancoyle.core.domain.Order
import com.seancoyle.core.ui.NotificationState
import com.seancoyle.core.ui.NotificationType
import com.seancoyle.core.ui.NotificationUiType
import com.seancoyle.core.ui.StringResource
import com.seancoyle.core.ui.asStringResource
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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LaunchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val launchesComponent: LaunchesComponent,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    var uiState: MutableStateFlow<LaunchesUiState> = MutableStateFlow(LaunchesUiState.Loading)
        private set

    var filterState = MutableStateFlow(LaunchesFilterState())
        private set

    var scrollState = MutableStateFlow(LaunchesScrollState())
        private set

    fun init() {
        if (uiState.value is LaunchesUiState.Loading) {
            restoreFilterAndOrderState()
            restoreStateOnProcessDeath()
            loadDataOnAppLaunchOrRestore()
        }
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
        savedStateHandle.get<LaunchesScrollState>(LAUNCH_LIST_STATE_KEY)?.let { listState ->
            scrollState.value = listState
        }
    }

    private fun restoreFilterAndOrderState() {
        /*viewModelScope.launch(ioDispatcher) {
            val filterString = dataStoreComponent.readStringUseCase(LAUNCH_FILTER_KEY) ?: LaunchStatus.ALL.name
            val orderString = dataStoreComponent.readStringUseCase(LAUNCH_ORDER_KEY) ?: Order.DESC.name
            setLaunchOrderState(Order.valueOf(orderString))
            setLaunchFilterState(LaunchStatus.valueOf(filterString))
        }*/
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
                                    uiState.update {
                                        LaunchesUiState.Success(
                                            launches = result.data,
                                            paginationState = PaginationState.None
                                        )
                                    }
                                }

                                is DataResult.Error -> {
                                    uiState.update {
                                        LaunchesUiState.Error(
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
                                uiState.update { currentState ->
                                    currentState.isSuccess {
                                        val updatedLaunches = it.launches + (result.data ?: emptyList())
                                        it.copy(
                                            launches = updatedLaunches,
                                            paginationState = PaginationState.None
                                        )
                                    }
                                }
                            }

                            is DataResult.Error -> {
                                uiState.update { currentState ->
                                    currentState.isSuccess { it.copy(paginationState = PaginationState.Error) }
                                }
                            }
                        }
                    }
                }

                is GetCompanyApiAndCacheEvent -> {
                    launchesComponent.getCompanyApiAndCacheUseCase()
                        .onStart { uiState.update { LaunchesUiState.Loading } }
                        .collect { result ->
                            when (result) {
                                is DataResult.Success -> setEvent(GetLaunchesApiAndCacheEvent)
                                is DataResult.Error -> {
                                    uiState.update {
                                        LaunchesUiState.Error(
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
                }

                is GetLaunchesApiAndCacheEvent -> {
                    launchesComponent.getLaunchesApiAndCacheUseCase()
                        .onStart { uiState.update { LaunchesUiState.Loading } }
                        .collect { result ->
                            when (result) {
                                is DataResult.Success -> setEvent(SortAndFilterLaunchesEvent)
                                is DataResult.Error -> {
                                    uiState.update {
                                        LaunchesUiState.Error(
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
                }

                is LaunchEvents.NotificationEvent -> {
                    uiState.update { currentState ->
                        currentState.isSuccess { it.copy(notificationState = event.notificationState) }
                    }
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

    private fun LaunchesUiState.isSuccess(updateState: (LaunchesUiState.Success) -> LaunchesUiState): LaunchesUiState {
        return if (this is LaunchesUiState.Success) {
            updateState(this)
        } else {
            this
        }
    }

    fun dismissError() {
        uiState.update { currentState ->
            currentState.isSuccess {
                it.copy(notificationState = null)
            }
        }
    }

    private fun clearListState() {
        uiState.update { currentState ->
            currentState.isSuccess { it.copy(launches = emptyList()) }
        }
    }

    fun newSearch() {
        clearListState()
        resetPageState()
        newSearchEvent()
        setDialogFilterDisplayedState(false)
    }

    fun nextPage(position: Int) {
        if ((position + 1) >= (getPageState() * PAGINATION_PAGE_SIZE)) {
            incrementPage()
            setEvent(PaginateLaunchesCacheEvent)
        }
    }

    private fun getScrollPositionState() = scrollState.value.scrollPosition
    fun getPageState() = scrollState.value.page
    private fun getSearchYearState() = filterState.value.year
    private fun getOrderState() = filterState.value.order
    private fun getFilterState(): LaunchStatus = filterState.value.launchStatus

    private fun clearQueryParameters() {
        clearListState()
        setYearState("")
        setLaunchFilterState(LaunchStatus.ALL)
        resetPageState()
    }

    fun swipeToRefresh() {
        clearQueryParameters()
        clearListState()
        setEvent(GetCompanyApiAndCacheEvent)
    }

    private fun updateFilterState(update: LaunchesFilterState.() -> LaunchesFilterState) {
        filterState.value = filterState.value.update()
    }

    private fun updateListState(update: LaunchesScrollState.() -> LaunchesScrollState) {
        scrollState.value = scrollState.value.update()
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
        val incrementedPage = scrollState.value.page + 1
        updateListState { copy(page = incrementedPage) }
        setPageState(incrementedPage)
    }

    fun saveState() {
        savedStateHandle[LAUNCH_LIST_STATE_KEY] = scrollState.value
    }

    private fun saveOrderToDatastore(order: Order) {
    /*    viewModelScope.launch(ioDispatcher) {
            dataStoreComponent.saveStringUseCase(LAUNCH_ORDER_KEY, order.name)
        }*/
    }

    private fun saveFilterToDataStore(filter: LaunchStatus) {
     /*   viewModelScope.launch(ioDispatcher) {
            dataStoreComponent.saveStringUseCase(LAUNCH_FILTER_KEY, filter.name)
        }*/
    }

    private fun newSearchEvent() {
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