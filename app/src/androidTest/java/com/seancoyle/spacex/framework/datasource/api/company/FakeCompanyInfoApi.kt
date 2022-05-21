package com.seancoyle.spacex.framework.datasource.api.company

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seancoyle.spacex.framework.datasource.network.api.company.CompanyInfoApi
import com.seancoyle.spacex.framework.datasource.network.model.company.CompanyInfoDto
import com.seancoyle.spacex.util.JsonFileReader
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