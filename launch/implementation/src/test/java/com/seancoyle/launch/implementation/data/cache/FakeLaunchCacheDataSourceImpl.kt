package com.seancoyle.launch.implementation.data.cache

import com.seancoyle.constants.LaunchDaoConstants.LAUNCH_ORDER_DESC
import com.seancoyle.constants.LaunchDaoConstants.LAUNCH_PAGINATION_PAGE_SIZE
import com.seancoyle.constants.LaunchNetworkConstants.LAUNCH_ALL
import com.seancoyle.launch.api.data.LaunchCacheDataSource
import com.seancoyle.launch.api.domain.model.ViewModel

const val FORCE_DELETE_LAUNCH_EXCEPTION = -2
const val FORCE_DELETES_LAUNCH_EXCEPTION = -3
const val FORCE_NEW_LAUNCH_EXCEPTION = -4
const val FORCE_LAUNCH_GENERAL_FAILURE = -5
const val FORCE_SEARCH_LAUNCH_EXCEPTION = "FORCE_SEARCH_LAUNCH_EXCEPTION"
const val INSERT_LAUNCH_LIST_FAILED = "Failed to insert new launch list."


class FakeLaunchCacheDataSourceImpl
constructor(
    private val fakeLaunchDatabase: FakeLaunchDatabase
) : LaunchCacheDataSource {

    override suspend fun insert(launch: ViewModel): Long {
        if (launch.id == FORCE_NEW_LAUNCH_EXCEPTION) {
            throw Exception("Something went wrong inserting the launch.")
        }
        if (launch.id == FORCE_LAUNCH_GENERAL_FAILURE) {
            return -1 // fail
        }
        fakeLaunchDatabase.launchList.add(launch)
        return 1 // success
    }

    override suspend fun deleteList(launches: List<ViewModel>): Int {
        var failOrSuccess = 1
        for (item in launches) {
            failOrSuccess = if (fakeLaunchDatabase.launchList.removeIf { it.id == item.id }) {
                1 // return 1 for success
            } else {
                -1 // fail
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
            1 // return 1 for success
        } else {
            -1 // fail
        }
    }

    override suspend fun deleteAll() {
        fakeLaunchDatabase.launchList.clear()
    }

    override suspend fun getById(id: Int): ViewModel? {
        return fakeLaunchDatabase.launchList.find { it.id == id }
    }

    override suspend fun getAll(): List<ViewModel> {
        return fakeLaunchDatabase.launchList
    }

    override suspend fun getTotalEntries(): Int {
        return fakeLaunchDatabase.launchList.size
    }

    override suspend fun insertList(launches: List<ViewModel>): LongArray {
        var results = LongArray(launches.size)
        for (item in launches.withIndex()) {
            when (item.value.id) {

                FORCE_LAUNCH_GENERAL_FAILURE -> {
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

    override suspend fun filterLaunchList(
        year: String?,
        order: String,
        launchFilter: Int?,
        page: Int
    ): List<ViewModel> {
        if (year == FORCE_SEARCH_LAUNCH_EXCEPTION) {
            throw Exception("Something went searching the cache for launch items.")
        }
        val results: ArrayList<ViewModel> = ArrayList()
        for (item in fakeLaunchDatabase.launchList) {

            when {

                year?.isNotEmpty() == true && launchFilter == LAUNCH_ALL -> {
                    results.add(item)
                }

                launchFilter == LAUNCH_ALL -> {
                    results.add(item)
                }

                launchFilter != null && year?.isEmpty() == true -> {
                    if (item.isLaunchSuccess == launchFilter) {
                        results.add(item)
                    }
                }

                year?.isNotEmpty() == true && launchFilter == null -> {
                    if (item.launchYear == year) {
                        results.add(item)
                    }
                }

                year?.isNotEmpty() == true && launchFilter != null ->
                    if (item.launchYear == year && item.isLaunchSuccess == launchFilter) {
                        results.add(item)
                    }

                else -> results.add(item)

            }

            if (launchFilter != LAUNCH_ALL) {
                if (results.size > (page * LAUNCH_PAGINATION_PAGE_SIZE)) {
                    break
                }
            }

        }

        // Apply filter to data
        if (order == LAUNCH_ORDER_DESC) {
            results.sortByDescending { it.launchDateLocalDateTime }
        } else {
            results.sortBy { it.launchDateLocalDateTime }
        }

        return results
    }
}















