package com.seancoyle.feature.launch.implementation.presentation

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seancoyle.core.common.coroutines.stateIn
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.AppStringResource
import com.seancoyle.core.domain.Order
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
import kotlinx.coroutines.flow.map

private const val SCROLL_STATE_KEY = "launches.scroll.state.key"
private const val TAG = "LaunchViewModel"

@HiltViewModel
internal class LaunchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val launchesComponent: LaunchesComponent,
    private val appStringResource: Lazy<AppStringResource>
) : ViewModel() {

    private val _paginationState = MutableStateFlow<PaginationState>(PaginationState.None)
    val paginationState = _paginationState.asStateFlow()

    private val _notificationState = MutableStateFlow<com.seancoyle.core.ui.NotificationState?>(null)
    val notificationState = _notificationState.asStateFlow()

    private val _filterState = MutableStateFlow(LaunchesFilterState())
    val filterState = _filterState.asStateFlow()

    private val _scrollState = MutableStateFlow(
        savedStateHandle.get<LaunchesScrollState>(SCROLL_STATE_KEY) ?: LaunchesScrollState()
    )
    val scrollState = _scrollState.asStateFlow()

    private val _bottomSheetState = MutableStateFlow(BottomSheetUiState())
    val bottomSheetState = _bottomSheetState.asStateFlow()

    private val _linkEvent = MutableSharedFlow<String>(replay = 0)
    val linkEvent = _linkEvent.asSharedFlow()

    private val _errorEvent = MutableSharedFlow<UIErrors>(replay = 0)
    val errorEvent = _errorEvent.asSharedFlow()

    private var hasCheckedInitialData = false

    private val _isLoading = MutableStateFlow(false)

    init {
        // Auto-save scroll state to SavedStateHandle whenever it changes
        viewModelScope.launch {
            _scrollState.collect { state ->
                savedStateHandle[SCROLL_STATE_KEY] = state
                Log.d(TAG, "💾 Auto-saved scroll state: page=${state.page}, isLastPage=${state.isLastPage}")
            }
        }

        // Restore state on init
        restoreStateOnProcessDeath()
    }

    fun init() {
        if (hasCheckedInitialData) {
            Log.d(TAG, "⏭️ init() already processed, skipping")
            return
        }
        hasCheckedInitialData = true

        val savedScrollState = savedStateHandle.get<LaunchesScrollState>(SCROLL_STATE_KEY)

        if (savedScrollState != null && savedScrollState.page > 0) {
            Log.d(TAG, "🔄 init() called with saved state - page=${savedScrollState.page}, restoring from cache")
            // We have saved state from config change with actual progress, don't trigger fresh pagination
            return
        }

        Log.d(TAG, "🏁 init() called - fresh start or page 0, savedState=${savedScrollState}")
        restoreFilterAndOrderState()

        // Observe feedState to detect if we have cached data
        viewModelScope.launch {
            feedState.collect { state ->
                when (state) {
                    is LaunchesUiState.Success -> {
                        if (state.launches.isNotEmpty() && getPageState() == 0) {
                            // We have cached data and page is still 0, calculate correct page
                            val calculatedPage = (state.launches.size / PAGINATION_LIMIT)
                            Log.d(TAG, "📦 Found ${state.launches.size} cached items - set page to $calculatedPage")
                            _scrollState.update { it.copy(page = calculatedPage) }
                            // Cancel this collector after handling
                            return@collect
                        }
                    }
                    is LaunchesUiState.Loading -> {
                        // Still loading, trigger network call after a small delay to let cache load
                        kotlinx.coroutines.delay(100)
                        if (feedState.value is LaunchesUiState.Loading) {
                            // Still loading after delay, no cache exists
                            Log.d(TAG, "🚀 No cached data detected - triggering initial network pagination")
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
                    is LaunchResult.Success -> {
                        _isLoading.value = false
                        result.data
                    }
                    is LaunchResult.Error -> {
                        _isLoading.value = false
                        emptyList()
                    }
                }
            }.stateIn(
                scope = viewModelScope,
                initialValue = emptyList()
            )

    // Combine data and loading state into final UI state
    val feedState: StateFlow<LaunchesUiState> = kotlinx.coroutines.flow.combine(
        launchesData,
        _isLoading,
        _notificationState
    ) { launches, isLoading, notification ->
        when {
            isLoading || (launches.isEmpty() && notification == null) -> LaunchesUiState.Loading
            launches.isEmpty() -> LaunchesUiState.Error(errorNotificationState = notification)
            else -> LaunchesUiState.Success(
                launches = launches.map { launch -> launch.toUiModel(appStringResource) }
            )
        }
    }.stateIn(
        scope = viewModelScope,
        initialValue = LaunchesUiState.Loading
    )

    private fun restoreStateOnProcessDeath() {
        savedStateHandle.get<LaunchesScrollState>(SCROLL_STATE_KEY)?.let { scrollState ->
            Log.d(TAG, "📦 Restoring scroll state from SavedStateHandle: page=${scrollState.page}, isLastPage=${scrollState.isLastPage}")
            this._scrollState.value = scrollState
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
        _notificationState.value = event.notificationState
    }

    private suspend fun paginateLaunchesNetworkUseCase() {
        Log.d(TAG, "⚙️ paginateLaunchesNetworkUseCase called - isLastPage=${getIsLastPageState()}, paginationState=${_paginationState.value}, isLoading=${_isLoading.value}")

        // Prevent concurrent pagination calls
        if (getIsLastPageState()) {
            Log.d(TAG, "🚫 Pagination blocked: isLastPage = true")
            return
        }
        if (_paginationState.value == PaginationState.Loading) {
            Log.d(TAG, "🚫 Pagination blocked: already loading")
            return
        }
        if (_isLoading.value) {
            Log.d(TAG, "🚫 Pagination blocked: initial loading")
            return
        }

        val currentPage = getPageState()
        Log.d(TAG, "📄 Starting pagination for page: $currentPage")

        // Set loading state before the call
        if (currentPage == 0) {
            _isLoading.value = true
            Log.d(TAG, "🔄 Set _isLoading = true for initial load")
        } else {
            _paginationState.value = PaginationState.Loading
            Log.d(TAG, "🔄 Set paginationState = Loading for page $currentPage")
        }

        launchesComponent.getLaunchesApiAndCacheUseCase(currentPage)
            .collect { result ->
                when (result) {
                    is LaunchResult.Success -> {
                        // Check if this page had fewer items than the limit
                        // This tells us if there are more pages available
                        val fetchedItemsCount = result.data.size
                        Log.d(TAG, "✅ Fetched $fetchedItemsCount items for page $currentPage")

                        val isLast = fetchedItemsCount < PAGINATION_LIMIT
                        setIsLastPageState(isLast)
                        Log.d(TAG, "📊 isLastPage set to: $isLast (fetched: $fetchedItemsCount, limit: $PAGINATION_LIMIT)")

                        // Data will automatically flow through launchesData and feedState
                        // Just update pagination state and increment page
                        _paginationState.value = PaginationState.None
                        _isLoading.value = false
                        Log.d(TAG, "🔄 Reset loading states: _isLoading = false, paginationState = None")

                        incrementPage()
                        Log.d(TAG, "➡️ Page incremented to: ${getPageState()}")
                    }

                    is LaunchResult.Error -> {
                        Log.e(TAG, "❌ Pagination error for page $currentPage: ${result.error}")
                        _paginationState.value = PaginationState.Error
                        _isLoading.value = false
                    }
                }
            }
    }

    private suspend fun openLink(link: String) {
        _linkEvent.emit(link)
    }


    private fun dismissNotification() {
        _notificationState.value = null
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

    private fun getScrollPositionState() = _scrollState.value.scrollPosition
    private fun getPageState() = _scrollState.value.page
    private fun getIsLastPageState() = _scrollState.value.isLastPage
    private fun getSearchYearState() = _filterState.value.launchYear
    private fun getOrderState() = _filterState.value.order
    private fun getLaunchStatusState() = _filterState.value.launchStatus

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
        _filterState.update { currentState ->
            currentState.copy(
                order = order,
                launchStatus = launchStatus,
                launchYear = year
            )
        }
    }

    private fun displayFilterDialog(isDisplayed: Boolean) {
        _filterState.update { currentState -> currentState.copy(isVisible = isDisplayed) }
    }

    private fun resetPageState() {
        _scrollState.update { currentState -> currentState.copy(page = 0, isLastPage = false, scrollPosition = 0) }
    }

    private fun setIsLastPageState(isLastPage: Boolean) {
        _scrollState.update { currentState -> currentState.copy(isLastPage = isLastPage) }
    }

    private fun setScrollPositionState(position: Int) {
        _scrollState.update { currentState -> currentState.copy(scrollPosition = position) }
    }

    private fun incrementPage() {
        _scrollState.update { currentState -> currentState.copy(page = currentState.page + 1) }
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
