package com.seancoyle.launch.implementation.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.seancoyle.constants.LaunchDaoConstants.LAUNCH_ORDER_DESC
import com.seancoyle.constants.LaunchDaoConstants.LAUNCH_PAGINATION_PAGE_SIZE
import com.seancoyle.constants.LaunchNetworkConstants.LAUNCH_ALL
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.core.di.MainDispatcher
import com.seancoyle.core.presentation.BaseViewModel
import com.seancoyle.core.state.DataState
import com.seancoyle.core.state.Event
import com.seancoyle.core.util.printLogDebug
import com.seancoyle.core_datastore.AppDataStore
import com.seancoyle.launch.api.model.CompanyInfoModel
import com.seancoyle.launch.api.model.LaunchModel
import com.seancoyle.launch.api.model.LaunchState
import com.seancoyle.launch.api.model.LaunchType
import com.seancoyle.launch.api.usecase.CreateMergedListUseCase
import com.seancoyle.launch.implementation.domain.CompanyInfoUseCases
import com.seancoyle.launch.implementation.domain.LaunchUseCases
import com.seancoyle.launch.implementation.presentation.LaunchEvent.CreateMessageEvent
import com.seancoyle.launch.implementation.presentation.LaunchEvent.FilterLaunchItemsInCacheEvent
import com.seancoyle.launch.implementation.presentation.LaunchEvent.GetCompanyInfoFromCacheEvent
import com.seancoyle.launch.implementation.presentation.LaunchEvent.GetCompanyInfoFromNetworkAndInsertToCacheEvent
import com.seancoyle.launch.implementation.presentation.LaunchEvent.GetLaunchListFromNetworkAndInsertToCacheEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
@HiltViewModel
class LaunchViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val launchUseCases: LaunchUseCases,
    private val companyInfoUseCases: CompanyInfoUseCases,
    private val appDataStoreManager: AppDataStore,
    private val createMergedListUseCase: CreateMergedListUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<LaunchState>(
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
            setEvent(GetLaunchListFromNetworkAndInsertToCacheEvent)
        }
    }

    private fun restoreStateOnProcessDeath() {
        savedStateHandle.get<LaunchState>(LAUNCH_UI_STATE_KEY)?.let { uiState ->
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

    override fun setUpdatedState(data: LaunchState) {

        data.let { viewState ->
            viewState.launchList?.let { launchList ->
                setLaunchListState(launchList)
                printLogDebug("CurrentState", ": ${getCurrentStateOrNew()}")
                if (launchList.isNotEmpty()) {
                    setMergedListState(
                        createMergedListUseCase.createLaunchData(
                            companyInfo = getCompanyInfoState(),
                            launchList = launchList
                        )
                    )
                }
            }

            viewState.company?.let { companyInfo ->
                setCompanyInfoState(companyInfo)
                printLogDebug("CurrentState", ": ${getCurrentStateOrNew()}")
            }
        }
    }

    override fun setEvent(event: Event) {

        val job: Flow<DataState<LaunchState>?> = when (event) {

            is GetLaunchListFromNetworkAndInsertToCacheEvent -> {
                launchUseCases.getLaunchListFromNetworkAndInsertToCacheUseCase.invoke(
                    event = event
                )
            }

            is GetCompanyInfoFromNetworkAndInsertToCacheEvent -> {
                companyInfoUseCases.getCompanyInfoFromNetworkAndInsertToCacheUseCase.invoke(
                    event = event
                )
            }

            is GetCompanyInfoFromCacheEvent -> {
                companyInfoUseCases.getCompanyInfoFromCacheUseCase.invoke(
                    event = event
                )
            }

            is FilterLaunchItemsInCacheEvent -> {
                launchUseCases.filterLaunchItemsInCacheUseCase.invoke(
                    year = getSearchQueryState(),
                    order = getOrderState(),
                    launchFilter = getFilterState(),
                    page = getPageState(),
                    event = event
                )
            }

            is CreateMessageEvent -> {
                emitStateMessageEvent(
                    stateMessage = event.stateMessage,
                    event = event
                )
            }

            else -> {
                emitInvalidEvent(event)
            }
        }
        launchJob(event, job)
    }

    override fun initNewUiState(): LaunchState {
        return LaunchState()
    }

    private fun setLaunchListState(launchList: List<LaunchModel>) {
        val currentState = getCurrentStateOrNew()
        currentState.launchList = launchList
        setState(currentState)
    }

    fun getLaunchListState() = getCurrentStateOrNew().launchList

    private fun setCompanyInfoState(companyInfo: CompanyInfoModel) {
        val currentState = getCurrentStateOrNew()
        currentState.company = companyInfo
        setState(currentState)
    }

    private fun getCompanyInfoState() = getCurrentStateOrNew().company

    private fun resetPageState() {
        val currentState = getCurrentStateOrNew()
        currentState.page = 1
        setState(currentState)
    }

    fun clearListState() {
        setState(
            getCurrentStateOrNew().copy(
                launchList = emptyList(),
                mergedList = emptyList()
            )
        )
    }

    fun newSearchEvent() {
        resetPageState()
        refreshSearchQueryEvent()
        printLogDebug("LaunchListViewModel", "loadFirstPage: ${getCurrentStateOrNew().yearQuery}")
    }

    fun nextPage() {
        if((getScrollPositionState() + 1) >= (getPageState() * LAUNCH_PAGINATION_PAGE_SIZE) ) {
            incrementPage()
            if (getPageState() > 1) {
                setEvent(FilterLaunchItemsInCacheEvent)
            }
        }
        printLogDebug("LaunchListViewModel", "nextPage: triggered: ${getPageState()}")
    }

    private fun getScrollPositionState() = getCurrentStateOrNew().scrollPosition ?: 0
    private fun getSearchQueryState() = getCurrentStateOrNew().yearQuery ?: ""
    fun getPageState() = getCurrentStateOrNew().page ?: 1
    fun getOrderState() = getCurrentStateOrNew().order ?: LAUNCH_ORDER_DESC
    fun getIsDialogFilterDisplayedState() = getCurrentStateOrNew().isDialogFilterDisplayed ?: false
    fun getRefreshState() = getCurrentStateOrNew().isRefreshing

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

    private fun setMergedListState(list: List<LaunchType>) {
        setState(getCurrentStateOrNew().copy(mergedList = list))
        printLogDebug("CurrentState", " after merge: ${getCurrentStateOrNew()}")
    }

    fun setQueryState(query: String?) {
        val currentState = getCurrentStateOrNew()
        currentState.yearQuery = query
        setState(currentState)
    }

    fun setLaunchOrderState(order: String?) {
        val currentState = getCurrentStateOrNew()
        currentState.order = order
        setState(currentState)
        saveOrderToDatastore(order ?: LAUNCH_ORDER_DESC)
    }

    fun setLaunchFilterState(filter: Int?) {
        val currentState = getCurrentStateOrNew()
        currentState.launchFilter = filter
        setState(currentState)
        saveFilterToDataStore(filter ?: LAUNCH_ALL)
    }

    fun setIsDialogFilterDisplayedState(isDisplayed: Boolean?) {
        val currentState = getCurrentStateOrNew()
        currentState.isDialogFilterDisplayed = isDisplayed
        setState(currentState)
    }

    private fun incrementPage() {
        val currentState = getCurrentStateOrNew()
        val newPageState = (currentState.page ?: 1) + 1
        setPageState(currentState, newPageState)
    }

    private fun setPageState(currentState: LaunchState, pageNum: Int) {
        currentState.page = pageNum
        setState(currentState)
    }

    fun setScrollPositionState(position: Int) {
        val currentState = getCurrentStateOrNew()
        currentState.scrollPosition = position
        setState(currentState)
    }

    fun saveState(){
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