package com.seancoyle.feature.launch.domain.usecase.component

import androidx.paging.PagingData
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.domain.model.Launch
import com.seancoyle.feature.launch.domain.model.LaunchPrefs
import com.seancoyle.feature.launch.domain.model.LaunchQuery
import com.seancoyle.feature.launch.domain.usecase.launch.GetLaunchPreferencesUseCase
import com.seancoyle.feature.launch.domain.usecase.launch.ObserveLaunchesUseCase
import com.seancoyle.feature.launch.domain.usecase.launch.SaveLaunchPreferencesUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import dagger.Lazy

internal class LaunchesComponentImpl @Inject constructor(
    private val saveLaunchPreferencesUseCase: Lazy<SaveLaunchPreferencesUseCase>,
    private val getLaunchPreferencesUseCase: GetLaunchPreferencesUseCase,
    private val observeLaunchesUseCase: ObserveLaunchesUseCase
) : LaunchesComponent {

    override fun observeLaunchesUseCase(query: LaunchQuery): Flow<PagingData<Launch>> {
        return observeLaunchesUseCase.invoke(query)
    }

    override suspend fun getLaunchPreferencesUseCase(): LaunchPrefs {
        return getLaunchPreferencesUseCase.invoke()
    }

    override suspend fun saveLaunchPreferencesUseCase(order: Order) {
        saveLaunchPreferencesUseCase.get().invoke(order)
    }
}
