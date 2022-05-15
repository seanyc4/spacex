package com.seancoyle.spacex.business.data.cache.launch

import com.seancoyle.spacex.business.data.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.spacex.business.domain.model.launch.LaunchDomainEntity
import com.seancoyle.spacex.business.interactors.launch.InsertLaunchListToCache.Companion.INSERT_LAUNCH_LIST_FAILED
import com.seancoyle.spacex.framework.datasource.cache.dao.launch.LAUNCH_PAGINATION_PAGE_SIZE

const val FORCE_DELETE_LAUNCH_EXCEPTION = -2
const val FORCE_DELETES_LAUNCH_EXCEPTION = -3
const val FORCE_NEW_LAUNCH_EXCEPTION = -4
const val FORCE_GENERAL_FAILURE = -5
const val FORCE_SEARCH_LAUNCH_EXCEPTION = "FORCE_SEARCH_LAUNCH_EXCEPTION"


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
        var results = LongArray(launchList.size)
        for (item in launchList.withIndex()) {

            when (item.value.id) {
                FORCE_GENERAL_FAILURE -> {
                    results = LongArray(0)
                }
                FORCE_NEW_LAUNCH_EXCEPTION -> {
                    throw Exception(INSERT_LAUNCH_LIST_FAILED)
                }
                else -> {
                    results[item.index] = 1
                    fakeLaunchDatabase.launchList.add(item.value)
                }
            }
        }
        return results
    }

    override suspend fun searchLaunchList(
        year: String,
        order: String,
        isLaunchSuccess: Boolean?,
        page: Int
    ): List<LaunchDomainEntity> {
        if (year == FORCE_SEARCH_LAUNCH_EXCEPTION) {
            throw Exception("Something went searching the cache for launch items.")
        }
        val results: ArrayList<LaunchDomainEntity> = ArrayList()
        for (item in fakeLaunchDatabase.launchList) {
            if (item.launchYear.contains(year)) {
                results.add(item)
            } else if (item.isLaunchSuccess == isLaunchSuccess) {
                results.add(item)
            }
            if (results.size > (page * LAUNCH_PAGINATION_PAGE_SIZE)) {
                break
            }
        }
        return results
    }
}















