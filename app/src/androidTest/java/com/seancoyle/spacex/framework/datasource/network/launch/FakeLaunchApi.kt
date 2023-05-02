package com.seancoyle.spacex.framework.datasource.network.launch

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seancoyle.core.testing.JsonFileReader
import com.seancoyle.launch.api.LaunchApi
import com.seancoyle.launch.api.LaunchDto
import com.seancoyle.launch.api.LaunchOptions
import javax.inject.Singleton

const val LAUNCH_LIST_RAW_JSON_FILE_NAME = "launch_list_raw.json"

@Singleton
class FakeLaunchApi(
    private val jsonFileReader: JsonFileReader
) : com.seancoyle.launch.api.LaunchApi {

    override suspend fun getLaunchList(options: com.seancoyle.launch.api.LaunchOptions): com.seancoyle.launch.api.LaunchDto {
        return Gson()
            .fromJson(
                jsonFileReader.readJSONFromAsset(LAUNCH_LIST_RAW_JSON_FILE_NAME),
                object : TypeToken<com.seancoyle.launch.api.LaunchDto>() {}.type
            )
    }
}