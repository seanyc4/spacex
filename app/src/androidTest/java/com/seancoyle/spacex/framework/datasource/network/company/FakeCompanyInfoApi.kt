package com.seancoyle.spacex.framework.datasource.network.company

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seancoyle.core.testing.JsonFileReader
import com.seancoyle.launch.api.CompanyInfoApi
import com.seancoyle.launch.api.CompanyInfoDto
import javax.inject.Singleton

const val COMPANY_INFO_RAW_JSON_FILE_NAME = "company_info_raw.json"

@Singleton
class FakeCompanyInfoApi(
    private val jsonFileReader: JsonFileReader
) : com.seancoyle.launch.api.CompanyInfoApi {

    override suspend fun getCompanyInfo(): com.seancoyle.launch.api.CompanyInfoDto {
        return Gson()
            .fromJson(
                jsonFileReader.readJSONFromAsset(COMPANY_INFO_RAW_JSON_FILE_NAME),
                object : TypeToken<com.seancoyle.launch.api.CompanyInfoDto>() {}.type
            )
    }

}