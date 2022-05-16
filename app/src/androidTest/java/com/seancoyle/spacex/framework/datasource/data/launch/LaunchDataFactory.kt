package com.seancoyle.spacex.framework.datasource.data.launch

import android.app.Application
import android.content.res.AssetManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seancoyle.spacex.business.domain.model.launch.*
import java.io.IOException
import java.io.InputStream
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
class LaunchDataFactory
@Inject
constructor(
    private val application: Application,
    private val factory: LaunchFactory
) {

    fun produceListOfLaunches(): List<LaunchDomainEntity> {
        return Gson()
            .fromJson(
                getDataFromFile("launch_list.json"),
                object : TypeToken<List<LaunchDomainEntity>>() {}.type
            )
    }

    fun produceEmptyListOfData(): List<LaunchDomainEntity> {
        return ArrayList()
    }

    fun getDataFromFile(fileName: String): String? {
        return readJSONFromAsset(fileName)
    }

    private fun readJSONFromAsset(fileName: String): String? {
        val json: String? = try {
            val inputStream: InputStream = (application.assets as AssetManager).open(fileName)
            inputStream.bufferedReader().use { it.readText() }
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    fun createSingleLaunch(
        id: Int,
        launchDate: String,
        isLaunchSuccess: Int,
        launchSuccessIcon: Int,
        launchDateLocalDateTime: LocalDateTime,
        launchYear: String,
        links: Links,
        missionName: String,
        rocket: Rocket,
        daysToFromTitle: Int,
        launchDaysDifference: String,
        type: Int
    ) = factory.createLaunchItem(
        id = id,
        launchDate = launchDate,
        isLaunchSuccess = isLaunchSuccess,
        launchSuccessIcon = launchSuccessIcon,
        launchDateLocalDateTime = launchDateLocalDateTime,
        launchYear = launchYear,
        links = links,
        missionName = missionName,
        rocket = rocket,
        daysToFromTitle = daysToFromTitle,
        launchDaysDifference = launchDaysDifference,
        type = type,

        )

    fun createLaunchList(
        num: Int,
        id: Int
    ) = factory.createLaunchListTest(
        num = num,
        id = id
    )
}













