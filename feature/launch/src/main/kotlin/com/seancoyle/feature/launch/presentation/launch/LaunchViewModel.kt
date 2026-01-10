package com.seancoyle.feature.launch.presentation.launch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.feature.launch.domain.usecase.component.LaunchesComponent
import com.seancoyle.feature.launch.presentation.LaunchUiMapper
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
    @Assisted private val launchId: String,
    @Assisted private val launchType: LaunchesType
) : ViewModel() {

    private val refreshEvent = MutableSharedFlow<Unit>(replay = 1)

    val launchState: StateFlow<LaunchUiState> =
        refreshEvent
            .onStart { emit(Unit) }
            .flatMapLatest {
                flow {
                    emit(LaunchUiState.Loading)

                    when (val result = launchesComponent.getLaunchUseCase(launchId, launchType)) {
                        is LaunchResult.Success -> {
                            val launch = result.data
                            val launchUi = uiMapper.mapToLaunchUi(launch)
                            emit(LaunchUiState.Success(launchUi))
                        }

                        is LaunchResult.Error -> {
                            emit(LaunchUiState.Error(result.error.toString()))
                        }
                    }
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = LaunchUiState.Loading
            )

    fun onEvent(event: LaunchEvent) {
        when (event) {
            is LaunchEvent.RetryFetch -> {
                refreshEvent.tryEmit(Unit)
            }
            is LaunchEvent.PullToRefreshEvent ->
                refreshEvent.tryEmit(Unit)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            launchId: String,
            launchType: LaunchesType
        ): LaunchViewModel
    }
}
