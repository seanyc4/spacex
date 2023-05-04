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
import com.seancoyle.core.state.StateEvent
import com.seancoyle.core.util.printLogDebug
import com.seancoyle.core_datastore.AppDataStore
import com.seancoyle.launch.api.model.CompanyInfoModel
import com.seancoyle.launch.api.model.LaunchModel
import com.seancoyle.launch.api.model.LaunchType
import com.seancoyle.launch.api.model.LaunchViewState
import com.seancoyle.launch.api.usecase.CreateMergedListUseCase
import com.seancoyle.launch.implementation.domain.CompanyInfoUseCases
import com.seancoyle.launch.implementation.domain.LaunchUseCases
import com.seancoyle.launch.implementation.presentation.LaunchStateEvent.CreateStateMessageEvent
import com.seancoyle.launch.implementation.presentation.LaunchStateEvent.FilterLaunchItemsInCacheEvent
import com.seancoyle.launch.implementation.presentation.LaunchStateEvent.GetAllLaunchItemsFromCacheEvent
import com.seancoyle.launch.implementation.presentation.LaunchStateEvent.GetCompanyInfoFromCacheEvent
import com.seancoyle.launch.implementation.presentation.LaunchStateEvent.GetCompanyInfoFromNetworkAndInsertToCacheEvent
import com.seancoyle.launch.implementation.presentation.LaunchStateEvent.GetLaunchListFromNetworkAndInsertToCacheEvent
import com.seancoyle.launch.implementation.presentation.LaunchStateEvent.GetNumLaunchItemsInCacheEvent
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
) : BaseViewModel<LaunchViewState>(
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher
) {

    init {
        setStateEvent(GetCompanyInfoFromNetworkAndInsertToCacheEvent)
        setStateEvent(GetLaunchListFromNetworkAndInsertToCacheEvent)

        // Get filter and order from datastore if available
        // And update state accordingly
        viewModelScope.launch(ioDispatcher) {
            setLaunchOrder(
                appDataStoreManager.readStringValue(LAUNCH_ORDER)
            )
            setLaunchFilter(
                appDataStoreManager.readIntValue(LAUNCH_FILTER)
            )
        }
    }

    override fun handleNewData(data: LaunchViewState) {

        data.let { viewState ->
            viewState.launchList?.let { launchList ->
                setLaunchList(launchList)
                printLogDebug("CurrentState", ": ${getCurrentViewStateOrNew()}")
                if (launchList.isNotEmpty()) {
                    setMergedList(
                        createMergedListUseCase.createLaunchData(
                            companyInfo = getCompanyInfo(),
                            launchList = launchList
                        )
                    )
                }
            }

            viewState.company?.let { companyInfo ->
                setCompanyInfo(companyInfo)
                printLogDebug("CurrentState", ": ${getCurrentViewStateOrNew()}")
            }

            viewState.numLaunchItemsInCache?.let { numItems ->
                setNumLaunchItemsInCache(numItems)
            }
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {

        val job: Flow<DataState<LaunchViewState>?> = when (stateEvent) {

            is GetLaunchListFromNetworkAndInsertToCacheEvent -> {
                launchUseCases.getLaunchListFromNetworkAndInsertToCacheUseCase.invoke(
                    stateEvent = stateEvent
                )
            }

            is GetAllLaunchItemsFromCacheEvent -> {
                launchUseCases.getAllLaunchItemsFromCacheUseCase.invoke(
                    stateEvent = stateEvent
                )
            }

            is GetCompanyInfoFromNetworkAndInsertToCacheEvent -> {
                companyInfoUseCases.getCompanyInfoFromNetworkAndInsertToCacheUseCase.invoke(
                    stateEvent = stateEvent
                )
            }

            is GetCompanyInfoFromCacheEvent -> {
                companyInfoUseCases.getCompanyInfoFromCacheUseCase.invoke(
                    stateEvent = stateEvent
                )
            }

            is FilterLaunchItemsInCacheEvent -> {
                launchUseCases.filterLaunchItemsInCacheUseCase.invoke(
                    year = getSearchQuery(),
                    order = getOrder(),
                    launchFilter = getFilter(),
                    page = getPage(),
                    stateEvent = stateEvent
                )
            }

            is GetNumLaunchItemsInCacheEvent -> {
                launchUseCases.getNumLaunchItemsFromCacheUseCase.invoke(
                    stateEvent = stateEvent
                )
            }

            is CreateStateMessageEvent -> {
                emitStateMessageEvent(
                    stateMessage = stateEvent.stateMessage,
                    stateEvent = stateEvent
                )
            }

            else -> {
                emitInvalidStateEvent(stateEvent)
            }
        }
        launchJob(stateEvent, job)
    }

    override fun initNewViewState(): LaunchViewState {
        return LaunchViewState()
    }

    private fun setLaunchList(launchList: List<LaunchModel>) {
        val currentState = getCurrentViewStateOrNew()
        currentState.launchList = launchList
        setViewState(currentState)
    }

    fun getLaunchList() = getCurrentViewStateOrNew().launchList

    fun setCompanyInfo(companyInfo: CompanyInfoModel) {
        val currentState = getCurrentViewStateOrNew()
        currentState.company = companyInfo
        setViewState(currentState)
    }

    fun getCompanyInfo() = getCurrentViewStateOrNew().company

    private fun setNumLaunchItemsInCache(numItems: Int) {
        val currentState = getCurrentViewStateOrNew()
        currentState.numLaunchItemsInCache = numItems
        setViewState(currentState)
    }

    private fun resetPage() {
        val currentState = getCurrentViewStateOrNew()
        currentState.page = 1
        setViewState(currentState)
    }

    fun clearList() {
        val currentState = getCurrentViewStateOrNew()
        currentState.launchList = ArrayList()
        setViewState(currentState)
    }

    fun loadFirstPage() {
        resetPage()
        setStateEvent(FilterLaunchItemsInCacheEvent)
        printLogDebug(
            "LaunchListViewModel", "loadFirstPage: ${getCurrentViewStateOrNew().yearQuery}"
        )
    }

    fun nextPage() {
        if ((getScrollPositionState()?.plus(1))!! >= (getPage().times(LAUNCH_PAGINATION_PAGE_SIZE))) {
            incrementPage()
            printLogDebug("LaunchListViewModel", "nextPage: triggered: ${getPage()}")

            if (getPage() > 1) {
                setStateEvent(FilterLaunchItemsInCacheEvent)
            }
        }
    }

    private fun getScrollPositionState() = getCurrentViewStateOrNew().scrollPosition ?: 0
    private fun getSearchQuery() = getCurrentViewStateOrNew().yearQuery ?: ""
    fun getPage() = getCurrentViewStateOrNew().page ?: 1
    fun getOrder() = getCurrentViewStateOrNew().order ?: LAUNCH_ORDER_DESC
    fun getIsDialogFilterDisplayed() = getCurrentViewStateOrNew().isDialogFilterDisplayed ?: false


    fun getFilter(): Int? {
        return if (getCurrentViewStateOrNew().launchFilter == LAUNCH_ALL) {
            setLaunchFilter(null)
            getCurrentViewStateOrNew().launchFilter
        } else {
            getCurrentViewStateOrNew().launchFilter
        }
    }

    fun clearQueryParameters() {
        clearList()
        setQuery(null)
        setLaunchFilter(null)
        resetPage()
    }


    private fun setMergedList(list: List<LaunchType>) {
        setViewState(getCurrentViewStateOrNew().copy(mergedList = list))
        printLogDebug("CurrentState", " after merge: ${getCurrentViewStateOrNew()}")
    }
    fun setQuery(query: String?) {
        val currentState = getCurrentViewStateOrNew()
        currentState.yearQuery = query
        setViewState(currentState)
    }

    fun setLaunchOrder(order: String?) {
        val currentState = getCurrentViewStateOrNew()
        currentState.order = order
        setViewState(currentState)
        saveOrder(order ?: LAUNCH_ORDER_DESC)
    }

    fun setLaunchFilter(filter: Int?) {
        val currentState = getCurrentViewStateOrNew()
        currentState.launchFilter = filter
        setViewState(currentState)
        saveFilter(filter ?: LAUNCH_ALL)
    }

    fun setIsDialogFilterDisplayed(isDisplayed: Boolean?) {
        val currentState = getCurrentViewStateOrNew()
        currentState.isDialogFilterDisplayed = isDisplayed
        setViewState(currentState)
    }

    private fun incrementPage() {
        val currentState = getCurrentViewStateOrNew()
        val newPageState = (currentState.page ?: 1) + 1
        currentState.page = newPageState
        setViewState(currentState)
        savedStateHandle[STATE_KEY_PAGE] = newPageState
    }

    fun setScrollPosition(position: Int) {
        val currentState = getCurrentViewStateOrNew()
        currentState.scrollPosition = position
        setViewState(currentState)
        savedStateHandle[STATE_KEY_LIST_POSITION] = position
    }

    private fun saveOrder(order: String) {
        viewModelScope.launch(ioDispatcher) {
            appDataStoreManager.setStringValue(LAUNCH_ORDER, order)
        }
    }

    private fun saveFilter(filter: Int) {
        viewModelScope.launch(ioDispatcher) {
            appDataStoreManager.setIntValue(LAUNCH_FILTER, filter)
        }
    }

    fun retrieveNumLaunchItemsInCache() {
        setStateEvent(GetNumLaunchItemsInCacheEvent)
    }

    fun refreshSearchQueryEvent() {
        setStateEvent(FilterLaunchItemsInCacheEvent)
    }

    companion object {
        // Shared Preference Files:
        private const val LAUNCH_PREFERENCES: String = "com.seancoyle.spacex"

        // Shared Preference Keys
        const val LAUNCH_ORDER: String = "$LAUNCH_PREFERENCES.LAUNCH_ORDER"
        const val LAUNCH_FILTER: String = "$LAUNCH_PREFERENCES.LAUNCH_FILTER"

        const val STATE_KEY_PAGE = "launch.state.page.key"
        const val STATE_KEY_QUERY = "launch.state.query.key"
        const val STATE_KEY_LIST_POSITION = "launch.state.list_position"
    }

}













































