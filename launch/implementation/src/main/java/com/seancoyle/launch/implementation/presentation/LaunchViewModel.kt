package com.seancoyle.launch.implementation.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.seancoyle.core.Constants.ORDER_DESC
import com.seancoyle.core.Constants.PAGINATION_PAGE_SIZE
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.core.di.MainDispatcher
import com.seancoyle.core.domain.DataState
import com.seancoyle.core.domain.Event
import com.seancoyle.core.util.printLogDebug
import com.seancoyle.core_datastore.AppDataStore
import com.seancoyle.core_ui.BaseViewModel
import com.seancoyle.launch.api.LaunchNetworkConstants.LAUNCH_ALL
import com.seancoyle.launch.api.domain.model.CompanyInfo
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.api.domain.model.ViewType
import com.seancoyle.launch.api.domain.usecase.CreateMergedLaunchesUseCase
import com.seancoyle.launch.api.presentation.LaunchUiState
import com.seancoyle.launch.implementation.domain.CompanyInfoUseCases
import com.seancoyle.launch.implementation.domain.LaunchUseCases
import com.seancoyle.launch.implementation.presentation.LaunchEvents.CreateMessageEvent
import com.seancoyle.launch.implementation.presentation.LaunchEvents.FilterLaunchItemsInCacheEvent
import com.seancoyle.launch.implementation.presentation.LaunchEvents.GetCompanyInfoFromCacheEvent
import com.seancoyle.launch.implementation.presentation.LaunchEvents.GetCompanyInfoFromNetworkAndInsertToCacheEvent
import com.seancoyle.launch.implementation.presentation.LaunchEvents.GetLaunchesFromNetworkAndInsertToCacheEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LaunchViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val launchUseCases: LaunchUseCases,
    private val companyInfoUseCases: CompanyInfoUseCases,
    private val appDataStoreManager: AppDataStore,
    private val createMergedLaunchesUseCase: CreateMergedLaunchesUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<LaunchUiState.LaunchState>(
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher
) {

    init {
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
            setEvent(GetCompanyInfoFromNetworkAndInsertToCacheEvent)
            setEvent(GetLaunchesFromNetworkAndInsertToCacheEvent)
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

    private fun createMergedList(
        launches: List<Launch>
    ) {
        if (launches.isNotEmpty() && getCompanyInfoState() != null) {
            setMergedListState(
                createMergedLaunchesUseCase.invoke(
                    companyInfo = getCompanyInfoState(),
                    launches = launches
                )
            )
        }
    }

    override fun setEvent(event: Event) {
        when (event) {

            is GetLaunchesFromNetworkAndInsertToCacheEvent -> {
                processJob(launchUseCases.getLaunchesFromNetworkAndInsertToCacheUseCase())
            }

            is GetCompanyInfoFromNetworkAndInsertToCacheEvent -> {
                processJob(companyInfoUseCases.getCompanyInfoFromNetworkAndInsertToCacheUseCase())
            }

            is GetCompanyInfoFromCacheEvent -> {
                processJob(companyInfoUseCases.getCompanyInfoFromCacheUseCase())
            }

            is FilterLaunchItemsInCacheEvent -> {
                val job = launchUseCases.filterLaunchItemsInCacheUseCase(
                    year = getSearchQueryState(),
                    order = getOrderState(),
                    launchFilter = getFilterState(),
                    page = getPageState()
                )
                processJob(job)
            }

            is CreateMessageEvent -> {
                emitStateMessageEvent(
                    stateMessage = event.stateMessage
                )
            }
        }
    }

    private fun processJob(job: Flow<DataState<LaunchUiState.LaunchState>?>) {
        viewModelScope.launch {
            job.collect { dataState->
                dataState?.data?.let { uiState ->
                    uiState.launches?.let { launches ->
                        setLaunchesState(launches)
                        createMergedList(launches)
                    }
                    uiState.company?.let { companyInfo ->
                        setCompanyInfoState(companyInfo)
                        printLogDebug("CurrentState", ": ${getCurrentStateOrNew()}")
                    }
                }
            }
        }
    }


    override fun initNewUIState(): LaunchUiState.LaunchState {
        return LaunchUiState.LaunchState()
    }

    private fun setLaunchesState(launches: List<Launch>) {
        setState(getCurrentStateOrNew().apply { this.launches = launches })
    }

    fun getLaunchesState() = getCurrentStateOrNew().launches

    private fun setCompanyInfoState(companyInfo: CompanyInfo) {
        setState(getCurrentStateOrNew().apply { company = companyInfo })
    }

    private fun getCompanyInfoState() = getCurrentStateOrNew().company

    private fun resetPageState() {
        setState(getCurrentStateOrNew().apply { page = 1 })
    }

    fun clearListState() {
        setState(
            getCurrentStateOrNew().copy(
                launches = emptyList(),
                mergedLaunches = emptyList()
            )
        )
    }

    fun newSearchEvent() {
        resetPageState()
        refreshSearchQueryEvent()
        printLogDebug("LaunchViewModel", "loadFirstPage: ${getCurrentStateOrNew().yearQuery}")
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

    private fun getScrollPositionState() = getCurrentStateOrNew().scrollPosition ?: 0
    private fun getSearchQueryState() = getCurrentStateOrNew().yearQuery.orEmpty()
    fun getPageState() = getCurrentStateOrNew().page ?: 1
    fun getOrderState() = getCurrentStateOrNew().order ?: ORDER_DESC
    fun getIsDialogFilterDisplayedState() = getCurrentStateOrNew().isDialogFilterDisplayed ?: false

    fun getFilterState(): Int? {
        return if (getCurrentStateOrNew().launchFilter == LAUNCH_ALL) {
            setLaunchFilterState(null)
            getCurrentStateOrNew().launchFilter
        } else {
            getCurrentStateOrNew().launchFilter
        }
    }

    fun clearQueryParameters() {
        clearListState()
        setQueryState(null)
        setLaunchFilterState(null)
        resetPageState()
    }

    private fun setMergedListState(list: List<ViewType>) {
        setState(getCurrentStateOrNew().copy(mergedLaunches = list))
        printLogDebug("CurrentState", " after merge: ${getCurrentStateOrNew()}")
    }

    fun setQueryState(query: String?) {
        setState(getCurrentStateOrNew().apply { yearQuery = query })
    }

    fun setLaunchOrderState(order: String?) {
        setState(getCurrentStateOrNew().apply { this.order = order })
        saveOrderToDatastore(order ?: ORDER_DESC)
    }

    fun setLaunchFilterState(filter: Int?) {
        setState(getCurrentStateOrNew().apply { launchFilter = filter })
        saveFilterToDataStore(filter ?: LAUNCH_ALL)
    }

    fun setIsDialogFilterDisplayedState(isDisplayed: Boolean?) {
        setState(getCurrentStateOrNew().apply { isDialogFilterDisplayed = isDisplayed })
    }

    private fun incrementPage() {
        val currentState = getCurrentStateOrNew()
        val newPageState = (currentState.page ?: 1) + 1
        setPageState(currentState, newPageState)
    }

    private fun setPageState(currentState: LaunchUiState.LaunchState, pageNum: Int) {
        currentState.page = pageNum
        setState(currentState)
    }

    fun setScrollPositionState(position: Int) {
        setState(getCurrentStateOrNew().apply { scrollPosition = position })
    }

    fun saveState() {
        savedStateHandle[LAUNCH_UI_STATE_KEY] = getCurrentStateOrNew()
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