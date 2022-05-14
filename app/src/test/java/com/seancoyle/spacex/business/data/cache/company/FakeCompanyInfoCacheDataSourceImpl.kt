package com.seancoyle.spacex.business.data.cache.company

import com.seancoyle.spacex.business.data.cache.abstraction.company.CompanyInfoCacheDataSource
import com.seancoyle.spacex.business.domain.model.company.CompanyInfoDomainEntity

const val FORCE_NEW_COMPANY_INFO_EXCEPTION = "FORCE_NEW_COMPANY_INFO_EXCEPTION"
const val FORCE_GENERAL_FAILURE = "FORCE_GENERAL_FAILURE"

class FakeCompanyInfoCacheDataSourceImpl
constructor(
    private val fakeCompanyInfoDatabase: FakeCompanyInfoDatabase
) : CompanyInfoCacheDataSource {

    override suspend fun insert(company: CompanyInfoDomainEntity): Long {
        if (company.id == FORCE_NEW_COMPANY_INFO_EXCEPTION) {
            throw Exception("Something went wrong inserting company info.")
        }
        if (company.id == FORCE_GENERAL_FAILURE) {
            return -1 // fail
        }
        fakeCompanyInfoDatabase.companyInfo = company
        return 1 // success
    }


    override suspend fun getCompanyInfo(): CompanyInfoDomainEntity? {
        return fakeCompanyInfoDatabase.companyInfo
    }
}














