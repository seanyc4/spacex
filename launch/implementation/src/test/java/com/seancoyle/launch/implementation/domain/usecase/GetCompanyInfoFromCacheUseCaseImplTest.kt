package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.domain.UsecaseResponses.EVENT_CACHE_SUCCESS
import com.seancoyle.core.testing.MainCoroutineRule
import com.seancoyle.launch.api.data.CompanyInfoCacheDataSource
import com.seancoyle.launch.api.domain.model.CompanyInfo
import com.seancoyle.launch.api.domain.usecase.GetCompanyInfoFromCacheUseCase
import com.seancoyle.launch.implementation.domain.GetCompanyInfoFromCacheUseCaseImpl
import com.seancoyle.launch.implementation.presentation.LaunchEvents
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetCompanyInfoFromCacheUseCaseImplTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var cacheDataSource: CompanyInfoCacheDataSource

    private lateinit var underTest: GetCompanyInfoFromCacheUseCase

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        underTest = GetCompanyInfoFromCacheUseCaseImpl(
            ioDispatcher = mainCoroutineRule.testDispatcher,
            cacheDataSource = cacheDataSource
        )
    }

    @Test
    fun getCompanyInfoFromCache_success_confirmCorrect() = runBlocking {

        var result: CompanyInfo? = null
        var stateMessage: String? = null
        coEvery { cacheDataSource.getCompanyInfo() } returns COMPANY_INFO

        underTest(
            event = LaunchEvents.GetCompanyInfoFromCacheEvent
        ).collect { value ->
            result = value?.data?.company
            stateMessage = value?.stateMessage?.response?.message
        }

        val expectedMessage = LaunchEvents.GetCompanyInfoFromCacheEvent.eventName() + EVENT_CACHE_SUCCESS
        assertEquals(expectedMessage, stateMessage)
        assertNotNull(result)
    }

    companion object {
        private val COMPANY_INFO = CompanyInfo(
            id = "1",
            employees = "employees",
            founded = 2000,
            founder = "founder",
            launchSites = 4,
            name = "name",
            valuation = "valuation"
        )
    }
}