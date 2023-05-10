package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.domain.UsecaseResponses.EVENT_CACHE_SUCCESS
import com.seancoyle.core.testing.MainCoroutineRule
import com.seancoyle.launch.api.data.CompanyInfoCacheDataSource
import com.seancoyle.launch.api.domain.model.CompanyInfoModel
import com.seancoyle.launch.api.domain.usecase.GetCompanyInfoFromCacheUseCase
import com.seancoyle.launch.implementation.domain.CompanyDependencies
import com.seancoyle.launch.implementation.domain.GetCompanyInfoFromCacheUseCaseImpl
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
    private lateinit var underTest: GetCompanyInfoFromCacheUseCase

    @BeforeEach
    fun setup() {
        dependencies.build()
        cacheDataSource = dependencies.companyInfoCacheDataSource
        underTest = GetCompanyInfoFromCacheUseCaseImpl(
            ioDispatcher = mainCoroutineRule.testDispatcher,
            cacheDataSource = cacheDataSource
        )
    }

    @Test
    fun getCompanyInfoFromCache_success_confirmCorrect() = runBlocking {

        var result: CompanyInfoModel? = null

        underTest(
            event = LaunchEvent.GetCompanyInfoFromCacheEvent
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                LaunchEvent.GetCompanyInfoFromCacheEvent.eventName() + EVENT_CACHE_SUCCESS
            )

            value?.data?.company?.let { companyInfo ->
                result = companyInfo
            }
        }

        assertTrue(result != null)
    }
}
















