package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.cache.CacheErrors
import com.seancoyle.core.testing.MainCoroutineRule
import com.seancoyle.launch.api.CompanyInfoCacheDataSource
import com.seancoyle.launch.api.model.CompanyInfoFactory
import com.seancoyle.launch.api.usecase.InsertCompanyInfoToCacheUseCase
import com.seancoyle.launch.implementation.data.cache.FORCE_GENERAL_FAILURE
import com.seancoyle.launch.implementation.data.cache.FORCE_NEW_COMPANY_INFO_EXCEPTION
import com.seancoyle.launch.implementation.domain.CompanyDependencies
import com.seancoyle.launch.implementation.domain.InsertCompanyInfoToCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.InsertCompanyInfoToCacheUseCaseImpl.Companion.INSERT_COMPANY_INFO_SUCCESS
import com.seancoyle.launch.implementation.presentation.LaunchEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class InsertCompanyInfoToCacheUseCaseImplTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val dependencies: CompanyDependencies = CompanyDependencies()
    private lateinit var cacheDataSource: CompanyInfoCacheDataSource
    private lateinit var infoFactory: CompanyInfoFactory
    private lateinit var insertCompanyInfoToCacheUseCase: InsertCompanyInfoToCacheUseCase

    @BeforeEach
    fun setup() {
        dependencies.build()
        cacheDataSource = dependencies.companyInfoCacheDataSource
        infoFactory = dependencies.companyInfoFactory
        insertCompanyInfoToCacheUseCase =
            InsertCompanyInfoToCacheUseCaseImpl(
                ioDispatcher = mainCoroutineRule.testDispatcher,
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
            stateEvent = LaunchEvent.InsertCompanyInfoToCacheEvent(
                companyInfo = newCompanyInfo
            )
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                INSERT_COMPANY_INFO_SUCCESS
            )
        }

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
            stateEvent = LaunchEvent.InsertCompanyInfoToCacheEvent(
                companyInfo = newCompanyInfo
            )
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                InsertCompanyInfoToCacheUseCaseImpl.INSERT_COMPANY_INFO_FAILED
            )
        }

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
            stateEvent = LaunchEvent.InsertCompanyInfoToCacheEvent(
                companyInfo = newCompanyInfo
            )
        ).collect { value ->
            assert(
                value?.stateMessage?.response?.message
                    ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN) ?: false
            )
        }

        val cachedCompanyInfo = cacheDataSource.getCompanyInfo()
        assertTrue { cachedCompanyInfo != newCompanyInfo }
    }
}





















