package com.seancoyle.feature.launch.implementation.data.cache

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.core.domain.Order
import com.seancoyle.database.entities.LaunchDateStatusEntity
import com.seancoyle.database.entities.LaunchEntity
import com.seancoyle.database.entities.LaunchStatusEntity
import com.seancoyle.database.entities.LinksEntity
import com.seancoyle.database.entities.RocketEntity
import com.seancoyle.feature.launch.api.LaunchConstants.DEFAULT_LAUNCH_IMAGE
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.data.network.launch.LinksDto
import com.seancoyle.feature.launch.implementation.data.network.launch.PatchDto
import com.seancoyle.feature.launch.implementation.data.repository.launch.LaunchLocalDataSource
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
@RunWith(AndroidJUnit4ClassRunner::class)
internal class LaunchLocalDataSourceTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    private lateinit var validLaunchYears: List<String>

    @Inject
    lateinit var underTest: LaunchLocalDataSource

    @Before
    fun setup() {
        hiltRule.inject()
        validLaunchYears = provideValidFilterYearDates()
    }

    @After
    fun tearDown() = runTest {
        underTest.deleteAll()
    }

    private suspend fun insertTestData(num: Int = DATA): List<LaunchEntity> {
        val givenList = createLaunchListTest(num)
        underTest.insertList(givenList)
        return givenList
    }

    @Test
    fun insertLaunchGetLaunchByIdSuccess() = runTest {
        val expectedResult = createLaunchEntity(id = "1")

        underTest.insert(expectedResult)

        val result = underTest.getById(id = "1")

        assertTrue(result.isSuccess)
        assertEquals(expectedResult, result.getOrNull())
    }

    @Test
    fun insertLaunchesGetLaunchesSuccess() = runTest {
        val expectedResult = insertTestData()

        val result = underTest.getAll()

        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
        result.getOrNull()?.filterIsInstance<List<LaunchTypes.Launch>>()
        assertTrue { expectedResult.containsAll(result.getOrNull()!!) }
    }

    @Test
    fun insertLaunchesConfirmNumLaunchesInDb() = runTest {
        val expectedResult = insertTestData()

        val result = underTest.getTotalEntries()

        assertTrue(result.isSuccess)
        assertEquals(expectedResult.size, result.getOrNull())
    }

    @Test
    fun insertLaunchDeleteLaunchConfirmDeleted() = runTest {
        val expectedResult = createLaunchEntity(id = "1")
        underTest.insert(expectedResult)

        val preDeleteResult = underTest.getById(expectedResult.id)

        assertTrue(preDeleteResult.isSuccess)
        assertEquals(expectedResult, preDeleteResult.getOrNull())

        underTest.deleteById(expectedResult.id)

        val postDeleteResult = underTest.getById(expectedResult.id)
        assertTrue(postDeleteResult.isSuccess)
        assertNull(postDeleteResult.getOrNull(), "Launch item should be null after deletion.")
    }

    @Test
    fun orderAllLaunchesByDateASCConfirm() = runTest {
        insertTestData()

        val result = underTest.paginate(
            launchYear = "",
            order = Order.ASC,
            launchStatus = LaunchStatusEntity.SUCCESS,
            page = PAGE,
        )

        assertTrue(result.isSuccess)

        assertTrue(result.getOrNull()!!.isNotEmpty())
        checkDateOrderAscending(result.getOrNull()!!)
    }

    @Test
    fun orderAllLaunchesByDateDESCConfirm() = runTest {
        insertTestData()

        val result = underTest.paginate(
            launchYear = "",
            order = Order.DESC,
            launchStatus = LaunchStatusEntity.SUCCESS,
            page = PAGE,
        )

        assertTrue(result.isSuccess)

        assertTrue(result.getOrNull()!!.isNotEmpty())
        checkDateOrderDescending(result.getOrNull()!!)
    }

    @Test
    fun filterLaunchesByYearAndDateOrderASCSuccess() = runTest {
        insertTestData()
        val launchYear = validLaunchYears.random()

        val result = underTest.paginate(
            launchYear = launchYear,
            order = Order.ASC,
            launchStatus = LaunchStatusEntity.ALL,
            page = PAGE,
        )

        assertTrue(result.isSuccess)

        assertTrue(result.getOrNull()!!.isNotEmpty())
        assertTrue { result.getOrNull()!!.all { it.launchYear == launchYear } }
        checkDateOrderAscending(result.getOrNull()!!)
    }

    @Test
    fun filterLaunchesByYearOrderDESCSuccess() = runTest {
        insertTestData()
        val launchYear = validLaunchYears.random()

        val result = underTest.paginate(
            launchYear = launchYear,
            order = Order.DESC,
            launchStatus = LaunchStatusEntity.ALL,
            page = PAGE,
        )

        assertTrue(result.isSuccess)

        assertTrue(result.getOrNull()!!.isNotEmpty())
        assertTrue { result.getOrNull()!!.all { it.launchYear == launchYear } }
        checkDateOrderDescending(result.getOrNull()!!)
    }

    @Test
    fun filterLaunchesByYearNoResultsFound() = runTest {
        insertTestData()
        val invalidYear = "1000"

        val result = underTest.paginate(
            launchYear = invalidYear,
            order = Order.DESC,
            launchStatus = LaunchStatusEntity.ALL,
            page = PAGE,
        )

        assertTrue(result.isSuccess)

        assertTrue(result.getOrNull()!!.isEmpty())
    }

    @Test
    fun filterLaunchesByLaunchStatusSuccess() = runTest {
        insertTestData()

        val result = underTest.paginate(
            launchYear = "",
            order = Order.DESC,
            launchStatus = LaunchStatusEntity.SUCCESS,
            page = PAGE,
        )

        assertTrue(result.isSuccess)

        assertTrue(result.getOrNull()!!.isNotEmpty())
        checkDateOrderDescending(result.getOrNull()!!)
        assertTrue { result.getOrNull()!!.all { it.launchStatus == LaunchStatusEntity.SUCCESS } }
    }

    @Test
    fun filterLaunchesByLaunchStatusFailed() = runTest {
        insertTestData()

        val result = underTest.paginate(
            launchYear = "",
            order = Order.DESC,
            launchStatus = LaunchStatusEntity.FAILED,
            page = PAGE,
        )

        assertTrue(result.isSuccess)

        assertTrue(result.getOrNull()!!.isNotEmpty())
        checkDateOrderDescending(result.getOrNull()!!)
        assertTrue { result.getOrNull()!!.all { it.launchStatus == LaunchStatusEntity.FAILED } }
    }

    @Test
    fun filterLaunchesByLaunchStatusAll() = runTest {
        val expectedResult = insertTestData()

        val result = underTest.paginate(
            launchYear = "",
            order = Order.DESC,
            launchStatus = LaunchStatusEntity.ALL,
            page = PAGE,
        )

        assertTrue(result.isSuccess)

        assertTrue(result.getOrNull()!!.isNotEmpty())
        assertTrue { expectedResult.containsAll(result.getOrNull()!!) }
        checkDateOrderDescending(result.getOrNull()!!)
    }

    @Test
    fun filterLaunchesByLaunchStatusUnknown() = runTest {
        insertTestData()

        val result = underTest.paginate(
            launchYear = "",
            order = Order.DESC,
            launchStatus = LaunchStatusEntity.UNKNOWN,
            page = PAGE,
        )

        assertTrue(result.isSuccess)

        assertTrue(result.getOrNull()!!.isNotEmpty())
        checkDateOrderDescending(result.getOrNull()!!)
        assertTrue { result.getOrNull()!!.all { it.launchStatus == LaunchStatusEntity.UNKNOWN } }
    }

    @Test
    fun filterLaunchesInvalidYearByLaunchStatusNoResultsFound() = runTest {
        insertTestData()
        val year = "0000"
        val result = underTest.paginate(
            launchYear = year,
            order = Order.DESC,
            launchStatus = LaunchStatusEntity.SUCCESS,
            page = PAGE,
        )

        assertTrue(result.isSuccess)

        assertTrue { result.getOrNull()!!.isEmpty() }
    }

    private fun checkDateOrderAscending(launchList: List<LaunchEntity>) {
        launchList.zipWithNext().forEach { (current, next) ->
            assertTrue(
                current.launchDateLocalDateTime <= next.launchDateLocalDateTime,
                "Dates are not in ascending order"
            )
        }
    }

private fun checkDateOrderDescending(launches: List<LaunchEntity>) {
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
    ): List<LaunchEntity> {
        return List(num) {
            createLaunchEntity(id = id)
        }
    }

    private fun createLaunchEntity(id: String): LaunchEntity {
        val randomLaunchState = Random.nextInt(0, 4)
        val now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        return LaunchEntity(
            id = id,
            launchDate = now.toLocalDate().toString(),
            launchDateLocalDateTime = now,
            launchYear = validLaunchYears.random(),
            launchStatus = when (randomLaunchState) {
                0 -> LaunchStatusEntity.SUCCESS
                1 -> LaunchStatusEntity.FAILED
                2 -> LaunchStatusEntity.UNKNOWN
                else -> LaunchStatusEntity.ALL
            },
            links = LinksEntity(
                articleLink = "https://www.google.com",
                webcastLink = "https://www.youtube.com",
                wikiLink = "https://www.wikipedia.org",
                missionImage = DEFAULT_LAUNCH_IMAGE
            ),
            missionName = UUID.randomUUID().toString(),
            rocket = RocketEntity(rocketNameAndType = "Falcon 9"),
            launchDateStatus = if (Random.nextBoolean()) LaunchDateStatusEntity.PAST else LaunchDateStatusEntity.FUTURE,
            launchDays = "${Random.nextInt(-1000, 1000)}d",
            isLaunchSuccess = true
        )
    }

    private fun provideValidFilterYearDates() = listOf(
        "2022", "2021", "2020", "2019", "2018", "2017", "2016", "2015", "2014", "2013",
        "2012", "2010", "2009", "2008", "2007", "2006"
    ).shuffled()
}