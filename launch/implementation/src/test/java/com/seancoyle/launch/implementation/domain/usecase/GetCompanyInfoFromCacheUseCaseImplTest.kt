package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.testing.MainCoroutineRule
import com.seancoyle.launch.api.CompanyInfoCacheDataSource
import com.seancoyle.launch.api.model.CompanyInfoFactory
import com.seancoyle.launch.api.model.CompanyInfoModel
import com.seancoyle.launch.api.usecase.GetCompanyInfoFromCacheUseCase
import com.seancoyle.launch.implementation.domain.CompanyDependencies
import com.seancoyle.launch.implementation.domain.GetCompanyInfoFromCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.GetCompanyInfoFromCacheUseCaseImpl.Companion.GET_COMPANY_INFO_SUCCESS
import com.seancoyle.launch.implementation.presentation.LaunchEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetCompanyInfoFromCacheUseCaseImplTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val dependencies: CompanyDependencies = CompanyDependencies()
    private lateinit var cacheDataSource: CompanyInfoCacheDataSource
    private lateinit var infoFactory: CompanyInfoFactory
    private lateinit var underTest: GetCompanyInfoFromCacheUseCase

    @BeforeEach
    fun setup() {
        dependencies.build()
        cacheDataSource = dependencies.companyInfoCacheDataSource
        infoFactory = dependencies.companyInfoFactory
        underTest = GetCompanyInfoFromCacheUseCaseImpl(
            ioDispatcher = mainCoroutineRule.testDispatcher,
            cacheDataSource = cacheDataSource
        )
    }

    @Test
    fun getCompanyInfoFromCache_success_confirmCorrect() = runBlocking {

        var result: CompanyInfoModel? = null

        underTest(
            stateEvent = LaunchEvent.GetCompanyInfoFromCacheEvent
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                GET_COMPANY_INFO_SUCCESS
            )

            value?.data?.company?.let { companyInfo ->
                result = companyInfo
            }
        }

        assertTrue(result != null)
    }
}
















