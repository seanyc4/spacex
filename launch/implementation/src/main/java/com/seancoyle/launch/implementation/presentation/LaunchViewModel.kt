package com.seancoyle.launch.implementation.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seancoyle.core.data.network.ApiResult
import com.seancoyle.core.data.network.asResult
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.core.domain.MessageDisplayType
import com.seancoyle.core.domain.MessageType
import com.seancoyle.core.domain.Response
import com.seancoyle.core.util.printLogDebug
import com.seancoyle.core_datastore.AppDataStore
import com.seancoyle.launch.api.LaunchConstants.LAUNCH_ALL
import com.seancoyle.launch.api.LaunchConstants.ORDER_ASC
import com.seancoyle.launch.api.LaunchConstants.PAGINATION_PAGE_SIZE
import com.seancoyle.launch.api.domain.usecase.CompanyInfoComponent
import com.seancoyle.launch.api.domain.usecase.LaunchesComponent
import com.seancoyle.launch.implementation.presentation.LaunchEvents.FilterLaunchItemsInCacheEvent
import com.seancoyle.launch.implementation.presentation.LaunchEvents.GetCompanyInfoApiAndCacheEvent
import com.seancoyle.launch.implementation.presentation.LaunchEvents.GetLaunchesApiAndCacheEvent
import com.seancoyle.launch.implementation.presentation.LaunchEvents.MergeDataEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
internal class LaunchViewModel @Inject constructor(
    private val launchesComponent: LaunchesComponent,
    private val companyInfoComponent: CompanyInfoComponent,
    private val appDataStoreManager: AppDataStore,
    private val savedStateHandle: SavedStateHandle,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState: MutableStateFlow<LaunchUiState> = MutableStateFlow(LaunchUiState.Loading)
    val uiState: StateFlow<LaunchUiState> = _uiState

    private val _filterState = MutableStateFlow(FilterState())
    val filterState: StateFlow<FilterState> = _filterState

    private val _listState = MutableStateFlow(ListState())
    val listState: StateFlow<ListState> = _listState

    init {
        restoreFilterAndOrderState()
        restoreStateOnProcessDeath()
        loadDataOnAppLaunchOrRestore()
    }

    private fun loadDataOnAppLaunchOrRestore() {
        if (getScrollPositionState() != 0) {
            //Restoring state from cache data
            setEvent(MergeDataEvent)
        } else {
            //Fresh app launch - get data from network
            setEvent(GetCompanyInfoApiAndCacheEvent)
        }
    }

    private fun restoreStateOnProcessDeath() {
        savedStateHandle.get<LaunchUiState>(LAUNCH_UI_STATE_KEY)?.let { uiState ->
            _uiState.value = uiState
        }
    }

    private fun restoreFilterAndOrderState() {
        viewModelScope.launch(ioDispatcher) {
            setLaunchOrderState(appDataStoreManager.readStringValue(LAUNCH_ORDER_KEY) ?: ORDER_ASC)
            setLaunchFilterState(appDataStoreManager.readIntValue(LAUNCH_FILTER_KEY))
        }
    }

    fun setEvent(event: LaunchEvents) {
        viewModelScope.launch {
            when (event) {

                is MergeDataEvent -> {
                    launchesComponent.createMergeLaunchesUseCase(
                        year = getSearchYearState(),
                        order = getOrderState(),
                        launchFilter = getFilterState(),
                        page = getPageState()
                    ).asResult()
                        .distinctUntilChanged()
                        .collect { result ->
                            when (result) {
                                is ApiResult.Success -> {
                                    _uiState.value = LaunchUiState.Success(
                                        launches = result.data,
                                        paginationState = PaginationState.None
                                    )
                                }

                                is ApiResult.Loading -> {
                                    _uiState.value = LaunchUiState.Loading
                                }

                                is ApiResult.Error -> {
                                    _uiState.value = LaunchUiState.Error(
                                        errorResponse = Response(
                                            message = result.exception?.message.orEmpty(),
                                            messageDisplayType = MessageDisplayType.Dialog,
                                            messageType = MessageType.Error
                                        )
                                    )
                                }
                            }
                        }
                }

                is FilterLaunchItemsInCacheEvent -> {
                    printLogDebug("SPACEXAPP: ", "FilterLaunchItemsInCacheEvent")
                    launchesComponent.filterLaunchItemsInCacheUseCase(
                        year = getSearchYearState(),
                        order = getOrderState(),
                        launchFilter = getFilterState(),
                        page = getPageState()
                    ).asResult()
                        .collect { result ->
                            when (result) {
                                is ApiResult.Success -> {
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

                                is ApiResult.Loading -> {
                                    _uiState.update {
                                        it.copy(paginationState = PaginationState.Loading)
                                    }
                                }

                                is ApiResult.Error -> {
                                    _uiState.update {
                                        it.copy(paginationState = PaginationState.Error)
                                    }
                                }
                            }
                        }
                }

                is GetCompanyInfoApiAndCacheEvent -> {
                    companyInfoComponent.getCompanyInfoFromNetworkAndInsertToCacheUseCase()
                        .onStart {  _uiState.value = LaunchUiState.Loading }
                        .collect { result ->
                            when (result) {
                                is ApiResult.Success -> {
                                    setEvent(GetLaunchesApiAndCacheEvent)
                                }
                                is ApiResult.Error -> {
                                    _uiState.value = LaunchUiState.Error(
                                        errorResponse = Response(
                                            message = result.exception?.message.orEmpty(),
                                            messageDisplayType = MessageDisplayType.Dialog,
                                            messageType = MessageType.Error
                                        )
                                    )
                                }

                                else -> {}
                            }
                        }
                }

                is GetLaunchesApiAndCacheEvent -> {
                    launchesComponent.getLaunchesFromNetworkAndInsertToCacheUseCase()
                        .onStart { _uiState.value = LaunchUiState.Loading  }
                        .collect { result ->
                            when (result) {
                                is ApiResult.Success -> {
                                    setEvent(MergeDataEvent)
                                }
                                is ApiResult.Error -> {
                                    _uiState.value = LaunchUiState.Error(
                                        errorResponse = Response(
                                            message = result.exception?.message.orEmpty(),
                                            messageDisplayType = MessageDisplayType.Dialog,
                                            messageType = MessageType.Error
                                        )
                                    )
                                }

                                else -> {}
                            }
                        }
                }

                else -> {}
            }
        }
    }

    private fun MutableStateFlow<LaunchUiState>.update(updateState: (LaunchUiState.Success) -> LaunchUiState.Success) {
        val state = this.value
        if (state is LaunchUiState.Success) {
            this.value = updateState(state)
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
            setEvent(FilterLaunchItemsInCacheEvent)
        }
    }

    private fun getScrollPositionState() = listState.value.scrollPosition
    fun getPageState() = listState.value.page
    private fun getSearchYearState() = filterState.value.year
    fun getOrderState() = filterState.value.order
    fun getIsDialogFilterDisplayedState() = filterState.value.isDialogFilterDisplayed
    // fun isLoading() = _uiState.value.isLoading

    fun getFilterState(): Int? {
        return if (filterState.value.launchFilter == LAUNCH_ALL) {
            setLaunchFilterState(null)
            filterState.value.launchFilter
        } else {
            filterState.value.launchFilter
        }
    }

    fun clearQueryParameters() {
        clearListState()
        setYearState(null)
        setLaunchFilterState(null)
        resetPageState()
    }

    private fun updateFilterState(update: FilterState.() -> FilterState) {
        _filterState.value = _filterState.value.update()
    }

    private fun updateListState(update: ListState.() -> ListState) {
        _listState.value = _listState.value.update()
    }

    fun setYearState(year: String?) {
        updateFilterState { copy(year = year.orEmpty()) }
    }

    fun setLaunchOrderState(order: String) {
        updateFilterState { copy(order = order) }
        saveOrderToDatastore(order)
    }

    fun setLaunchFilterState(filter: Int?) {
        updateFilterState { copy(launchFilter = filter) }
        saveFilterToDataStore(filter ?: LAUNCH_ALL)
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
        val incrementedPage = listState.value.page + 1
        updateListState { copy(page = incrementedPage) }
        setPageState(incrementedPage)
    }

    fun saveState() {
        savedStateHandle[LAUNCH_UI_STATE_KEY] = _uiState.value
    }

    private fun saveOrderToDatastore(order: String) {
        viewModelScope.launch(ioDispatcher) {
            appDataStoreManager.setStringValue(LAUNCH_ORDER_KEY, order)
        }
    }

    private fun saveFilterToDataStore(filter: Int) {
        viewModelScope.launch(ioDispatcher) {
            appDataStoreManager.setIntValue(LAUNCH_FILTER_KEY, filter)
        }
    }

    fun newSearchEvent() {
        setEvent(MergeDataEvent)
    }

    companion object {
        // Datastore Files:
        private const val LAUNCH_DATASTORE_KEY: String = "com.seancoyle.spacex.launch"

        // Datastore Keys
        const val LAUNCH_ORDER_KEY: String = "$LAUNCH_DATASTORE_KEY.LAUNCH_ORDER"
        const val LAUNCH_FILTER_KEY: String = "$LAUNCH_DATASTORE_KEY.LAUNCH_FILTER"

        const val LAUNCH_UI_STATE_KEY = "launch.state.key"
    }

}