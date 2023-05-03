package com.seancoyle.launch.implementation.presentation

import android.os.Parcelable
import androidx.lifecycle.viewModelScope
import com.seancoyle.constants.LaunchDaoConstants.LAUNCH_ORDER_DESC
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
import com.seancoyle.launch.api.model.LaunchOptions
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
import com.seancoyle.launch.implementation.presentation.LaunchStateEvent.GetLaunchItemsFromNetworkAndInsertToCacheEvent
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
class LaunchViewModel
@Inject
constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val launchUseCases: LaunchUseCases,
    private val companyInfoUseCases: CompanyInfoUseCases,
    val launchOptions: LaunchOptions,
    private val appDataStoreManager: AppDataStore,
    private val createMergedListUseCase: CreateMergedListUseCase
) : BaseViewModel<LaunchViewState>(
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher
) {

    init {
        setStateEvent(GetCompanyInfoFromNetworkAndInsertToCacheEvent)

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
                setMergedList(
                    createMergedListUseCase.createLaunchData(
                        companyInfo = getCompanyInfo(),
                        launchList = launchList
                    )
                )
            }

            viewState.company?.let { companyInfo ->
                setCompanyInfo(companyInfo)
            }

            viewState.numLaunchItemsInCache?.let { numItems ->
                setNumLaunchItemsInCache(numItems)
            }
        }
    }

    private fun setMergedList(list: List<LaunchType>) {
        setViewState(getCurrentViewStateOrNew().copy(mergedList = list))
    }

    override fun setStateEvent(stateEvent: StateEvent) {

        val job: Flow<DataState<LaunchViewState>?> = when (stateEvent) {

            is GetLaunchItemsFromNetworkAndInsertToCacheEvent -> {
                launchUseCases.getLaunchListFromNetworkAndInsertToCacheUseCase.invoke(
                    launchOptions = stateEvent.launchOptions,
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
                if (stateEvent.clearLayoutManagerState) {
                    clearLayoutManagerState()
                }
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

    fun setLaunchList(launchList: List<LaunchModel>) {
        val update = getCurrentViewStateOrNew()
        update.launchList = launchList
        setViewState(update)
    }

    fun getLaunchList() = getCurrentViewStateOrNew().launchList

    fun setCompanyInfo(companyInfo: CompanyInfoModel) {
        val update = getCurrentViewStateOrNew()
        update.company = companyInfo
        setViewState(update)
    }

    fun getCompanyInfo() = getCurrentViewStateOrNew().company

    private fun setNumLaunchItemsInCache(numItems: Int) {
        val update = getCurrentViewStateOrNew()
        update.numLaunchItemsInCache = numItems
        setViewState(update)
    }

    private fun getLaunchListSize() = getCurrentViewStateOrNew().launchList?.size ?: 0

    private fun getNumLunchItemsInCache() = getCurrentViewStateOrNew().numLaunchItemsInCache ?: 0

    fun isPaginationExhausted(): Boolean {
        printLogDebug(
            "LaunchListViewModel",
            "isPaginationExhausted: ${getLaunchListSize()}, ${getNumLunchItemsInCache()}"
        )
        return getLaunchListSize() >= getNumLunchItemsInCache()
    }

    private fun resetPage() {
        val update = getCurrentViewStateOrNew()
        update.page = 1
        setViewState(update)
    }

    fun isQueryExhausted(): Boolean {
        printLogDebug(
            "LaunchListViewModel",
            "isQueryExhausted: ${getCurrentViewStateOrNew().isQueryExhausted}"
        )
        return getCurrentViewStateOrNew().isQueryExhausted ?: false
    }

    fun clearList() {
        val update = getCurrentViewStateOrNew()
        update.launchList = ArrayList()
        setViewState(update)
    }

    fun loadFirstPage() {
        setQueryExhausted(false)
        resetPage()
        setStateEvent(FilterLaunchItemsInCacheEvent())
        printLogDebug(
            "LaunchListViewModel",
            "loadFirstPage: ${getCurrentViewStateOrNew().yearQuery}"
        )
    }

    fun nextPage() {
        if (!isQueryExhausted()) {
            printLogDebug("LaunchListViewModel", "attempting to load next page...")
            clearLayoutManagerState()
            incrementPageNumber()
            setStateEvent(FilterLaunchItemsInCacheEvent())
        }
    }

    fun refreshSearchQuery() {
        setQueryExhausted(false)
        setStateEvent(FilterLaunchItemsInCacheEvent(false))
    }

    private fun incrementPageNumber() {
        val update = getCurrentViewStateOrNew()
        val page = update.copy().page ?: 1
        update.page = page.plus(1)
        setViewState(update)
    }

    fun retrieveNumLaunchItemsInCache() {
        setStateEvent(GetNumLaunchItemsInCacheEvent)
    }

    fun getLayoutManagerState(): Parcelable? {
        return getCurrentViewStateOrNew().layoutManagerState
    }

    fun setLayoutManagerState(layoutManagerState: Parcelable) {
        val update = getCurrentViewStateOrNew()
        update.layoutManagerState = layoutManagerState
        setViewState(update)
    }

    private fun clearLayoutManagerState() {
        val update = getCurrentViewStateOrNew()
        update.layoutManagerState = null
        setViewState(update)
    }

    private fun getSearchQuery() = getCurrentViewStateOrNew().yearQuery ?: ""
    private fun getPage() = getCurrentViewStateOrNew().page ?: 1
    fun getOrder() = getCurrentViewStateOrNew().order ?: LAUNCH_ORDER_DESC

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
        setQueryExhausted(false)
        resetPage()
    }

    fun setQueryExhausted(isExhausted: Boolean) {
        val update = getCurrentViewStateOrNew()
        update.isQueryExhausted = isExhausted
        setViewState(update)
    }

    fun setQuery(query: String?) {
        val update = getCurrentViewStateOrNew()
        update.yearQuery = query
        setViewState(update)
    }

    fun setLaunchOrder(order: String?) {
        val update = getCurrentViewStateOrNew()
        update.order = order
        setViewState(update)
        saveOrder(order ?: LAUNCH_ORDER_DESC)
    }

    fun setLaunchFilter(filter: Int?) {
        val update = getCurrentViewStateOrNew()
        update.launchFilter = filter
        setViewState(update)
        saveFilter(filter ?: LAUNCH_ALL)
    }

    fun setIsDialogFilterDisplayed(isDisplayed: Boolean?) {
        val update = getCurrentViewStateOrNew()
        update.isDialogFilterDisplayed = isDisplayed
        setViewState(update)
    }

    fun getIsDialogFilterDisplayed() = getCurrentViewStateOrNew().isDialogFilterDisplayed

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

    companion object {
        // Shared Preference Files:
        private const val LAUNCH_PREFERENCES: String = "com.seancoyle.spacex"

        // Shared Preference Keys
        const val LAUNCH_ORDER: String = "$LAUNCH_PREFERENCES.LAUNCH_ORDER"
        const val LAUNCH_FILTER: String = "$LAUNCH_PREFERENCES.LAUNCH_FILTER"
    }

}













































