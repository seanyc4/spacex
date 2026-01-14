package com.seancoyle.feature.launch.data.remote

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.feature.launch.data.repository.LaunchesRemoteDataSource
import com.seancoyle.feature.launch.domain.model.LaunchesQuery
import com.seancoyle.feature.launch.presentation.LaunchesConstants
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
    fun `getUpcomingDetailedLaunches returns summaries and details when API call is successful`() = runTest {
        val page = 0
        val launchesQuery = LaunchesQuery(query = "Falcon")
        val offset = 0
        coEvery {
            api.getUpcomingLaunches(
                offset = offset,
                search = launchesQuery.query,
                status = null
            )
        } returns TestData.createLaunchesDto()

        val result = underTest.getUpcomingDetailedLaunches(page, launchesQuery)

        assertTrue(result is LaunchResult.Success)
        val detailedResult = result.data
        assertTrue(detailedResult.summaries.isNotEmpty())
        assertTrue(detailedResult.details.isNotEmpty())

        val actualSummary = detailedResult.summaries[0]
        assertEquals("faf4a0bc-7dad-4842-b74c-73a9f648b5cc", actualSummary.id)
        assertEquals("Falcon 9 Block 5 | Starlink Group 15-12", actualSummary.missionName)
        assertEquals("Go for Launch", actualSummary.status.name)
        assertEquals("2025-12-13T05:34:00Z", actualSummary.net)
    }

    @Test
    fun `getPastDetailedLaunches returns summaries and details when API call is successful`() = runTest {
        val page = 0
        val launchesQuery = LaunchesQuery(query = "Falcon")
        val offset = 0
        coEvery {
            api.getPreviousLaunches(
                offset = offset,
                search = launchesQuery.query,
                status = null
            )
        } returns TestData.createLaunchesDto()

        val result = underTest.getPastDetailedLaunches(page, launchesQuery)

        assertTrue(result is LaunchResult.Success)
        val detailedResult = result.data
        assertTrue(detailedResult.summaries.isNotEmpty())
        assertTrue(detailedResult.details.isNotEmpty())

        val actualSummary = detailedResult.summaries[0]
        assertEquals("faf4a0bc-7dad-4842-b74c-73a9f648b5cc", actualSummary.id)
    }

    @Test
    fun `getUpcomingDetailedLaunches returns error when API call fails`() = runTest {
        val page = 0
        val launchesQuery = LaunchesQuery(query = "")
        val offset = 0
        val exception = RuntimeException("Network Failure")
        coEvery {
            api.getUpcomingLaunches(
                offset = offset,
                search = launchesQuery.query,
                status = null
            )
        } throws exception

        val result = underTest.getUpcomingDetailedLaunches(page, launchesQuery)

        assertTrue(result is LaunchResult.Error)
        assertEquals(exception, result.error)
    }

    @Test
    fun `getPastDetailedLaunches returns error when API call fails`() = runTest {
        val page = 0
        val launchesQuery = LaunchesQuery(query = "")
        val offset = 0
        val exception = RuntimeException("Network Failure")
        coEvery {
            api.getPreviousLaunches(
                offset = offset,
                search = launchesQuery.query,
                status = null
            )
        } throws exception

        val result = underTest.getPastDetailedLaunches(page, launchesQuery)

        assertTrue(result is LaunchResult.Error)
        assertEquals(exception, result.error)
    }

    @Test
    fun `getUpcomingDetailedLaunches calculates correct offset for pagination`() = runTest {
        val page = 2
        val launchesQuery = LaunchesQuery(query = "SpaceX")
        val expectedOffset = LaunchesConstants.PAGINATION_LIMIT * 2
        coEvery {
            api.getUpcomingLaunches(
                offset = expectedOffset,
                search = launchesQuery.query,
                status = null
            )
        } returns TestData.createLaunchesDto()

        val result = underTest.getUpcomingDetailedLaunches(page, launchesQuery)

        assertTrue(result is LaunchResult.Success)
    }

    @Test
    fun `getPastDetailedLaunches calculates correct offset for pagination`() = runTest {
        val page = 2
        val launchesQuery = LaunchesQuery(query = "SpaceX")
        val expectedOffset = LaunchesConstants.PAGINATION_LIMIT * 2
        coEvery {
            api.getPreviousLaunches(
                offset = expectedOffset,
                search = launchesQuery.query,
                status = null
            )
        } returns TestData.createLaunchesDto()

        val result = underTest.getPastDetailedLaunches(page, launchesQuery)

        assertTrue(result is LaunchResult.Success)
    }

    @Test
    fun `getUpcomingDetailedLaunches passes query parameters correctly`() = runTest {
        val page = 1
        val launchesQuery = LaunchesQuery(query = "Dragon")
        val expectedOffset = LaunchesConstants.PAGINATION_LIMIT
        coEvery {
            api.getUpcomingLaunches(
                offset = expectedOffset,
                search = "Dragon",
                status = null
            )
        } returns TestData.createLaunchesDto()

        val result = underTest.getUpcomingDetailedLaunches(page, launchesQuery)

        assertTrue(result is LaunchResult.Success)
    }

    @Test
    fun `getPastDetailedLaunches passes query parameters correctly`() = runTest {
        val page = 1
        val launchesQuery = LaunchesQuery(query = "Dragon")
        val expectedOffset = LaunchesConstants.PAGINATION_LIMIT
        coEvery {
            api.getPreviousLaunches(
                offset = expectedOffset,
                search = "Dragon",
                status = null
            )
        } returns TestData.createLaunchesDto()

        val result = underTest.getPastDetailedLaunches(page, launchesQuery)

        assertTrue(result is LaunchResult.Success)
    }

    @Test
    fun `getLaunch returns upcoming launch when API call is successful`() = runTest {
        val id = "test-id"
        val launchType = LaunchesType.UPCOMING
        coEvery { api.getUpcomingLaunch(id) } returns TestData.createLaunchDto()

        val result = underTest.getLaunch(id, launchType)

        assertTrue(result is LaunchResult.Success)
        val actualLaunch = result.data
        assertEquals("faf4a0bc-7dad-4842-b74c-73a9f648b5cc", actualLaunch.id)
    }

    @Test
    fun `getLaunch returns past launch when API call is successful`() = runTest {
        val id = "test-id"
        val launchType = LaunchesType.PAST
        coEvery { api.getPreviousLaunch(id) } returns TestData.createLaunchDto()

        val result = underTest.getLaunch(id, launchType)

        assertTrue(result is LaunchResult.Success)
        val actualLaunch = result.data
        assertEquals("faf4a0bc-7dad-4842-b74c-73a9f648b5cc", actualLaunch.id)
    }

    @Test
    fun `getLaunch returns error when API call fails for upcoming launch`() = runTest {
        val id = "test-id"
        val launchType = LaunchesType.UPCOMING
        val givenException = RuntimeException("Network Failure")
        val expectedException = DataError.RemoteError.NETWORK_UNKNOWN_ERROR
        coEvery { api.getUpcomingLaunch(id) } throws givenException

        val result = underTest.getLaunch(id, launchType)

        assertTrue(result is LaunchResult.Error)
        assertEquals(expectedException, result.error)
    }

    @Test
    fun `getLaunch returns error when API call fails for past launch`() = runTest {
        val id = "test-id"
        val launchType = LaunchesType.PAST
        val givenException = RuntimeException("Network Failure")
        val expectedException = DataError.RemoteError.NETWORK_UNKNOWN_ERROR
        coEvery { api.getPreviousLaunch(id) } throws givenException

        val result = underTest.getLaunch(id, launchType)

        assertTrue(result is LaunchResult.Error)
        assertEquals(expectedException, result.error)
    }

    @Test
    fun `getUpcomingDetailedLaunches returns empty lists when API returns no results`() = runTest {
        val page = 0
        val launchesQuery = LaunchesQuery(query = "nonexistent")
        val offset = 0
        coEvery {
            api.getUpcomingLaunches(
                offset = offset,
                search = launchesQuery.query,
                status = null
            )
        } returns TestData.createEmptyLaunchesDto()

        val result = underTest.getUpcomingDetailedLaunches(page, launchesQuery)

        assertTrue(result is LaunchResult.Success)
        val detailedResult = result.data
        assertTrue(detailedResult.summaries.isEmpty())
        assertTrue(detailedResult.details.isEmpty())
    }

    @Test
    fun `getPastDetailedLaunches returns empty lists when API returns no results`() = runTest {
        val page = 0
        val launchesQuery = LaunchesQuery(query = "nonexistent")
        val offset = 0
        coEvery {
            api.getPreviousLaunches(
                offset = offset,
                search = launchesQuery.query,
                status = null
            )
        } returns TestData.createEmptyLaunchesDto()

        val result = underTest.getPastDetailedLaunches(page, launchesQuery)

        assertTrue(result is LaunchResult.Success)
        val detailedResult = result.data
        assertTrue(detailedResult.summaries.isEmpty())
        assertTrue(detailedResult.details.isEmpty())
    }
}
