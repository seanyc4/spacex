package com.seancoyle.spacex.framework.presentation.launch

import android.content.SharedPreferences
import android.os.Parcelable
import com.seancoyle.spacex.business.domain.model.company.CompanyInfoDomainEntity
import com.seancoyle.spacex.business.domain.model.company.CompanySummary
import com.seancoyle.spacex.business.domain.model.launch.LaunchDomainEntity
import com.seancoyle.spacex.business.domain.model.launch.LaunchType
import com.seancoyle.spacex.business.domain.model.launch.SectionTitle
import com.seancoyle.spacex.business.interactors.launch.LaunchInteractors
import com.seancoyle.spacex.business.domain.state.*
import com.seancoyle.spacex.business.interactors.company.CompanyInfoInteractors
import com.seancoyle.spacex.framework.datasource.cache.dao.launch.LAUNCH_ORDER_ASC
import com.seancoyle.spacex.framework.datasource.cache.dao.launch.LAUNCH_ORDER_DESC
import com.seancoyle.spacex.framework.presentation.common.BaseViewModel
import com.seancoyle.spacex.framework.presentation.launch.state.LaunchStateEvent.*
import com.seancoyle.spacex.framework.presentation.launch.state.LaunchViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
@HiltViewModel
class LaunchViewModel
@Inject
constructor(
    private val launchInteractors: LaunchInteractors,
    private val companyInfoInteractors: CompanyInfoInteractors,
    private val editor: SharedPreferences.Editor,
    sharedPreferences: SharedPreferences
) : BaseViewModel<LaunchViewState>() {

    init {
        setStateEvent(GetCompanyInfoFromNetworkAndInsertToCacheEvent)

        setLaunchOrder(
            sharedPreferences.getString(
                LAUNCH_ORDER,
                LAUNCH_ORDER_DESC
            )
        )
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

            is GetLaunchListFromNetworkAndInsertToCacheEvent -> {
                launchInteractors.getLaunchListFromNetworkAndInsertToCache.execute(
                    stateEvent = stateEvent
                )
            }

            is GetAllLaunchDataFromCacheEvent -> {
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

            is SearchLaunchListEvent -> {
                if (stateEvent.clearLayoutManagerState) {
                    clearLayoutManagerState()
                }
                launchInteractors.searchLaunchItemsInCache.execute(
                    query = getSearchQuery(),
                    order = getOrder(),
                    isLaunchSuccess = getIsLaunchSuccess(),
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

    private fun setLaunchList(launchList: List<LaunchDomainEntity>) {
        val update = getCurrentViewStateOrNew()
        update.launchList = launchList
        setViewState(update)
    }

    fun getLaunchList() = getCurrentViewStateOrNew().launchList

    private fun setCompanyInfo(companyInfo: CompanyInfoDomainEntity) {
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

    fun isPaginationExhausted() = getLaunchListSize() >= getNumLunchItemsInCache()

    private fun resetPage() {
        val update = getCurrentViewStateOrNew()
        update.page = 1
        setViewState(update)
    }

    fun isQueryExhausted(): Boolean{
        return getCurrentViewStateOrNew().isQueryExhausted?: false
    }

    // for debugging
    fun getActiveJobs() = dataChannelManager.getActiveJobs()

    fun clearList() {
        val update = getCurrentViewStateOrNew()
        update.launchList = ArrayList()
        setViewState(update)
    }

    fun loadFirstPage() {
        setQueryExhausted(false)
        resetPage()
        setStateEvent(SearchLaunchListEvent())
        Timber.e(
            "LaunchListViewModel",
            "loadFirstPage: ${getCurrentViewStateOrNew().searchQuery}"
        )
    }

    fun nextPage() {
        if (!isQueryExhausted()) {
            Timber.e("LaunchListViewModel", "attempting to load next page...")
            clearLayoutManagerState()
            incrementPageNumber()
            setStateEvent(SearchLaunchListEvent())
        }
    }

    fun refreshSearchQuery() {
        setQueryExhausted(false)
        setStateEvent(SearchLaunchListEvent(false))
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

    fun getOrder() = getCurrentViewStateOrNew().order ?: LAUNCH_ORDER_ASC
    private fun getIsLaunchSuccess() = getCurrentViewStateOrNew().isLaunchSuccess
    private fun getSearchQuery() = getCurrentViewStateOrNew().searchQuery ?: ""
    private fun getPage() = getCurrentViewStateOrNew().page ?: 1

    fun clearQueryParameters(){
        setQuery(null)
        setIsLaunchSuccess(null)
        resetPage()
    }

    fun setQueryExhausted(isExhausted: Boolean) {
        val update = getCurrentViewStateOrNew()
        update.isQueryExhausted = isExhausted
        setViewState(update)
    }

    fun setQuery(query: String?) {
        val update = getCurrentViewStateOrNew()
        update.searchQuery = query
        setViewState(update)
    }

    fun setIsLaunchSuccess(filter: Boolean?) {
        val update = getCurrentViewStateOrNew()
        update.isLaunchSuccess = filter
        setViewState(update)
    }

    fun setLaunchOrder(order: String?) {
        val update = getCurrentViewStateOrNew()
        update.order = order
        setViewState(update)
    }

    fun saveOrder(order: String) {
        editor.putString(LAUNCH_ORDER, order)
        editor.apply()
    }

    companion object {

        // Shared Preference Files:
        const val LAUNCH_PREFERENCES: String = "com.seancoyle.spacex"

        // Shared Preference Keys
        const val LAUNCH_ORDER: String = "${LAUNCH_PREFERENCES}.LAUNCH_ORDER"

    }

}













































