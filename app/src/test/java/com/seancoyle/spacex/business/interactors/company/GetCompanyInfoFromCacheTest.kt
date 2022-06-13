package com.seancoyle.spacex.business.interactors.company

import com.seancoyle.spacex.business.data.cache.abstraction.company.CompanyInfoCacheDataSource
import com.seancoyle.launch_domain.model.company.CompanyInfoModel
import com.seancoyle.launch_domain.model.company.CompanyInfoFactory
import com.seancoyle.spacex.business.interactors.company.GetCompanyInfoFromCache.Companion.GET_COMPANY_INFO_SUCCESS
import com.seancoyle.spacex.di.CompanyDependencies
import com.seancoyle.spacex.framework.presentation.launch.state.LaunchStateEvent.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/*
Test cases:
1. getCompanyInfoFromCache_success_confirmCorrect()
    a) query the cache to return the company info from the table.
    b) listen for GET_COMPANY_INFO_SUCCESS from flow emission
    c) check the company info is not null
*/
@InternalCoroutinesApi
class GetCompanyInfoFromCacheTest {

    // system in test
    private val getCompanyInfo: GetCompanyInfoFromCache

    // dependencies
    private val dependencies: CompanyDependencies = CompanyDependencies()
    private val cacheDataSource: CompanyInfoCacheDataSource
    private val infoFactory: CompanyInfoFactory


    init {
        dependencies.build()
        cacheDataSource = dependencies.companyInfoCacheDataSource
        infoFactory = dependencies.companyInfoFactory
        getCompanyInfo = GetCompanyInfoFromCache(
            cacheDataSource = cacheDataSource
        )
    }

    @Test
    fun getCompanyInfoFromCache_success_confirmCorrect() = runBlocking {

        var result: CompanyInfoModel? = null

        getCompanyInfo.execute(
            stateEvent = GetCompanyInfoFromCacheEvent
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                GET_COMPANY_INFO_SUCCESS
            )

            value?.data?.company?.let { companyInfo ->
                result = companyInfo
            }
        }

        // confirm company info was was retrieved
        assertTrue { result != null }

    }

}
















