package com.seancoyle.feature.launch.implementation.data.network

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.implementation.domain.model.LaunchStatus
import com.seancoyle.feature.launch.implementation.data.remote.LaunchApi
import com.seancoyle.feature.launch.implementation.data.remote.LaunchRemoteDataSourceImpl
import com.seancoyle.feature.launch.implementation.data.repository.LaunchRemoteDataSource
import com.seancoyle.feature.launch.implementation.util.TestData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LaunchRemoteDataSourceImplTest {

    @MockK
    private lateinit var api: LaunchApi

    @RelaxedMockK
    private lateinit var crashlytics: Crashlytics

    private lateinit var underTest: LaunchRemoteDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = LaunchRemoteDataSourceImpl(
            api = api,
            crashlytics = crashlytics
        )
    }

    @Test
    fun `getLaunches returns launches when API call is successful`() = runTest {
        val offset = 0
        coEvery { api.getUpcomingLaunches(offset) } returns TestData.createLaunchesDto()

        val result = underTest.getLaunches(offset)

        assertTrue(result is LaunchResult.Success)

        val actualLaunch = result.data[0]
        assertEquals("faf4a0bc-7dad-4842-b74c-73a9f648b5cc", actualLaunch.id)
        assertEquals("https://lldev.thespacedevs.com/2.3.0/launches/faf4a0bc-7dad-4842-b74c-73a9f648b5cc/", actualLaunch.url)
        assertEquals("Falcon 9 Block 5 | Starlink Group 15-12", actualLaunch.name)
        assertEquals("list", actualLaunch.responseMode)
        assertEquals(LaunchStatus.SUCCESS.name, actualLaunch.launchStatus.name)
        assertEquals("2025-12-05T18:39:36Z", actualLaunch.lastUpdated)
        assertEquals("2025-12-13T05:34:00Z", actualLaunch.net)
        assertEquals("Minute", actualLaunch.netPrecision?.name)
        assertEquals("2025-12-13T09:34:00Z", actualLaunch.windowEnd)
        assertEquals("2025-12-13T05:34:00Z", actualLaunch.windowStart)
        assertEquals("Starlink night fairing", actualLaunch.image?.name)
        assertEquals(false, actualLaunch.webcastLive)

        // Verify mapped fields that are set by RemoteMappingExtensions
        assertEquals("2025-12-13T05:34:00Z", actualLaunch.launchDate)
        assertEquals(null, actualLaunch.launchYear)
        assertEquals(null, actualLaunch.launchDateStatus)
        assertEquals(null, actualLaunch.launchDays)
        assertEquals(null, actualLaunch.launchDateLocalDateTime)
    }

    @Test
    fun `getLaunches returns DataError when API call fails`() = runTest {
        val offset = 0
        val exception = RuntimeException("Network Failure")
        val expected = DataError.RemoteError.NETWORK_UNKNOWN_ERROR
        coEvery { api.getUpcomingLaunches(offset) } throws exception

        val result = underTest.getLaunches(offset)

        assertTrue(result is LaunchResult.Error)
        assertEquals(expected, result.error)
    }
}
