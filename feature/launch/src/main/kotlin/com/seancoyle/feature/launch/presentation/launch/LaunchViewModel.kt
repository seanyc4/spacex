package com.seancoyle.feature.launch.presentation.launch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seancoyle.core.common.coroutines.stateIn
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.feature.launch.domain.usecase.component.LaunchesComponent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

@HiltViewModel(assistedFactory = LaunchViewModel.Factory::class)
class LaunchViewModel @AssistedInject constructor(
    private val launchesComponent: LaunchesComponent,
    private val uiMapper: LaunchUiMapper,
    @Assisted private val launchId: String,
    @Assisted private val launchType: LaunchesType
) : ViewModel() {

    val launchState: StateFlow<LaunchUiState> = flow {
        emit(LaunchUiState.Loading)

        when (val result = launchesComponent.getLaunchUseCase(launchId, launchType)) {
            is LaunchResult.Success -> {
                val launch = result.data
                val launchUi = uiMapper(launch)
                emit(LaunchUiState.Success(launchUi))
            }
            is LaunchResult.Error -> {
                emit(LaunchUiState.Error(result.error.toString()))
            }
        }
    }.stateIn(
        scope = viewModelScope,
        initialValue = LaunchUiState.Loading
    )

    @AssistedFactory
    internal interface Factory {
        fun create(
            launchId: String,
            launchType: LaunchesType
        ): LaunchViewModel
    }
}
