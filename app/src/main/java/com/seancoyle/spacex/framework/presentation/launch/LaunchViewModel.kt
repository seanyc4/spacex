package com.seancoyle.spacex.framework.presentation.launch

import android.os.Parcelable
import androidx.lifecycle.viewModelScope
import com.seancoyle.spacex.business.datastore.AppDataStore
import com.seancoyle.spacex.business.domain.model.company.CompanyInfoModel
import com.seancoyle.spacex.business.domain.model.company.CompanySummary
import com.seancoyle.spacex.business.domain.model.launch.LaunchModel
import com.seancoyle.spacex.business.domain.model.launch.LaunchType
import com.seancoyle.spacex.business.domain.model.launch.SectionTitle
import com.seancoyle.spacex.business.interactors.launch.LaunchInteractors
import com.seancoyle.spacex.business.domain.state.*
import com.seancoyle.spacex.business.interactors.company.CompanyInfoInteractors
import com.seancoyle.spacex.framework.datasource.cache.dao.launch.LAUNCH_ORDER_DESC
import com.seancoyle.spacex.framework.datasource.network.mappers.launch.LAUNCH_ALL
import com.seancoyle.spacex.framework.datasource.network.model.launch.LaunchOptions
import com.seancoyle.spacex.framework.presentation.common.BaseViewModel
import com.seancoyle.spacex.framework.presentation.launch.state.LaunchStateEvent.*
import com.seancoyle.spacex.framework.presentation.launch.state.LaunchViewState
import com.seancoyle.spacex.util.printLogDebug
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
@HiltViewModel
class LaunchViewModel
@Inject
constructor(
    private val launchInteractors: LaunchInteractors,
    private val companyInfoInteractors: CompanyInfoInteractors,
    val launchOptions: LaunchOptions,
    private val appDataStoreManager: AppDataStore,
) : BaseViewModel<LaunchViewState>() {

    init {
        setStateEvent(GetCompanyInfoFromNetworkAndInsertToCacheEvent)

        // Get filter and order from datastore if available
        // And update state accordingly
        viewModelScope.launch {
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
            }

            viewState.company?.let { companyInfo ->
                setCompanyInfo(companyInfo)
            }

            viewState.numLaunchItemsInCache?.let { numItems ->
                setNumLaunchItemsInCache(numItems)
            }
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {

        val job: Flow<DataState<LaunchViewState>?> = when (stateEvent) {

            is GetLaunchItemsFromNetworkAndInsertToCacheEvent -> {
                launchInteractors.getLaunchListFromNetworkAndInsertToCache.execute(
                    launchOptions = stateEvent.launchOptions,
                    stateEvent = stateEvent
                )
            }

            is GetAllLaunchItemsFromCacheEvent -> {
                launchInteractors.getAllLaunchItemsFromCache.execute(
                    stateEvent = stateEvent
                )
            }

            is GetCompanyInfoFromNetworkAndInsertToCacheEvent -> {
                companyInfoInteractors.getCompanyInfoFromNetworkAndInsertToCache.execute(
                    stateEvent = stateEvent
                )
            }

            is GetCompanyInfoFromCacheEvent -> {
                companyInfoInteractors.getCompanyInfoFromCache.execute(
                    stateEvent = stateEvent
                )
            }

            is FilterLaunchItemsInCacheEvent -> {
                if (stateEvent.clearLayoutManagerState) {
                    clearLayoutManagerState()
                }
                launchInteractors.filterLaunchItemsInCache.execute(
                    year = getSearchQuery(),
                    order = getOrder(),
                    launchFilter = getFilter(),
                    page = getPage(),
                    stateEvent = stateEvent
                )
            }

            is GetNumLaunchItemsInCacheEvent -> {
                launchInteractors.getNumLaunchItemsFromCache.execute(
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
        val update = getCurrentViewStateOrNew()
        update.launchList = launchList
        setViewState(update)
    }

    fun getLaunchList() = getCurrentViewStateOrNew().launchList

    private fun setCompanyInfo(companyInfo: CompanyInfoModel) {
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

    fun createLaunchData(companySummary: String?): List<Any> {

        val consolidatedList = mutableListOf<LaunchType>()

        consolidatedList.add(
            SectionTitle(
                title = "COMPANY",
                type = LaunchType.TYPE_TITLE
            )
        )
        consolidatedList.add(
            CompanySummary(
                summary = companySummary ?: "",
                type = LaunchType.TYPE_COMPANY
            )
        )
        consolidatedList.add(
            SectionTitle(
                title = "LAUNCH",
                type = LaunchType.TYPE_TITLE
            )
        )
        getLaunchList()?.map { launchItems ->
            consolidatedList.add(
                launchItems
            )

        }

        return consolidatedList
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
        saveOrder(order?: LAUNCH_ORDER_DESC)
    }

    fun setLaunchFilter(filter: Int?) {
        val update = getCurrentViewStateOrNew()
        update.launchFilter = filter
        setViewState(update)
        saveFilter(filter?: LAUNCH_ALL)
    }

    fun setIsDialogFilterDisplayed(isDisplayed: Boolean?) {
        val update = getCurrentViewStateOrNew()
        update.isDialogFilterDisplayed = isDisplayed
        setViewState(update)
    }

    fun getIsDialogFilterDisplayed() = getCurrentViewStateOrNew().isDialogFilterDisplayed

    private fun saveOrder(order: String) {
        viewModelScope.launch {
            appDataStoreManager.setStringValue(LAUNCH_ORDER, order)
        }
    }

    private fun saveFilter(filter: Int) {
        viewModelScope.launch {
            appDataStoreManager.setIntValue(LAUNCH_FILTER, filter)
        }
    }


    companion object {

        // Shared Preference Files:
        private const val LAUNCH_PREFERENCES: String = "com.seancoyle.spacex"

        // Shared Preference Keys
        const val LAUNCH_ORDER: String = "${LAUNCH_PREFERENCES}.LAUNCH_ORDER"
        const val LAUNCH_FILTER: String = "${LAUNCH_PREFERENCES}.LAUNCH_FILTER"

    }

}













































