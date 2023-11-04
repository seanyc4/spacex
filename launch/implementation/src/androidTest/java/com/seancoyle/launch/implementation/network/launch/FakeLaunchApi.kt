package com.seancoyle.launch.implementation.network.launch

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seancoyle.core.testing.JsonFileReader
import com.seancoyle.launch.api.domain.model.LaunchOptions
import com.seancoyle.launch.implementation.data.network.LaunchApi
import com.seancoyle.launch.implementation.data.network.dto.LaunchDto
import javax.inject.Inject

const val LAUNCH_LIST_RAW_JSON_FILE_NAME = "launch_list_raw.json"

internal class FakeLaunchApi @Inject constructor(
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