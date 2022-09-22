package com.seancoyle.launch_usecases.company

import com.seancoyle.core.cache.CacheErrors
import com.seancoyle.launch_datasource.cache.abstraction.company.CompanyInfoCacheDataSource
import com.seancoyle.launch_datasource_test.CompanyDependencies
import com.seancoyle.launch_datasource_test.cache.company.FORCE_GENERAL_FAILURE
import com.seancoyle.launch_datasource_test.cache.company.FORCE_NEW_COMPANY_INFO_EXCEPTION
import com.seancoyle.launch_models.model.company.CompanyInfoFactory
import com.seancoyle.launch_usecases.company.InsertCompanyInfoToCacheUseCase.Companion.INSERT_COMPANY_INFO_SUCCESS
import com.seancoyle.launch_viewstate.LaunchStateEvent
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

/*
Test cases:
1. insertCompanyInfo_success()
    a) insert new company info
    b) listen for INSERT_COMPANY_INFO_SUCCESS emission from flow
    c) confirm cache was updated with new company info
2. insertCompanyInfo_fail()
    a) insert new company info
    b) force a failure (return -1 from db operation)
    c) listen for INSERT_COMPANY_INFO_FAILED emission from flow
    e) confirm cache was not updated
3. throwException_checkGenericError()
    a) insert new company info
    b) force an exception
    c) listen for CACHE_ERROR_UNKNOWN emission from flow
    e) confirm cache was not updated
 */

class InsertCompanyInfoToCacheUseCaseTest {

    // system in test
    private lateinit var insertCompanyInfoToCacheUseCase: InsertCompanyInfoToCacheUseCase

    // dependencies
    private val dependencies: CompanyDependencies = CompanyDependencies()
    private lateinit var cacheDataSource: CompanyInfoCacheDataSource
    private lateinit var infoFactory: CompanyInfoFactory

    @BeforeEach
    fun setup() {
        dependencies.build()
        cacheDataSource = dependencies.companyInfoCacheDataSource
        infoFactory = dependencies.companyInfoFactory
        insertCompanyInfoToCacheUseCase = InsertCompanyInfoToCacheUseCase(
            cacheDataSource = cacheDataSource,
            factory = infoFactory
        )
    }

    @Test
    fun insertCompanyInfo_success() = runBlocking {

        val newCompanyInfo = infoFactory.createCompanyInfo(
            id = UUID.randomUUID().toString(),
            employees = UUID.randomUUID().toString(),
            founded = UUID.randomUUID().hashCode(),
            founder = UUID.randomUUID().toString(),
            launchSites = UUID.randomUUID().hashCode(),
            name = UUID.randomUUID().toString(),
            valuation = UUID.randomUUID().hashCode().toString(),
        )

        insertCompanyInfoToCacheUseCase(
            companyInfo = newCompanyInfo,
            stateEvent = LaunchStateEvent.InsertCompanyInfoToCacheEvent(
                companyInfo = newCompanyInfo
            )
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                INSERT_COMPANY_INFO_SUCCESS
            )
        }

        // confirm cache was updated
        val cachedCompanyInfo = cacheDataSource.getCompanyInfo()
        assertTrue { cachedCompanyInfo == newCompanyInfo }
    }

    @Test
    fun insertCompanyInfo_fail() = runBlocking {

        val newCompanyInfo = infoFactory.createCompanyInfo(
            id = FORCE_GENERAL_FAILURE,
            employees = UUID.randomUUID().toString(),
            founded = UUID.randomUUID().hashCode(),
            founder = UUID.randomUUID().toString(),
            launchSites = UUID.randomUUID().hashCode(),
            name = UUID.randomUUID().toString(),
            valuation = UUID.randomUUID().hashCode().toString(),
        )

        insertCompanyInfoToCacheUseCase(
            companyInfo = newCompanyInfo,
            stateEvent = LaunchStateEvent.InsertCompanyInfoToCacheEvent(
                companyInfo = newCompanyInfo
            )
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                InsertCompanyInfoToCacheUseCase.INSERT_COMPANY_INFO_FAILED
            )
        }

        // confirm new data does not match whats in the db
        val cachedCompanyData = cacheDataSource.getCompanyInfo()
        assertTrue { cachedCompanyData != newCompanyInfo }
    }

    @Test
    fun throwException_checkGenericError() = runBlocking {

        val newCompanyInfo = infoFactory.createCompanyInfo(
            id = FORCE_NEW_COMPANY_INFO_EXCEPTION,
            employees = UUID.randomUUID().toString(),
            founded = UUID.randomUUID().hashCode(),
            founder = UUID.randomUUID().toString(),
            launchSites = UUID.randomUUID().hashCode(),
            name = UUID.randomUUID().toString(),
            valuation = UUID.randomUUID().hashCode().toString(),
        )

        insertCompanyInfoToCacheUseCase(
            companyInfo = newCompanyInfo,
            stateEvent = LaunchStateEvent.InsertCompanyInfoToCacheEvent(
                companyInfo = newCompanyInfo
            )
        ).collect { value ->
            assert(
                value?.stateMessage?.response?.message
                    ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN) ?: false
            )
        }

        // confirm new data does not match whats in the db
        val cachedCompanyInfo = cacheDataSource.getCompanyInfo()
        assertTrue { cachedCompanyInfo != newCompanyInfo }
    }
}





















