package com.seancoyle.feature.launch.implementation.presentation.state

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seancoyle.core.common.result.Result
import com.seancoyle.core.domain.Order
import com.seancoyle.core.ui.NotificationState
import com.seancoyle.core.ui.NotificationType
import com.seancoyle.core.ui.NotificationUiType
import com.seancoyle.core.ui.StringResource
import com.seancoyle.core.ui.asStringResource
import com.seancoyle.feature.launch.api.LaunchConstants.PAGINATION_PAGE_SIZE
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LinkType
import com.seancoyle.feature.launch.api.domain.model.Links
import com.seancoyle.feature.launch.implementation.R
import com.seancoyle.feature.launch.implementation.domain.usecase.LaunchesComponent
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchEvents.CreateMergedLaunchesEvent
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchEvents.GetCompanyApiAndCacheEvent
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchEvents.GetLaunchesApiAndCacheEvent
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchEvents.NotificationEvent
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchEvents.PaginateLaunchesCacheEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LaunchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val launchesComponent: LaunchesComponent
) : ViewModel() {

    var uiState: MutableStateFlow<LaunchesUiState> = MutableStateFlow(LaunchesUiState.Loading)
        private set

    var filterState = MutableStateFlow(LaunchesFilterState())
        private set

    var scrollState = MutableStateFlow(LaunchesScrollState())
        private set

    var bottomSheetState = MutableStateFlow(BottomSheetUiState())
        private set

    fun init() {
        if (uiState.value is LaunchesUiState.Loading) {
            restoreFilterAndOrderState()
            restoreStateOnProcessDeath()
            loadDataOnAppLaunchOrRestore()
        }
    }

    private fun loadDataOnAppLaunchOrRestore() {
        if (getScrollPositionState() != 0) {
            // Restoring state from cache data
            setEvent(CreateMergedLaunchesEvent)
        } else {
            // Fresh app launch - get data from network
            setEvent(GetCompanyApiAndCacheEvent)
        }
    }

    private fun restoreStateOnProcessDeath() {
        savedStateHandle.get<LaunchesScrollState>(LAUNCH_LIST_STATE_KEY)?.let { listState ->
            scrollState.value = listState
        }
    }

    private fun restoreFilterAndOrderState() {
        viewModelScope.launch {
            val result = launchesComponent.getLaunchPreferencesUseCase()
            setLaunchFilterState(
                order = result.order,
                launchStatus = result.launchStatus,
                year = result.launchYear
            )
        }
    }

    private fun setEvent(event: LaunchEvents) {
        viewModelScope.launch {
            when (event) {
                is CreateMergedLaunchesEvent -> mergedLaunchesCacheUseCase()
                is PaginateLaunchesCacheEvent -> paginateLaunchesCacheUseCase()
                is GetCompanyApiAndCacheEvent -> getCompanyApiAndCacheUseCase()
                is GetLaunchesApiAndCacheEvent -> getLaunchesApiAndCacheUseCase()
                is NotificationEvent -> updateNotificationState(event)

                else -> {}
            }
        }
    }

    private fun updateNotificationState(event: NotificationEvent) {
        uiState.update { currentState ->
            currentState.isSuccess { it.copy(notificationState = event.notificationState) }
        }
    }

    private suspend fun getLaunchesApiAndCacheUseCase() {
        launchesComponent.getLaunchesApiAndCacheUseCase()
            .onStart { uiState.update { LaunchesUiState.Loading } }
            .collect { result ->
                when (result) {
                    is Result.Success -> setEvent(CreateMergedLaunchesEvent)
                    is Result.Error -> {
                        uiState.update {
                            LaunchesUiState.Error(
                                errorNotificationState = NotificationState(
                                    message = result.error.asStringResource(),
                                    notificationUiType = NotificationUiType.Dialog,
                                    notificationType = NotificationType.Error
                                )
                            )
                        }
                    }
                }
            }
    }

    private suspend fun getCompanyApiAndCacheUseCase() {
        launchesComponent.getCompanyApiAndCacheUseCase()
            .onStart { uiState.update { LaunchesUiState.Loading } }
            .collect { result ->
                when (result) {
                    is Result.Success -> setEvent(GetLaunchesApiAndCacheEvent)
                    is Result.Error -> {
                        uiState.update {
                            LaunchesUiState.Error(
                                errorNotificationState = NotificationState(
                                    message = result.error.asStringResource(),
                                    notificationUiType = NotificationUiType.Dialog,
                                    notificationType = NotificationType.Error
                                )
                            )
                        }
                    }
                }
            }
    }

    private suspend fun paginateLaunchesCacheUseCase() {
        launchesComponent.paginateLaunchesCacheUseCase(
            year = getSearchYearState(),
            order = getOrderState(),
            launchFilter = getLaunchStatusState(),
            page = getPageState()
        ).collect { result ->
            when (result) {
                is Result.Success -> {
                    // Pagination - We append the next 30 rows to the current state as a new list
                    // This triggers a recompose and keeps immutability
                    uiState.update { currentState ->
                        currentState.isSuccess {
                            val updatedLaunches =
                                it.launches + (result.data ?: emptyList())
                            it.copy(
                                launches = updatedLaunches,
                                paginationState = PaginationState.None
                            )
                        }
                    }
                }

                is Result.Error -> {
                    uiState.update { currentState ->
                        currentState.isSuccess { it.copy(paginationState = PaginationState.Error) }
                    }
                }
            }
        }
    }

    private suspend fun mergedLaunchesCacheUseCase() {
        launchesComponent.createMergedLaunchesCacheUseCase(
            year = getSearchYearState(),
            order = getOrderState(),
            launchFilter = getLaunchStatusState(),
            page = getPageState()
        ).distinctUntilChanged()
            .collect { result ->
                when (result) {
                    is Result.Success -> {
                        uiState.update {
                            LaunchesUiState.Success(
                                launches = result.data,
                                paginationState = PaginationState.None
                            )
                        }
                    }

                    is Result.Error -> {
                        uiState.update {
                            LaunchesUiState.Error(
                                errorNotificationState = NotificationState(
                                    message = result.error.asStringResource(),
                                    notificationUiType = NotificationUiType.Snackbar,
                                    notificationType = NotificationType.Error
                                )
                            )
                        }
                    }
                }
            }
    }

    fun buildCompanySummary(company: Company) =
        StringResource.AndroidStringResource(
            R.string.company_info,
            arrayOf(
                company.name,
                company.founder,
                company.founded,
                company.employees,
                company.launchSites,
                company.valuation
            )
        )

    fun getLaunchStatusIcon(status: LaunchStatus): Int = when (status) {
        LaunchStatus.SUCCESS -> R.drawable.ic_launch_success
        LaunchStatus.FAILED -> R.drawable.ic_launch_fail
        LaunchStatus.UNKNOWN -> R.drawable.ic_launch_unknown
        LaunchStatus.ALL -> throw IllegalArgumentException("LaunchStatus.ALL is not supported here")
    }

    fun getLaunchDateText(status: LaunchDateStatus): Int {
        return when (status) {
            LaunchDateStatus.PAST -> R.string.days_since_now
            LaunchDateStatus.FUTURE -> R.string.days_from_now
        }
    }

    private fun LaunchesUiState.isSuccess(updateState: (LaunchesUiState.Success) -> LaunchesUiState): LaunchesUiState {
        return if (this is LaunchesUiState.Success) {
            updateState(this)
        } else {
            this
        }
    }

    fun dismissError() {
        uiState.update { currentState ->
            currentState.isSuccess {
                it.copy(notificationState = null)
            }
        }
    }

    private fun clearListState() {
        uiState.update { currentState ->
            currentState.isSuccess { it.copy(launches = emptyList()) }
        }
    }

    fun newSearch() {
        clearListState()
        resetPageState()
        newSearchEvent()
        setDialogFilterDisplayedState(false)
    }

    fun nextPage(position: Int) {
        if ((position + 1) >= (getPageState() * PAGINATION_PAGE_SIZE)) {
            incrementPage()
            setEvent(PaginateLaunchesCacheEvent)
        }
    }

    private fun getScrollPositionState() = scrollState.value.scrollPosition
    fun getPageState() = scrollState.value.page
    private fun getSearchYearState() = filterState.value.launchYear
    private fun getOrderState() = filterState.value.order
    private fun getLaunchStatusState(): LaunchStatus = filterState.value.launchStatus

    private fun clearQueryParameters() {
        clearListState()
        setLaunchFilterState(
            order = Order.DESC,
            launchStatus = LaunchStatus.ALL,
            year = ""
        )
        resetPageState()
    }

    fun swipeToRefresh() {
        clearQueryParameters()
        clearListState()
        setEvent(GetCompanyApiAndCacheEvent)
    }

    private fun updateFilterState(update: LaunchesFilterState.() -> LaunchesFilterState) {
        filterState.value = filterState.value.update()
    }

    private fun updateListState(update: LaunchesScrollState.() -> LaunchesScrollState) {
        scrollState.value = scrollState.value.update()
    }

    fun setLaunchFilterState(
        order: Order,
        launchStatus: LaunchStatus,
        year: String
    ) {
        updateFilterState {
            copy(
                order = order,
                launchStatus = launchStatus,
                launchYear = year
            )
        }
    }

    fun setDialogFilterDisplayedState(isDisplayed: Boolean) {
        updateFilterState { copy(isVisible = isDisplayed) }
    }

    private fun resetPageState() {
        updateListState { copy(page = 1) }
    }

    private fun setPageState(pageNum: Int) {
        updateListState { copy(page = pageNum) }
    }

    fun setScrollPositionState(position: Int) {
        updateListState { copy(scrollPosition = position) }
    }

    private fun incrementPage() {
        val incrementedPage = scrollState.value.page + 1
        updateListState { copy(page = incrementedPage) }
        setPageState(incrementedPage)
    }

    fun saveState() {
        savedStateHandle[LAUNCH_LIST_STATE_KEY] = scrollState.value
    }

    private fun saveLaunchPreferences(
        order: Order,
        launchStatus: LaunchStatus,
        launchYear: String
    ) {
        viewModelScope.launch {
            launchesComponent.saveLaunchPreferencesUseCase(
                order = order,
                launchStatus = launchStatus,
                launchYear = launchYear
            )
        }
    }

    private fun newSearchEvent() {
        setEvent(CreateMergedLaunchesEvent)
        saveLaunchPreferences(
            order = getOrderState(),
            launchStatus = getLaunchStatusState(),
            launchYear = getSearchYearState()
        )
    }

    fun handleLaunchClick(links: Links?) {
        val linkTypes = createLinkTypeList(links)

        if (validateLinks(linkTypes)) {
            bottomSheetState.update { currentState ->
                currentState.copy(isVisible = true, linkTypes = linkTypes)
            }
        } else {
            bottomSheetState.update {
                displayNoLinksError()
                BottomSheetUiState()
            }
        }
    }

    private fun createLinkTypeList(links: Links?): List<LinkType> {
        val linkTypes = listOfNotNull(
            LinkType(R.string.article, links?.articleLink),
            LinkType(R.string.webcast, links?.webcastLink),
            LinkType(R.string.wikipedia, links?.wikiLink)
        )
        return linkTypes
    }

    private fun validateLinks(links: List<LinkType>): Boolean {
        return links.all { it.link?.isNotEmpty() == true }
    }

    private fun displayNoLinksError() {
        setEvent(
            event = NotificationEvent(
                notificationState = NotificationState(
                    notificationType = NotificationType.Info,
                    message = R.string.no_links.asStringResource(),
                    notificationUiType = NotificationUiType.Dialog
                ),
            )
        )
    }

    fun displayUnableToLoadLinkError() {
        setEvent(
            event = NotificationEvent(
                notificationState = NotificationState(
                    notificationType = NotificationType.Error,
                    message = R.string.error_links.asStringResource(),
                    notificationUiType = NotificationUiType.Snackbar
                )
            )
        )
    }

    fun dismissBottomSheet() {
        bottomSheetState.update { currentState ->
            currentState.copy(isVisible = false)
        }
    }

    private companion object {
        const val LAUNCH_LIST_STATE_KEY = "launches.list.position.key"
    }

}