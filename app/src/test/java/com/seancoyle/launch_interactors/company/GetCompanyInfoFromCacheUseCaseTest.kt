package com.seancoyle.launch_interactors.company

import com.seancoyle.launch_datasource.cache.abstraction.company.CompanyInfoCacheDataSource
import com.seancoyle.launch_models.model.company.CompanyInfoModel
import com.seancoyle.launch_models.model.company.CompanyInfoFactory
import com.seancoyle.launch_interactors.company.GetCompanyInfoFromCacheUseCase.Companion.GET_COMPANY_INFO_SUCCESS
import com.seancoyle.spacex.di.CompanyDependencies
import com.seancoyle.ui_launch.state.LaunchStateEvent.*
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
class GetCompanyInfoFromCacheUseCaseTest {

    // system in test
    private val getCompanyInfo: GetCompanyInfoFromCacheUseCase

    // dependencies
    private val dependencies: CompanyDependencies = CompanyDependencies()
    private val cacheDataSource: CompanyInfoCacheDataSource
    private val infoFactory: CompanyInfoFactory


    init {
        dependencies.build()
        cacheDataSource = dependencies.companyInfoCacheDataSource
        infoFactory = dependencies.companyInfoFactory
        getCompanyInfo = GetCompanyInfoFromCacheUseCase(
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
















