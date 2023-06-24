package com.seancoyle.launch.implementation.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.seancoyle.constants.LaunchDaoConstants.LAUNCH_ORDER_DESC
import com.seancoyle.constants.LaunchDaoConstants.LAUNCH_PAGINATION_PAGE_SIZE
import com.seancoyle.constants.LaunchNetworkConstants.LAUNCH_ALL
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.core.di.MainDispatcher
import com.seancoyle.core.domain.DataState
import com.seancoyle.core.domain.Event
import com.seancoyle.core.domain.MessageStack
import com.seancoyle.core.presentation.BaseViewModel
import com.seancoyle.core.util.printLogDebug
import com.seancoyle.core_datastore.AppDataStore
import com.seancoyle.launch.api.domain.model.CompanyInfo
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.api.domain.model.LaunchState
import com.seancoyle.launch.api.domain.model.ViewType
import com.seancoyle.launch.api.domain.usecase.CreateMergedLaunchesUseCase
import com.seancoyle.launch.implementation.domain.CompanyInfoUseCases
import com.seancoyle.launch.implementation.domain.LaunchUseCases
import com.seancoyle.launch.implementation.presentation.LaunchEvents.CreateMessageEvents
import com.seancoyle.launch.implementation.presentation.LaunchEvents.FilterLaunchItemsInCacheEvents
import com.seancoyle.launch.implementation.presentation.LaunchEvents.GetCompanyInfoFromCacheEvents
import com.seancoyle.launch.implementation.presentation.LaunchEvents.GetCompanyInfoFromNetworkAndInsertToCacheEvents
import com.seancoyle.launch.implementation.presentation.LaunchEvents.GetLaunchesFromNetworkAndInsertToCacheEvents
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
    private val createMergedLaunchesUseCase: CreateMergedLaunchesUseCase,
    private val savedStateHandle: SavedStateHandle,
    messageStack: MessageStack,
) : BaseViewModel<LaunchState>(
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher,
    messageStack = messageStack
) {

    init {
        restoreFilterAndOrderState()
        restoreStateOnProcessDeath()
        loadDataOnAppLaunchOrRestore()
    }

    private fun loadDataOnAppLaunchOrRestore() {
        if (getScrollPositionState() != 0) {
            //Restoring state from cache data
            setEvent(GetCompanyInfoFromCacheEvents)
            setEvent(FilterLaunchItemsInCacheEvents)
        } else {
            //Fresh app launch - get data from network
            setEvent(GetCompanyInfoFromNetworkAndInsertToCacheEvents)
            setEvent(GetLaunchesFromNetworkAndInsertToCacheEvents)
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

        data.let { uiState ->
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

        val job: Flow<DataState<LaunchState>?> = when (event) {

            is GetLaunchesFromNetworkAndInsertToCacheEvents -> {
                launchUseCases.getLaunchesFromNetworkAndInsertToCacheUseCase.invoke(
                    event = event
                )
            }

            is GetCompanyInfoFromNetworkAndInsertToCacheEvents -> {
                companyInfoUseCases.getCompanyInfoFromNetworkAndInsertToCacheUseCase.invoke(
                    event = event
                )
            }

            is GetCompanyInfoFromCacheEvents -> {
                companyInfoUseCases.getCompanyInfoFromCacheUseCase.invoke(
                    event = event
                )
            }

            is FilterLaunchItemsInCacheEvents -> {
                launchUseCases.filterLaunchItemsInCacheUseCase.invoke(
                    year = getSearchQueryState(),
                    order = getOrderState(),
                    launchFilter = getFilterState(),
                    page = getPageState(),
                    event = event
                )
            }

            is CreateMessageEvents -> {
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

    override fun initNewUIState(): LaunchState {
        return LaunchState()
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
        if ((getScrollPositionState() + 1) >= (getPageState() * LAUNCH_PAGINATION_PAGE_SIZE)) {
            incrementPage()
            if (getPageState() > 1) {
                setEvent(FilterLaunchItemsInCacheEvents)
            }
        }
        printLogDebug("LaunchViewModel", "nextPage: triggered: ${getPageState()}")
    }

    private fun getScrollPositionState() = getCurrentStateOrNew().scrollPosition ?: 0
    private fun getSearchQueryState() = getCurrentStateOrNew().yearQuery.orEmpty()
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

    private fun setMergedListState(list: List<ViewType>) {
        setState(getCurrentStateOrNew().copy(mergedLaunches = list))
        printLogDebug("CurrentState", " after merge: ${getCurrentStateOrNew()}")
    }

    fun setQueryState(query: String?) {
        setState(getCurrentStateOrNew().apply { yearQuery = query })
    }

    fun setLaunchOrderState(order: String?) {
        setState(getCurrentStateOrNew().apply { this.order = order })
        saveOrderToDatastore(order ?: LAUNCH_ORDER_DESC)
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

    private fun setPageState(currentState: LaunchState, pageNum: Int) {
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
        setEvent(FilterLaunchItemsInCacheEvents)
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