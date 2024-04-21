package com.seancoyle.feature.launch.implementation.presentation.state

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seancoyle.core.common.result.Result
import com.seancoyle.core.domain.AppStringResource
import com.seancoyle.core.domain.Order
import com.seancoyle.core.ui.NotificationState
import com.seancoyle.core.ui.NotificationType
import com.seancoyle.core.ui.NotificationUiType
import com.seancoyle.core.ui.asStringResource
import com.seancoyle.feature.launch.api.LaunchConstants.PAGINATION_PAGE_SIZE
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.api.domain.model.LinkType
import com.seancoyle.feature.launch.api.domain.model.Links
import com.seancoyle.feature.launch.implementation.R
import com.seancoyle.feature.launch.implementation.domain.usecase.LaunchesComponent
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchEvents.CreateMergedLaunchesEvent
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchEvents.DismissBottomSheetEvent
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchEvents.DismissFilterDialogEvent
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchEvents.DisplayFilterDialogEvent
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchEvents.GetCompanyApiAndCacheEvent
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchEvents.GetLaunchesApiAndCacheEvent
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchEvents.HandleLaunchClickEvent
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchEvents.LoadNextPageEvent
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchEvents.NewSearchEvent
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchEvents.NotificationEvent
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchEvents.OpenLinkEvent
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchEvents.PaginateLaunchesCacheEvent
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchEvents.SaveScrollPositionEvent
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchEvents.SetFilterStateEvent
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchEvents.SwipeToRefreshEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LaunchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val launchesComponent: LaunchesComponent,
    private val appStringResource: AppStringResource
) : ViewModel() {

    var uiState: MutableStateFlow<LaunchesUiState> = MutableStateFlow(LaunchesUiState.Loading)
        private set

    var filterState = MutableStateFlow(LaunchesFilterState())
        private set

    var scrollState = MutableStateFlow(LaunchesScrollState())
        private set

    var bottomSheetState = MutableStateFlow(BottomSheetUiState())
        private set

    var linkEvent = MutableSharedFlow<String>(replay = 0)
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
            onEvent(CreateMergedLaunchesEvent)
        } else {
            // Fresh app launch - get data from network
            onEvent(GetCompanyApiAndCacheEvent)
        }
    }

    private fun restoreStateOnProcessDeath() {
        savedStateHandle.get<LaunchesScrollState>(LAUNCH_LIST_STATE_KEY)?.let { scrollState ->
            this.scrollState.value = scrollState
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

    fun onEvent(event: LaunchEvents) {
        viewModelScope.launch {
            when (event) {
                is CreateMergedLaunchesEvent -> mergedLaunchesCacheUseCase()
                is DismissBottomSheetEvent -> dismissBottomSheet()
                is DismissFilterDialogEvent -> displayFilterDialog(false)
                is DisplayFilterDialogEvent -> displayFilterDialog(true)
                is GetCompanyApiAndCacheEvent -> getCompanyApiAndCacheUseCase()
                is GetLaunchesApiAndCacheEvent -> getLaunchesApiAndCacheUseCase()
                is HandleLaunchClickEvent -> handleLaunchClick(event.links)
                is LoadNextPageEvent -> loadNextPage(event.page)
                is NewSearchEvent -> newSearch()
                is NotificationEvent -> updateNotificationState(event)
                is OpenLinkEvent -> openLink(event.url)
                is PaginateLaunchesCacheEvent -> paginateLaunchesCacheUseCase()
                is SaveScrollPositionEvent -> setScrollPositionState(event.position)
                is SetFilterStateEvent -> setLaunchFilterState(
                    order = event.order,
                    launchStatus = event.launchStatus,
                    year = event.launchYear
                )
                is SwipeToRefreshEvent -> swipeToRefresh()
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
                    is Result.Success -> onEvent(CreateMergedLaunchesEvent)
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
                    is Result.Success -> onEvent(GetLaunchesApiAndCacheEvent)
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
                        val updatedLaunches = updateLaunchesWithAndroidResources(result)
                        currentState.isSuccess {
                            val paginatedLaunches = it.launches + (updatedLaunches)
                            it.copy(
                                launches = paginatedLaunches,
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
                            val updatedLaunches = updateLaunchesWithAndroidResources(result)
                            LaunchesUiState.Success(
                                launches = updatedLaunches,
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

    private fun updateLaunchesWithAndroidResources(result: Result.Success<List<LaunchTypes>>): List<LaunchTypes> {
        val updatedLaunches = result.data.map { launchType ->
            when (launchType) {
                is LaunchTypes.CompanySummary -> {
                    launchType.copy(company = buildCompanySummary(launchType.company))
                }

                is LaunchTypes.Launch -> {
                    launchType.copy(
                        launchDaysResId = getLaunchDateStringRes(launchType.launchDateStatus),
                        launchStatusIconResId = getLaunchStatusDrawableRes(launchType.launchStatus)
                    )
                }

                else -> launchType
            }
        }
        return updatedLaunches
    }

    private fun openLink(link: String) {
        viewModelScope.launch {
            linkEvent.emit(link)
        }
    }

    private fun buildCompanySummary(company: Company): Company {
        val summary = appStringResource.getString(
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
        return company.copy(summary = summary)
    }

    private fun getLaunchStatusDrawableRes(status: LaunchStatus): Int = when (status) {
        LaunchStatus.SUCCESS -> R.drawable.ic_launch_success
        LaunchStatus.FAILED -> R.drawable.ic_launch_fail
        LaunchStatus.UNKNOWN -> R.drawable.ic_launch_unknown
        LaunchStatus.ALL -> throw IllegalArgumentException("LaunchStatus.ALL is not supported here")
    }

    private fun getLaunchDateStringRes(status: LaunchDateStatus): Int {
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

    fun dismissNotification() {
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

    private fun newSearch() {
        clearListState()
        resetPageState()
        newSearchEvent()
        displayFilterDialog(false)
    }

    private fun loadNextPage(position: Int) {
        if ((position + 1) >= (getPageState() * PAGINATION_PAGE_SIZE)) {
            incrementPage()
            onEvent(PaginateLaunchesCacheEvent)
        }
    }

    private fun getScrollPositionState() = scrollState.value.scrollPosition
    private fun getPageState() = scrollState.value.page
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

    private fun swipeToRefresh() {
        clearQueryParameters()
        clearListState()
        onEvent(GetCompanyApiAndCacheEvent)
    }

    private fun setLaunchFilterState(
        order: Order,
        launchStatus: LaunchStatus,
        year: String
    ) {
        filterState.update { currentState ->
            currentState.copy(
                order = order,
                launchStatus = launchStatus,
                launchYear = year
            )
        }
    }

    fun displayFilterDialog(isDisplayed: Boolean) {
        filterState.update { currentState -> currentState.copy(isVisible = isDisplayed) }
    }

    private fun resetPageState() {
        scrollState.update { currentState -> currentState.copy(page = 1) }
    }

    private fun setPageState(pageNum: Int) {
        scrollState.update { currentState -> currentState.copy(page = pageNum) }
    }

    private fun setScrollPositionState(position: Int) {
        scrollState.update { currentState -> currentState.copy(scrollPosition = position) }
    }

    private fun incrementPage() {
        val incrementedPage = scrollState.value.page + 1
        scrollState.update { currentState -> currentState.copy(page = incrementedPage) }
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
        onEvent(CreateMergedLaunchesEvent)
        saveLaunchPreferences(
            order = getOrderState(),
            launchStatus = getLaunchStatusState(),
            launchYear = getSearchYearState()
        )
    }

    private fun handleLaunchClick(links: Links?) {
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
        onEvent(
            event = NotificationEvent(
                notificationState = NotificationState(
                    notificationType = NotificationType.Info,
                    message = R.string.no_links.asStringResource(),
                    notificationUiType = NotificationUiType.Dialog
                ),
            )
        )
    }

    private fun dismissBottomSheet() {
        bottomSheetState.update { currentState ->
            currentState.copy(isVisible = false)
        }
    }

    private companion object {
        const val LAUNCH_LIST_STATE_KEY = "launches.list.position.key"
    }

}