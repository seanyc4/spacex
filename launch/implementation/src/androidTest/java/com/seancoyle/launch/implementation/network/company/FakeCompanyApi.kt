package com.seancoyle.launch.implementation.network.company

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seancoyle.core.testing.JsonFileReader
import com.seancoyle.launch.implementation.TestConstants.COMPANY_200_RESPONSE
import com.seancoyle.launch.implementation.TestConstants.ERROR_404_RESPONSE
import com.seancoyle.launch.implementation.data.network.CompanyApi
import com.seancoyle.launch.implementation.data.network.dto.CompanyDto
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

internal class FakeCompanyApi @Inject constructor(
    private val jsonFileReader: JsonFileReader
) : CompanyApi {

    var jsonFileName: String = COMPANY_200_RESPONSE

    override suspend fun getCompany(): CompanyDto {
        if (jsonFileName == ERROR_404_RESPONSE) {
            val errorContent = jsonFileReader.readJSONFromAsset(jsonFileName)
            val responseBody = ResponseBody.create("application/json".toMediaTypeOrNull(), errorContent)
            val response = Response.error<String>(404, responseBody)
            throw HttpException(response)
        }
        return Gson()
            .fromJson(
                jsonFileReader.readJSONFromAsset(jsonFileName),
                object : TypeToken<CompanyDto>() {}.type
            )
    }
}