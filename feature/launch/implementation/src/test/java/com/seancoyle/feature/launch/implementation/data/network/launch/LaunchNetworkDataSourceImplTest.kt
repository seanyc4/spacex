package com.seancoyle.feature.launch.implementation.data.network.launch

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.result.DataSourceError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.test.TestCoroutineRule
import com.seancoyle.feature.launch.implementation.data.repository.launch.LaunchNetworkDataSource
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
class LaunchNetworkDataSourceImplTest {

    @get:Rule
    val testDispatcher = TestCoroutineRule()

    @MockK
    private lateinit var api: LaunchApiService

    @RelaxedMockK
    private lateinit var crashlytics: Crashlytics

    private lateinit var underTest: LaunchNetworkDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = LaunchNetworkDataSourceImpl(
            api = api,
            crashlytics = crashlytics,
            ioDispatcher = testDispatcher.testCoroutineDispatcher
        )
    }

    @Test
    fun `getLaunches returns launches DTO when API call is successful`() = runTest {
        coEvery { api.getLaunches(launchOptions) } returns launchesDto

        val result = underTest.getLaunches(launchOptions)

        assertTrue(result is LaunchResult.Success)
        assertEquals(launchesDto, result.data)
    }

    @Test
    fun `getLaunches returns DataError when API call fails`() = runTest {
        coEvery { api.getLaunches(launchOptions) } throws RuntimeException("Network failure")

        val result = underTest.getLaunches(launchOptions)

        assertTrue(result is LaunchResult.Error)
        assertEquals(DataSourceError.NETWORK_UNKNOWN_ERROR, result.error)
    }
}
