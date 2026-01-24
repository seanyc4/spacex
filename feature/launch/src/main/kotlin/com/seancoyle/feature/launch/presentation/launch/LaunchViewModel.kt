package com.seancoyle.feature.launch.presentation.launch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.feature.launch.domain.usecase.analytics.LaunchAnalyticsComponent
import com.seancoyle.feature.launch.domain.usecase.component.LaunchesComponent
import com.seancoyle.feature.launch.presentation.LaunchUiMapper
import com.seancoyle.feature.launch.presentation.launch.model.LaunchUI
import com.seancoyle.feature.launch.presentation.launch.state.LaunchEvent
import com.seancoyle.feature.launch.presentation.launch.state.LaunchUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel(assistedFactory = LaunchViewModel.Factory::class)
class LaunchViewModel @AssistedInject constructor(
    private val launchesComponent: LaunchesComponent,
    private val uiMapper: LaunchUiMapper,
    private val launchAnalyticsComponent: LaunchAnalyticsComponent,
    @Assisted private val launchId: String,
    @Assisted private val launchType: LaunchesType
) : ViewModel() {

    private val loadTriggers = MutableSharedFlow<Boolean>(replay = 1)

    val launchState: StateFlow<LaunchUiState> =
        loadTriggers
            .onStart { emit(false) }
            .flatMapLatest { forceRefresh ->
                flow {
                    emit(LaunchUiState.Loading)

                    when (
                        val result = launchesComponent.getLaunchUseCase(
                            launchId = launchId,
                            launchType = launchType,
                            isRefresh = forceRefresh
                        )
                    ) {
                        is LaunchResult.Success -> {
                            val launchUi = uiMapper.mapToLaunchUi(result.data)
                            trackDetailView(launchUi)
                            emit(LaunchUiState.Success(launchUi))
                        }

                        is LaunchResult.Error -> {
                            launchAnalyticsComponent.trackErrorDisplayed(
                                errorType = result.error.toString(),
                                launchType = launchType.name
                            )
                            emit(LaunchUiState.Error(result.error.toString()))
                        }
                    }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = LaunchUiState.Loading
            )

    fun onEvent(event: LaunchEvent) {
        when (event) {
            is LaunchEvent.Retry -> {
                launchAnalyticsComponent.trackRetryTap(launchType.name)
                loadTriggers.tryEmit(true)
            }

            is LaunchEvent.PullToRefresh -> {
                launchAnalyticsComponent.trackPullRefresh(launchType.name)
                loadTriggers.tryEmit(true)
            }
        }
    }

    fun trackVideoPlay(videoId: String, isLive: Boolean) {
        launchAnalyticsComponent.trackVideoPlay(
            launchId = launchId,
            videoId = videoId,
            isLive = isLive,
            launchType = launchType.name
        )
    }

    fun trackExternalLinkTap(linkType: String) {
        launchAnalyticsComponent.trackExternalLinkTap(
            launchId = launchId,
            linkType = linkType,
            launchType = launchType.name
        )
    }

    fun trackSectionExpand(sectionName: String) {
        launchAnalyticsComponent.trackDetailSectionExpand(
            launchId = launchId,
            sectionName = sectionName,
            launchType = launchType.name
        )
    }

    private fun trackDetailView(launch: LaunchUI) {
        launchAnalyticsComponent.trackDetailView(
            launchId = launchId,
            launchType = launchType.name,
            status = launch.status.name,
            hasVideo = launch.vidUrls.isNotEmpty(),
            agency = launch.launchServiceProvider?.abbrev ?: "unknown"
        )
    }

    @AssistedFactory
    interface Factory {
        fun create(
            launchId: String,
            launchType: LaunchesType
        ): LaunchViewModel
    }
}
