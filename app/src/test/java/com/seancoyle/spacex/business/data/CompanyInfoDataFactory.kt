package com.seancoyle.spacex.business.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seancoyle.spacex.business.data.cache.company.FakeCompanyInfoDatabase
import com.seancoyle.spacex.business.domain.model.company.CompanyInfoModel

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


















