package com.seancoyle.feature.launch.data.remote

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.LaunchConstants
import com.seancoyle.feature.launch.data.repository.LaunchRemoteDataSource
import com.seancoyle.feature.launch.domain.model.LaunchQuery
import com.seancoyle.feature.launch.domain.model.LaunchStatus
import com.seancoyle.feature.launch.util.TestData
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
        val page = 0
        val launchQuery = LaunchQuery(query = "Falcon", order = Order.DESC)
        val offset = 0
        coEvery {
            api.getUpcomingLaunches(
                offset = offset,
                search = launchQuery.query,
                ordering = launchQuery.order.value
            )
        } returns TestData.createLaunchesDto()

        val result = underTest.getLaunches(page, launchQuery)

        assertTrue(result is LaunchResult.Success)

        val actualLaunch = result.data[0]
        assertEquals("faf4a0bc-7dad-4842-b74c-73a9f648b5cc", actualLaunch.id)
        assertEquals(
            "https://lldev.thespacedevs.com/2.3.0/launches/faf4a0bc-7dad-4842-b74c-73a9f648b5cc/",
            actualLaunch.url
        )
        assertEquals("Falcon 9 Block 5 | Starlink Group 15-12", actualLaunch.name)
        assertEquals("list", actualLaunch.responseMode)
        assertEquals(LaunchStatus.SUCCESS.name, actualLaunch.launchStatus.name)
        assertEquals("2025-12-05T18:39:36Z", actualLaunch.lastUpdated)
        assertEquals("2025-12-13T05:34:00Z", actualLaunch.net)
        assertEquals("Minute", actualLaunch.netPrecision?.name)
        assertEquals("2025-12-13T09:34:00Z", actualLaunch.windowEnd)
        assertEquals("2025-12-13T05:34:00Z", actualLaunch.windowStart)
        assertEquals("Starlink night fairing", actualLaunch.image.name)
        assertEquals(false, actualLaunch.webcastLive)

        // Verify mapped fields that are set by RemoteMappingExtensions
        assertEquals("2025-12-13T05:34:00Z", actualLaunch.launchDate)
        assertEquals(null, actualLaunch.launchYear)
        assertEquals(null, actualLaunch.launchDateStatus)
        assertEquals(null, actualLaunch.launchDays)
        assertEquals(null, actualLaunch.launchDateLocalDateTime)
    }

    @Test
    fun `getLaunches returns error when API call fails`() = runTest {
        val page = 0
        val launchQuery = LaunchQuery(query = "", order = Order.ASC)
        val offset = 0
        val exception = RuntimeException("Network Failure")
        coEvery {
            api.getUpcomingLaunches(
                offset = offset,
                search = launchQuery.query,
                ordering = launchQuery.order.value
            )
        } throws exception

        val result = underTest.getLaunches(page, launchQuery)

        assertTrue(result is LaunchResult.Error)
        assertEquals(exception, result.error)
    }

    @Test
    fun `getLaunches calculates correct offset for pagination`() = runTest {
        val page = 2
        val launchQuery = LaunchQuery(query = "SpaceX", order = Order.DESC)
        val expectedOffset = LaunchConstants.PAGINATION_LIMIT * 2 // page 2 * PAGINATION_LIMIT (20)
        coEvery {
            api.getUpcomingLaunches(
                offset = expectedOffset,
                search = launchQuery.query,
                ordering = launchQuery.order.value
            )
        } returns TestData.createLaunchesDto()

        val result = underTest.getLaunches(page, launchQuery)

        assertTrue(result is LaunchResult.Success)
    }

    @Test
    fun `getLaunches passes query parameters correctly`() = runTest {
        val page = 1
        val launchQuery = LaunchQuery(query = "Dragon", order = Order.ASC)
        val expectedOffset = LaunchConstants.PAGINATION_LIMIT // page 1 * PAGINATION_LIMIT (20)
        coEvery {
            api.getUpcomingLaunches(
                offset = expectedOffset,
                search = "Dragon",
                ordering = Order.ASC.value
            )
        } returns TestData.createLaunchesDto()

        val result = underTest.getLaunches(page, launchQuery)

        assertTrue(result is LaunchResult.Success)
    }
}
