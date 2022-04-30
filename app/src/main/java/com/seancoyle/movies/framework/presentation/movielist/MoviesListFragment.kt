package com.seancoyle.movies.framework.presentation.movielist

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.seancoyle.movies.R
import com.seancoyle.movies.business.domain.model.movielist.Movie
import com.seancoyle.movies.business.domain.state.StateMessageCallback
import com.seancoyle.movies.business.interactors.movielist.GetAllMoviesFromCache
import com.seancoyle.movies.business.interactors.movielist.GetMoviesFromNetworkAndInsertToCache
import com.seancoyle.movies.databinding.FragmentMovieListBinding
import com.seancoyle.movies.framework.presentation.common.BaseFragment
import com.seancoyle.movies.framework.presentation.common.viewBinding
import com.seancoyle.movies.framework.presentation.movielist.adapter.MovieListAdapter
import com.seancoyle.movies.framework.presentation.movielist.state.*
import com.seancoyle.movies.util.AndroidTestUtils
import com.seancoyle.movies.util.printLogD
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.*
import javax.inject.Inject

const val MOVIE_LIST_STATE_BUNDLE_KEY =
    "com.seancoyle.movies.framework.presentation.movielist.state"

@FlowPreview
@ExperimentalCoroutinesApi
class MoviesListFragment
constructor(
    private val viewModelFactory: ViewModelProvider.Factory
) : BaseFragment(R.layout.fragment_movie_list),
    MovieListAdapter.Interaction {

    @Inject
    lateinit var androidTestUtils: AndroidTestUtils

    private val viewModel: MovieListViewModel by viewModels { viewModelFactory }
    private val binding by viewBinding(FragmentMovieListBinding::bind)
    private var listAdapter: MovieListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
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
            (inState[MOVIE_LIST_STATE_BUNDLE_KEY] as MovieListViewState?)?.let { viewState ->
                viewModel.setViewState(viewState)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val viewState = viewModel.viewState.value

        //clear the list. Don't want to save a large list to bundle.
        viewState?.movieList = ArrayList()

        outState.putParcelable(
            MOVIE_LIST_STATE_BUNDLE_KEY,
            viewState
        )
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        updateAdapter()
    }

    private fun saveLayoutManagerState() {
        binding.rvMovieList.layoutManager?.onSaveInstanceState()?.let { lmState ->
            viewModel.setLayoutManagerState(lmState)
        }
    }

    private fun setupRecyclerView() {
        with(binding) {
            rvMovieList.apply {

                listAdapter = MovieListAdapter(
                    interaction = this@MoviesListFragment,
                )

                adapter = listAdapter
                listAdapter?.stateRestorationPolicy =
                    RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                setHasFixedSize(true)
            }
        }
    }

    private fun subscribeObservers() {

        /* viewModel.shouldDisplayProgressBar.observe(viewLifecycleOwner) {
             uiController.displayProgressBar(it)
         }*/


        viewModel.stateMessage.observe(viewLifecycleOwner) { stateMessage ->
            stateMessage?.response?.let { response ->
                when (response.message) {

                    GetMoviesFromNetworkAndInsertToCache.MOVIES_INSERT_SUCCESS -> {
                        viewModel.clearStateMessage()
                        updateAdapter()
                        disableSwipeToRefreshAnimation()
                    }

                    GetAllMoviesFromCache.GET_ALL_MOVIES_SUCCESS -> {
                        viewModel.clearStateMessage()
                        updateAdapter()
                        disableSwipeToRefreshAnimation()
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

                            GetMoviesFromNetworkAndInsertToCache.MOVIES_INSERT_FAILED -> {
                                // Check cache for movies if insert fails
                                getMoviesFromCacheEvent()
                            }

                            GetMoviesFromNetworkAndInsertToCache.MOVIES_ERROR -> {
                                // Check cache for movies if net connection fails
                                getMoviesFromCacheEvent()
                            }

                            GetAllMoviesFromCache.NO_DATA -> {
                                disableSwipeToRefreshAnimation()
                            }

                            GetAllMoviesFromCache.GET_ALL_MOVIES_FAILED -> {
                                disableSwipeToRefreshAnimation()
                            }

                            GetAllMoviesFromCache.GET_ALL_MOVIES_NO_MATCHING_RESULTS -> {
                                disableSwipeToRefreshAnimation()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getMoviesFromCacheEvent() {
        viewModel.setStateEvent(
            MovieListStateEvent.GetMoviesFromCacheEvent
        )
    }

    private fun disableSwipeToRefreshAnimation() {
        binding.swipeRefresh.isRefreshing = false

    }

    private fun updateAdapter() {
        listAdapter?.updateAdapter(
            viewModel.getMovies()?.movies
                ?: viewModel.getMovieList()?.getOrNull(0)?.movies
                ?: emptyList()
        )
    }

    // for debugging
    private fun printActiveJobs() {

        for ((index, job) in viewModel.getActiveJobs().withIndex()) {
            printLogD(
                "MovieList",
                "${index}: ${job}"
            )
        }
    }

    private fun setupUI() {
        with(binding) {

            rvMovieList.applyInsetter {
                // Apply the navigation bar insets...
                type(navigationBars = true) {
                    // Add to padding on all sides
                    padding()
                }

                // Apply the status bar insets...
                type(statusBars = true) {
                    // Add to padding on all sides
                    padding()
                }
            }
        }
    }

    override fun inject() {
        getAppComponent().inject(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listAdapter = null // can leak memory
    }

    private fun setupSwipeRefresh() {
        with(binding) {
            swipeRefresh.setOnRefreshListener {
                swipeRefresh.isRefreshing = true
                viewModel.setStateEvent(MovieListStateEvent.GetMoviesFromNetworkAndInsertToCacheEvent)
            }
        }
    }

    override fun onItemSelected(position: Int, selectedMovie: Movie) {
        val bundle = bundleOf(MOVIE_DETAIL_SELECTED_MOVIE_BUNDLE_KEY to selectedMovie)
        navigateToMovieDetailFragment(bundle)
    }

    private fun navigateToMovieDetailFragment(bundle: Bundle) {
        if (findNavController().currentDestination?.id == R.id.moviesListFragment) {
            findNavController().navigate(
                R.id.action_movieListFragment_to_movieDetailFragment,
                bundle
            )
        }
    }

}










































