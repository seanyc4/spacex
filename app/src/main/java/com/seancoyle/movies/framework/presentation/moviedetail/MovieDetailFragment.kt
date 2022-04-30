package com.seancoyle.movies.framework.presentation.moviedetail

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.seancoyle.movies.R
import com.seancoyle.movies.business.domain.model.moviedetail.Cast
import com.seancoyle.movies.business.domain.model.movielist.Movie
import com.seancoyle.movies.business.domain.state.*
import com.seancoyle.movies.business.interactors.moviedetail.GetMovieCastByIdFromCache
import com.seancoyle.movies.business.interactors.moviedetail.GetMovieCastFromNetworkAndInsertToCache
import com.seancoyle.movies.databinding.FragmentMovieDetailBinding
import com.seancoyle.movies.framework.presentation.common.*
import com.seancoyle.movies.framework.presentation.moviedetail.adapter.MovieCastAdapter
import com.seancoyle.movies.framework.presentation.moviedetail.state.MovieDetailStateEvent
import com.seancoyle.movies.framework.presentation.moviedetail.state.MovieDetailViewState
import com.seancoyle.movies.framework.presentation.movielist.MOVIE_DETAIL_ERROR_RETRIEVEING_SELECTED_MOVIE
import com.seancoyle.movies.framework.presentation.movielist.MOVIE_DETAIL_SELECTED_MOVIE_BUNDLE_KEY
import com.seancoyle.movies.framework.presentation.movielist.glideLoadMoviePosters
import kotlinx.coroutines.*

const val MOVIE_DETAIL_STATE_BUNDLE_KEY =
    "com.seancoyle.movies.framework.presentation.moviedetail.state"

@FlowPreview
@ExperimentalCoroutinesApi
class MovieDetailFragment
constructor(
    private val viewModelFactory: ViewModelProvider.Factory
) : BaseFragment(R.layout.fragment_movie_detail),
    MovieCastAdapter.Interaction {

    private val viewModel: MovieDetailViewModel by viewModels { viewModelFactory }
    private val binding by viewBinding(FragmentMovieDetailBinding::bind)
    private var castAdapter: MovieCastAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setupChannel()
        getMovieData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupOnBackPressDispatcher()
        restoreInstanceState()
        setupRecyclerView()
        subscribeObservers()
        getMovieCastFromNetworkEvent()

    }

    private fun getMovieCastFromNetworkEvent() {
        viewModel.setStateEvent(
            MovieDetailStateEvent.GetMovieCastFromNetworkAndInsertToCacheEvent(
                movieId = viewModel.getMovie()?.id.toString()
            )
        )
    }

    private fun getMovieData() {
        arguments?.let { args ->
            (args.getParcelable(MOVIE_DETAIL_SELECTED_MOVIE_BUNDLE_KEY) as Movie?)?.let { selectedMovie ->
                viewModel.setMovie(selectedMovie)
            } ?: onErrorRetrievingMovieFromPreviousFragment()
        }
    }

    private fun onErrorRetrievingMovieFromPreviousFragment() {
        viewModel.setStateEvent(
            MovieDetailStateEvent.CreateStateMessageEvent(
                stateMessage = StateMessage(
                    response = Response(
                        message = MOVIE_DETAIL_ERROR_RETRIEVEING_SELECTED_MOVIE,
                        uiComponentType = UIComponentType.Dialog,
                        messageType = MessageType.Error
                    )
                )
            )
        )
    }

    override fun inject() {
        getAppComponent().inject(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val viewState = viewModel.getCurrentViewStateOrNew()
        outState.putParcelable(MOVIE_DETAIL_STATE_BUNDLE_KEY, viewState)
        super.onSaveInstanceState(outState)
    }

    private fun onBackPressed() {
        view?.hideKeyboard()
        findNavController().popBackStack()
    }

    private fun restoreInstanceState() {
        arguments?.let { args ->
            (args.getParcelable(MOVIE_DETAIL_STATE_BUNDLE_KEY) as MovieDetailViewState?)?.let { viewState ->
                viewModel.setViewState(viewState)

            }
        }
    }

    private fun setupRecyclerView() {
        with(binding) {
            rvMovieCast.apply {

                castAdapter = MovieCastAdapter(
                    interaction = this@MovieDetailFragment,
                )

                adapter = castAdapter
                castAdapter?.stateRestorationPolicy =
                    RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                setHasFixedSize(true)
            }
        }
    }

    override fun onItemSelected(position: Int, item: Cast) {

    }

    private fun setupUI() {
        with(binding) {

            ViewCompat.setOnApplyWindowInsetsListener(nestedScrollview) { _, insets ->
                // Set inset to appbar for full screen mode
                nestedScrollview.updatePadding(
                    bottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
                )
                insets
            }

            viewModel.getMovie()?.apply {

                bannerImage.glideLoadMoviePosters(backdrop_path)

                // concat title and date together & format date
                ("$title (${viewModel.dateUtil.removeTimeFromDateString(release_date)})").also {
                    movieTitle.text = it
                }
                movieOverview.text = overview
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

                    GetMovieCastFromNetworkAndInsertToCache.MOVIE_CAST_INSERT_SUCCESS -> {
                        viewModel.clearStateMessage()
                        updateAdapter()
                    }

                    GetMovieCastByIdFromCache.GET_MOVIE_CAST_BY_ID_SUCCESS -> {
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

                            GetMovieCastFromNetworkAndInsertToCache.MOVIE_CAST_INSERT_FAILED -> {
                                // Check cache for movie cast if insert fails
                                getMovieCastFromCacheEvent()
                            }

                            GetMovieCastFromNetworkAndInsertToCache.MOVIE_CAST_ERROR -> {
                                // Check cache for movie cast if net connection fails
                                getMovieCastFromCacheEvent()
                            }

                        }
                    }
                }
            }
        }
    }

    private fun getMovieCastFromCacheEvent() {
        viewModel.setStateEvent(
            MovieDetailStateEvent.GetMovieCastByIdFromCacheEvent(
                id = viewModel.getMovie()?.id ?: 1
            )
        )
    }

    private fun updateAdapter() {
        castAdapter?.updateAdapter(
            viewModel.getMovieCast()?.cast ?: emptyList()
        )
    }

    private fun setupOnBackPressDispatcher() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

}














