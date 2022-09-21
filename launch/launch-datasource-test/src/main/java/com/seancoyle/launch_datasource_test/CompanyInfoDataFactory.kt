package com.seancoyle.launch_datasource_test

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seancoyle.launch_datasource_test.cache.company.FakeCompanyInfoDatabase
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

    fun produceFakeCompanyInfoDatabase(companyInfo: CompanyInfoModel): FakeCompanyInfoDatabase {
        val db = FakeCompanyInfoDatabase()
        db.companyInfo = companyInfo
        return db
    }

    private fun getDataFromFile(fileName: String): String {
        return testClassLoader.getResource(fileName).readText()
    }

}


















