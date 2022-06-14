package com.seancoyle.launch_datasource.cache.launch

import com.seancoyle.launch_datasource.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.launch_domain.model.launch.LaunchModel
import com.seancoyle.launch_interactors.launch.InsertLaunchListToCache.Companion.INSERT_LAUNCH_LIST_FAILED
import com.seancoyle.database.dao.launch.LAUNCH_ORDER_DESC
import com.seancoyle.database.dao.launch.LAUNCH_PAGINATION_PAGE_SIZE
import com.seancoyle.launch_datasource.network.mappers.launch.LAUNCH_ALL

const val FORCE_DELETE_LAUNCH_EXCEPTION = -2
const val FORCE_DELETES_LAUNCH_EXCEPTION = -3
const val FORCE_NEW_LAUNCH_EXCEPTION = -4
const val FORCE_GENERAL_FAILURE = -5
const val FORCE_SEARCH_LAUNCH_EXCEPTION = "FORCE_SEARCH_LAUNCH_EXCEPTION"


class FakeLaunchCacheDataSourceImpl
constructor(
    private val fakeLaunchDatabase: FakeLaunchDatabase
) : LaunchCacheDataSource {

    override suspend fun insert(launch: LaunchModel): Long {
        if (launch.id == FORCE_NEW_LAUNCH_EXCEPTION) {
            throw Exception("Something went wrong inserting the launch.")
        }
        if (launch.id == FORCE_GENERAL_FAILURE) {
            return -1 // fail
        }
        fakeLaunchDatabase.launchList.add(launch)
        return 1 // success
    }

    override suspend fun deleteList(launchList: List<LaunchModel>): Int {
        var failOrSuccess = 1
        for (item in launchList) {
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

    override suspend fun getById(id: Int): LaunchModel? {
        return fakeLaunchDatabase.launchList.find { it.id == id }
    }

    override suspend fun getAll(): List<LaunchModel> {
        return fakeLaunchDatabase.launchList
    }

    override suspend fun getTotalEntries(): Int {
        return fakeLaunchDatabase.launchList.size
    }

    override suspend fun insertLaunchList(launchList: List<LaunchModel>): LongArray {
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

    override suspend fun filterLaunchList(
        year: String,
        order: String,
        launchFilter: Int?,
        page: Int
    ): List<LaunchModel> {
        if (year == FORCE_SEARCH_LAUNCH_EXCEPTION) {
            throw Exception("Something went searching the cache for launch items.")
        }
        val results: ArrayList<LaunchModel> = ArrayList()
        for (item in fakeLaunchDatabase.launchList) {

            when {

                year.isNotEmpty() && launchFilter == LAUNCH_ALL -> {
                    results.add(item)
                }

                launchFilter == LAUNCH_ALL -> {
                    results.add(item)
                }

                launchFilter != null && year.isEmpty() -> {
                    if (item.isLaunchSuccess == launchFilter) {
                        results.add(item)
                    }
                }

                year.isNotEmpty() && launchFilter == null -> {
                    if (item.launchYear == year) {
                        results.add(item)
                    }
                }

                year.isNotEmpty() && launchFilter != null ->
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















