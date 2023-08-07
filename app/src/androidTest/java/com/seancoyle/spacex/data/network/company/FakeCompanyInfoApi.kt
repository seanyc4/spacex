package com.seancoyle.spacex.data.network.company

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seancoyle.core.testing.JsonFileReader
import com.seancoyle.launch.implementation.data.model.CompanyInfoDto
import com.seancoyle.launch.implementation.data.network.CompanyInfoApi
import javax.inject.Singleton

const val COMPANY_INFO_RAW_JSON_FILE_NAME = "company_info_raw.json"

@Singleton
class FakeCompanyInfoApi(
    private val jsonFileReader: JsonFileReader
) : CompanyInfoApi {

    override suspend fun getCompanyInfo(): CompanyInfoDto {
        return Gson()
            .fromJson(
                jsonFileReader.readJSONFromAsset(COMPANY_INFO_RAW_JSON_FILE_NAME),
                object : TypeToken<CompanyInfoDto>() {}.type
            )
    }
}