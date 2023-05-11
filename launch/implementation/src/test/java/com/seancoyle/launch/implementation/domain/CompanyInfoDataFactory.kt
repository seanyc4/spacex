package com.seancoyle.launch.implementation.domain

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seancoyle.launch.api.domain.model.CompanyInfo
import com.seancoyle.launch.implementation.data.cache.FakeCompanyInfoDatabase

class CompanyInfoDataFactory(
    private val testClassLoader: ClassLoader
) {

    fun produceCompanyInfo(): CompanyInfo {
        return Gson()
            .fromJson(
                getDataFromFile("company_info.json"),
                object : TypeToken<CompanyInfo>() {}.type
            )
    }

    fun produceFakeCompanyInfoDatabase(companyInfo: CompanyInfo): FakeCompanyInfoDatabase {
        val db = FakeCompanyInfoDatabase()
        db.companyInfo = companyInfo
        return db
    }

    private fun getDataFromFile(fileName: String): String {
        return testClassLoader.getResource(fileName).readText()
    }

}


















