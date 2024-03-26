package com.seancoyle.launch.implementation.data

import com.seancoyle.launch.api.domain.model.Company
import com.seancoyle.launch.implementation.domain.cache.CompanyCacheDataSource

const val FORCE_NEW_COMPANY_INFO_EXCEPTION = "FORCE_NEW_COMPANY_INFO_EXCEPTION"
const val FORCE_GENERAL_FAILURE = "FORCE_GENERAL_FAILURE"

class FakeCompanyCacheDataSourceImpl(
    private val fakeCompanyInfoDatabase: FakeCompanyInfoDatabase
) : CompanyCacheDataSource {

    override suspend fun insert(company: Company): Long {
        if (company.id == FORCE_NEW_COMPANY_INFO_EXCEPTION) {
            throw Exception("Something went wrong inserting company info.")
        }
        if (company.id == FORCE_GENERAL_FAILURE) {
            return -1 // fail
        }
        fakeCompanyInfoDatabase.company = company
        return 1 // success
    }


    override suspend fun getCompany(): Company? {
        return fakeCompanyInfoDatabase.company
    }

    override suspend fun deleteAll() {
         fakeCompanyInfoDatabase.company = null
    }
}