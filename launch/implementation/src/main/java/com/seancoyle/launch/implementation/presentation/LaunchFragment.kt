package com.seancoyle.launch.implementation.presentation

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.seancoyle.core.Constants.ORDER_ASC
import com.seancoyle.core.Constants.ORDER_DESC
import com.seancoyle.core.Constants.PAGINATION_PAGE_SIZE
import com.seancoyle.core.domain.MessageDisplayType
import com.seancoyle.core.domain.MessageType
import com.seancoyle.core.domain.Response
import com.seancoyle.core.domain.StateMessage
import com.seancoyle.core.domain.StateMessageCallback
import com.seancoyle.core.domain.UIInteractionHandler
import com.seancoyle.core.domain.UsecaseResponses.ERROR_UNKNOWN
import com.seancoyle.core.domain.UsecaseResponses.EVENT_CACHE_INSERT_FAILED
import com.seancoyle.core.domain.UsecaseResponses.EVENT_CACHE_INSERT_SUCCESS
import com.seancoyle.core.util.printLogDebug
import com.seancoyle.launch.api.LaunchNetworkConstants.LAUNCH_ALL
import com.seancoyle.launch.api.LaunchNetworkConstants.LAUNCH_FAILED
import com.seancoyle.launch.api.LaunchNetworkConstants.LAUNCH_SUCCESS
import com.seancoyle.launch.api.LaunchNetworkConstants.LAUNCH_UNKNOWN
import com.seancoyle.launch.api.domain.model.CompanySummary
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.api.domain.model.Links
import com.seancoyle.launch.api.domain.model.SectionTitle
import com.seancoyle.launch.api.domain.model.ViewCarousel
import com.seancoyle.launch.api.domain.model.ViewGrid
import com.seancoyle.launch.api.domain.model.ViewType
import com.seancoyle.launch.implementation.R
import com.seancoyle.launch.implementation.presentation.composables.CompanySummaryCard
import com.seancoyle.launch.implementation.presentation.composables.HomeAppBar
import com.seancoyle.launch.implementation.presentation.composables.LaunchCard
import com.seancoyle.launch.implementation.presentation.composables.LaunchCarouselCard
import com.seancoyle.launch.implementation.presentation.composables.LaunchGridCard
import com.seancoyle.launch.implementation.presentation.composables.LaunchHeading
import com.seancoyle.launch.implementation.presentation.composables.LoadingLaunchCardList
import com.seancoyle.launch.implementation.presentation.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import javax.inject.Inject

const val LINKS_KEY = "links"
private const val GRID_COLUMN_SIZE = 2

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class LaunchFragment : Fragment() {

    @Inject
    lateinit var uiInteractionHandler: UIInteractionHandler
    private val launchViewModel by viewModels<LaunchViewModel>()

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {

                val scaffoldState = rememberScaffoldState()
                val refreshing = rememberPullRefreshState(
                    refreshing = launchViewModel.getRefreshState(),
                    onRefresh = {
                        launchViewModel.clearQueryParameters()
                        launchViewModel.setEvent(LaunchEvents.GetLaunchesFromNetworkAndInsertToCacheEvent)
                    }
                )

                AppTheme(
                    darkTheme = false,
                    displayProgressBar = false,
                    scaffoldState = scaffoldState
                ) {
                    Scaffold(
                        topBar = {
                            HomeAppBar(
                                onClick = {
                                    launchViewModel.setIsDialogFilterDisplayedState(true)
                                    displayFilterDialog()
                                }
                            )
                        },
                        scaffoldState = scaffoldState
                    ) { padding ->
                        LaunchScreen(
                            Modifier.padding(padding),
                            viewModel = launchViewModel,
                            refreshState = refreshing
                        )
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun LaunchScreen(
        modifier: Modifier = Modifier,
        viewModel: LaunchViewModel,
        refreshState: PullRefreshState
    ) {
        val viewState = viewModel.uiState.collectAsState()
        val loading = viewModel.loading.collectAsState()
        printLogDebug("RECOMPOSING", "RECOMPOSING $viewState")

        if (loading.value && viewState.value.mergedLaunches.isNullOrEmpty()) {
            LoadingLaunchCardList(itemCount = 10)
        } else {
            LaunchContent(
                launchItems = viewState.value.mergedLaunches ?: emptyList(),
                modifier = modifier,
                loading = loading.value,
                onChangeScrollPosition = viewModel::setScrollPositionState,
                loadNextPage = viewModel::nextPage,
                page = viewModel.getPageState(),
                pullRefreshState = refreshState,
                isRefreshing = viewModel.getRefreshState()
            )
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun LaunchContent(
        launchItems: List<ViewType>,
        loading: Boolean,
        onChangeScrollPosition: (Int) -> Unit,
        page: Int,
        loadNextPage: () -> Unit,
        pullRefreshState: PullRefreshState,
        modifier: Modifier = Modifier,
        isRefreshing: Boolean
    ) {
        if (launchItems.isNotEmpty()) {
            Box(
                modifier = modifier
                    .background(MaterialTheme.colors.background)
                    .pullRefresh(pullRefreshState)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(GRID_COLUMN_SIZE),
                    modifier = modifier.semantics { testTag = "Launch Grid" }
                ) {
                    itemsIndexed(
                        items = launchItems,
                        span = { _, item ->
                            GridItemSpan(if (item.type == ViewType.TYPE_GRID) 1 else 2)
                        }
                    ) { index, launchItem ->
                        printLogDebug("LazyVerticalGrid", ": index$index")
                        onChangeScrollPosition(index)
                        if ((index + 1) >= (page * PAGINATION_PAGE_SIZE) && !loading) {
                            loadNextPage()
                        }
                        if (!isRefreshing) {
                            when (launchItem.type) {
                                ViewType.TYPE_SECTION_TITLE -> {
                                    LaunchHeading(launchItem as SectionTitle)
                                }

                                ViewType.TYPE_HEADER -> {
                                    CompanySummaryCard(launchItem as CompanySummary)
                                }

                                ViewType.TYPE_LIST -> {
                                    LaunchCard(
                                        launchItem = launchItem as Launch,
                                        onClick = { onCardClicked(launchItem.links) }
                                    )
                                }

                                ViewType.TYPE_GRID -> {
                                    LaunchGridCard(
                                        launchItem = launchItem as ViewGrid,
                                        onClick = { onCardClicked(launchItem.links) })
                                }

                                ViewType.TYPE_CAROUSEL -> {
                                    val carouselItems = (launchItem as ViewCarousel).items
                                    LazyRow {
                                        itemsIndexed(carouselItems) { index, carouselItem ->
                                            LaunchCarouselCard(
                                                launchItem = carouselItem,
                                                onClick = { onCardClicked(carouselItem.links) })
                                            printLogDebug("Recyclerview - ROW ", ": index${index}")
                                        }
                                    }
                                }

                                else -> throw ClassCastException("Unknown viewType ${launchItem.type}")
                            }
                        }
                    }
                }
                PullRefreshIndicator(
                    launchViewModel.getRefreshState(),
                    pullRefreshState,
                    Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
    }

    override fun onPause() {
        super.onPause()
        launchViewModel.clearAllStateMessages()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        launchViewModel.saveState()
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        if (!launchViewModel.getLaunchesState().isNullOrEmpty()) {
            launchViewModel.refreshSearchQueryEvent()
        }
        if (launchViewModel.getIsDialogFilterDisplayedState()) {
            displayFilterDialog()
        }
    }

    private fun subscribeObservers() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launchViewModel.loading.collect { isLoading ->
                    uiInteractionHandler.displayProgressBar(isLoading)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launchViewModel.stateMessage.collect { stateMessage ->
                    stateMessage?.response?.let { response ->

                        when (response.message) {
                            LaunchEvents.GetLaunchesFromNetworkAndInsertToCacheEvent.eventName() + EVENT_CACHE_INSERT_SUCCESS -> {
                                launchViewModel.clearStateMessage()
                                filterLaunchItemsInCacheEvent()
                            }

                            else -> {
                                uiInteractionHandler.onResponseReceived(
                                    response = stateMessage.response,
                                    stateMessageCallback = object : StateMessageCallback {
                                        override fun removeMessageFromStack() {
                                            launchViewModel.clearStateMessage()
                                        }
                                    }
                                )

                                when (response.message) {
                                    // Check cache for data if net connection fails
                                    LaunchEvents.GetLaunchesFromNetworkAndInsertToCacheEvent.eventName() + EVENT_CACHE_INSERT_FAILED -> {
                                        filterLaunchItemsInCacheEvent()
                                    }

                                    LaunchEvents.GetLaunchesFromNetworkAndInsertToCacheEvent.eventName() + ERROR_UNKNOWN -> {
                                        filterLaunchItemsInCacheEvent()
                                    }

                                    LaunchEvents.GetCompanyInfoFromNetworkAndInsertToCacheEvent.eventName() + EVENT_CACHE_INSERT_FAILED -> {
                                        getCompanyInfoFromCacheEvent()
                                    }

                                    LaunchEvents.GetCompanyInfoFromNetworkAndInsertToCacheEvent.eventName() + ERROR_UNKNOWN -> {
                                        getCompanyInfoFromCacheEvent()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Get result from bottom action sheet fragment
        setFragmentResultListener(LINKS_KEY) { key, bundle ->
            if (key == LINKS_KEY) {
                launchIntent(bundle.getString(LINKS_KEY))
            }
        }
    }

    // Load url link in external browser
    private fun launchIntent(url: String?) {
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(url.toString())
                )
            )
        } catch (e: ActivityNotFoundException) {
            displayErrorDialogUnableToLoadLink()
        }
    }


    private fun onCardClicked(links: Links) {
        if (isLinksNullOrEmpty(links)) {
            displayErrorDialogNoLinks()
        } else {
            displayBottomActionSheet(links)
        }
    }

    private fun isLinksNullOrEmpty(links: Links) =
        links.articleLink.isNullOrEmpty() &&
                links.webcastLink.isNullOrEmpty() &&
                links.wikiLink.isNullOrEmpty()


    private fun displayBottomActionSheet(links: Links) {
        if (findNavController().currentDestination?.id == R.id.launchFragment) {
            findNavController().navigate(
                R.id.action_launchFragment_to_launchBottomActionSheet,
                bundleOf(LINKS_KEY to links)
            )
        }
    }

    private fun displayFilterDialog() {

        activity?.let {
            val dialog = MaterialDialog(it)
                .noAutoDismiss()
                .onDismiss { launchViewModel.setIsDialogFilterDisplayedState(false) }
                .customView(R.layout.dialog_filter)
                .cornerRadius(res = R.dimen.default_corner_radius)

            val view = dialog.getCustomView()
            val order = launchViewModel.getOrderState()
            val filter = launchViewModel.getFilterState()
            var newOrder: String? = null

            view.findViewById<RadioGroup>(R.id.filter_group).apply {
                when (filter) {
                    null -> check(R.id.filter_all)
                    LAUNCH_SUCCESS -> check(R.id.filter_success)
                    LAUNCH_FAILED -> check(R.id.filter_failure)
                    LAUNCH_UNKNOWN -> check(R.id.filter_unknown)
                }
            }

            // set switch to on/off based on state
            val orderSwitch = view.findViewById<SwitchMaterial>(R.id.order_switch).apply {
                when (order) {
                    ORDER_ASC -> isChecked = false
                    ORDER_DESC -> isChecked = true
                }
            }

            orderSwitch.setOnCheckedChangeListener { _, isChecked ->
                newOrder = if (isChecked) {
                    ORDER_DESC
                } else {
                    ORDER_ASC
                }
            }

            view.findViewById<TextView>(R.id.apply_btn).setOnClickListener {

                val newFilter =
                    when (view.findViewById<RadioGroup>(R.id.filter_group).checkedRadioButtonId) {
                        R.id.filter_success -> LAUNCH_SUCCESS
                        R.id.filter_failure -> LAUNCH_FAILED
                        R.id.filter_unknown -> LAUNCH_UNKNOWN
                        R.id.filter_all -> LAUNCH_ALL
                        else -> null
                    }

                val yearQuery = view.findViewById<EditText>(R.id.year_query).text.toString()

                // Save data to view model
                launchViewModel.apply {
                    newOrder?.let { order ->
                        setLaunchOrderState(order)
                    }
                    newFilter?.let { filter ->
                        setLaunchFilterState(filter)
                    }
                    setQueryState(yearQuery)
                }

                startNewSearch()
                dialog.dismiss()
            }

            view.findViewById<TextView>(R.id.cancel_btn).setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    private fun startNewSearch() {
        printLogDebug("EventExecutor", "start new search")
        launchViewModel.clearListState()
        launchViewModel.newSearchEvent()
    }

    private fun filterLaunchItemsInCacheEvent() {
        launchViewModel.setEvent(
            LaunchEvents.FilterLaunchItemsInCacheEvent
        )
    }

    private fun getCompanyInfoFromCacheEvent() {
        launchViewModel.setEvent(
            LaunchEvents.GetCompanyInfoFromCacheEvent
        )
    }

    private fun displayErrorDialogNoLinks() {
        launchViewModel.setEvent(
            event = LaunchEvents.CreateMessageEvent(
                StateMessage(
                    response = Response(
                        messageType = MessageType.Info,
                        message = getString(R.string.no_links),
                        messageDisplayType = MessageDisplayType.Dialog
                    )
                )
            )
        )
    }

    private fun displayErrorDialogUnableToLoadLink() {
        launchViewModel.setEvent(
            event = LaunchEvents.CreateMessageEvent(
                StateMessage(
                    response = Response(
                        messageType = MessageType.Error,
                        message = getString(R.string.error_links),
                        messageDisplayType = MessageDisplayType.Dialog
                    )
                )
            )
        )
    }

}