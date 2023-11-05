package com.seancoyle.launch.implementation.cache

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.launch.api.LaunchNetworkConstants.DEFAULT_LAUNCH_IMAGE
import com.seancoyle.launch.api.LaunchNetworkConstants.LAUNCH_FAILED
import com.seancoyle.launch.api.LaunchNetworkConstants.LAUNCH_SUCCESS
import com.seancoyle.launch.api.LaunchNetworkConstants.LAUNCH_UNKNOWN
import com.seancoyle.launch.api.LaunchNetworkConstants.ORDER_ASC
import com.seancoyle.launch.api.LaunchNetworkConstants.ORDER_DESC
import com.seancoyle.launch.api.data.LaunchCacheDataSource
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.api.domain.model.Links
import com.seancoyle.launch.api.domain.model.Rocket
import com.seancoyle.launch.api.domain.model.ViewType.Companion.TYPE_LIST
import com.seancoyle.launch.implementation.LaunchFactory
import com.seancoyle.launch.implementation.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import javax.inject.Inject
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

private const val PAGE = 1
private const val DATA = 100

@ExperimentalCoroutinesApi
@FlowPreview
@HiltAndroidTest
@Suppress("UNCHECKED_CAST")
@RunWith(AndroidJUnit4ClassRunner::class)
class LaunchCacheDataSourceTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var factory: LaunchFactory

    private lateinit var validLaunchYears: List<String>

    @Inject
    lateinit var underTest: LaunchCacheDataSource

    @Before
    fun setup() {
        hiltRule.inject()
        validLaunchYears = factory.provideValidFilterYearDates()
    }

    @After
    fun tearDown() = runTest {
        underTest.deleteAll()
    }

    private suspend fun insertTestData(num: Int = DATA): List<Launch> {
        val givenList = createLaunchListTest(num)
        underTest.insertList(givenList)
        return givenList
    }

    @Test
    fun insertLaunch_getLaunchById_success() = runTest {
        val givenLaunch = createRandomLaunchItem(id = "1")

        underTest.insert(givenLaunch)

        val result = underTest.getById("1")
        assertEquals(result, givenLaunch)
    }

    @Test
    fun insertLaunches_getLaunches_success() = runTest {
        val givenLaunches = insertTestData()
        val resultViewTypeList = underTest.getAll()
        val result = resultViewTypeList as? List<Launch>

        assertTrue { givenLaunches.containsAll(result!!) }
    }

    @Test
    fun insertLaunches_confirmNumLaunchesInDb() = runTest {
        val givenLaunches = insertTestData()
        val result = underTest.getTotalEntries()
        assertEquals(result, givenLaunches.size)
    }

    @Test
    fun insertLaunch_deleteLaunch_confirmDeleted() = runTest {
        val givenLaunch = createRandomLaunchItem(id = "2")
        underTest.insert(givenLaunch)

        val preDeleteResult = underTest.getById(givenLaunch.id)
        assertEquals(givenLaunch, preDeleteResult)

        underTest.deleteById(givenLaunch.id)

        val postDeleteResult = underTest.getById(givenLaunch.id)
        assertNull(postDeleteResult, "Launch item should be null after deletion.")
    }

    @Test
    fun orderAllLaunchItemsByDateASC_confirm() = runTest {
        insertTestData()
        val viewTypeList = underTest.filterLaunchList(
            year = null,
            order = ORDER_ASC,
            launchFilter = LAUNCH_SUCCESS,
            page = PAGE,
        )

        val result = viewTypeList!!.filterIsInstance<Launch>()

        assertTrue(result.isNotEmpty())
        checkDateOrderAscending(result)
    }

    @Test
    fun orderAllLaunchItemsByDateDESC_confirm() = runTest {
        insertTestData()
        val viewTypeList = underTest.filterLaunchList(
            year = null,
            order = ORDER_DESC,
            launchFilter = LAUNCH_SUCCESS,
            page = PAGE,
        )

        val result = viewTypeList!!.filterIsInstance<Launch>()

        assertTrue(viewTypeList.isNotEmpty())
        checkDateOrderDescending(result)
    }

    @Test
    fun filterLaunchItemsByYearAndDateOrderASC_success() = runTest {
        insertTestData()
        val launchYear = validLaunchYears.random()
        val viewTypeList = underTest.filterLaunchList(
            year = launchYear,
            order = ORDER_ASC,
            launchFilter = null,
            page = PAGE,
        )
        val result = viewTypeList!!.filterIsInstance<Launch>()

        assertTrue(result.isNotEmpty())
        assertTrue { result.all { it.launchYear == launchYear } }
        checkDateOrderAscending(result)
    }

    @Test
    fun filterLaunchItemsByYearAndDateOrderDESC_success() = runTest {
        insertTestData()
        val launchYear = validLaunchYears.random()
        val viewTypeList = underTest.filterLaunchList(
            year = launchYear,
            order = ORDER_DESC,
            launchFilter = null,
            page = PAGE,
        )
        val result = viewTypeList!!.filterIsInstance<Launch>()

        assertTrue(result.isNotEmpty())
        assertTrue { result.all { it.launchYear == launchYear } }
        checkDateOrderDescending(result)
    }

    @Test
    fun filterLaunchItemsByYear_noResultsFound() = runTest {
        insertTestData()
        val launchYear = "1000"
        val result = underTest.filterLaunchList(
            year = launchYear,
            order = ORDER_DESC,
            launchFilter = null,
            page = PAGE,
        )

        assertTrue(result?.isEmpty() == true)
    }

    @Test
    fun filterLaunchItemsByLaunchStatus_launchSuccess() = runTest {
        insertTestData()
        val viewTypeList = underTest.filterLaunchList(
            year = null,
            order = ORDER_DESC,
            launchFilter = LAUNCH_SUCCESS,
            page = PAGE,
        )
        val result = viewTypeList!!.filterIsInstance<Launch>()

        assertTrue(result.isNotEmpty())
        checkDateOrderDescending(result)
        assertTrue { result.all { it.isLaunchSuccess == LAUNCH_SUCCESS } }
    }

    @Test
    fun filterLaunchItemsByLaunchStatus_launchFailed() = runTest {
        insertTestData()
        val viewTypeList = underTest.filterLaunchList(
            year = null,
            order = ORDER_DESC,
            launchFilter = LAUNCH_FAILED,
            page = PAGE,
        )
        val result = viewTypeList!!.filterIsInstance<Launch>()

        assertTrue(result.isNotEmpty())
        checkDateOrderDescending(result)
        assertTrue { result.all { it.isLaunchSuccess == LAUNCH_FAILED } }
    }

    @Test
    fun filterLaunchItemsByLaunchStatus_launchAll() = runTest {
        insertTestData(30)
        val allLaunchItems = underTest.getAll()
        val viewTypeList = underTest.filterLaunchList(
            year = null,
            order = ORDER_DESC,
            launchFilter = null,
            page = PAGE,
        )
        val result = viewTypeList!!.filterIsInstance<Launch>()

        assertTrue(result.isNotEmpty())
        assertTrue { viewTypeList.containsAll(allLaunchItems!!) }
        checkDateOrderDescending(result)
    }

    @Test
    fun filterLaunchItemsByLaunchStatus_unknown() = runTest {
        insertTestData()
        val viewTypeList = underTest.filterLaunchList(
            year = null,
            order = ORDER_DESC,
            launchFilter = LAUNCH_UNKNOWN,
            page = PAGE,
        )
        val result = viewTypeList!!.filterIsInstance<Launch>()

        assertTrue(viewTypeList.isNotEmpty())
        checkDateOrderDescending(result)
        assertTrue { result.all { it.isLaunchSuccess == LAUNCH_UNKNOWN } }
    }

    @Test
    fun filterLaunchItemsByLaunchStatus_noResultsFound() = runTest {
        insertTestData()
        val year = "0000"
        val result = underTest.filterLaunchList(
            year = year,
            order = ORDER_DESC,
            launchFilter = LAUNCH_SUCCESS,
            page = PAGE,
        )

        assertTrue { result?.isEmpty() == true }
    }

    @Test
    fun filterLaunchItemsFail_noResultsFound() = runTest {
        insertTestData()
        val FORCE_SEARCH_LAUNCH_EXCEPTION = "FORCE_SEARCH_LAUNCH_EXCEPTION"
        val launchList = underTest.filterLaunchList(
            year = FORCE_SEARCH_LAUNCH_EXCEPTION,
            order = ORDER_DESC,
            launchFilter = LAUNCH_SUCCESS,
            page = PAGE,
        )

        assertTrue { launchList?.isEmpty() == true }

        // confirm there are launch items in the cache
        val result = underTest.getAll()
        if (result != null) {
            assertTrue { result.isNotEmpty() }
        }
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

    private fun createLaunchListTest(
        num: Int,
        id: String? = null
    ): List<Launch> {
        val list: ArrayList<Launch> = ArrayList()

        for (item in 0 until num) {
            // Generate a random launch state
            val randomLaunchState = when (Random.nextInt(3)) {
                STATE_SUCCESS -> LAUNCH_SUCCESS
                STATE_FAILED -> LAUNCH_FAILED
                STATE_UNKNOWN -> LAUNCH_UNKNOWN
                else -> throw IllegalStateException("Invalid launch state")
            }

            val launchYear = validLaunchYears.random()

            list.add(
                createLaunchItem(
                    id = id ?: UUID.randomUUID().toString(),
                    launchDate = UUID.randomUUID().toString(),
                    launchDateLocalDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
                    isLaunchSuccess = randomLaunchState,
                    launchSuccessIcon = getIconBasedOnState(randomLaunchState),
                    launchYear = launchYear,
                    links = Links(
                        missionImage = DEFAULT_LAUNCH_IMAGE,
                        articleLink = "https://www.google.com",
                        webcastLink = "https://www.youtube.com",
                        wikiLink = "https://www.wikipedia.com"
                    ),
                    missionName = UUID.randomUUID().toString(),
                    rocket = Rocket(
                        rocketNameAndType = UUID.randomUUID().toString()
                    ),
                    daysToFromTitle = UUID.randomUUID().hashCode(),
                    launchDaysDifference = UUID.randomUUID().toString(),
                    type = TYPE_LIST
                )
            )
        }
        return list
    }

    private fun createRandomLaunchItem(id: String? = null): Launch {
        val randomLaunchState = Random.nextInt(0, 3)
        return createLaunchItem(
            id = id ?: UUID.randomUUID().toString(),
            launchDate = UUID.randomUUID().toString(),
            launchDateLocalDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
            isLaunchSuccess = randomLaunchState,
            launchSuccessIcon = getIconBasedOnState(randomLaunchState),
            launchYear = validLaunchYears.random(),
            links = Links(
                missionImage = DEFAULT_LAUNCH_IMAGE,
                articleLink = "https://www.google.com",
                webcastLink = "https://www.youtube.com",
                wikiLink = "https://www.wikipedia.com"
            ),
            missionName = UUID.randomUUID().toString(),
            rocket = Rocket(
                rocketNameAndType = UUID.randomUUID().toString()
            ),
            daysToFromTitle = UUID.randomUUID().hashCode(),
            launchDaysDifference = UUID.randomUUID().toString(),
            type = TYPE_LIST,
        )
    }

    private fun getIconBasedOnState(state: Int): Int {
        return when (state) {
            LAUNCH_SUCCESS -> R.drawable.ic_launch_success
            LAUNCH_FAILED -> R.drawable.ic_launch_fail
            LAUNCH_UNKNOWN -> R.drawable.ic_launch_unknown
            else -> throw IllegalArgumentException("Invalid launch state")
        }
    }

    private fun createLaunchItem(
        id: String,
        launchDate: String,
        isLaunchSuccess: Int,
        launchSuccessIcon: Int,
        launchDateLocalDateTime: LocalDateTime,
        launchYear: String,
        links: Links,
        missionName: String,
        rocket: Rocket,
        daysToFromTitle: Int,
        launchDaysDifference: String,
        type: Int
    ) = factory.createLaunchItem(
        id = id,
        launchDate = launchDate,
        isLaunchSuccess = isLaunchSuccess,
        launchSuccessIcon = launchSuccessIcon,
        launchDateLocalDateTime = launchDateLocalDateTime,
        launchYear = launchYear,
        links = links,
        missionName = missionName,
        rocket = rocket,
        daysToFromTitle = daysToFromTitle,
        launchDaysDifference = launchDaysDifference,
        type = type,
    )

    companion object {
        const val STATE_SUCCESS = 0
        const val STATE_FAILED = 1
        const val STATE_UNKNOWN = 2
    }
}