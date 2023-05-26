package com.seancoyle.launch.implementation.data.network

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seancoyle.core.testing.JsonFileReader
import com.seancoyle.launch.contract.domain.model.LaunchOptions
import com.seancoyle.launch.implementation.data.model.LaunchDto
import javax.inject.Singleton

const val LAUNCH_LIST_RAW_JSON_FILE_NAME = "launch_list_raw.json"

@Singleton
class FakeLaunchApi(
    private val jsonFileReader: JsonFileReader
) : LaunchApi {

    override suspend fun getLaunchList(options: LaunchOptions): LaunchDto {
        return Gson()
            .fromJson(
                jsonFileReader.readJSONFromAsset(LAUNCH_LIST_RAW_JSON_FILE_NAME),
                object : TypeToken<LaunchDto>() {}.type
            )
    }
}