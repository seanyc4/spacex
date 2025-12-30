package com.seancoyle.feature.launch.data.remote

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.LaunchesConstants
import com.seancoyle.feature.launch.data.repository.LaunchesRemoteDataSource
import com.seancoyle.feature.launch.domain.model.LaunchesQuery
import com.seancoyle.feature.launch.domain.model.LaunchStatus
import com.seancoyle.feature.launch.domain.model.LaunchesType
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

class LaunchesRemoteDataSourceImplTest {

    @MockK
    private lateinit var api: LaunchApi

    @RelaxedMockK
    private lateinit var crashlytics: Crashlytics

    private lateinit var underTest: LaunchesRemoteDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = LaunchesRemoteDataSourceImpl(
            api = api,
            crashlytics = crashlytics
        )
    }

    @Test
    fun `getLaunches returns upcoming launches when API call is successful`() = runTest {
        val page = 0
        val launchesQuery = LaunchesQuery(
            query = "Falcon",
            launchesType = LaunchesType.UPCOMING
        )
        val offset = 0
        coEvery {
            api.getUpcomingLaunches(
                offset = offset,
                search = launchesQuery.query
            )
        } returns TestData.createLaunchesDto()

        val result = underTest.getLaunches(page, launchesQuery)

        assertTrue(result is LaunchResult.Success)

        val actualLaunch = result.data[0]
        assertEquals("faf4a0bc-7dad-4842-b74c-73a9f648b5cc", actualLaunch.id)
        assertEquals(
            "https://lldev.thespacedevs.com/2.3.0/launches/faf4a0bc-7dad-4842-b74c-73a9f648b5cc/",
            actualLaunch.url
        )
        assertEquals("Falcon 9 Block 5 | Starlink Group 15-12", actualLaunch.name)
        assertEquals("list", actualLaunch.responseMode)
        assertEquals(LaunchStatus.SUCCESS.text, actualLaunch.status.text)
        assertEquals("2025-12-05T18:39:36Z", actualLaunch.lastUpdated)
        assertEquals("2025-12-13T05:34:00Z", actualLaunch.net)
        assertEquals("Minute", actualLaunch.netPrecision?.name)
        assertEquals("2025-12-13T09:34:00Z", actualLaunch.windowEnd)
        assertEquals("2025-12-13T05:34:00Z", actualLaunch.windowStart)
        assertEquals("Starlink night fairing", actualLaunch.image.name)
        assertEquals(false, actualLaunch.webcastLive)
    }

    @Test
    fun `getLaunches returns past launches when API call is successful`() = runTest {
        val page = 0
        val launchesQuery = LaunchesQuery(
            query = "Falcon",
            launchesType = LaunchesType.PAST
        )
        val offset = 0
        coEvery {
            api.getPreviousLaunches(
                offset = offset,
                search = launchesQuery.query
            )
        } returns TestData.createLaunchesDto()

        val result = underTest.getLaunches(page, launchesQuery)

        assertTrue(result is LaunchResult.Success)

        val actualLaunch = result.data[0]
        assertEquals("faf4a0bc-7dad-4842-b74c-73a9f648b5cc", actualLaunch.id)
    }

    @Test
    fun `getLaunches returns error when API call fails for upcoming launches`() = runTest {
        val page = 0
        val launchesQuery = LaunchesQuery(
            query = "",
            launchesType = LaunchesType.UPCOMING
        )
        val offset = 0
        val exception = RuntimeException("Network Failure")
        coEvery {
            api.getUpcomingLaunches(
                offset = offset,
                search = launchesQuery.query
            )
        } throws exception

        val result = underTest.getLaunches(page, launchesQuery)

        assertTrue(result is LaunchResult.Error)
        assertEquals(exception, result.error)
    }

    @Test
    fun `getLaunches returns error when API call fails for past launches`() = runTest {
        val page = 0
        val launchesQuery = LaunchesQuery(
            query = "",
            launchesType = LaunchesType.PAST
        )
        val offset = 0
        val exception = RuntimeException("Network Failure")
        coEvery {
            api.getPreviousLaunches(
                offset = offset,
                search = launchesQuery.query
            )
        } throws exception

        val result = underTest.getLaunches(page, launchesQuery)

        assertTrue(result is LaunchResult.Error)
        assertEquals(exception, result.error)
    }

    @Test
    fun `getLaunches calculates correct offset for pagination`() = runTest {
        val page = 2
        val launchesQuery = LaunchesQuery(
            query = "SpaceX",
            launchesType = LaunchesType.UPCOMING
        )
        val expectedOffset = LaunchesConstants.PAGINATION_LIMIT * 2 // page 2 * PAGINATION_LIMIT (20)
        coEvery {
            api.getUpcomingLaunches(
                offset = expectedOffset,
                search = launchesQuery.query
            )
        } returns TestData.createLaunchesDto()

        val result = underTest.getLaunches(page, launchesQuery)

        assertTrue(result is LaunchResult.Success)
    }

    @Test
    fun `getLaunches passes query parameters correctly for upcoming launches`() = runTest {
        val page = 1
        val launchesQuery = LaunchesQuery(
            query = "Dragon",
            launchesType = LaunchesType.UPCOMING
        )
        val expectedOffset = LaunchesConstants.PAGINATION_LIMIT // page 1 * PAGINATION_LIMIT (20)
        coEvery {
            api.getUpcomingLaunches(
                offset = expectedOffset,
                search = "Dragon"
            )
        } returns TestData.createLaunchesDto()

        val result = underTest.getLaunches(page, launchesQuery)

        assertTrue(result is LaunchResult.Success)
    }

    @Test
    fun `getLaunches passes query parameters correctly for past launches`() = runTest {
        val page = 1
        val launchesQuery = LaunchesQuery(
            query = "Dragon",
            launchesType = LaunchesType.PAST
        )
        val expectedOffset = LaunchesConstants.PAGINATION_LIMIT // page 1 * PAGINATION_LIMIT (20)
        coEvery {
            api.getPreviousLaunches(
                offset = expectedOffset,
                search = "Dragon"
            )
        } returns TestData.createLaunchesDto()

        val result = underTest.getLaunches(page, launchesQuery)

        assertTrue(result is LaunchResult.Success)
    }
}
