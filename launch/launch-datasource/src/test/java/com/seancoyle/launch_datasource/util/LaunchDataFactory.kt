package com.seancoyle.launch_datasource.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seancoyle.launch_models.model.launch.*
import com.seancoyle.launch_datasource.cache.launch.FakeLaunchDatabase

class LaunchDataFactory(
    private val testClassLoader: ClassLoader
) {

    fun provideValidFilterYearDates() = listOf(
        "2022", "2021", "2020", "2019", "2018", "2017", "2016", "2015", "2014", "2013",
        "2012", "2010", "2009", "2008", "2007", "2006"
    ).shuffled()

    fun produceListOfLaunchItems(): List<LaunchModel> {
        return Gson()
            .fromJson(
                getDataFromFile("launch_list.json"),
                object : TypeToken<List<LaunchModel>>() {}.type
            )
    }

    fun produceFakeAppDatabase(launchList: List<LaunchModel>): FakeLaunchDatabase {
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
