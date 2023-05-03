package com.seancoyle.launch.implementation.presentation

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.seancoyle.constants.LaunchDaoConstants.LAUNCH_ORDER_ASC
import com.seancoyle.constants.LaunchDaoConstants.LAUNCH_ORDER_DESC
import com.seancoyle.constants.LaunchNetworkConstants.LAUNCH_ALL
import com.seancoyle.constants.LaunchNetworkConstants.LAUNCH_FAILED
import com.seancoyle.constants.LaunchNetworkConstants.LAUNCH_SUCCESS
import com.seancoyle.constants.LaunchNetworkConstants.LAUNCH_UNKNOWN
import com.seancoyle.core.presentation.BaseFragment
import com.seancoyle.core.state.*
import com.seancoyle.core.testing.AndroidTestUtils
import com.seancoyle.core.util.printLogDebug
import com.seancoyle.launch.api.model.CompanySummary
import com.seancoyle.launch.api.model.LaunchModel
import com.seancoyle.launch.api.model.LaunchType
import com.seancoyle.launch.api.model.LaunchViewState
import com.seancoyle.launch.api.model.Links
import com.seancoyle.launch.api.model.SectionTitle
import com.seancoyle.launch.implementation.R
import com.seancoyle.launch.implementation.domain.FilterLaunchItemsInCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.GetAllLaunchItemsFromCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.GetCompanyInfoFromCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.GetCompanyInfoFromNetworkAndInsertToCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.GetLaunchListFromNetworkAndInsertToCacheUseCaseImpl
import com.seancoyle.launch.implementation.presentation.composables.CompanySummaryCard
import com.seancoyle.launch.implementation.presentation.composables.HomeAppBar
import com.seancoyle.launch.implementation.presentation.composables.LaunchCard
import com.seancoyle.launch.implementation.presentation.composables.LaunchHeading
import com.seancoyle.launch.implementation.presentation.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

const val LINKS_KEY = "links"
const val LAUNCH_STATE_BUNDLE_KEY = "com.seancoyle.launch.presentation.launch.state"

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class LaunchFragment : BaseFragment(R.layout.fragment_launch) {

    @Inject
    lateinit var androidTestUtils: AndroidTestUtils
    private val launchViewModel by viewModels<LaunchViewModel>()
    private var links: Links? = null

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {

                val scaffoldState = rememberScaffoldState()
            //    val loading = launchViewModel.shouldDisplayProgressBar.value ?: false

                AppTheme(
                    darkTheme = false,
                    displayProgressBar = false,
                    scaffoldState = scaffoldState
                ) {
                    Scaffold(
                        topBar = {
                            HomeAppBar(
                                title = { stringResource(id = R.string.app_name) },
                                onClick = {
                                    launchViewModel.setIsDialogFilterDisplayed(true)
                                    displayFilterDialog()
                                }
                            )
                        },
                        scaffoldState = scaffoldState
                    ) { padding ->
                        LaunchScreen(
                            Modifier.padding(padding),
                            viewModel = launchViewModel
                        )
                    }
                }
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSwipeRefresh()
        subscribeObservers()
        restoreInstanceState(savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        launchViewModel.clearAllStateMessages()
    }

    private fun restoreInstanceState(savedInstanceState: Bundle?) {
        savedInstanceState?.let { inState ->
            (inState[LAUNCH_STATE_BUNDLE_KEY] as LaunchViewState?)?.let { viewState ->
                launchViewModel.setViewState(viewState)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val viewState = launchViewModel.viewState.value

        //clear the list. Don't save a large list to bundle.
        viewState.launchList = ArrayList()
        viewState.mergedList = ArrayList()

        outState.putParcelable(
            LAUNCH_STATE_BUNDLE_KEY,
            viewState
        )
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        if (launchViewModel.getLaunchList() != null) {
            getTotalNumEntriesInLaunchCacheEvent()
            launchViewModel.refreshSearchQuery()
        }
        if (launchViewModel.getIsDialogFilterDisplayed() == true) {
            displayFilterDialog()
        }
    }

    private fun subscribeObservers() {

        launchViewModel.shouldDisplayProgressBar.observe(viewLifecycleOwner) {
            uiController.displayProgressBar(it)
        }

        launchViewModel.stateMessage.observe(viewLifecycleOwner) { stateMessage ->
            stateMessage?.response?.let { response ->
                when (response.message) {

                    GetCompanyInfoFromNetworkAndInsertToCacheUseCaseImpl.COMPANY_INFO_INSERT_SUCCESS -> {
                        launchViewModel.clearStateMessage()
                        launchViewModel.setStateEvent(LaunchStateEvent.GetCompanyInfoFromCacheEvent)
                    }

                    GetCompanyInfoFromCacheUseCaseImpl.GET_COMPANY_INFO_SUCCESS -> {
                        launchViewModel.clearStateMessage()
                        getLaunchListFromNetworkAndInsertToCacheEvent()
                    }

                    GetLaunchListFromNetworkAndInsertToCacheUseCaseImpl.LAUNCH_INSERT_SUCCESS -> {
                        launchViewModel.clearStateMessage()
                        filterLaunchItemsInCacheEvent()
                        getTotalNumEntriesInLaunchCacheEvent()
                    }

                    FilterLaunchItemsInCacheUseCaseImpl.SEARCH_LAUNCH_SUCCESS -> {
                        launchViewModel.clearStateMessage()
                    }

                    GetAllLaunchItemsFromCacheUseCaseImpl.GET_ALL_LAUNCH_ITEMS_SUCCESS -> {
                        launchViewModel.clearStateMessage()
                    }

                    else -> {
                        uiController.onResponseReceived(
                            response = stateMessage.response,
                            stateMessageCallback = object : StateMessageCallback {
                                override fun removeMessageFromStack() {
                                    launchViewModel.clearStateMessage()
                                }
                            }
                        )

                        when (response.message) {
                            // Check cache for data if net connection fails
                            GetLaunchListFromNetworkAndInsertToCacheUseCaseImpl.LAUNCH_INSERT_FAILED -> {
                                getTotalNumEntriesInLaunchCacheEvent()
                                filterLaunchItemsInCacheEvent()
                            }

                            GetLaunchListFromNetworkAndInsertToCacheUseCaseImpl.LAUNCH_ERROR -> {
                                getTotalNumEntriesInLaunchCacheEvent()
                                filterLaunchItemsInCacheEvent()
                            }

                            GetCompanyInfoFromNetworkAndInsertToCacheUseCaseImpl.COMPANY_INFO_INSERT_FAILED -> {
                                getCompanyInfoFromCacheEvent()
                            }

                            GetCompanyInfoFromNetworkAndInsertToCacheUseCaseImpl.COMPANY_INFO_ERROR -> {
                                getCompanyInfoFromCacheEvent()
                            }
                        }
                    }
                }
            }
        }

        // Update UI when list size changes
        /*launchViewModel.viewState.observe(viewLifecycleOwner) { viewState ->

            if (viewState != null) {
                viewState.launchList?.let { _ ->
                    if (launchViewModel.isPaginationExhausted() && !launchViewModel.isQueryExhausted()) {
                        launchViewModel.setQueryExhausted(true)
                    }
                }
            }
        }*/

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            launchViewModel.viewState.collect { viewState ->
                viewState.launchList?.let { _ ->
                    if (launchViewModel.isPaginationExhausted() && !launchViewModel.isQueryExhausted()) {
                        launchViewModel.setQueryExhausted(true)
                    }
                }
            }
        }

        // Get result from bottom action sheet fragment which will be a link type string
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

    private fun filterLaunchItemsInCacheEvent() {
        launchViewModel.setStateEvent(
            LaunchStateEvent.FilterLaunchItemsInCacheEvent()
        )
    }

    private fun getCompanyInfoFromCacheEvent() {
        launchViewModel.setStateEvent(
            LaunchStateEvent.GetCompanyInfoFromCacheEvent
        )
    }

    private fun getTotalNumEntriesInLaunchCacheEvent() {
        launchViewModel.retrieveNumLaunchItemsInCache()
    }

    private fun onCardClicked(launchLinks: Links) {
        links = launchLinks
        if (isLinksNullOrEmpty()) {
            displayErrorDialogNoLinks()
        } else {
            displayBottomActionSheet(launchLinks)
        }
    }

    private fun getLaunchListFromNetworkAndInsertToCacheEvent() {
        launchViewModel.setStateEvent(
            LaunchStateEvent.GetLaunchItemsFromNetworkAndInsertToCacheEvent(
                launchOptions = launchViewModel.launchOptions
            )
        )
    }

    private fun getCompanyInfoFromNetworkAndInsertToCacheEvent() {
        launchViewModel.setStateEvent(
            LaunchStateEvent.GetCompanyInfoFromNetworkAndInsertToCacheEvent
        )
    }

    private fun setupSwipeRefresh() {
        /*  with(binding) {
              swipeRefresh.setOnRefreshListener {
                  swipeRefresh.isRefreshing = false
                  viewModel.clearQueryParameters()
                  getCompanyInfoFromNetworkAndInsertToCacheEvent()
              }
          }*/
    }

    private fun isLinksNullOrEmpty() =
        links?.articleLink.isNullOrEmpty() &&
                links?.webcastLink.isNullOrEmpty() &&
                links?.wikiLink.isNullOrEmpty()


    private fun displayBottomActionSheet(launchLinks: Links) {
        if (findNavController().currentDestination?.id == R.id.launchFragment) {
            findNavController().navigate(
                R.id.action_launchFragment_to_launchBottomActionSheet,
                bundleOf(LINKS_KEY to launchLinks)
            )
        }
    }

    private fun displayFilterDialog() {

        activity?.let {
            val dialog = MaterialDialog(it)
                .noAutoDismiss()
                .onDismiss { launchViewModel.setIsDialogFilterDisplayed(false) }
                .customView(R.layout.dialog_filter)
                .cornerRadius(res = R.dimen.default_corner_radius)

            val view = dialog.getCustomView()
            val order = launchViewModel.getOrder()
            val filter = launchViewModel.getFilter()
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
                        setLaunchOrder(order)
                    }
                    newFilter?.let { filter ->
                        setLaunchFilter(filter)
                    }
                    setQuery(yearQuery)
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
        printLogDebug("DCM", "start new search")
        launchViewModel.clearList()
        launchViewModel.loadFirstPage()
    }

    private fun displayErrorDialogNoLinks() {
        launchViewModel.setStateEvent(
            stateEvent = LaunchStateEvent.CreateStateMessageEvent(
                StateMessage(
                    response = Response(
                        messageType = MessageType.Info,
                        message = getString(R.string.no_links),
                        uiComponentType = UIComponentType.Dialog
                    )
                )
            )
        )
    }

    private fun displayErrorDialogUnableToLoadLink() {
        launchViewModel.setStateEvent(
            stateEvent = LaunchStateEvent.CreateStateMessageEvent(
                StateMessage(
                    response = Response(
                        messageType = MessageType.Error,
                        message = getString(R.string.error_links),
                        uiComponentType = UIComponentType.Dialog
                    )
                )
            )
        )
    }

    @Composable
    fun LaunchScreen(
        modifier: Modifier = Modifier,
        viewModel: LaunchViewModel
    ) {
        val viewState = viewModel.viewState.collectAsState()
        LaunchContent(viewState.value.mergedList ?: emptyList())
    }

    @Composable
    private fun LaunchContent(launchItems: List<LaunchType>) {
        if (launchItems.isNotEmpty()) {
            Box(

            ) {
                LazyColumn {
                    itemsIndexed(
                        items = launchItems
                    ) { index, launchItem ->
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
        }
    }

}










































