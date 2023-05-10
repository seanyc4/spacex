package com.seancoyle.launch.implementation.domain

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seancoyle.launch.api.domain.model.CompanyInfoModel
import com.seancoyle.launch.implementation.data.cache.FakeCompanyInfoDatabase

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


















