package com.seancoyle.launch.implementation.network.launch

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seancoyle.core_testing.JsonFileReader
import com.seancoyle.launch.implementation.TestConstants
import com.seancoyle.launch.implementation.data.network.LaunchApi
import com.seancoyle.launch.implementation.data.network.dto.LaunchDto
import com.seancoyle.launch.implementation.domain.model.LaunchOptions
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

internal class FakeLaunchApi @Inject constructor(
    private val jsonFileReader: JsonFileReader
) : LaunchApi {

    var jsonFileName: String = TestConstants.LAUNCHES_200_RESPONSE

    override suspend fun getLaunchList(options: LaunchOptions): LaunchDto {
        if (jsonFileName == TestConstants.ERROR_404_RESPONSE) {
            val errorContent = jsonFileReader.readJSONFromAsset(jsonFileName)
            val responseBody =
                ResponseBody.create("application/json".toMediaTypeOrNull(), errorContent)
            val response = Response.error<String>(404, responseBody)
            throw HttpException(response)
        }
        return Gson()
            .fromJson(
                jsonFileReader.readJSONFromAsset(jsonFileName),
                object : TypeToken<LaunchDto>() {}.type
            )
    }
}