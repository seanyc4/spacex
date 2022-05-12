package com.seancoyle.spacex.business.data

import com.seancoyle.spacex.business.domain.model.launch.LaunchDomainEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seancoyle.spacex.business.data.cache.launch.FakeLaunchDatabase

class LaunchDataFactory(
    private val testClassLoader: ClassLoader
) {

    fun produceListOfLaunchItems(): List<LaunchDomainEntity> {
        return Gson()
            .fromJson(
                getDataFromFile("launch_list.json"),
                object : TypeToken<List<LaunchDomainEntity>>() {}.type
            )
    }

    fun produceFakeAppDatabase(launchList: List<LaunchDomainEntity>): FakeLaunchDatabase {
        val database = FakeLaunchDatabase()
        for (item in launchList) {
            database.launchList.add(item)
        }
        return database
    }

    private fun getDataFromFile(fileName: String): String {
        return testClassLoader.getResource(fileName).readText()
    }

}


















