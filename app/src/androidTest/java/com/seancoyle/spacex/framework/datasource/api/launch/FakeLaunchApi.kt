package com.seancoyle.spacex.framework.datasource.api.launch

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seancoyle.spacex.framework.datasource.network.api.launch.LaunchApi
import com.seancoyle.spacex.framework.datasource.network.model.launch.LaunchDto
import com.seancoyle.spacex.framework.datasource.network.model.launch.LaunchOptions
import com.seancoyle.spacex.util.JsonFileReader
import javax.inject.Singleton

const val LAUNCH_LIST_RAW_JSON_FILE_NAME = "launch_list_raw.json"

@Singleton
class FakeLaunchApi(
    private val jsonFileReader: JsonFileReader
) :LaunchApi{

    override suspend fun getLaunchList(options: LaunchOptions): LaunchDto {
        return Gson()
            .fromJson(
                jsonFileReader.readJSONFromAsset(LAUNCH_LIST_RAW_JSON_FILE_NAME),
                object : TypeToken<LaunchDto>() {}.type
            )
    }
}