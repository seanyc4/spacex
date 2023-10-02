package com.seancoyle.launch.implementation.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.core.domain.Result
import com.seancoyle.core.domain.asResult
import com.seancoyle.core.util.printLogDebug
import com.seancoyle.core_datastore.AppDataStore
import com.seancoyle.launch.api.LaunchNetworkConstants.LAUNCH_ALL
import com.seancoyle.launch.api.LaunchNetworkConstants.ORDER_ASC
import com.seancoyle.launch.api.LaunchNetworkConstants.PAGINATION_PAGE_SIZE
import com.seancoyle.launch.api.domain.usecase.CreateMergedLaunchesUseCase
import com.seancoyle.launch.api.presentation.LaunchUiState
import com.seancoyle.launch.implementation.domain.CompanyInfoUseCases
import com.seancoyle.launch.implementation.domain.LaunchUseCases
import com.seancoyle.launch.implementation.presentation.LaunchEvents.GetCompanyInfoApiAndCacheEvent
import com.seancoyle.launch.implementation.presentation.LaunchEvents.GetLaunchesApiAndCacheEvent
import com.seancoyle.launch.implementation.presentation.LaunchEvents.MergeDataEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
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
) : BaseViewModel() {

    init {
        setEvent(MergeDataEvent)
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
        savedStateHandle.get<LaunchUiState.LaunchState>(LAUNCH_UI_STATE_KEY)?.let { uiState ->
            //  setState(uiState)
        }
    }

    private fun restoreFilterAndOrderState() {
        viewModelScope.launch(ioDispatcher) {
            setLaunchOrderState(
                appDataStoreManager.readStringValue(LAUNCH_ORDER_KEY)
            )
            setLaunchFilterState(
                appDataStoreManager.readIntValue(LAUNCH_FILTER_KEY)
            )
        }
    }

    fun setEvent(event: LaunchEvents) {
        viewModelScope.launch {
            when (event) {

                is MergeDataEvent -> {
                    printLogDebug("SPACEXAPP: LAUNCHVIEWMODEL: LAUNCH EVENT", MergeDataEvent.toString())
                    createMergedLaunchesUseCase(
                        year = getSearchYearState(),
                        order = getOrderState(),
                        launchFilter = getFilterState(),
                        page = getPageState()
                    ).asResult()
                        .collect { result ->
                            when (result) {
                                is Result.Success -> _uiState.emit(
                                    LaunchUiState.LaunchState(mergedLaunches = result.data)
                                )
                                is Result.Loading -> {
                                    _uiState.emit(LaunchUiState.Loading)
                                }
                                is Result.Error -> {
                                    _uiState.emit(LaunchUiState.ErrorState(result.exception?.message.toString()))
                                }
                            }
                        }
                }

                is GetLaunchesApiAndCacheEvent -> {
                    printLogDebug("SPACEXAPP: LAUNCHVIEWMODEL: LAUNCH EVENT", GetLaunchesApiAndCacheEvent.toString())
                    launchUseCases.getLaunchesFromNetworkAndInsertToCacheUseCase().collect()
                }

                is GetCompanyInfoApiAndCacheEvent -> {
                    printLogDebug("SPACEXAPP: LAUNCHVIEWMODEL :LAUNCH EVENT", GetCompanyInfoApiAndCacheEvent.toString())
                    companyInfoUseCases.getCompanyInfoFromNetworkAndInsertToCacheUseCase().collect()
                }

                else -> {

                }
            }
        }
    }

    fun clearListState() {
        if (_uiState.value is LaunchUiState.LaunchState) {
            _uiState.value = LaunchUiState.LaunchState(mergedLaunches = emptyList())
        }
        printLogDebug("SPACEXAPP: LAUNCHVIEWMODEL", "clearListState(): ${_uiState.value}")
    }

    fun newSearch() {
        resetPageState()
        newSearchEvent()
        printLogDebug("SPACEXAPP: LAUNCHVIEWMODEL", "loadFirstPage: ${year}")
    }

    fun nextPage() {
        if ((getScrollPositionState() + 1) >= (getPageState() * PAGINATION_PAGE_SIZE)) {
            incrementPage()
            if (getPageState() > 1) {
                setEvent(MergeDataEvent)
            }
        }
        printLogDebug("SPACEXAPP: LAUNCHVIEWMODEL", "nextPage: triggered: ${getPageState()}")
    }

    private fun getScrollPositionState() = scrollPosition.value
    private fun getSearchYearState() = year.value
    fun getPageState() = page.value
    fun getOrderState() = order.value
    fun getIsDialogFilterDisplayedState() = isDialogFilterDisplayed.value

    fun getFilterState(): Int? {
        return if (launchFilter.value == LAUNCH_ALL) {
            setLaunchFilterState(null)
            launchFilter.value
        } else {
            launchFilter.value
        }
    }

    fun clearQueryParameters() {
        clearListState()
        setYearState(null)
        setLaunchFilterState(null)
        resetPageState()
    }

    fun setYearState(query: String?) {
        _year.value = query ?: ""
    }

    fun setLaunchOrderState(order: String?) {
        val nonNullOrder = order?:  ORDER_ASC
        _order.value = nonNullOrder
        saveOrderToDatastore(nonNullOrder)
    }

    fun setLaunchFilterState(filter: Int?) {
        _launchFilter.value = filter
        saveFilterToDataStore(filter ?: LAUNCH_ALL)
    }

    fun setIsDialogFilterDisplayedState(isDisplayed: Boolean) {
        _isDialogFilterDisplayed.value = isDisplayed
    }

    private fun resetPageState() {
        _page.value = 1
    }

    private fun setPageState(pageNum: Int) {
        _page.value = pageNum
    }

    fun setScrollPositionState(position: Int) {
        _scrollPosition.value = position
    }

    private fun incrementPage() {
        val incrementedPage = _page.value + 1
        _page.value = incrementedPage
        setPageState(incrementedPage)
    }

    fun saveState() {
        //       savedStateHandle[LAUNCH_UI_STATE_KEY] = getCurrentState()
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