package com.seancoyle.launch.implementation.presentation

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.seancoyle.constants.LaunchDaoConstants.LAUNCH_ORDER_ASC
import com.seancoyle.constants.LaunchDaoConstants.LAUNCH_ORDER_DESC
import com.seancoyle.constants.LaunchDaoConstants.LAUNCH_PAGINATION_PAGE_SIZE
import com.seancoyle.constants.LaunchNetworkConstants.LAUNCH_ALL
import com.seancoyle.constants.LaunchNetworkConstants.LAUNCH_FAILED
import com.seancoyle.constants.LaunchNetworkConstants.LAUNCH_SUCCESS
import com.seancoyle.constants.LaunchNetworkConstants.LAUNCH_UNKNOWN
import com.seancoyle.core.presentation.UIInteractionHandler
import com.seancoyle.core.presentation.UIInteractionHandlerDelegate
import com.seancoyle.core.state.*
import com.seancoyle.core.util.GenericErrors.ERROR_UNKNOWN
import com.seancoyle.core.util.GenericErrors.EVENT_CACHE_INSERT_FAILED
import com.seancoyle.core.util.GenericErrors.EVENT_CACHE_INSERT_SUCCESS
import com.seancoyle.core.util.printLogDebug
import com.seancoyle.launch.api.domain.model.CompanySummary
import com.seancoyle.launch.api.domain.model.LaunchModel
import com.seancoyle.launch.api.domain.model.LaunchType
import com.seancoyle.launch.api.domain.model.Links
import com.seancoyle.launch.api.domain.model.SectionTitle
import com.seancoyle.launch.implementation.R
import com.seancoyle.launch.implementation.presentation.composables.CompanySummaryCard
import com.seancoyle.launch.implementation.presentation.composables.HomeAppBar
import com.seancoyle.launch.implementation.presentation.composables.LaunchCard
import com.seancoyle.launch.implementation.presentation.composables.LaunchHeading
import com.seancoyle.launch.implementation.presentation.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

const val LINKS_KEY = "links"

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class LaunchFragment: Fragment() {

    private val uiInteractionHandler: UIInteractionHandler by UIInteractionHandlerDelegate()
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
                        launchViewModel.setEvent(LaunchEvent.GetLaunchesFromNetworkAndInsertToCacheEvent)
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

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun LaunchContent(
        launchItems: List<LaunchType>,
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
                LazyColumn {
                    itemsIndexed(
                        items = launchItems
                    ) { index, launchItem ->
                        onChangeScrollPosition(index)
                        if ((index + 1) >= (page * LAUNCH_PAGINATION_PAGE_SIZE) && !loading) {
                            loadNextPage()
                        }
                        if (!isRefreshing) {
                            when (launchItem.type) {
                                LaunchType.TYPE_TITLE -> {
                                    LaunchHeading(launchItem as SectionTitle)
                                }

                                LaunchType.TYPE_COMPANY -> {
                                    CompanySummaryCard(launchItem as CompanySummary)
                                }

                                LaunchType.TYPE_LAUNCH -> {
                                    LaunchCard(
                                        launchItem = launchItem as LaunchModel,
                                        onClick = { onCardClicked(launchItem.links) }
                                    )
                                }

                                else -> throw ClassCastException("Unknown viewType ${launchItem.type}")
                            }
                        }
                    }
                }
                PullRefreshIndicator(launchViewModel.getRefreshState(), pullRefreshState, Modifier.align(Alignment.TopCenter))
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
            repeatOnLifecycle(Lifecycle.State.STARTED){
                launchViewModel.loading.collect{ isLoading ->
                    uiInteractionHandler.displayProgressBar(isLoading)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launchViewModel.stateMessage.collect { stateMessage ->
                    stateMessage?.response?.let { response ->

                        when (response.message) {
                            LaunchEvent.GetLaunchesFromNetworkAndInsertToCacheEvent.eventName() + EVENT_CACHE_INSERT_SUCCESS -> {
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
                                    LaunchEvent.GetLaunchesFromNetworkAndInsertToCacheEvent.eventName() + EVENT_CACHE_INSERT_FAILED -> {
                                        filterLaunchItemsInCacheEvent()
                                    }

                                    LaunchEvent.GetLaunchesFromNetworkAndInsertToCacheEvent.eventName() + ERROR_UNKNOWN -> {
                                        filterLaunchItemsInCacheEvent()
                                    }

                                    LaunchEvent.GetCompanyInfoFromNetworkAndInsertToCacheEvent.eventName() + EVENT_CACHE_INSERT_FAILED -> {
                                        getCompanyInfoFromCacheEvent()
                                    }

                                    LaunchEvent.GetCompanyInfoFromNetworkAndInsertToCacheEvent.eventName() + ERROR_UNKNOWN -> {
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
                    LAUNCH_ORDER_ASC -> isChecked = false
                    LAUNCH_ORDER_DESC -> isChecked = true
                }
            }

            orderSwitch.setOnCheckedChangeListener { _, isChecked ->
                newOrder = if (isChecked) {
                    LAUNCH_ORDER_DESC
                } else {
                    LAUNCH_ORDER_ASC
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
            LaunchEvent.FilterLaunchItemsInCacheEvent
        )
    }

    private fun getCompanyInfoFromCacheEvent() {
        launchViewModel.setEvent(
            LaunchEvent.GetCompanyInfoFromCacheEvent
        )
    }

    private fun displayErrorDialogNoLinks() {
        launchViewModel.setEvent(
            event = LaunchEvent.CreateMessageEvent(
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
            event = LaunchEvent.CreateMessageEvent(
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