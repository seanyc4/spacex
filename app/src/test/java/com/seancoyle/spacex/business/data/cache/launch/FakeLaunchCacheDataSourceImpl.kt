package com.seancoyle.spacex.business.data.cache.launch

import com.seancoyle.spacex.business.data.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.spacex.business.domain.model.launch.LaunchDomainEntity

const val FORCE_DELETE_LAUNCH_EXCEPTION = -2
const val FORCE_DELETES_LAUNCH_EXCEPTION = -3
const val FORCE_NEW_LAUNCH_EXCEPTION = -4
const val FORCE_GENERAL_FAILURE = -5

class FakeLaunchCacheDataSourceImpl
constructor(
    private val fakeLaunchDatabase: FakeLaunchDatabase
) : LaunchCacheDataSource {

    override suspend fun insert(launch: LaunchDomainEntity): Long {
        if (launch.id == FORCE_NEW_LAUNCH_EXCEPTION) {
            throw Exception("Something went wrong inserting the launch.")
        }
        if (launch.id == FORCE_GENERAL_FAILURE) {
            return -1 // fail
        }
        fakeLaunchDatabase.launchList.add(launch)
        return 1 // success
    }

    override suspend fun deleteList(launchList: List<LaunchDomainEntity>): Int {
        var failOrSuccess = 1
        for (item in launchList) {
            failOrSuccess = if (fakeLaunchDatabase.launchList.removeIf { it.id == item.id }) {
                1 // return 1 for success
            } else {
                -1 // mark for failure
            }
        }
        return failOrSuccess
    }

    override suspend fun deleteById(id: Int): Int {
        if (id == FORCE_DELETE_LAUNCH_EXCEPTION) {
            throw Exception("Something went wrong deleting the item.")
        } else if (id == FORCE_DELETES_LAUNCH_EXCEPTION) {
            throw Exception("Something went wrong deleting the item.")
        }
        return if (fakeLaunchDatabase.launchList.removeIf { it.id == id }) {
            1
        } else {
            -1
        }
    }

    override suspend fun deleteAll() {
        fakeLaunchDatabase.launchList.clear()
    }

    override suspend fun getById(id: Int): LaunchDomainEntity? {
        return fakeLaunchDatabase.launchList.find { it.id == id }
    }

    override suspend fun getAll(): List<LaunchDomainEntity> {
        return fakeLaunchDatabase.launchList
    }

    override suspend fun getTotalEntries(): Int {
        return fakeLaunchDatabase.launchList.size
    }

    override suspend fun insertLaunchList(launchList: List<LaunchDomainEntity>): LongArray {
        val results = LongArray(launchList.size)
        for (item in launchList.withIndex()) {
            results[item.index] = 1
            fakeLaunchDatabase.launchList.add(item.value)
        }
        return results
    }
}















