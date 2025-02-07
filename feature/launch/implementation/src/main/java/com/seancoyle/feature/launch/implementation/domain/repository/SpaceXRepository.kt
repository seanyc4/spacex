package com.seancoyle.feature.launch.implementation.domain.repository

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.api.domain.model.LaunchPrefs
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.domain.model.LaunchOptions

internal interface SpaceXRepository {
    suspend fun getCompany(): Result<Company, DataError>
    suspend fun getLaunches(launchOptions: LaunchOptions): Result<List<LaunchTypes.Launch>, DataError>
    suspend fun getLaunchPreferences(): LaunchPrefs
    suspend fun saveLaunchPreferences(
        order: Order,
        launchStatus: LaunchStatus,
        launchYear: String
    )
}