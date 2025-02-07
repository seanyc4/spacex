package com.seancoyle.feature.launch.implementation.data.repository

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.api.domain.model.LaunchPrefs
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.data.network.CompanyNetworkMapper
import com.seancoyle.feature.launch.implementation.data.network.LaunchNetworkMapper
import com.seancoyle.feature.launch.implementation.domain.cache.LaunchPreferencesDataSource
import com.seancoyle.feature.launch.implementation.domain.model.LaunchOptions
import com.seancoyle.feature.launch.implementation.domain.network.CompanyNetworkDataSource
import com.seancoyle.feature.launch.implementation.domain.network.LaunchNetworkDataSource
import com.seancoyle.feature.launch.implementation.domain.repository.SpaceXRepository
import javax.inject.Inject

internal class SpaceXRepositoryImpl @Inject constructor(
    private val companyNetworkDataSource: CompanyNetworkDataSource,
    private val launchNetworkDataSource: LaunchNetworkDataSource,
    private val launchPreferencesDataSource: LaunchPreferencesDataSource,
    private val companyNetworkMapper: CompanyNetworkMapper,
    private val launchNetworkMapper: LaunchNetworkMapper,
) : SpaceXRepository {

    override suspend fun getCompany(): Result<Company, DataError> {
        return when (val result = companyNetworkDataSource.getCompany()) {
            is Result.Success -> Result.Success(companyNetworkMapper.mapFromEntity(result.data))
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun getLaunches(launchOptions: LaunchOptions): Result<List<LaunchTypes.Launch>, DataError> {
        return when (val result = launchNetworkDataSource.getLaunches(launchOptions)) {
            is Result.Success -> Result.Success(launchNetworkMapper.mapEntityToList(result.data))
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun saveLaunchPreferences(
        order: Order,
        launchStatus: LaunchStatus,
        launchYear: String
    ) {
        launchPreferencesDataSource.saveLaunchPreferences(
            order = order,
            launchStatus = launchStatus,
            launchYear = launchYear
        )
    }

    override suspend fun getLaunchPreferences(): LaunchPrefs {
        return launchPreferencesDataSource.getLaunchPreferences()
    }
}