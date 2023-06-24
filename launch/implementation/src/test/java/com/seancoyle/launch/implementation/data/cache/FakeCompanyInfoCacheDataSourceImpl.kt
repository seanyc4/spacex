package com.seancoyle.launch.implementation.data.cache

import com.seancoyle.launch.api.data.CompanyInfoCacheDataSource
import com.seancoyle.launch.api.domain.model.CompanyInfo

const val FORCE_NEW_COMPANY_INFO_EXCEPTION = "FORCE_NEW_COMPANY_INFO_EXCEPTION"
const val FORCE_GENERAL_FAILURE = "FORCE_GENERAL_FAILURE"

class FakeCompanyInfoCacheDataSourceImpl
constructor(
    private val fakeCompanyInfoDatabase: FakeCompanyInfoDatabase
) : CompanyInfoCacheDataSource {

    override suspend fun insert(company: CompanyInfo): Long {
        if (company.id == FORCE_NEW_COMPANY_INFO_EXCEPTION) {
            throw Exception("Something went wrong inserting company info.")
        }
        if (company.id == FORCE_GENERAL_FAILURE) {
            return -1 // fail
        }
        fakeCompanyInfoDatabase.companyInfo = company
        return 1 // success
    }


    override suspend fun getCompanyInfo(): CompanyInfo? {
        return fakeCompanyInfoDatabase.companyInfo
    }

    override suspend fun deleteAll() {
         fakeCompanyInfoDatabase.companyInfo = null
    }
}















