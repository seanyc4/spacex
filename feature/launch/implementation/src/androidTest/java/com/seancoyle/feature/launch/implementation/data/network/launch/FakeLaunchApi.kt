package com.seancoyle.feature.launch.implementation.data.network.launch

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seancoyle.core.test.JsonFileReader
import com.seancoyle.feature.launch.implementation.TestConstants
import com.seancoyle.feature.launch.implementation.data.network.LaunchApi
import com.seancoyle.feature.launch.implementation.data.network.dto.LaunchesDto
import com.seancoyle.feature.launch.implementation.domain.model.LaunchOptions
import javax.inject.Inject

internal class FakeLaunchApi @Inject constructor(
    private val jsonFileReader: JsonFileReader
) : LaunchApi {

    var jsonFileName: String = TestConstants.LAUNCHES_200_RESPONSE

    override suspend fun getLaunches(options: LaunchOptions): LaunchesDto {
        if (jsonFileName == TestConstants.ERROR_404_RESPONSE) {
            val errorContent = jsonFileReader.readJSONFromAsset(jsonFileName)
           /* val responseBody =
                ResponseBody.create("application/json".toMediaTypeOrNull(), errorContent)
            val response = Response.error<String>(404, responseBody)
            throw HttpException(response)*/
        }
        return Gson()
            .fromJson(
                jsonFileReader.readJSONFromAsset(jsonFileName),
                object : TypeToken<LaunchesDto>() {}.type
            )
    }
}