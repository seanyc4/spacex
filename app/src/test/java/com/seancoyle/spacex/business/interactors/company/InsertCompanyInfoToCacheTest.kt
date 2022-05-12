package com.seancoyle.spacex.business.interactors.company

import com.seancoyle.spacex.business.data.cache.CacheErrors
import com.seancoyle.spacex.business.data.cache.abstraction.company.CompanyInfoCacheDataSource
import com.seancoyle.spacex.business.data.cache.company.FORCE_NEW_COMPANY_INFO_EXCEPTION
import com.seancoyle.spacex.business.domain.model.company.CompanyInfoFactory
import com.seancoyle.spacex.business.interactors.company.InsertCompanyInfoToCache.Companion.INSERT_COMPANY_INFO_SUCCESS
import com.seancoyle.spacex.di.DependencyContainer
import com.seancoyle.spacex.framework.presentation.launch.state.LaunchStateEvent
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
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
@InternalCoroutinesApi
class InsertCompanyInfoToCacheTest {

    // system in test
    private val insertCompanyInfoToCache: InsertCompanyInfoToCache

    // dependencies
    private val dependencyContainer: DependencyContainer = DependencyContainer()
    private val cacheDataSource: CompanyInfoCacheDataSource
    private val infoFactory: CompanyInfoFactory

    init {
        dependencyContainer.build()
        cacheDataSource = dependencyContainer.companyInfoCacheDataSource
        infoFactory = dependencyContainer.companyInfoFactory
        insertCompanyInfoToCache = InsertCompanyInfoToCache(
            cacheDataSource = cacheDataSource,
            factory = infoFactory
        )
    }

    @Test
    fun insertCompanyInfo_success() = runBlocking {

        val newCompanyInfo = infoFactory.createCompanyInfo(
            id = "1",
            employees = UUID.randomUUID().hashCode(),
            founded = UUID.randomUUID().hashCode(),
            founder = UUID.randomUUID().toString(),
            launchSites = UUID.randomUUID().hashCode(),
            name = UUID.randomUUID().toString(),
            valuation = UUID.randomUUID().hashCode().toLong(),
        )

        insertCompanyInfoToCache.execute(
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
            id = com.seancoyle.spacex.business.data.cache.company.FORCE_GENERAL_FAILURE,
            employees = UUID.randomUUID().hashCode(),
            founded = UUID.randomUUID().hashCode(),
            founder = UUID.randomUUID().toString(),
            launchSites = UUID.randomUUID().hashCode(),
            name = UUID.randomUUID().toString(),
            valuation = UUID.randomUUID().hashCode().toLong(),
        )

        insertCompanyInfoToCache.execute(
            companyInfo = newCompanyInfo,
            stateEvent = LaunchStateEvent.InsertCompanyInfoToCacheEvent(
                companyInfo = newCompanyInfo
            )
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                InsertCompanyInfoToCache.INSERT_COMPANY_INFO_FAILED
            )
        }

        // confirm cache was not changed
        val cachedCompanyData = cacheDataSource.getCompanyInfo()
        assertTrue { cachedCompanyData == null }
    }

    @Test
    fun throwException_checkGenericError() = runBlocking {

        val newCompanyInfo = infoFactory.createCompanyInfo(
            id = FORCE_NEW_COMPANY_INFO_EXCEPTION,
            employees = UUID.randomUUID().hashCode(),
            founded = UUID.randomUUID().hashCode(),
            founder = UUID.randomUUID().toString(),
            launchSites = UUID.randomUUID().hashCode(),
            name = UUID.randomUUID().toString(),
            valuation = UUID.randomUUID().hashCode().toLong(),
        )

        insertCompanyInfoToCache.execute(
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

        // confirm cache was not changed
        val cachedCompanyInfo = cacheDataSource.getCompanyInfo()
        assertTrue { cachedCompanyInfo == null }
    }
}





















