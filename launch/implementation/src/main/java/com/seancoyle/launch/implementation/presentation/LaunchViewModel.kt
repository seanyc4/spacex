package com.seancoyle.launch.implementation.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.core.domain.Result
import com.seancoyle.core.domain.asResult
import com.seancoyle.core_datastore.AppDataStore
import com.seancoyle.launch.api.LaunchNetworkConstants.LAUNCH_ALL
import com.seancoyle.launch.api.LaunchNetworkConstants.ORDER_ASC
import com.seancoyle.launch.api.LaunchNetworkConstants.PAGINATION_PAGE_SIZE
import com.seancoyle.launch.api.domain.usecase.CreateMergedLaunchesUseCase
import com.seancoyle.launch.implementation.domain.CompanyInfoUseCases
import com.seancoyle.launch.implementation.domain.LaunchUseCases
import com.seancoyle.launch.implementation.presentation.LaunchEvents.FilterLaunchItemsInCacheEvent
import com.seancoyle.launch.implementation.presentation.LaunchEvents.GetCompanyInfoApiAndCacheEvent
import com.seancoyle.launch.implementation.presentation.LaunchEvents.GetLaunchesApiAndCacheEvent
import com.seancoyle.launch.implementation.presentation.LaunchEvents.MergeDataEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class LaunchViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val launchUseCases: LaunchUseCases,
    private val companyInfoUseCases: CompanyInfoUseCases,
    private val appDataStoreManager: AppDataStore,
    private val createMergedLaunchesUseCase: CreateMergedLaunchesUseCase,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    protected val _uiState = MutableStateFlow(LaunchState())
    val uiState: StateFlow<LaunchState> = _uiState

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
            setEvent(GetLaunchesApiAndCacheEvent)
        }
    }

    private fun restoreStateOnProcessDeath() {
        savedStateHandle.get<LaunchState>(LAUNCH_UI_STATE_KEY)?.let { uiState ->
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
                    createMergedLaunchesUseCase(
                        year = getSearchYearState(),
                        order = getOrderState(),
                        launchFilter = getFilterState(),
                        page = getPageState()
                    ).asResult()
                        .distinctUntilChanged()
                        .collect { result ->
                            when (result) {
                                is Result.Success -> {
                                    _uiState.emit(
                                        _uiState.value.copy(
                                            mergedLaunches = result.data,
                                            isLoading = false
                                        )
                                    )
                                }

                                is Result.Loading -> { _uiState.emit(_uiState.value.copy(isLoading = true)) }
                                is Result.Error -> {}
                            }
                        }
                }

                is FilterLaunchItemsInCacheEvent -> {
                    launchUseCases.filterLaunchItemsInCacheUseCase(
                        year = getSearchYearState(),
                        order = getOrderState(),
                        launchFilter = getFilterState(),
                        page = getPageState()
                    ).asResult()
                        .collect { result ->
                            when (result) {
                                is Result.Success -> {
                                    // Pagination - We append the next 30 rows to the current state as a new list
                                    // This triggers a recompose and keeps immutability
                                    val currentLaunches = uiState.value.mergedLaunches

                                    val allLaunches = result.data?.let { newLaunches ->
                                        currentLaunches + newLaunches
                                    } ?: currentLaunches

                                    _uiState.emit(
                                        _uiState.value.copy(
                                            mergedLaunches = allLaunches,
                                            isLoading = false
                                        )
                                    )
                                }

                                is Result.Loading -> { _uiState.emit(_uiState.value.copy(isLoading = true)) }
                                is Result.Error -> {}
                            }
                        }
                }

                is GetLaunchesApiAndCacheEvent -> {
                    launchUseCases.getLaunchesFromNetworkAndInsertToCacheUseCase().onCompletion {
                        setEvent(MergeDataEvent)
                    }.collect()
                }

                is GetCompanyInfoApiAndCacheEvent -> {
                    companyInfoUseCases.getCompanyInfoFromNetworkAndInsertToCacheUseCase().collect()
                }

                else -> {}
            }
        }
    }

    fun clearListState() {
        _uiState.value = uiState.value.copy(mergedLaunches = emptyList())
    }

    fun newSearch() {
        resetPageState()
        newSearchEvent()
    }

    fun nextPage() {
        if ((getScrollPositionState() + 1) >= (getPageState() * PAGINATION_PAGE_SIZE)) {
            incrementPage()
            setEvent(FilterLaunchItemsInCacheEvent)
        }
    }

    private fun getScrollPositionState() = uiState.value.scrollPosition
    private fun getSearchYearState() = uiState.value.year
    fun getPageState() = uiState.value.page
    fun getOrderState() = uiState.value.order
    fun getIsDialogFilterDisplayedState() = uiState.value.isDialogFilterDisplayed

    fun getFilterState(): Int? {
        return if (uiState.value.launchFilter == LAUNCH_ALL) {
            setLaunchFilterState(null)
            uiState.value.launchFilter
        } else {
            uiState.value.launchFilter
        }
    }

    fun clearQueryParameters() {
        clearListState()
        setYearState(null)
        setLaunchFilterState(null)
        resetPageState()
    }

    private fun updateUiState(update: LaunchState.() -> LaunchState) {
        _uiState.value = _uiState.value.update()
    }

    fun setYearState(year: String?) {
        updateUiState { copy(year = year.orEmpty()) }
    }

    fun setLaunchOrderState(order: String) {
        updateUiState { copy(order = order) }
        saveOrderToDatastore(order)
    }

    fun setLaunchFilterState(filter: Int?) {
        updateUiState { copy(launchFilter = filter) }
        saveFilterToDataStore(filter ?: LAUNCH_ALL)
    }

    fun setDialogFilterDisplayedState(isDisplayed: Boolean) {
        updateUiState { copy(isDialogFilterDisplayed = isDisplayed) }
    }

    private fun resetPageState() {
        updateUiState { copy(page = 1) }
    }

    private fun setPageState(pageNum: Int) {
        updateUiState { copy(page = pageNum) }
    }

    fun setScrollPositionState(position: Int) {
        updateUiState { copy(scrollPosition = position) }
    }

    private fun incrementPage() {
        val incrementedPage = uiState.value.page + 1
        updateUiState { copy(page = incrementedPage) }
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