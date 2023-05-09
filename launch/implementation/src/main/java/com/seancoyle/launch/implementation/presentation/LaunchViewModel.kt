package com.seancoyle.launch.implementation.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.seancoyle.constants.LaunchDaoConstants.LAUNCH_ORDER_DESC
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
import com.seancoyle.launch.api.model.LaunchType
import com.seancoyle.launch.api.model.LaunchViewState
import com.seancoyle.launch.api.usecase.CreateMergedListUseCase
import com.seancoyle.launch.implementation.domain.CompanyInfoUseCases
import com.seancoyle.launch.implementation.domain.LaunchUseCases
import com.seancoyle.launch.implementation.presentation.LaunchEvent.CreateMessageEvent
import com.seancoyle.launch.implementation.presentation.LaunchEvent.FilterLaunchItemsInCacheEvent
import com.seancoyle.launch.implementation.presentation.LaunchEvent.GetAllLaunchItemsFromCacheEvent
import com.seancoyle.launch.implementation.presentation.LaunchEvent.GetCompanyInfoFromCacheEvent
import com.seancoyle.launch.implementation.presentation.LaunchEvent.GetCompanyInfoFromNetworkAndInsertToCacheEvent
import com.seancoyle.launch.implementation.presentation.LaunchEvent.GetLaunchListFromNetworkAndInsertToCacheEvent
import com.seancoyle.launch.implementation.presentation.LaunchEvent.GetNumLaunchItemsInCacheEvent
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
        setEvent(GetCompanyInfoFromNetworkAndInsertToCacheEvent)
        setEvent(GetLaunchListFromNetworkAndInsertToCacheEvent)

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

    override fun setUpdatedState(data: LaunchViewState) {

        data.let { viewState ->
            viewState.launchList?.let { launchList ->
                setLaunchList(launchList)
                createMergedList(launchList)
            }

            viewState.company?.let { companyInfo ->
                setCompanyInfo(companyInfo)
            }

            viewState.numLaunchItemsInCache?.let { numItems ->
                setNumLaunchItemsInCache(numItems)
            }
        }
    }

    private fun createMergedList(
        launchList: List<LaunchModel>
    ) {
        if (launchList.isNotEmpty() && getCompanyInfo() != null) {
            setMergedList(
                createMergedListUseCase.createLaunchData(
                    companyInfo = getCompanyInfo(),
                    launchList = launchList
                )
            )
        }
    }

    override fun setEvent(event: Event) {

        val job: Flow<DataState<LaunchViewState>?> = when (event) {

            is GetLaunchListFromNetworkAndInsertToCacheEvent -> {
                launchUseCases.getLaunchListFromNetworkAndInsertToCacheUseCase.invoke(
                    event = event
                )
            }

            is GetAllLaunchItemsFromCacheEvent -> {
                launchUseCases.getAllLaunchItemsFromCacheUseCase.invoke(
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
                    year = getSearchQuery(),
                    order = getOrder(),
                    launchFilter = getFilter(),
                    page = getPage(),
                    event = event
                )
            }

            is GetNumLaunchItemsInCacheEvent -> {
                launchUseCases.getNumLaunchItemsFromCacheUseCase.invoke(
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

    override fun initNewUiState(): LaunchViewState {
        return LaunchViewState()
    }

    private fun setLaunchList(launchList: List<LaunchModel>) {
        val currentState = getCurrentStateOrNew()
        currentState.launchList = launchList
        setState(currentState)
    }

    fun getLaunchList() = getCurrentStateOrNew().launchList

    fun setCompanyInfo(companyInfo: CompanyInfoModel) {
        val currentState = getCurrentStateOrNew()
        currentState.company = companyInfo
        setState(currentState)
    }

    fun getCompanyInfo() = getCurrentStateOrNew().company

    private fun setNumLaunchItemsInCache(numItems: Int) {
        val currentState = getCurrentStateOrNew()
        currentState.numLaunchItemsInCache = numItems
        setState(currentState)
    }

    private fun resetPage() {
        val currentState = getCurrentStateOrNew()
        currentState.page = 1
        setState(currentState)
    }

    fun clearList() {
        val currentState = getCurrentStateOrNew()
        currentState.launchList = ArrayList()
        setState(currentState)
    }

    fun loadFirstPage() {
        resetPage()
        setEvent(FilterLaunchItemsInCacheEvent)
        printLogDebug(
            "LaunchListViewModel", "loadFirstPage: ${getCurrentStateOrNew().yearQuery}"
        )
    }

    fun nextPage() {
        incrementPage()
        if (getPage() > 1) {
            setEvent(FilterLaunchItemsInCacheEvent)
        }
        printLogDebug("LaunchListViewModel", "nextPage: triggered: ${getPage()}")
    }

    private fun getScrollPositionState() = getCurrentStateOrNew().scrollPosition ?: 0
    private fun getSearchQuery() = getCurrentStateOrNew().yearQuery ?: ""
    fun getPage() = getCurrentStateOrNew().page ?: 1
    fun getOrder() = getCurrentStateOrNew().order ?: LAUNCH_ORDER_DESC
    fun getIsDialogFilterDisplayed() = getCurrentStateOrNew().isDialogFilterDisplayed ?: false
    private fun getMergedList() = getCurrentStateOrNew().mergedList ?: emptyList()


    fun getFilter(): Int? {
        return if (getCurrentStateOrNew().launchFilter == LAUNCH_ALL) {
            setLaunchFilter(null)
            getCurrentStateOrNew().launchFilter
        } else {
            getCurrentStateOrNew().launchFilter
        }
    }

    fun clearQueryParameters() {
        clearList()
        setQuery(null)
        setLaunchFilter(null)
        resetPage()
    }


    private fun setMergedList(list: List<LaunchType>) {
        setState(getCurrentStateOrNew().copy(mergedList = list))
        printLogDebug("CurrentState", " after merge: ${getCurrentStateOrNew()}")
    }

    fun setQuery(query: String?) {
        val currentState = getCurrentStateOrNew()
        currentState.yearQuery = query
        setState(currentState)
    }

    fun setLaunchOrder(order: String?) {
        val currentState = getCurrentStateOrNew()
        currentState.order = order
        setState(currentState)
        saveOrder(order ?: LAUNCH_ORDER_DESC)
    }

    fun setLaunchFilter(filter: Int?) {
        val currentState = getCurrentStateOrNew()
        currentState.launchFilter = filter
        setState(currentState)
        saveFilter(filter ?: LAUNCH_ALL)
    }

    fun setIsDialogFilterDisplayed(isDisplayed: Boolean?) {
        val currentState = getCurrentStateOrNew()
        currentState.isDialogFilterDisplayed = isDisplayed
        setState(currentState)
    }

    private fun incrementPage() {
        val currentState = getCurrentStateOrNew()
        val newPageState = (currentState.page ?: 1) + 1
        currentState.page = newPageState
        setState(currentState)
        savedStateHandle[STATE_KEY_PAGE] = newPageState
    }

    fun setScrollPosition(position: Int) {
        val currentState = getCurrentStateOrNew()
        currentState.scrollPosition = position
        setState(currentState)
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

    fun refreshSearchQueryEvent() {
        setEvent(FilterLaunchItemsInCacheEvent)
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













































