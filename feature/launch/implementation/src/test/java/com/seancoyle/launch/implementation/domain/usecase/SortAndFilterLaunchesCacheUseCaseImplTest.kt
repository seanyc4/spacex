package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.core.domain.Order
import com.seancoyle.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.launch.api.domain.model.LaunchStatus
import com.seancoyle.launch.api.domain.model.LaunchTypes
import com.seancoyle.launch.api.domain.model.Links
import com.seancoyle.launch.api.domain.model.Rocket
import com.seancoyle.launch.implementation.domain.cache.LaunchCacheDataSource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SortAndFilterLaunchesCacheUseCaseImplTest {

    @MockK
    private lateinit var cacheDataSource: LaunchCacheDataSource

    private lateinit var underTest: SortAndFilterLaunchesCacheUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = SortAndFilterLaunchesCacheUseCaseImpl(cacheDataSource)
    }

    @Test
    fun `invoke should emit correct launches when data is available`() = runTest {
        val year = "2024"
        val order = Order.DESC
        val status = LaunchStatus.SUCCESS
        val page = 1

        coEvery { cacheDataSource.filterLaunchList(year, order, status, page) } returns Result.Success(LAUNCH_LIST)

        val results = mutableListOf<Result<List<LaunchTypes>?, DataError>>()
        underTest(year, order, status, page).collect { results.add(it) }

        assertTrue(results.first() is Result.Success)
        assertEquals(LAUNCH_LIST, (results.first() as Result.Success).data)
    }

    @Test
    fun `invoke should emit error when there is a problem accessing cache`() = runTest {
        val year = "2024"
        val order = Order.DESC
        val status = LaunchStatus.SUCCESS
        val page = 1
        val error = DataError.CACHE_ERROR

        coEvery { cacheDataSource.filterLaunchList(year, order, status, page) } returns Result.Error(error)

        val results = mutableListOf<Result<List<LaunchTypes>?, DataError>>()
        underTest(year, order, status, page).collect { results.add(it) }

        assertTrue(results.first() is Result.Error)
        assertEquals(error, (results.first() as Result.Error).error)
    }


    private fun checkDateOrderAscending(launchList: List<LaunchTypes.Launch>) {
        // Check the list and verify the date is less than the next index
        for (item in 0..launchList.size.minus(2)) {
            assertTrue(
                launchList[item].launchDateLocalDateTime <=
                        launchList[item.plus(1)].launchDateLocalDateTime
            )
        }
    }

    private fun checkDateOrderDescending(launchList: List<LaunchTypes.Launch>) {
        // Check the list and verify the date is greater than the next index
        for (item in 0..launchList.size.minus(2)) {
            assertTrue(
                launchList[item].launchDateLocalDateTime >=
                        launchList[item.plus(1)].launchDateLocalDateTime
            )
        }
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
    }
}