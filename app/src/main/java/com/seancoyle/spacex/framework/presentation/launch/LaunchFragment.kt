package com.seancoyle.spacex.framework.presentation.launch

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.seancoyle.spacex.R
import com.seancoyle.spacex.business.domain.model.company.CompanyInfoDomainEntity
import com.seancoyle.spacex.business.domain.model.launch.LaunchDomainEntity
import com.seancoyle.spacex.business.domain.model.launch.LaunchType
import com.seancoyle.spacex.business.domain.state.StateMessageCallback
import com.seancoyle.spacex.business.interactors.company.GetCompanyInfoFromCache
import com.seancoyle.spacex.business.interactors.company.GetCompanyInfoFromNetworkAndInsertToCache
import com.seancoyle.spacex.business.interactors.launch.GetAllLaunchItemsFromCache
import com.seancoyle.spacex.business.interactors.launch.GetLaunchItemsFromNetworkAndInsertToCache
import com.seancoyle.spacex.databinding.FragmentLaunchBinding
import com.seancoyle.spacex.framework.presentation.common.BaseFragment
import com.seancoyle.spacex.framework.presentation.common.viewBinding
import com.seancoyle.spacex.framework.presentation.launch.adapter.LaunchAdapter
import com.seancoyle.spacex.framework.presentation.launch.state.*
import com.seancoyle.spacex.util.AndroidTestUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

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

    override fun onSaveInstanceState(outState: Bundle) {
        val viewState = viewModel.viewState.value

        //clear the list. Don't want to save a large list to bundle.
        viewState?.launchList = ArrayList()

        outState.putParcelable(
            LAUNCH_STATE_BUNDLE_KEY,
            viewState
        )
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
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

                adapter = listAdapter
                listAdapter?.stateRestorationPolicy =
                    RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                setHasFixedSize(true)
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

                    GetLaunchItemsFromNetworkAndInsertToCache.LAUNCH_INSERT_SUCCESS -> {
                        viewModel.clearStateMessage()
                        viewModel.setStateEvent(LaunchStateEvent.GetLaunchListFromCacheEvent)
                    }

                    GetAllLaunchItemsFromCache.GET_ALL_LAUNCH_ITEMS_SUCCESS -> {
                        viewModel.clearStateMessage()
                    }

                    GetCompanyInfoFromNetworkAndInsertToCache.COMPANY_INFO_INSERT_SUCCESS -> {
                        viewModel.clearStateMessage()
                        viewModel.setStateEvent(LaunchStateEvent.GetCompanyInfoFromCacheEvent)
                    }

                    GetCompanyInfoFromCache.GET_COMPANY_INFO_SUCCESS -> {
                        viewModel.clearStateMessage()
                        updateAdapter()
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
                            GetLaunchItemsFromNetworkAndInsertToCache.LAUNCH_INSERT_FAILED -> {
                                getLaunchDataFromCacheEvent()
                            }

                            GetLaunchItemsFromNetworkAndInsertToCache.LAUNCH_ERROR -> {
                                getLaunchDataFromCacheEvent()
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
    }

    private fun getLaunchDataFromCacheEvent() {
        viewModel.setStateEvent(
            LaunchStateEvent.GetLaunchListFromCacheEvent
        )
    }

    private fun getCompanyInfoFromCacheEvent() {
        viewModel.setStateEvent(
            LaunchStateEvent.GetCompanyInfoFromCacheEvent
        )
    }

    private fun updateAdapter() {
        listAdapter?.updateAdapter(
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

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listAdapter = null // can leak memory
    }

    private fun setupSwipeRefresh() {
        with(binding) {
            swipeRefresh.setOnRefreshListener {
                swipeRefresh.isRefreshing = false
                viewModel.setStateEvent(LaunchStateEvent.GetLaunchListFromNetworkAndInsertToCacheEvent)
            }
        }
    }

    override fun onItemSelected(position: Int, selectedItem: LaunchDomainEntity) {
        /**
         * Show options menu
         */
    }

}










































