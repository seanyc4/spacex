package com.seancoyle.spacex.business.data.cache.implementation.launch

import com.seancoyle.spacex.business.data.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.spacex.business.domain.model.launch.LaunchModel
import com.seancoyle.spacex.framework.datasource.cache.abstraction.launch.LaunchDaoService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LaunchCacheDataSourceImpl
@Inject
constructor(
    private val daoService: LaunchDaoService
) : LaunchCacheDataSource {

    override suspend fun insert(launch: LaunchModel): Long {
        return daoService.insert(
            launch = launch
        )
    }

    override suspend fun deleteById(id: Int): Int {
        return daoService.deleteById(
            id = id
        )
    }

    override suspend fun deleteList(launchList: List<LaunchModel>): Int {
        return daoService.deleteList(
            launchList = launchList
        )
    }

    override suspend fun deleteAll() {
        return daoService.deleteAll()
    }

    override suspend fun getAll(): List<LaunchModel>? {
        return daoService.getAll()
    }

    override suspend fun getById(id: Int): LaunchModel? {
        return daoService.getById(
            id = id
        )
    }

    override suspend fun getTotalEntries(): Int {
        return daoService.getTotalEntries()
    }

    override suspend fun insertLaunchList(launchList: List<LaunchModel>): LongArray {
        return daoService.insertList(
            launchList = launchList
        )
    }

    override suspend fun filterLaunchList(
        year: String,
        order: String,
        isLaunchSuccess: Int?,
        page: Int
    ): List<LaunchModel>? {
        return daoService.filterLaunchList(
            year = year,
            order = order,
            isLaunchSuccess = isLaunchSuccess,
            page = page
        )
    }

}





















