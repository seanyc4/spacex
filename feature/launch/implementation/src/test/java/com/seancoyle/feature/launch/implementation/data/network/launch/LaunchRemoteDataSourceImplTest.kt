package com.seancoyle.feature.launch.implementation.data.network.launch

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.test.TestCoroutineRule
import com.seancoyle.feature.launch.implementation.data.repository.launch.LaunchRemoteDataSource
import com.seancoyle.feature.launch.implementation.util.TestData.launchOptions
import com.seancoyle.feature.launch.implementation.util.TestData.launchesDto
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class LaunchRemoteDataSourceImplTest {

    @get:Rule
    val testDispatcher = TestCoroutineRule()

    @MockK
    private lateinit var api: LaunchApiService

    @RelaxedMockK
    private lateinit var crashlytics: Crashlytics

    private lateinit var underTest: LaunchRemoteDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = LaunchRemoteDataSourceImpl(
            api = api,
            crashlytics = crashlytics,
            ioDispatcher = testDispatcher.testCoroutineDispatcher
        )
    }

    @Test
    fun `getLaunches returns launches DTO when API call is successful`() = runTest {
        coEvery { api.getLaunches(launchOptions) } returns launchesDto

        val result = underTest.getLaunches(launchOptions)

        assertTrue(result.isSuccess)
        assertEquals(launchesDto, result.getOrNull())
    }

    @Test
    fun `getLaunches returns DataError when API call fails`() = runTest {
        val exception = RuntimeException("Network Failure")
        coEvery { api.getLaunches(launchOptions) } throws exception

        val result = underTest.getLaunches(launchOptions)

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
