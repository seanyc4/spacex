package com.seancoyle.feature.launch.implementation.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.seancoyle.core.common.coroutines.stateIn
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.AppStringResource
import com.seancoyle.core.domain.Order
import com.seancoyle.core.ui.NotificationState
import com.seancoyle.feature.launch.api.LaunchConstants.PAGINATION_LIMIT
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.presentation.model.UIErrors
import com.seancoyle.feature.launch.implementation.domain.usecase.component.LaunchesComponent
import com.seancoyle.feature.launch.implementation.presentation.model.LinksUi
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.Lazy
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import kotlinx.coroutines.flow.map

private const val TAG = "LaunchViewModel"

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
internal class LaunchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val launchesComponent: LaunchesComponent,
    private val appStringResource: Lazy<AppStringResource>
) : ViewModel() {

    var scrollState by savedStateHandle.saveable { mutableStateOf(LaunchesScrollState()) }
        private set

    var filterState by savedStateHandle.saveable { mutableStateOf(LaunchesFilterState()) }
        private set

    private val _paginationState = MutableStateFlow<PaginationState>(PaginationState.None)
    val paginationState = _paginationState.asStateFlow()

    private val _notificationState = MutableStateFlow<NotificationState?>(null)
    val notificationState = _notificationState.asStateFlow()

    private val _bottomSheetState = MutableStateFlow(BottomSheetUiState())
    val bottomSheetState = _bottomSheetState.asStateFlow()

    private val _linkEvent = MutableSharedFlow<String>(replay = 0)
    val linkEvent = _linkEvent.asSharedFlow()

    private val _errorEvent = MutableSharedFlow<UIErrors>(replay = 0)
    val errorEvent = _errorEvent.asSharedFlow()

    private var hasCheckedInitialData = false

    init {
        // Log initial state when ViewModel is created (restored from SavedStateHandle)
        Timber.tag(TAG).d("🔄 ViewModel created - Restoring scrollState: page=${scrollState.page}, position=${scrollState.scrollPosition}, isLastPage=${scrollState.isLastPage}")
        Timber.tag(TAG).d("🔄 ViewModel created - Restoring filterState: order=${filterState.order}, status=${filterState.launchStatus}, year=${filterState.launchYear}")
    }

    fun init() {
        if (hasCheckedInitialData) {
            Timber.tag(TAG).d("⏭️ init() already processed, skipping")
            return
        }
        hasCheckedInitialData = true

        if (scrollState.page > 0) {
            Timber.tag(TAG).d("🔄 init() called with saved state - page=${scrollState.page}, restoring from cache")
            // We have saved state from config change with actual progress, don't trigger fresh pagination
            return
        }

        Timber.tag(TAG).d("🏁 init() called - fresh start or page 0, savedState=${scrollState}")
        restoreFilterAndOrderState()

        // Observe feedState to detect if we have cached data
        viewModelScope.launch {
            feedState.collect { state ->
                when (state) {
                    is LaunchesUiState.Success -> {
                        if (state.launches.isNotEmpty() && getPageState() == 0) {
                            // We have cached data and page is still 0, calculate correct page
                            val calculatedPage = (state.launches.size / PAGINATION_LIMIT)
                            Timber.tag(TAG).d("📦 Found ${state.launches.size} cached items - set page to $calculatedPage")
                            scrollState = scrollState.copy(page = calculatedPage)
                            // Cancel this collector after handling
                            return@collect
                        }
                    }
                    is LaunchesUiState.Loading -> {
                        // Still loading, trigger network call after a small delay to let cache load
                        kotlinx.coroutines.delay(100)
                        if (feedState.value is LaunchesUiState.Loading) {
                            Timber.tag(TAG).d("🚀 No cached data detected - triggering initial network pagination")
                            onEvent(PaginateLaunchesNetworkEvent)
                            return@collect
                        }
                    }
                    else -> { }
                }
            }
        }
    }

    // Main data flow from the use case
    private val launchesData: StateFlow<List<LaunchTypes.Launch>> =
        launchesComponent.observeLaunchesUseCase()
            .map { result ->
                when (result) {
                    is LaunchResult.Success -> result.data
                    is LaunchResult.Error -> emptyList()
                }
            }.stateIn(
                scope = viewModelScope,
                initialValue = emptyList()
            )

    // Combine data and loading state into final UI state
    val feedState: StateFlow<LaunchesUiState> = kotlinx.coroutines.flow.combine(
        launchesData,
        _notificationState
    ) { launches, notification ->
        when {
            launches.isEmpty() && notification == null -> LaunchesUiState.Loading
            launches.isEmpty() -> LaunchesUiState.Error(errorNotificationState = notification)
            else -> LaunchesUiState.Success(
                launches = launches.map { launch -> launch.toUiModel(appStringResource) }
            )
        }
    }.stateIn(
        scope = viewModelScope,
        initialValue = LaunchesUiState.Loading
    )

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
                is DismissBottomSheetEvent -> dismissBottomSheet()
                is DismissFilterDialogEvent -> displayFilterDialog(false)
                is DisplayFilterDialogEvent -> displayFilterDialog(true)
                is DismissNotificationEvent -> dismissNotification()
                is HandleLaunchClickEvent -> handleLaunchClick(event.links)
                is LoadNextPageEvent ->  Unit //loadNextPage(event.page)
                is NewSearchEvent -> newSearch()
                is NotificationEvent -> updateNotificationState(event)
                is OpenLinkEvent -> openLink(event.url)
                is PaginateLaunchesCacheEvent -> Unit //paginateLaunchesCacheUseCase()
                is PaginateLaunchesNetworkEvent -> paginateLaunchesNetworkUseCase()
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
        _notificationState.update { event.notificationState }
    }

    private suspend fun paginateLaunchesNetworkUseCase() {
        Timber.tag(TAG).d("⚙️ paginateLaunchesNetworkUseCase called - isLastPage=${getIsLastPageState()}, paginationState=${_paginationState.value}")

        // Prevent concurrent pagination calls
        if (getIsLastPageState()) {
            Timber.tag(TAG).d("🚫 Pagination blocked: isLastPage = true")
            return
        }
        if (_paginationState.value == PaginationState.Loading) {
            Timber.tag(TAG).d("🚫 Pagination blocked: already loading")
            return
        }

        val currentPage = getPageState()
        Timber.tag(TAG).d("📄 Starting pagination for page: $currentPage")

        // Set loading state before the call
        _paginationState.update { PaginationState.Loading }
        Timber.tag(TAG).d("🔄 Set paginationState = Loading for page $currentPage")

        launchesComponent.getLaunchesApiAndCacheUseCase(currentPage)
            .collect { result ->
                when (result) {
                    is LaunchResult.Success -> {
                        // Check if this page had fewer items than the limit
                        // This tells us if there are more pages available
                        val fetchedItemsCount = result.data.size
                        Timber.tag(TAG).d("✅ Fetched $fetchedItemsCount items for page $currentPage")

                        val isLast = fetchedItemsCount < PAGINATION_LIMIT
                        setIsLastPageState(isLast)
                        Timber.tag(TAG).d("📊 isLastPage set to: $isLast (fetched: $fetchedItemsCount, limit: $PAGINATION_LIMIT)")

                        // Data will automatically flow through launchesData and feedState
                        // Just update pagination state and increment page
                        _paginationState.update { PaginationState.None }
                        Timber.tag(TAG).d("🔄 Reset loading states: paginationState = None")

                        incrementPage()
                        Timber.tag(TAG).d("➡️ Page incremented to: ${getPageState()}")
                    }

                    is LaunchResult.Error -> {
                        Timber.tag(TAG).e("❌ Pagination error for page $currentPage: ${result.error}")
                        _paginationState.update { PaginationState.Error }
                    }
                }
            }
    }

    private suspend fun openLink(link: String) {
        _linkEvent.emit(link)
    }


    private fun dismissNotification() {
        _notificationState.update { null }
    }

    private suspend fun newSearch() {
        resetPageState()
        newSearchEvent()
        displayFilterDialog(false)
    }

    /*private fun loadNextPage(position: Int) {
        if ((position + 1) >= (getPageState() * PAGINATION_LIMIT)) {
            incrementPage()
            onEvent(PaginateLaunchesCacheEvent)
        }
    }*/

    private fun getScrollPositionState() = scrollState.scrollPosition
    private fun getPageState() = scrollState.page
    private fun getIsLastPageState() = scrollState.isLastPage
    private fun getSearchYearState() = filterState.launchYear
    private fun getOrderState() = filterState.order
    private fun getLaunchStatusState() = filterState.launchStatus

    private fun clearQueryParameters() {
        setLaunchFilterState(
            order = Order.DESC,
            launchStatus = LaunchStatus.ALL,
            year = ""
        )
        resetPageState()
    }

    private fun swipeToRefresh() {
        clearQueryParameters()
        onEvent(PaginateLaunchesNetworkEvent)
    }

    private fun setLaunchFilterState(
        order: Order,
        launchStatus: LaunchStatus,
        year: String
    ) {
        filterState = filterState.copy(
            order = order,
            launchStatus = launchStatus,
            launchYear = year
        )
        Timber.tag(TAG).d("💾 Updated filterState: order=$order, status=$launchStatus, year=$year")
    }

    private fun displayFilterDialog(isDisplayed: Boolean) {
        filterState = filterState.copy(isVisible = isDisplayed)
        Timber.tag(TAG).d("💾 Updated filterState.isVisible: $isDisplayed")
    }

    private fun resetPageState() {
        scrollState = scrollState.copy(page = 0, isLastPage = false, scrollPosition = 0)
        Timber.tag(TAG).d("💾 Reset scrollState: page=0, isLastPage=false, scrollPosition=0")
    }

    private fun setIsLastPageState(isLastPage: Boolean) {
        scrollState = scrollState.copy(isLastPage = isLastPage)
        Timber.tag(TAG).d("💾 Updated scrollState.isLastPage: $isLastPage")
    }

    private fun setScrollPositionState(position: Int) {
        scrollState = scrollState.copy(scrollPosition = position)
        Timber.tag(TAG).d("💾 Updated scrollState.scrollPosition: $position")
    }

    private fun incrementPage() {
        scrollState = scrollState.copy(page = scrollState.page + 1)
        Timber.tag(TAG).d("💾 Updated scrollState.page: ${scrollState.page}")
    }

    private suspend fun saveLaunchPreferences(
        order: Order,
        launchStatus: LaunchStatus,
        launchYear: String
    ) {
        launchesComponent.saveLaunchPreferencesUseCase(
            order = order,
            launchStatus = launchStatus,
            launchYear = launchYear
        )
    }

    private suspend fun newSearchEvent() {
        // onEvent(CreateMergedLaunchesEvent)
        saveLaunchPreferences(
            order = getOrderState(),
            launchStatus = getLaunchStatusState(),
            launchYear = getSearchYearState()
        )
    }

    private suspend fun handleLaunchClick(links: LinksUi?) {
        val bottomSheetLinks = links.getLinks()

        if (bottomSheetLinks.isNotEmpty()) {
            _bottomSheetState.update { currentState ->
                currentState.copy(
                    isVisible = true,
                    bottomSheetLinks = bottomSheetLinks
                )

            }
        } else {
            _errorEvent.emit(UIErrors.NO_LINKS)
        }
    }

    private fun dismissBottomSheet() {
        _bottomSheetState.update { currentState ->
            currentState.copy(isVisible = false)
        }
    }

}
