package com.seancoyle.launch_datasource_test.network.company

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seancoyle.core.testing.JsonFileReader
import com.seancoyle.launch_datasource.network.api.company.CompanyInfoApi
import com.seancoyle.launch_datasource.network.model.company.CompanyInfoDto

const val COMPANY_INFO_RAW_JSON_FILE_NAME = "company_info_raw.json"


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