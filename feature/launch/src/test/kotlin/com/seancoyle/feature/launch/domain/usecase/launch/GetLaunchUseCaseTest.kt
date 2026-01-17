package com.seancoyle.feature.launch.domain.usecase.launch

import com.seancoyle.core.common.result.DataError.RemoteError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.feature.launch.domain.repository.LaunchesRepository
import com.seancoyle.feature.launch.util.TestData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetLaunchUseCaseTest {

    @MockK
    private lateinit var launchesRepository: LaunchesRepository

    private lateinit var underTest: GetLaunchUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        underTest = GetLaunchUseCase(launchesRepository)
    }

    @Test
    fun `GIVEN repository returns launch WHEN invoke with isRefresh false THEN returns success`() = runTest {
        val id = "123"
        val launchType = LaunchesType.UPCOMING
        val launch = TestData.createLaunch()
        coEvery { launchesRepository.getLaunch(id, launchType, isRefresh = false) } returns LaunchResult.Success(launch)

        val result = underTest(id, launchType, isRefresh = false)

        assertTrue(result is LaunchResult.Success)
        assertEquals(launch, (result as LaunchResult.Success).data)
    }

    @Test
    fun `GIVEN repository returns error WHEN invoke THEN returns error`() = runTest {
        val id = "123"
        val launchType = LaunchesType.PAST
        val error = RemoteError.NETWORK_UNKNOWN_ERROR
        coEvery { launchesRepository.getLaunch(id, launchType, isRefresh = false) } returns LaunchResult.Error(error)

        val result = underTest(id, launchType, isRefresh = false)

        assertTrue(result is LaunchResult.Error)
        assertEquals(error, (result as LaunchResult.Error).error)
    }

    @Test
    fun `GIVEN launch with mixed video sources WHEN invoke THEN filters out non-YouTube sources`() = runTest {
        val id = "456"
        val launchType = LaunchesType.UPCOMING
        val youtubeVid = TestData.createVidUrl(source = "youtube")
        val vimeoVid = TestData.createVidUrl(source = "vimeo")
        val otherVid = TestData.createVidUrl(source = "other")
        val launch = TestData.createLaunch(vidUrls = listOf(youtubeVid, vimeoVid, otherVid))
        coEvery { launchesRepository.getLaunch(id, launchType, isRefresh = false) } returns LaunchResult.Success(launch)

        val result = underTest(id, launchType, isRefresh = false)

        assertTrue(result is LaunchResult.Success)
        val filtered = (result as LaunchResult.Success).data.vidUrls
        assertEquals(1, filtered.size)
        assertEquals("youtube", filtered.first().source)
    }

    @Test
    fun `GIVEN launch with YouTube video with null url WHEN invoke THEN filters out null url videos`() = runTest {
        val id = "456"
        val launchType = LaunchesType.UPCOMING
        val youtubeVidWithUrl = TestData.createVidUrl(source = "youtube", url = "https://youtube.com/watch?v=123")
        val youtubeVidNullUrl = TestData.createVidUrl(source = "youtube", url = null)
        val launch = TestData.createLaunch(vidUrls = listOf(youtubeVidWithUrl, youtubeVidNullUrl))
        coEvery { launchesRepository.getLaunch(id, launchType, isRefresh = false) } returns LaunchResult.Success(launch)

        val result = underTest(id, launchType, isRefresh = false)

        assertTrue(result is LaunchResult.Success)
        val filtered = (result as LaunchResult.Success).data.vidUrls
        assertEquals(1, filtered.size)
        assertEquals("https://youtube.com/watch?v=123", filtered.first().url)
    }

    @Test
    fun `GIVEN isRefresh true WHEN invoke THEN passes isRefresh to repository`() = runTest {
        val id = "123"
        val launchType = LaunchesType.UPCOMING
        val launch = TestData.createLaunch()
        coEvery { launchesRepository.getLaunch(id, launchType, isRefresh = true) } returns LaunchResult.Success(launch)

        underTest(id, launchType, isRefresh = true)

        coVerify(exactly = 1) { launchesRepository.getLaunch(id, launchType, isRefresh = true) }
    }

    @Test
    fun `GIVEN isRefresh false WHEN invoke THEN passes isRefresh false to repository`() = runTest {
        val id = "123"
        val launchType = LaunchesType.PAST
        val launch = TestData.createLaunch()
        coEvery { launchesRepository.getLaunch(id, launchType, isRefresh = false) } returns LaunchResult.Success(launch)

        underTest(id, launchType, isRefresh = false)

        coVerify(exactly = 1) { launchesRepository.getLaunch(id, launchType, isRefresh = false) }
    }

    @Test
    fun `GIVEN launch with no videos WHEN invoke THEN returns launch with empty vidUrls`() = runTest {
        val id = "789"
        val launchType = LaunchesType.UPCOMING
        val launch = TestData.createLaunch(vidUrls = emptyList())
        coEvery { launchesRepository.getLaunch(id, launchType, isRefresh = false) } returns LaunchResult.Success(launch)

        val result = underTest(id, launchType, isRefresh = false)

        assertTrue(result is LaunchResult.Success)
        assertTrue((result as LaunchResult.Success).data.vidUrls.isEmpty())
    }

    @Test
    fun `GIVEN launch with only YouTube videos WHEN invoke THEN returns all YouTube videos`() = runTest {
        val id = "789"
        val launchType = LaunchesType.UPCOMING
        val youtubeVid1 = TestData.createVidUrl(source = "youtube", url = "https://youtube.com/1")
        val youtubeVid2 = TestData.createVidUrl(source = "YouTube", url = "https://youtube.com/2")
        val launch = TestData.createLaunch(vidUrls = listOf(youtubeVid1, youtubeVid2))
        coEvery { launchesRepository.getLaunch(id, launchType, isRefresh = false) } returns LaunchResult.Success(launch)

        val result = underTest(id, launchType, isRefresh = false)

        assertTrue(result is LaunchResult.Success)
        assertEquals(2, (result as LaunchResult.Success).data.vidUrls.size)
    }

    @Test
    fun `GIVEN UPCOMING launch type WHEN invoke THEN delegates to repository with UPCOMING type`() = runTest {
        val id = "123"
        val launch = TestData.createLaunch()
        coEvery { launchesRepository.getLaunch(id, LaunchesType.UPCOMING, isRefresh = false) } returns LaunchResult.Success(launch)

        underTest(id, LaunchesType.UPCOMING, isRefresh = false)

        coVerify(exactly = 1) { launchesRepository.getLaunch(id, LaunchesType.UPCOMING, isRefresh = false) }
    }

    @Test
    fun `GIVEN PAST launch type WHEN invoke THEN delegates to repository with PAST type`() = runTest {
        val id = "123"
        val launch = TestData.createLaunch()
        coEvery { launchesRepository.getLaunch(id, LaunchesType.PAST, isRefresh = false) } returns LaunchResult.Success(launch)

        underTest(id, LaunchesType.PAST, isRefresh = false)

        coVerify(exactly = 1) { launchesRepository.getLaunch(id, LaunchesType.PAST, isRefresh = false) }
    }
}
