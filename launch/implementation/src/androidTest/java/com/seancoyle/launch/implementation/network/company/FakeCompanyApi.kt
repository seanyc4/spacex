package com.seancoyle.launch.implementation.network.company

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seancoyle.core.testing.JsonFileReader
import com.seancoyle.launch.implementation.data.network.CompanyApi
import com.seancoyle.launch.implementation.data.network.dto.CompanyInfoDto
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

const val COMPANY_200_RESPONSE = "company_200_response.json"
const val COMPANY_404_RESPONSE = "company_404_response.json"

internal class FakeCompanyApi @Inject constructor(
    private val jsonFileReader: JsonFileReader
) : CompanyApi {

    var jsonFileName: String = COMPANY_200_RESPONSE

    override suspend fun getCompanyInfo(): CompanyInfoDto {
        if (jsonFileName == COMPANY_404_RESPONSE) {
            val errorContent = jsonFileReader.readJSONFromAsset(jsonFileName)
            val responseBody = ResponseBody.create("application/json".toMediaTypeOrNull(), errorContent)
            val response = Response.error<String>(404, responseBody)
            throw HttpException(response)
        }
        return Gson()
            .fromJson(
                jsonFileReader.readJSONFromAsset(jsonFileName),
                object : TypeToken<CompanyInfoDto>() {}.type
            )
    }
}