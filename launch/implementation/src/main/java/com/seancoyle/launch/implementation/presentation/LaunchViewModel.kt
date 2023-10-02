package com.seancoyle.launch.implementation.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.core.di.MainDispatcher
import com.seancoyle.core.domain.Result
import com.seancoyle.core.domain.Event
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
import com.seancoyle.launch.implementation.presentation.LaunchEvents.FilterLaunchItemsInCacheEvent
import com.seancoyle.launch.implementation.presentation.LaunchEvents.GetCompanyInfoFromCacheEvent
import com.seancoyle.launch.implementation.presentation.LaunchEvents.GetCompanyInfoApiAndCacheEvent
import com.seancoyle.launch.implementation.presentation.LaunchEvents.GetLaunchesApiAndCacheEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class LaunchViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val launchUseCases: LaunchUseCases,
    private val companyInfoUseCases: CompanyInfoUseCases,
    private val appDataStoreManager: AppDataStore,
    private val createMergedLaunchesUseCase: CreateMergedLaunchesUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<LaunchUiState>(LaunchUiState.Loading)
    val uiState: StateFlow<LaunchUiState> = _uiState

    private val _scrollPosition = MutableStateFlow(0)
    val scrollPosition: StateFlow<Int> get() = _scrollPosition

    private val _numLaunchesInCache = MutableStateFlow(0)
    val numLaunchesInCache: StateFlow<Int> get() = _numLaunchesInCache

    private val _isDialogFilterDisplayed = MutableStateFlow(false)
    val isDialogFilterDisplayed: StateFlow<Boolean> get() = _isDialogFilterDisplayed

    private val _launchFilter = MutableStateFlow<Int?>(null)
    val launchFilter: StateFlow<Int?> get() = _launchFilter

    private val _order = MutableStateFlow<String?>(null)
    val order: StateFlow<String?> get() = _order

    private val _year = MutableStateFlow("")
    val year: StateFlow<String> get() = _year

    private val _page = MutableStateFlow(1)
    val page: StateFlow<Int> get() = _page

    init {
        setEvent(LaunchEvents.MergeDataEvent)
        restoreFilterAndOrderState()
        restoreStateOnProcessDeath()
        loadDataOnAppLaunchOrRestore()
    }

    private fun loadDataOnAppLaunchOrRestore() {
        if (getScrollPositionState() != 0) {
            //Restoring state from cache data
            setEvent(GetCompanyInfoFromCacheEvent)
            setEvent(FilterLaunchItemsInCacheEvent)
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

    fun setEvent(event: Event) {
        viewModelScope.launch {
            when (event) {

                is LaunchEvents.MergeDataEvent -> {
                    createMergedLaunchesUseCase(
                        year = getSearchYearState(),
                        order = getOrderState(),
                        launchFilter = getFilterState(),
                        page = getPageState()
                    ).asResult()
                        .collect { result ->
                            printLogDebug("USECASE", result.toString())
                            when (result) {
                                is Result.Success -> _uiState.emit(
                                    LaunchUiState.LaunchState(
                                        mergedLaunches = result.data
                                    )
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

               /* is FilterLaunchItemsInCacheEvent -> {
                    launchUseCases.filterLaunchItemsInCacheUseCase(
                        year = getSearchYearState(),
                        order = getOrderState(),
                        launchFilter = getFilterState(),
                        page = getPageState()
                    ).asResult()
                        .map { result ->
                            when (result) {
                                is Result.Success -> { *//*LaunchUiState.LaunchState(launches = result.data)*//*
                                }

                                is Result.Loading -> LaunchUiState.Loading
                                is Result.Error -> TODO()
                            }
                        }
                }*/

                is GetLaunchesApiAndCacheEvent -> {
                    launchUseCases.getLaunchesFromNetworkAndInsertToCacheUseCase().collect()
                }

                is GetCompanyInfoApiAndCacheEvent -> {
                    companyInfoUseCases.getCompanyInfoFromNetworkAndInsertToCacheUseCase().collect()
                }
            }
        }
    }


    fun clearListState() {
        if (_uiState.value is LaunchUiState.LaunchState) {
            _uiState.value = LaunchUiState.LaunchState(mergedLaunches = emptyList())
        }
    }


    fun newSearchEvent() {
        resetPageState()
        refreshSearchQueryEvent()
        printLogDebug("LaunchViewModel", "loadFirstPage: ${year}")
    }

    fun nextPage() {
        if ((getScrollPositionState() + 1) >= (getPageState() * PAGINATION_PAGE_SIZE)) {
            incrementPage()
            if (getPageState() > 1) {
                setEvent(FilterLaunchItemsInCacheEvent)
            }
        }
        printLogDebug("LaunchViewModel", "nextPage: triggered: ${getPageState()}")
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

    fun refreshSearchQueryEvent() {
        setEvent(LaunchEvents.MergeDataEvent)
    }

    companion object {
        // Shared Preference Files:
        private const val LAUNCH_PREFERENCES_KEY: String = "com.seancoyle.spacex.launch"

        // Shared Preference Keys
        const val LAUNCH_ORDER_KEY: String = "$LAUNCH_PREFERENCES_KEY.LAUNCH_ORDER"
        const val LAUNCH_FILTER_KEY: String = "$LAUNCH_PREFERENCES_KEY.LAUNCH_FILTER"

        const val LAUNCH_UI_STATE_KEY = "launch.state.key"
    }

}