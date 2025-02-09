package com.seancoyle.feature.launch.implementation.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seancoyle.core.common.result.Result
import com.seancoyle.core.domain.AppStringResource
import com.seancoyle.core.domain.Order
import com.seancoyle.core.ui.NotificationState
import com.seancoyle.core.ui.NotificationType
import com.seancoyle.core.ui.UiComponentType
import com.seancoyle.core.ui.extensions.asStringResource
import com.seancoyle.feature.launch.api.LaunchConstants.PAGINATION_PAGE_SIZE
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.domain.model.UIErrors
import com.seancoyle.feature.launch.implementation.domain.usecase.component.LaunchesComponent
import com.seancoyle.feature.launch.implementation.presentation.model.LaunchTypesUiModel
import com.seancoyle.feature.launch.implementation.presentation.model.LinksUi
import com.seancoyle.feature.launch.implementation.presentation.model.RocketUi
import com.seancoyle.feature.launch.implementation.presentation.model.RocketWithMissionUi
import com.seancoyle.feature.launch.implementation.presentation.state.BottomSheetUiState
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchEvents
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchEvents.*
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchesFilterState
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchesScrollState
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchesUiState
import com.seancoyle.feature.launch.implementation.presentation.state.PaginationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.Lazy

@HiltViewModel
internal class LaunchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val launchesComponent: LaunchesComponent,
    private val appStringResource: Lazy<AppStringResource>
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

    var errorEvent = MutableSharedFlow<UIErrors>(replay = 0)
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
            onEvent(GetSpaceXDataEvent)
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
                is DismissNotificationEvent -> dismissNotification()
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
                is GetSpaceXDataEvent -> getSpaceXDataUseCase()
            }

        }
    }

    private fun updateNotificationState(event: NotificationEvent) {
        uiState.update { currentState ->
            currentState.isSuccess { it.copy(notificationState = event.notificationState) }
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

    private suspend fun getSpaceXDataUseCase() {
        launchesComponent.getSpaceXDataUseCase()
            .onStart { uiState.update { LaunchesUiState.Loading } }
            .collect { result ->
                when (result) {
                    is Result.Success -> onEvent(CreateMergedLaunchesEvent)
                    is Result.Error -> {
                        uiState.update {
                            LaunchesUiState.Error(
                                errorNotificationState = NotificationState(
                                    message = result.error.asStringResource(),
                                    uiComponentType = UiComponentType.Dialog,
                                    notificationType = NotificationType.Error
                                )
                            )
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
                                    uiComponentType = UiComponentType.Snackbar,
                                    notificationType = NotificationType.Error
                                )
                            )
                        }
                    }
                }
            }
    }

    private fun updateLaunchesWithAndroidResources(
        result: Result.Success<List<LaunchTypes>>
    ): List<LaunchTypesUiModel> {
        return result.data.map { launchType ->
            when (launchType) {
                is LaunchTypes.CompanySummary -> {
                    LaunchTypesUiModel.CompanySummaryUi(
                        id = launchType.id,
                        summary = launchType.getSummary(appStringResource.get()),
                        name = launchType.name,
                        founder = launchType.founder,
                        founded = launchType.founded.toString(),
                        employees = launchType.employees,
                        launchSites = launchType.launchSites.toString(),
                        valuation = launchType.valuation
                    )
                }

                is LaunchTypes.Launch -> {
                    LaunchTypesUiModel.LaunchUi(
                        id = launchType.id,
                        launchDate = launchType.launchDate,
                        launchYear = launchType.launchYear,
                        launchStatus = launchType.launchStatus,
                        links = LinksUi(
                            missionImage = launchType.links.missionImage,
                            articleLink = launchType.links.articleLink,
                            webcastLink = launchType.links.webcastLink,
                            wikiLink = launchType.links.wikiLink
                        ),
                        missionName = launchType.missionName,
                        rocket = RocketUi(
                            rocketNameAndType = launchType.rocket.rocketNameAndType
                        ),
                        launchDateStatus = launchType.launchDateStatus,
                        launchDays = launchType.launchDays,
                        launchDaysResId = launchType.launchDateStatus.getDateStringRes(),
                        launchStatusIconResId = launchType.launchStatus.getDrawableRes()
                    )
                }

                is LaunchTypes.SectionTitle -> {
                    LaunchTypesUiModel.SectionTitleUi(
                        id = launchType.id,
                        title = launchType.title
                    )
                }

                is LaunchTypes.Grid -> {
                    LaunchTypesUiModel.GridUi(
                        id = launchType.id,
                        items = RocketWithMissionUi(
                            links = LinksUi(
                                missionImage = launchType.items.links.missionImage,
                                articleLink = launchType.items.links.articleLink,
                                webcastLink = launchType.items.links.webcastLink,
                                wikiLink = launchType.items.links.wikiLink
                            ),
                            rocket = RocketUi(
                                rocketNameAndType = launchType.items.rocket.rocketNameAndType
                            )
                        )
                    )
                }

                is LaunchTypes.Carousel -> {
                    LaunchTypesUiModel.CarouselUi(
                        id = launchType.id,
                        items = launchType.items.map { rocketWithMission ->
                            RocketWithMissionUi(
                                links = LinksUi(
                                    missionImage = rocketWithMission.links.missionImage,
                                    articleLink = rocketWithMission.links.articleLink,
                                    webcastLink = rocketWithMission.links.webcastLink,
                                    wikiLink = rocketWithMission.links.wikiLink
                                ),
                                rocket = RocketUi(
                                    rocketNameAndType = rocketWithMission.rocket.rocketNameAndType
                                )
                            )
                        }
                    )
                }
            }
        }
    }

    private fun openLink(link: String) {
        viewModelScope.launch {
            linkEvent.emit(link)
        }
    }

    private fun LaunchesUiState.isSuccess(updateState: (LaunchesUiState.Success) -> LaunchesUiState): LaunchesUiState {
        return if (this is LaunchesUiState.Success) {
            updateState(this)
        } else {
            this
        }
    }

    private fun dismissNotification() {
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
    private fun getLaunchStatusState() = filterState.value.launchStatus

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
        onEvent(GetSpaceXDataEvent)
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

    private fun displayFilterDialog(isDisplayed: Boolean) {
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

    private suspend fun handleLaunchClick(links: LinksUi?) {
        val bottomSheetLinks = links.getLinks()

        if (bottomSheetLinks.isNotEmpty()) {
            bottomSheetState.update { currentState ->
                currentState.copy(
                    isVisible = true,
                    bottomSheetLinks = bottomSheetLinks
                )

            }
        } else {
            errorEvent.emit(UIErrors.NO_LINKS)
        }
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