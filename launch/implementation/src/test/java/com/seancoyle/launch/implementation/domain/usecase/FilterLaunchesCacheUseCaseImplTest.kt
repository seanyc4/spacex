package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.CacheErrors
import com.seancoyle.core_testing.MainCoroutineRule
import com.seancoyle.launch.api.LaunchConstants.LAUNCH_ALL
import com.seancoyle.launch.api.LaunchConstants.LAUNCH_FAILED
import com.seancoyle.launch.api.LaunchConstants.LAUNCH_SUCCESS
import com.seancoyle.launch.api.LaunchConstants.LAUNCH_UNKNOWN
import com.seancoyle.launch.api.LaunchConstants.ORDER_ASC
import com.seancoyle.launch.api.LaunchConstants.ORDER_DESC
import com.seancoyle.launch.implementation.R
import com.seancoyle.launch.implementation.domain.cache.LaunchCacheDataSource
import com.seancoyle.launch.implementation.domain.model.Launch
import com.seancoyle.launch.implementation.domain.model.Links
import com.seancoyle.launch.implementation.domain.model.Rocket
import com.seancoyle.launch.implementation.presentation.state.LaunchEvents
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalCoroutinesApi::class)
class FilterLaunchesCacheUseCaseImplTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var cacheDataSource: LaunchCacheDataSource

    private lateinit var underTest: SortAndFilterLaunchesCacheUseCase

    @BeforeEach
    fun init() {
        MockKAnnotations.init(this)
        underTest = SortAndFilterLaunchesCacheUseCaseImpl(
            ioDispatcher = mainCoroutineRule.testDispatcher,
            cacheDataSource = cacheDataSource
        )
    }

    @Test
    fun `Order All Launch Items By Date Ascending - success`() = runBlocking {

        var expectedResult = emptyList<Launch>()
        val givenLaunches = listOf(
            createLaunch(id = "1", launchDate = "2023-01-01", isLaunchSuccess = LAUNCH_SUCCESS),
            createLaunch(id = "2", launchDate = "2023-01-02", isLaunchSuccess = LAUNCH_SUCCESS),
            createLaunch(id = "3", launchDate = "2023-01-03", isLaunchSuccess = LAUNCH_SUCCESS)
        )
        coEvery { cacheDataSource.filterLaunchList(
            year = "",
            order = ORDER_ASC,
            launchFilter = LAUNCH_ALL,
            page = 1
        ) } returns givenLaunches

        underTest(
            year = "",
            order = ORDER_ASC,
            launchFilter = LAUNCH_ALL,
            page = 1
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                LaunchEvents.PaginateLaunchesCacheEvent.eventName() + EVENT_CACHE_SUCCESS
            )

            value?.data?.launches?.let {
                expectedResult = it
            }
        }

        assertTrue(expectedResult.isNotEmpty())
        checkDateOrderAscending(expectedResult)
    }

    @Test
    fun `Order All Launch Items By Date Descending - success`() = runBlocking {

        var expectedResult = emptyList<Launch>()
        val givenLaunches = listOf(
            createLaunch(id = "1", launchDate = "2023-01-03", isLaunchSuccess = LAUNCH_SUCCESS),
            createLaunch(id = "2", launchDate = "2023-01-02", isLaunchSuccess = LAUNCH_SUCCESS),
            createLaunch(id = "3", launchDate = "2023-01-01", isLaunchSuccess = LAUNCH_SUCCESS)
        )
        coEvery { cacheDataSource.filterLaunchList(
            year = "",
            order = ORDER_DESC,
            launchFilter = LAUNCH_ALL,
            page = 1
        ) } returns givenLaunches

        underTest(
            year = "",
            order = ORDER_DESC,
            launchFilter = LAUNCH_ALL,
            page = 1,
            event = LaunchEvents.PaginateLaunchesCacheEvent
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                LaunchEvents.PaginateLaunchesCacheEvent.eventName() + EVENT_CACHE_SUCCESS
            )

            value?.data?.launches?.let {
                expectedResult = it
            }
        }

        assertTrue(expectedResult.isNotEmpty())
        checkDateOrderDescending(expectedResult)
    }

    @Test
    fun `filter launch items by year and date order ASC - success`() = runBlocking {

        var expectedResult = emptyList<Launch>()
        val launchYear = "2023"
        val givenLaunches = listOf(
            createLaunch(id = 1, launchDate = "2023-01-01", isLaunchSuccess = LAUNCH_SUCCESS),
            createLaunch(id = 2, launchDate = "2023-02-01", isLaunchSuccess = LAUNCH_SUCCESS),
            createLaunch(id = 3, launchDate = "2023-03-01", isLaunchSuccess = LAUNCH_SUCCESS)
        )
        coEvery { cacheDataSource.filterLaunchList(
            year = launchYear,
            order = ORDER_ASC,
            launchFilter = null,
            page = 1
        ) } returns givenLaunches

        underTest(
            year = launchYear,
            order = ORDER_ASC,
            launchFilter = null,
            page = 1
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                LaunchEvents.PaginateLaunchesCacheEvent.eventName() + EVENT_CACHE_SUCCESS
            )

            value?.data?.launches?.let {
                expectedResult = it
            }
        }

        assertTrue(expectedResult.isNotEmpty())
        assertTrue { expectedResult.all { it.launchYear == launchYear } }
        checkDateOrderAscending(expectedResult)
    }

    @Test
    fun `Filter Launch Items By Year - Results Found`() = runBlocking {

        val givenYear = "2024"

        val givenLaunches = listOf(
            createLaunch(id = 1, launchDate = "2024-01-01", isLaunchSuccess = LAUNCH_SUCCESS),
            createLaunch(id = 3, launchDate = "2024-03-01", isLaunchSuccess = LAUNCH_SUCCESS),
        )
        coEvery { cacheDataSource.filterLaunchList(
            year = givenYear,
            order = ORDER_ASC,
            launchFilter = LAUNCH_SUCCESS,
            page = 1
        ) } returns givenLaunches

        underTest(
            year = givenYear,
            order = ORDER_ASC,
            launchFilter = LAUNCH_SUCCESS,
            page = 1
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                LaunchEvents.PaginateLaunchesCacheEvent.eventName() + EVENT_CACHE_SUCCESS
            )

            value?.data?.launches?.let { actualResult ->
                actualResult.forEach {
                    assertTrue { it.launchYear == "2024" }
                }
            }
        }
    }

    @Test
    fun `Filter Launch Items By Year - No Results Found`() = runBlocking {

        val givenYear = "2026"

        coEvery {
            cacheDataSource.filterLaunchList(
                year = givenYear,
                order = ORDER_ASC,
                launchFilter = LAUNCH_SUCCESS,
                page = 1
            )
        } returns emptyList()

        underTest(
            year = givenYear,
            order = ORDER_ASC,
            launchFilter = LAUNCH_SUCCESS,
            page = 1,
            event = LaunchEvents.PaginateLaunchesCacheEvent
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                LaunchEvents.PaginateLaunchesCacheEvent.eventName() + EVENT_CACHE_NO_MATCHING_RESULTS
            )

            assertTrue(value?.data?.launches?.isEmpty() == true)
        }
    }

    @Test
    fun `Filter Launch Items By Launch Status LAUNCH_SUCCESS - success`() = runBlocking {

        var expectedResult = emptyList<Launch>()
        val givenLaunches = listOf(
            createLaunch(id = 1, launchDate = "2023-01-01", isLaunchSuccess = LAUNCH_SUCCESS),
            createLaunch(id = 2, launchDate = "2023-02-01", isLaunchSuccess = LAUNCH_SUCCESS),
            createLaunch(id = 3, launchDate = "2023-03-01", isLaunchSuccess = LAUNCH_SUCCESS)
        )
        coEvery { cacheDataSource.filterLaunchList(
            year = "",
            order = ORDER_ASC,
            launchFilter = LAUNCH_SUCCESS,
            page = 1
        ) } returns givenLaunches

        underTest(
            year = "",
            order = ORDER_ASC,
            launchFilter = LAUNCH_SUCCESS,
            page = 1,
            event = LaunchEvents.PaginateLaunchesCacheEvent
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                LaunchEvents.PaginateLaunchesCacheEvent.eventName() + EVENT_CACHE_SUCCESS
            )

            value?.data?.launches?.let {
                expectedResult = it
            }
        }

        assertTrue(expectedResult.isNotEmpty())
        checkDateOrderAscending(expectedResult)
        assertTrue { expectedResult.all { it.isLaunchSuccess == LAUNCH_SUCCESS } }
    }

    @Test
    fun `Filter Launch Items By Launch Status LAUNCH_FAILED - success`() = runBlocking {

        var expectedResult = emptyList<Launch>()
        val givenLaunches = listOf(
            createLaunch(id = 1, launchDate = "2023-01-01", isLaunchSuccess = LAUNCH_FAILED),
            createLaunch(id = 2, launchDate = "2023-02-01", isLaunchSuccess = LAUNCH_FAILED),
            createLaunch(id = 3, launchDate = "2023-03-01", isLaunchSuccess = LAUNCH_FAILED)
        )
        coEvery { cacheDataSource.filterLaunchList(
            year = "",
            order = ORDER_ASC,
            launchFilter = LAUNCH_FAILED,
            page = 1
        ) } returns givenLaunches

        underTest(
            year = "",
            order = ORDER_ASC,
            launchFilter = LAUNCH_FAILED,
            page = 1,
            event = LaunchEvents.PaginateLaunchesCacheEvent
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                LaunchEvents.PaginateLaunchesCacheEvent.eventName() + EVENT_CACHE_SUCCESS
            )

            value?.data?.launches?.let {
                expectedResult = it
            }
        }

        assertTrue(expectedResult.isNotEmpty())
        checkDateOrderAscending(expectedResult)
        assertTrue { expectedResult.all { it.isLaunchSuccess == LAUNCH_FAILED } }
    }

    @Test
    fun `Filter Launch Items By Launch Status ALL - success`() = runBlocking {

        var expectedResult = emptyList<Launch>()
        val givenLaunches = listOf(
            createLaunch(id = 1, launchDate = "2023-01-01", isLaunchSuccess = LAUNCH_FAILED),
            createLaunch(id = 2, launchDate = "2023-02-01", isLaunchSuccess = LAUNCH_SUCCESS),
            createLaunch(id = 3, launchDate = "2023-03-01", isLaunchSuccess = LAUNCH_UNKNOWN)
        )
        coEvery { cacheDataSource.filterLaunchList(
            year = "",
            order = ORDER_ASC,
            launchFilter = LAUNCH_ALL,
            page = 1
        ) } returns givenLaunches

        underTest(
            year = "",
            order = ORDER_ASC,
            launchFilter = LAUNCH_ALL,
            page = 1,
            event = LaunchEvents.PaginateLaunchesCacheEvent
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                LaunchEvents.PaginateLaunchesCacheEvent.eventName() + EVENT_CACHE_SUCCESS
            )

            value?.data?.launches?.let {
                expectedResult = it
            }
        }

        checkDateOrderAscending(expectedResult)
        assertTrue { expectedResult.containsAll(expectedResult) }
    }

    @Test
    fun `Filter Launch Items By Launch Status UNKOWN - success`() = runBlocking {

        var expectedResult = emptyList<Launch>()
        val givenLaunches = listOf(
            createLaunch(id = 1, launchDate = "2023-01-01", isLaunchSuccess = LAUNCH_UNKNOWN),
            createLaunch(id = 2, launchDate = "2023-02-01", isLaunchSuccess = LAUNCH_UNKNOWN),
            createLaunch(id = 3, launchDate = "2023-03-01", isLaunchSuccess = LAUNCH_UNKNOWN)
        )
        coEvery { cacheDataSource.filterLaunchList(
            year = "",
            order = ORDER_ASC,
            launchFilter = LAUNCH_UNKNOWN,
            page = 1
        ) } returns givenLaunches

        underTest(
            year = "",
            order = ORDER_ASC,
            launchFilter = LAUNCH_UNKNOWN,
            page = 1,
            event = LaunchEvents.PaginateLaunchesCacheEvent
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                LaunchEvents.PaginateLaunchesCacheEvent.eventName() + EVENT_CACHE_SUCCESS
            )

            value?.data?.launches?.let {
                expectedResult = it
            }
        }

        assertTrue(expectedResult.isNotEmpty())
        assertTrue { expectedResult.all { it.isLaunchSuccess == LAUNCH_UNKNOWN } }
    }

    @Test
    fun `Filter Launch Items By isLaunchSuccess - no results found`() = runBlocking {

        var expectedResult = emptyList<Launch>()
        val givenLaunches = emptyList<Launch>()
        coEvery { cacheDataSource.filterLaunchList(
            year = "",
            order = ORDER_ASC,
            launchFilter = LAUNCH_SUCCESS,
            page = 1
        ) } returns givenLaunches

        underTest(
            year = "",
            order = ORDER_ASC,
            launchFilter = LAUNCH_SUCCESS,
            page = 1,
            event = LaunchEvents.PaginateLaunchesCacheEvent
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                LaunchEvents.PaginateLaunchesCacheEvent.eventName() + EVENT_CACHE_NO_MATCHING_RESULTS
            )

            value?.data?.launches?.let {
                expectedResult = it
            }
        }

        assertTrue { expectedResult.isEmpty() }
    }

    @Test
    fun `Filter Launch Items Fail - no results found`() = runBlocking {

        var expectedResult = emptyList<Launch>()
        coEvery { cacheDataSource.filterLaunchList(
            year = com.seancoyle.launch.implementation.cache.FORCE_SEARCH_LAUNCH_EXCEPTION,
            order = ORDER_DESC,
            launchFilter = null,
            page = 1
        ) } throws RuntimeException("Cache error")

        underTest(
            year = com.seancoyle.launch.implementation.cache.FORCE_SEARCH_LAUNCH_EXCEPTION,
            order = ORDER_DESC,
            launchFilter = null,
            page = 1,
            event = LaunchEvents.PaginateLaunchesCacheEvent
        ).collect { value ->
            assert(
                value?.stateMessage?.response?.message
                    ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN) ?: false
            )

            value?.data?.launches?.let {
                expectedResult = it
            }
        }

        assertTrue { expectedResult.isEmpty() }
    }

    private fun checkDateOrderAscending(launchList: List<Launch>) {
        // Check the list and verify the date is less than the next index
        for (item in 0..launchList.size.minus(2)) {
            assertTrue(
                launchList[item].launchDateLocalDateTime <=
                        launchList[item.plus(1)].launchDateLocalDateTime
            )
        }
    }

    private fun checkDateOrderDescending(launchList: List<Launch>) {
        // Check the list and verify the date is greater than the next index
        for (item in 0..launchList.size.minus(2)) {
            assertTrue(
                launchList[item].launchDateLocalDateTime >=
                        launchList[item.plus(1)].launchDateLocalDateTime
            )
        }
    }

    private fun createLaunch(
        id: String,
        launchDate: String,
        isLaunchSuccess: Int
    ): Launch {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val localDateTime = LocalDate.parse(launchDate, formatter).atStartOfDay()
        val launchYear = launchDate.split("-")[0]
        return Launch(
            id = id,
            launchDate = launchDate,
            launchDateLocalDateTime = localDateTime,
            launchYear = launchYear,
            isLaunchSuccess = isLaunchSuccess,
            launchStatus = R.drawable.ic_launch_success,
            links = Links("missionImage$id", "articleLink$id", "webcastLink$id", "wikiLink$id"),
            missionName = "Mission $id",
            rocket = Rocket("Falcon 9"),
            launchDateStatus = R.string.days_from_now,
            launchDays = "1 day",
            type = ViewType.TYPE_LIST
        )
    }
}