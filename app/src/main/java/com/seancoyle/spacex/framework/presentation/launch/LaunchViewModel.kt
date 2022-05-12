package com.seancoyle.spacex.framework.presentation.launch

import android.os.Parcelable
import com.seancoyle.spacex.business.domain.model.company.CompanyInfoDomainEntity
import com.seancoyle.spacex.business.domain.model.company.CompanySummary
import com.seancoyle.spacex.business.domain.model.launch.LaunchDomainEntity
import com.seancoyle.spacex.business.domain.model.launch.LaunchType
import com.seancoyle.spacex.business.domain.model.launch.SectionTitle
import com.seancoyle.spacex.business.interactors.launch.LaunchInteractors
import com.seancoyle.spacex.business.domain.state.*
import com.seancoyle.spacex.business.interactors.company.CompanyInfoInteractors
import com.seancoyle.spacex.framework.presentation.common.BaseViewModel
import com.seancoyle.spacex.framework.presentation.launch.state.LaunchStateEvent.*
import com.seancoyle.spacex.framework.presentation.launch.state.LaunchViewState
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
    private val companyInfoInteractors: CompanyInfoInteractors
) : BaseViewModel<LaunchViewState>() {

    init {
        setStateEvent(GetLaunchListFromNetworkAndInsertToCacheEvent)
    }

    override fun handleNewData(data: LaunchViewState) {

        data.let { viewState ->
            viewState.launchList?.let { launchList ->
                setLaunchList(launchList)
                setStateEvent(GetCompanyInfoFromNetworkAndInsertToCacheEvent)
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
                launchInteractors.getLaunchItemsFromNetworkAndInsertToCache.execute(
                    stateEvent = stateEvent
                )
            }

            is GetLaunchListFromCacheEvent -> {
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

    fun getLaunchListSize() = getCurrentViewStateOrNew().launchList?.size ?: 0

    private fun getNumLunchItemsInCache() = getCurrentViewStateOrNew().numLaunchItemsInCache ?: 0

    // for debugging
    fun getActiveJobs() = dataChannelManager.getActiveJobs()

    fun clearList() {
        val update = getCurrentViewStateOrNew()
        update.launchList = ArrayList()
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

    fun clearLayoutManagerState() {
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

}













































