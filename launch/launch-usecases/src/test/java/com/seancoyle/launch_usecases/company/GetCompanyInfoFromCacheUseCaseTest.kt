package com.seancoyle.launch_usecases.company

import com.seancoyle.launch_datasource.cache.abstraction.company.CompanyInfoCacheDataSource
import com.seancoyle.launch_datasource_test.CompanyDependencies
import com.seancoyle.launch_models.model.company.CompanyInfoModel
import com.seancoyle.launch_models.model.company.CompanyInfoFactory
import com.seancoyle.launch_usecases.company.GetCompanyInfoFromCacheUseCase.Companion.GET_COMPANY_INFO_SUCCESS
import com.seancoyle.launch_viewstate.LaunchStateEvent
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach

/*
Test cases:
1. getCompanyInfoFromCache_success_confirmCorrect()
    a) query the cache to return the company info from the table.
    b) listen for GET_COMPANY_INFO_SUCCESS from flow emission
    c) check the company info is not null
*/

class GetCompanyInfoFromCacheUseCaseTest {

    // system in test
    private lateinit var getCompanyInfo: GetCompanyInfoFromCacheUseCase

    // dependencies
    private val dependencies: CompanyDependencies = CompanyDependencies()
    private lateinit var cacheDataSource: CompanyInfoCacheDataSource
    private lateinit var infoFactory: CompanyInfoFactory


    @BeforeEach
    fun setup() {
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

        getCompanyInfo(
            stateEvent = LaunchStateEvent.GetCompanyInfoFromCacheEvent
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
        assertTrue(result != null)

    }

}
















