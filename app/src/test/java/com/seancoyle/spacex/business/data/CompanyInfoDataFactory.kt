package com.seancoyle.spacex.business.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seancoyle.spacex.business.data.cache.company.FakeCompanyInfoDatabase
import com.seancoyle.spacex.business.domain.model.company.CompanyInfoDomainEntity

class CompanyInfoDataFactory(
    private val testClassLoader: ClassLoader
) {

    fun produceCompanyInfo(): CompanyInfoDomainEntity {
        return Gson()
            .fromJson(
                getDataFromFile("company_info.json"),
                object : TypeToken<CompanyInfoDomainEntity>() {}.type
            )
    }

    fun produceFakeCompanyInfoDatabase(companyInfo: CompanyInfoDomainEntity): FakeCompanyInfoDatabase {
        val db = FakeCompanyInfoDatabase()
        db.companyInfo = companyInfo
        return db
    }

    private fun getDataFromFile(fileName: String): String {
        return testClassLoader.getResource(fileName).readText()
    }

}


















