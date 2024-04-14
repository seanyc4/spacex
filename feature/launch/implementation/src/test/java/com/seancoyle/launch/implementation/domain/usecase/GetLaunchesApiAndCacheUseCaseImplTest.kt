package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.launch.api.domain.model.LaunchStatus
import com.seancoyle.launch.api.domain.model.LaunchTypes
import com.seancoyle.launch.api.domain.model.Links
import com.seancoyle.launch.api.domain.model.Rocket
import com.seancoyle.launch.api.domain.usecase.GetLaunchesApiAndCacheUseCase
import com.seancoyle.launch.implementation.domain.model.LaunchOptions
import com.seancoyle.launch.implementation.domain.network.LaunchNetworkDataSource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetLaunchesApiAndCacheUseCaseImplTest {

    @MockK
    private lateinit var insertLaunchesToCacheUseCase: InsertLaunchesToCacheUseCase

    @MockK
    private lateinit var launchNetworkDataSource: LaunchNetworkDataSource

    @MockK
    private lateinit var launchOptions: LaunchOptions

    private lateinit var underTest: GetLaunchesApiAndCacheUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = GetLaunchesApiAndCacheUseCaseImpl(
            insertLaunchesToCacheUseCase = insertLaunchesToCacheUseCase,
            launchNetworkDataSource = launchNetworkDataSource,
            launchOptions = launchOptions
        )
    }

    @Test
    fun `invoke should emit success result when network and cache operations are successful`() = runTest {
        coEvery { launchNetworkDataSource.getLaunches(launchOptions) } returns Result.Success(LAUNCH_LIST)
        coEvery { insertLaunchesToCacheUseCase(LAUNCH_LIST) } returns Result.Success(INSERT_SUCCESS)

        val results = mutableListOf<Result<List<LaunchTypes.Launch>, DataError>>()
        underTest().collect { results.add(it) }

        assertTrue(results.first() is Result.Success)
        assertEquals(LAUNCH_LIST, (results.first() as Result.Success).data)
    }

    @Test
    fun `invoke should emit error when network fetch fails`() = runTest {
        val networkError = DataError.NETWORK_UNKNOWN_ERROR
        coEvery { launchNetworkDataSource.getLaunches(launchOptions) } returns Result.Error(networkError)

        val results = mutableListOf<Result<List<LaunchTypes.Launch>, DataError>>()
        underTest().collect { results.add(it) }

        assertTrue(results.first() is Result.Error)
        assertEquals(networkError, (results.first() as Result.Error).error)
    }

    @Test
    fun `invoke should emit error when cache operation fails`() = runTest {
        val cacheError = DataError.CACHE_ERROR
        coEvery { launchNetworkDataSource.getLaunches(launchOptions) } returns Result.Success(LAUNCH_LIST)
        coEvery { insertLaunchesToCacheUseCase(LAUNCH_LIST) } returns Result.Error(cacheError)

        val results = mutableListOf<Result<List<LaunchTypes.Launch>, DataError>>()
        underTest().collect { results.add(it) }

        assertTrue(results.first() is Result.Error)
        assertEquals(cacheError, (results.first() as Result.Error).error)
    }


    private companion object {
        val LAUNCH_LIST = listOf(
            LaunchTypes.Launch(
                id = "5",
                launchDate = "2024-01-01",
                launchDateLocalDateTime = LocalDateTime.now(),
                launchYear = "2024",
                launchStatus = LaunchStatus.SUCCESS,
                links = Links(
                    missionImage = "https://example.com/mission3.jpg",
                    articleLink = "https://example.com/article3",
                    webcastLink = "https://example.com/webcast3",
                    wikiLink = "https://example.com/wiki3"
                ),
                missionName = "Starlink Mission",
                rocket = Rocket("Falcon 9 Block 5"),
                launchDateStatus = LaunchDateStatus.FUTURE,
                launchDays = "5 days"
            )
        )
        val INSERT_SUCCESS = longArrayOf(1L)
    }
}