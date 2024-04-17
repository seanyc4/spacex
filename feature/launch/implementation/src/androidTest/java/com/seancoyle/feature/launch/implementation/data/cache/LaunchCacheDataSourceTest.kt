package com.seancoyle.feature.launch.implementation.data.cache

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.core.common.result.Result
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.LaunchConstants.DEFAULT_LAUNCH_IMAGE
import com.seancoyle.feature.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.api.domain.model.Links
import com.seancoyle.feature.launch.api.domain.model.Rocket
import com.seancoyle.feature.launch.implementation.domain.cache.LaunchCacheDataSource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID
import javax.inject.Inject
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

private const val PAGE = 1
private const val DATA = 1000

@HiltAndroidTest
@Suppress("UNCHECKED_CAST")
@RunWith(AndroidJUnit4ClassRunner::class)
internal class LaunchCacheDataSourceTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    private lateinit var validLaunchYears: List<String>

    @Inject
    lateinit var underTest: LaunchCacheDataSource

    @Before
    fun setup() {
        hiltRule.inject()
        validLaunchYears = provideValidFilterYearDates()
    }

    @After
    fun tearDown() = runTest {
        underTest.deleteAll()
    }

    private suspend fun insertTestData(num: Int = DATA): List<LaunchTypes.Launch> {
        val givenList = createLaunchListTest(num)
        underTest.insertList(givenList)
        return givenList
    }

    @Test
    fun insertLaunchGetLaunchByIdSuccess() = runTest {
        val expectedResult = createRandomLaunchItem(id = "1")

        underTest.insert(expectedResult)

        val result = underTest.getById(id = "1")

        assertTrue(result is Result.Success)
        assertEquals(expectedResult, result.data)
    }

    @Test
    fun insertLaunchesGetLaunchesSuccess() = runTest {
        val expectedResult = insertTestData()

        val result = underTest.getAll()

        assertTrue(result is Result.Success)
        assertNotNull(result.data)
        result.data.filterIsInstance<List<LaunchTypes.Launch>>()
        assertTrue { expectedResult.containsAll(result.data) }
    }

    @Test
    fun insertLaunchesConfirmNumLaunchesInDb() = runTest {
        val expectedResult = insertTestData()

        val result = underTest.getTotalEntries()

        assertTrue(result is Result.Success)
        assertEquals(expectedResult.size, result.data)
    }

    @Test
    fun insertLaunchDeleteLaunchConfirmDeleted() = runTest {
        val expectedResult = createRandomLaunchItem(id = "2")
        underTest.insert(expectedResult)

        val preDeleteResult = underTest.getById(expectedResult.id)

        assertTrue(preDeleteResult is Result.Success)
        assertEquals(expectedResult, preDeleteResult.data)

        underTest.deleteById(expectedResult.id)

        val postDeleteResult = underTest.getById(expectedResult.id)
        assertTrue(postDeleteResult is Result.Success)
        assertNull(postDeleteResult.data, "Launch item should be null after deletion.")
    }

    @Test
    fun orderAllLaunchesByDateASCConfirm() = runTest {
        insertTestData()

        val result = underTest.filterLaunchList(
            launchYear = "",
            order = Order.ASC,
            launchStatus = LaunchStatus.SUCCESS,
            page = PAGE,
        )

        assertTrue(result is Result.Success)
        result as Result.Success<List<LaunchTypes.Launch>>

        assertTrue(result.data.isNotEmpty())
        checkDateOrderAscending(result.data)
    }

    @Test
    fun orderAllLaunchesByDateDESCConfirm() = runTest {
        insertTestData()

        val result = underTest.filterLaunchList(
            launchYear = "",
            order = Order.DESC,
            launchStatus = LaunchStatus.SUCCESS,
            page = PAGE,
        )

        assertTrue(result is Result.Success)
        result as Result.Success<List<LaunchTypes.Launch>>

        assertTrue(result.data.isNotEmpty())
        checkDateOrderDescending(result.data)
    }

    @Test
    fun filterLaunchesByYearAndDateOrderASCSuccess() = runTest {
        insertTestData()
        val launchYear = validLaunchYears.random()

        val result = underTest.filterLaunchList(
            launchYear = launchYear,
            order = Order.ASC,
            launchStatus = LaunchStatus.ALL,
            page = PAGE,
        )

        assertTrue(result is Result.Success)
        result as Result.Success<List<LaunchTypes.Launch>>

        assertTrue(result.data.isNotEmpty())
        assertTrue { result.data.all { it.launchYear == launchYear } }
        checkDateOrderAscending(result.data)
    }

    @Test
    fun filterLaunchesByYearOrderDESCSuccess() = runTest {
        insertTestData()
        val launchYear = validLaunchYears.random()

        val result = underTest.filterLaunchList(
            launchYear = launchYear,
            order = Order.DESC,
            launchStatus = LaunchStatus.ALL,
            page = PAGE,
        )

        assertTrue(result is Result.Success)
        result as Result.Success<List<LaunchTypes.Launch>>

        assertTrue(result.data.isNotEmpty())
        assertTrue { result.data.all { it.launchYear == launchYear } }
        checkDateOrderDescending(result.data)
    }

    @Test
    fun filterLaunchesByYearNoResultsFound() = runTest {
        insertTestData()
        val invalidYear = "1000"

        val result = underTest.filterLaunchList(
            launchYear = invalidYear,
            order = Order.DESC,
            launchStatus = LaunchStatus.ALL,
            page = PAGE,
        )

        assertTrue(result is Result.Success)
        result as Result.Success<List<LaunchTypes.Launch>>

        assertTrue(result.data.isEmpty())
    }

    @Test
    fun filterLaunchesByLaunchStatusSuccess() = runTest {
        insertTestData()

        val result = underTest.filterLaunchList(
            launchYear = "",
            order = Order.DESC,
            launchStatus = LaunchStatus.SUCCESS,
            page = PAGE,
        )

        assertTrue(result is Result.Success)
        result as Result.Success<List<LaunchTypes.Launch>>

        assertTrue(result.data.isNotEmpty())
        checkDateOrderDescending(result.data)
        assertTrue { result.data.all { it.launchStatus == LaunchStatus.SUCCESS } }
    }

    @Test
    fun filterLaunchesByLaunchStatusFailed() = runTest {
        insertTestData()

        val result = underTest.filterLaunchList(
            launchYear = "",
            order = Order.DESC,
            launchStatus = LaunchStatus.FAILED,
            page = PAGE,
        )

        assertTrue(result is Result.Success)
        result as Result.Success<List<LaunchTypes.Launch>>

        assertTrue(result.data.isNotEmpty())
        checkDateOrderDescending(result.data)
        assertTrue { result.data.all { it.launchStatus == LaunchStatus.FAILED } }
    }

    @Test
    fun filterLaunchesByLaunchStatusAll() = runTest {
        val expectedResult = insertTestData()

        val result = underTest.filterLaunchList(
            launchYear = "",
            order = Order.DESC,
            launchStatus = LaunchStatus.ALL,
            page = PAGE,
        )

        assertTrue(result is Result.Success)
        result as Result.Success<List<LaunchTypes.Launch>>

        assertTrue(result.data.isNotEmpty())
        assertTrue { expectedResult.containsAll(result.data) }
        checkDateOrderDescending(result.data)
    }

    @Test
    fun filterLaunchesByLaunchStatusUnknown() = runTest {
        insertTestData()

        val result = underTest.filterLaunchList(
            launchYear = "",
            order = Order.DESC,
            launchStatus = LaunchStatus.UNKNOWN,
            page = PAGE,
        )

        assertTrue(result is Result.Success)
        result as Result.Success<List<LaunchTypes.Launch>>

        assertTrue(result.data.isNotEmpty())
        checkDateOrderDescending(result.data)
        assertTrue { result.data.all { it.launchStatus == LaunchStatus.UNKNOWN } }
    }

    @Test
    fun filterLaunchesInvalidYearByLaunchStatusNoResultsFound() = runTest {
        insertTestData()
        val year = "0000"
        val result = underTest.filterLaunchList(
            launchYear = year,
            order = Order.DESC,
            launchStatus = LaunchStatus.SUCCESS,
            page = PAGE,
        )

        assertTrue(result is Result.Success)
        result as Result.Success<List<LaunchTypes.Launch>>

        assertTrue { result.data.isEmpty() }
    }

    private fun checkDateOrderAscending(launchList: List<LaunchTypes.Launch>) {
        launchList.zipWithNext().forEach { (current, next) ->
            assertTrue(
                current.launchDateLocalDateTime <= next.launchDateLocalDateTime,
                "Dates are not in ascending order"
            )
        }
    }

    private fun checkDateOrderDescending(launches: List<LaunchTypes.Launch>) {
        launches.zipWithNext().forEach { (current, next) ->
            assertTrue(
                current.launchDateLocalDateTime >= next.launchDateLocalDateTime,
                "Dates are not in descending order"
            )
        }
    }

    private fun createLaunchListTest(
        num: Int,
        id: String = UUID.randomUUID().toString()
    ): List<LaunchTypes.Launch> {
        return List(num) {
            createRandomLaunchItem(id = id)
        }
    }

    private fun createRandomLaunchItem(id: String): LaunchTypes.Launch {
        val randomLaunchState = Random.nextInt(0, 4)
        val now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        return LaunchTypes.Launch(
            id = id,
            launchDate = now.toLocalDate().toString(),
            launchDateLocalDateTime = now,
            launchYear = validLaunchYears.random(),
            launchStatus = when (randomLaunchState) {
                0 -> LaunchStatus.SUCCESS
                1 -> LaunchStatus.FAILED
                2 -> LaunchStatus.UNKNOWN
                else -> LaunchStatus.ALL
            },
            links = Links(
                missionImage = DEFAULT_LAUNCH_IMAGE,
                articleLink = "https://www.google.com",
                webcastLink = "https://www.youtube.com",
                wikiLink = "https://www.wikipedia.org"
            ),
            missionName = UUID.randomUUID().toString(),
            rocket = Rocket(rocketNameAndType = "Falcon 9"),
            launchDateStatus = if (Random.nextBoolean()) LaunchDateStatus.PAST else LaunchDateStatus.FUTURE,
            launchDays = "${Random.nextInt(-1000, 1000)}d"
        )
    }

    private fun provideValidFilterYearDates() = listOf(
        "2022", "2021", "2020", "2019", "2018", "2017", "2016", "2015", "2014", "2013",
        "2012", "2010", "2009", "2008", "2007", "2006"
    ).shuffled()
}