package com.seancoyle.spacex.framework.presentation.launch

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.seancoyle.spacex.R
import com.seancoyle.spacex.business.domain.model.company.CompanyInfoDomainEntity
import com.seancoyle.spacex.business.domain.model.launch.LaunchType
import com.seancoyle.spacex.business.domain.model.launch.Links
import com.seancoyle.spacex.business.domain.state.*
import com.seancoyle.spacex.business.interactors.company.GetCompanyInfoFromCache
import com.seancoyle.spacex.business.interactors.company.GetCompanyInfoFromNetworkAndInsertToCache
import com.seancoyle.spacex.business.interactors.launch.GetAllLaunchItemsFromCache
import com.seancoyle.spacex.business.interactors.launch.GetLaunchListFromNetworkAndInsertToCache
import com.seancoyle.spacex.business.interactors.launch.SearchLaunchItemsInCache
import com.seancoyle.spacex.databinding.FragmentLaunchBinding
import com.seancoyle.spacex.framework.datasource.cache.dao.launch.LAUNCH_ORDER_ASC
import com.seancoyle.spacex.framework.datasource.cache.dao.launch.LAUNCH_ORDER_DESC
import com.seancoyle.spacex.framework.datasource.network.mappers.launch.*
import com.seancoyle.spacex.framework.presentation.common.BaseFragment
import com.seancoyle.spacex.framework.presentation.common.viewBinding
import com.seancoyle.spacex.framework.presentation.launch.adapter.LaunchAdapter
import com.seancoyle.spacex.framework.presentation.launch.state.*
import com.seancoyle.spacex.util.AndroidTestUtils
import com.seancoyle.spacex.util.printLogDebug
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.collections.ArrayList

const val LINKS_KEY = "links"
const val LAUNCH_STATE_BUNDLE_KEY =
    "com.seancoyle.launch.framework.presentation.launch.state"

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class LaunchFragment : BaseFragment(R.layout.fragment_launch),
    LaunchAdapter.Interaction {

    @Inject
    lateinit var androidTestUtils: AndroidTestUtils

    private val viewModel: LaunchViewModel by viewModels()
    private val binding by viewBinding(FragmentLaunchBinding::bind)
    private var listAdapter: LaunchAdapter? = null
    private var links: Links? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        setupRecyclerView()
        setupSwipeRefresh()
        subscribeObservers()
        restoreInstanceState(savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        saveLayoutManagerState()
    }

    private fun restoreInstanceState(savedInstanceState: Bundle?) {
        savedInstanceState?.let { inState ->
            (inState[LAUNCH_STATE_BUNDLE_KEY] as LaunchViewState?)?.let { viewState ->
                viewModel.setViewState(viewState)
            }
        }
    }

    override fun restoreListPosition() {
        viewModel.getLayoutManagerState()?.let { lmState ->
            view?.findViewById<RecyclerView>(R.id.rv_launch)?.layoutManager?.onRestoreInstanceState(
                lmState
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val viewState = viewModel.viewState.value

        //clear the list. Don't save a large list to bundle.
        viewState?.launchList = ArrayList()

        outState.putParcelable(
            LAUNCH_STATE_BUNDLE_KEY,
            viewState
        )
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.getLaunchList() != null) {
            getTotalNumEntriesInLaunchCacheEvent()
            viewModel.refreshSearchQuery()
        }
    }

    private fun saveLayoutManagerState() {
        binding.rvLaunch.layoutManager?.onSaveInstanceState()?.let { lmState ->
            viewModel.setLayoutManagerState(lmState)
        }
    }

    private fun setupRecyclerView() {
        with(binding) {
            rvLaunch.apply {

                listAdapter = LaunchAdapter(
                    interaction = this@LaunchFragment,
                )
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                        val lastPosition = layoutManager.findLastVisibleItemPosition()
                        if (lastPosition == listAdapter?.itemCount?.minus(1)) {
                            viewModel.nextPage()
                        }
                    }
                })

                adapter = listAdapter
                listAdapter?.stateRestorationPolicy =
                    RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }
        }
    }

    private fun subscribeObservers() {

        viewModel.shouldDisplayProgressBar.observe(viewLifecycleOwner) {
            uiController.displayProgressBar(it)
        }

        viewModel.stateMessage.observe(viewLifecycleOwner) { stateMessage ->
            stateMessage?.response?.let { response ->
                when (response.message) {

                    GetCompanyInfoFromNetworkAndInsertToCache.COMPANY_INFO_INSERT_SUCCESS -> {
                        viewModel.clearStateMessage()
                        viewModel.setStateEvent(LaunchStateEvent.GetCompanyInfoFromCacheEvent)
                    }

                    GetCompanyInfoFromCache.GET_COMPANY_INFO_SUCCESS -> {
                        viewModel.clearStateMessage()
                        getLaunchListFromNetworkAndInsertToCacheEvent()
                    }

                    GetLaunchListFromNetworkAndInsertToCache.LAUNCH_INSERT_SUCCESS -> {
                        viewModel.clearStateMessage()
                        getTotalNumEntriesInLaunchCacheEvent()
                        searchLaunchDataFromCacheEvent()
                    }

                    SearchLaunchItemsInCache.SEARCH_LAUNCH_SUCCESS -> {
                        viewModel.clearStateMessage()
                        submitList()
                    }

                    GetAllLaunchItemsFromCache.GET_ALL_LAUNCH_ITEMS_SUCCESS -> {
                        viewModel.clearStateMessage()
                        submitList()
                    }

                    else -> {
                        uiController.onResponseReceived(
                            response = stateMessage.response,
                            stateMessageCallback = object : StateMessageCallback {
                                override fun removeMessageFromStack() {
                                    viewModel.clearStateMessage()
                                }
                            }
                        )

                        when (response.message) {
                            // Check cache for data if net connection fails
                            GetLaunchListFromNetworkAndInsertToCache.LAUNCH_INSERT_FAILED -> {
                                searchLaunchDataFromCacheEvent()
                            }

                            GetLaunchListFromNetworkAndInsertToCache.LAUNCH_ERROR -> {
                                searchLaunchDataFromCacheEvent()
                            }

                            GetCompanyInfoFromNetworkAndInsertToCache.COMPANY_INFO_INSERT_FAILED -> {
                                getCompanyInfoFromCacheEvent()
                            }

                            GetCompanyInfoFromNetworkAndInsertToCache.COMPANY_INFO_ERROR -> {
                                getCompanyInfoFromCacheEvent()
                            }
                        }
                    }
                }
            }
        }

        // Update UI when list size changes
        viewModel.viewState.observe(viewLifecycleOwner) { viewState ->

            if (viewState != null) {
                viewState.launchList?.let { _ ->
                    if (viewModel.isPaginationExhausted() && !viewModel.isQueryExhausted()) {
                        viewModel.setQueryExhausted(true)
                    }
                    submitList()
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

    private fun searchLaunchDataFromCacheEvent() {
        viewModel.setStateEvent(
            LaunchStateEvent.SearchLaunchListEvent()
        )
    }

    private fun getCompanyInfoFromCacheEvent() {
        viewModel.setStateEvent(
            LaunchStateEvent.GetCompanyInfoFromCacheEvent
        )
    }

    private fun getTotalNumEntriesInLaunchCacheEvent() {
        viewModel.retrieveNumLaunchItemsInCache()
    }

    private fun submitList() {
        listAdapter?.submitList(
            viewModel.createLaunchData(
                viewModel.getCompanyInfo()?.let { buildCompanyInfoString(it) }
            ) as List<LaunchType>
        )
    }

    private fun buildCompanyInfoString(companyInfo: CompanyInfoDomainEntity) = String.format(
        getString(R.string.company_info),
        companyInfo.name,
        companyInfo.founder,
        companyInfo.founded,
        companyInfo.employees,
        companyInfo.launchSites,
        companyInfo.valuation
    )

    private fun setListeners() {
        with(binding) {
            toolbar.filterBtn.setOnClickListener {
                showFilterDialog()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listAdapter = null // can leak memory
    }

    private fun getLaunchListFromNetworkAndInsertToCacheEvent() {
        viewModel.setStateEvent(
            LaunchStateEvent.GetLaunchListFromNetworkAndInsertToCacheEvent(
                launchOptions = viewModel.launchOptions
            )
        )
    }

    private fun setupSwipeRefresh() {
        with(binding) {
            swipeRefresh.setOnRefreshListener {
                swipeRefresh.isRefreshing = false
                viewModel.clearQueryParameters()
                getLaunchListFromNetworkAndInsertToCacheEvent()
            }
        }
    }

    override fun onItemSelected(position: Int, launchLinks: Links) {
        links = launchLinks
        if (isLinksNullOrEmpty()) {
            displayErrorDialogNoLinks()
        } else {
            displayBottomActionSheet(launchLinks)
        }
    }

    private fun isLinksNullOrEmpty() =
        links?.articleLink.isNullOrEmpty() &&
                links?.videoLink.isNullOrEmpty() &&
                links?.wikipedia.isNullOrEmpty()


    private fun displayBottomActionSheet(launchLinks: Links) {
        if (findNavController().currentDestination?.id == R.id.launchFragment) {
            findNavController().navigate(
                R.id.action_launchFragment_to_launchBottomActionSheet,
                bundleOf(LINKS_KEY to launchLinks)
            )
        }
    }

    private fun showFilterDialog() {

        activity?.let {
            val dialog = MaterialDialog(it)
                .noAutoDismiss()
                .customView(R.layout.dialog_filter)
                .cornerRadius(res = R.dimen.default_corner_radius)

            val view = dialog.getCustomView()
            val order = viewModel.getOrder()
            val filter = viewModel.getFilter()
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

                val searchQuery = view.findViewById<EditText>(R.id.search_query).text.toString()


                // Save data to view model
                viewModel.apply {
                    newOrder?.let { order ->
                        saveOrder(order)
                        setLaunchOrder(newOrder)
                    }
                    newFilter?.let { filter ->
                        saveFilter(filter)
                        setLaunchFilter(newFilter)
                    }
                    setQuery(searchQuery)

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
        viewModel.clearList()
        viewModel.loadFirstPage()
    }

    private fun displayErrorDialogNoLinks() {
        viewModel.setStateEvent(
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
        viewModel.setStateEvent(
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

}










































