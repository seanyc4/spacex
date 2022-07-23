package com.seancoyle.launch_datasource

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seancoyle.launch_models.model.company.CompanyInfoModel

class CompanyInfoDataFactory(
    private val testClassLoader: ClassLoader
) {

    fun produceCompanyInfo(): CompanyInfoModel {
        return Gson()
            .fromJson(
                getDataFromFile("company_info.json"),
                object : TypeToken<CompanyInfoModel>() {}.type
            )
    }

    private fun getDataFromFile(fileName: String): String {
        return testClassLoader.getResource(fileName).readText()
    }

}


















