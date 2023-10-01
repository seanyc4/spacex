package com.seancoyle.launch.implementation.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.seancoyle.core.Constants.ORDER_DESC
import com.seancoyle.core.Constants.PAGINATION_PAGE_SIZE
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.core.di.MainDispatcher
import com.seancoyle.core.domain.Result
import com.seancoyle.core.domain.Event
import com.seancoyle.core.domain.asResult
import com.seancoyle.core.util.printLogDebug
import com.seancoyle.core_datastore.AppDataStore
import com.seancoyle.launch.api.LaunchNetworkConstants.LAUNCH_ALL
import com.seancoyle.launch.api.domain.model.CompanyInfo
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.api.domain.model.ViewType
import com.seancoyle.launch.api.domain.usecase.CreateMergedLaunchesUseCase
import com.seancoyle.launch.api.presentation.LaunchUiState
import com.seancoyle.launch.implementation.domain.CompanyInfoUseCases
import com.seancoyle.launch.implementation.domain.LaunchUseCases
import com.seancoyle.launch.implementation.presentation.LaunchEvents.FilterLaunchItemsInCacheEvent
import com.seancoyle.launch.implementation.presentation.LaunchEvents.GetCompanyInfoFromCacheEvent
import com.seancoyle.launch.implementation.presentation.LaunchEvents.GetCompanyInfoFromNetworkAndInsertToCacheEvent
import com.seancoyle.launch.implementation.presentation.LaunchEvents.FetchLaunchesAndCacheAndUpdateUiStateEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
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

    private var lastKnownState: Pair<CompanyInfo?, List<Launch>?>? = null
    init {
        restoreFilterAndOrderState()
        restoreStateOnProcessDeath()
        loadDataOnAppLaunchOrRestore()

        updateState()
    }

    private fun updateState() {
        viewModelScope.launch {
            combine(
                _uiState.map { it.company },
                _uiState.map { it.launches }
            ) { companyInfo, launches -> companyInfo to launches }
                .collect { currentState ->
                    // Only proceed if the current state is different from the last known state
                    if (currentState != lastKnownState) {
                        lastKnownState = currentState
                        val (companyInfo, launches) = currentState
                        if (companyInfo != null && !launches.isNullOrEmpty()) {
                            setEvent(LaunchEvents.MergeDataEvent)
                        }
                    }
                }
        }
    }

    private fun loadDataOnAppLaunchOrRestore() {
        if (getScrollPositionState() != 0) {
            //Restoring state from cache data
            setEvent(GetCompanyInfoFromCacheEvent)
            setEvent(FilterLaunchItemsInCacheEvent)
        } else {
            //Fresh app launch - get data from network
            setEvent(GetCompanyInfoFromNetworkAndInsertToCacheEvent)
            setEvent(FetchLaunchesAndCacheAndUpdateUiStateEvent)
        }
    }

    private fun restoreStateOnProcessDeath() {
        savedStateHandle.get<LaunchUiState.LaunchState>(LAUNCH_UI_STATE_KEY)?.let { uiState ->
            setState(uiState)
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

    override fun setEvent(event: Event) {
        viewModelScope.launch {
            when (event) {

                is LaunchEvents.MergeDataEvent -> {
                    createMergedLaunchesUseCase(
                        companyInfo = getCompanyInfoState(),
                        launches = getLaunchesState()
                    ).collect { newState ->
                        _uiState.value = uiState.value.copy(mergedLaunches = newState)
                    }
                }

                is GetCompanyInfoFromCacheEvent -> {
                    companyInfoUseCases.getCompanyInfoFromCacheUseCase().asResult()
                        .map { result ->
                            when (result) {
                                is Result.Success -> {
                                    LaunchUiState.LaunchState(company = result.data)
                                }

                                is Result.Loading -> LaunchUiState.LaunchState(loading = true)
                                is Result.Error -> TODO()
                            }
                        }.stateIn(
                            scope = viewModelScope,
                            started = SharingStarted.WhileSubscribed(5_000),
                            initialValue = LaunchUiState.LaunchState(loading = true)
                        ).collect { newState ->
                            _uiState.value = _uiState.value.copy(company = newState.company)
                        }
                }

                is FilterLaunchItemsInCacheEvent -> {
                    launchUseCases.filterLaunchItemsInCacheUseCase(
                        year = getSearchQueryState(),
                        order = getOrderState(),
                        launchFilter = getFilterState(),
                        page = getPageState()
                    ).asResult()
                        .map { result ->
                            when (result) {
                                is Result.Success -> {
                                    LaunchUiState.LaunchState(launches = result.data)
                                }

                                is Result.Loading -> LaunchUiState.LaunchState(loading = true)
                                is Result.Error -> TODO()
                            }
                        }.stateIn(
                            scope = viewModelScope,
                            started = SharingStarted.WhileSubscribed(5_000),
                            initialValue = LaunchUiState.LaunchState(loading = true)
                        ).collect { newState ->
                            _uiState.value = _uiState.value.copy(launches = newState.launches)
                        }
                }

                is FetchLaunchesAndCacheAndUpdateUiStateEvent -> {
                    launchUseCases.getLaunchesFromNetworkAndInsertToCacheUseCase().onCompletion {
                        setEvent(FilterLaunchItemsInCacheEvent)
                    }.collect()
                }

                is GetCompanyInfoFromNetworkAndInsertToCacheEvent -> {
                    companyInfoUseCases.getCompanyInfoFromNetworkAndInsertToCacheUseCase()
                        .onCompletion {
                            setEvent(GetCompanyInfoFromCacheEvent)
                        }.collect()
                }
            }
        }
    }

    override fun initNewUIState(): LaunchUiState.LaunchState {
        return LaunchUiState.LaunchState()
    }

    fun getLaunchesState() = getCurrentState().launches ?: emptyList()

    private fun getCompanyInfoState() = getCurrentState().company

    fun clearListState() {
        setState(
            getCurrentState().copy(
                launches = emptyList(),
                mergedLaunches = emptyList()
            )
        )
    }

    fun newSearchEvent() {
        resetPageState()
        refreshSearchQueryEvent()
        printLogDebug("LaunchViewModel", "loadFirstPage: ${getCurrentState().yearQuery}")
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

    private fun getScrollPositionState() = getCurrentState().scrollPosition ?: 0
    private fun getSearchQueryState() = getCurrentState().yearQuery.orEmpty()
    fun getPageState() = getCurrentState().page ?: 1
    fun getOrderState() = getCurrentState().order ?: ORDER_DESC
    fun getIsDialogFilterDisplayedState() = getCurrentState().isDialogFilterDisplayed ?: false

    fun getFilterState(): Int? {
        return if (getCurrentState().launchFilter == LAUNCH_ALL) {
            setLaunchFilterState(null)
            getCurrentState().launchFilter
        } else {
            getCurrentState().launchFilter
        }
    }

    fun clearQueryParameters() {
        clearListState()
        setQueryState(null)
        setLaunchFilterState(null)
        resetPageState()
    }

    private fun setMergedListState(list: List<ViewType>) {
        setState(getCurrentState().copy(mergedLaunches = list))
        printLogDebug("CurrentState", " after merge: ${getCurrentState()}")
    }

    fun setQueryState(query: String?) {
        setState(getCurrentState().copy(yearQuery = query))
    }

    fun setLaunchOrderState(order: String?) {
        setState(getCurrentState().copy(order = order))
        saveOrderToDatastore(order ?: ORDER_DESC)
    }

    fun setLaunchFilterState(filter: Int?) {
        setState(getCurrentState().copy(launchFilter = filter))
        saveFilterToDataStore(filter ?: LAUNCH_ALL)
    }

    fun setIsDialogFilterDisplayedState(isDisplayed: Boolean?) {
        setState(getCurrentState().copy(isDialogFilterDisplayed = isDisplayed))
    }

    private fun resetPageState() {
        setState(getCurrentState().copy(page = 1))
    }

    private fun setCompanyInfoState(companyInfo: CompanyInfo) {
        setState(getCurrentState().copy(company = companyInfo))
    }

    private fun setLaunchesState(launches: List<Launch>) {
        setState(getCurrentState().copy(launches = launches))
    }

    private fun setPageState(currentState: LaunchUiState.LaunchState, pageNum: Int) {
        setState(currentState.copy(page = pageNum))
    }

    fun setScrollPositionState(position: Int) {
        setState(getCurrentState().copy(scrollPosition = position))
    }

    private fun incrementPage() {
        val currentState = getCurrentState()
        val newPageState = (currentState.page ?: 1) + 1
        setPageState(currentState, newPageState)
    }

    fun saveState() {
        savedStateHandle[LAUNCH_UI_STATE_KEY] = getCurrentState()
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
        setEvent(FilterLaunchItemsInCacheEvent)
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