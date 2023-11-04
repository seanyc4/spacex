package com.seancoyle.launch.implementation.cache

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.core_database.api.LaunchDao
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
import com.seancoyle.launch.implementation.data.cache.LaunchEntityMapper
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

const val PAGE_ALL = 100000

@ExperimentalCoroutinesApi
@FlowPreview
@HiltAndroidTest
@RunWith(AndroidJUnit4ClassRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class LaunchDaoServiceTests {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var dao: LaunchDao

    @Inject
    lateinit var launchEntityMapper: LaunchEntityMapper

    @Inject
    lateinit var factory: LaunchFactory

    private lateinit var validLaunchYears: List<String>

    @Inject
    lateinit var underTest: LaunchCacheDataSource

    @Before
    fun setup() {
        hiltRule.inject()
        insertTestData()
        validLaunchYears = provideValidFilterYearDates()
    }


    private fun insertTestData() = runBlocking {
        val entityList = launchEntityMapper.mapDomainListToEntityList(
            createLaunchListTest(1000)
        )
        dao.insertList(entityList)
    }

    /**
     * This test runs first. Check to make sure the test data was inserted from
     * CacheTest class.
     */
    @Test
    fun searchLaunchTable_confirmDbNotEmpty() = runBlocking {

        val totalNum = underTest.getTotalEntries()

        assertTrue { totalNum > 0 }

    }

    @Test
    fun insertLaunchItem_getLaunchItem_success() = runBlocking {

        val newLaunchItem = createLaunchItem(
            id = "1",
            launchDate = UUID.randomUUID().toString(),
            launchDateLocalDateTime = LocalDateTime.now(),
            isLaunchSuccess = 2,
            launchSuccessIcon = R.drawable.ic_launch_success,
            launchYear = UUID.randomUUID().toString(),
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

        underTest.insert(newLaunchItem)

        val cachedLaunchItem = underTest.getById("1")
        assertEquals(cachedLaunchItem, newLaunchItem)
    }

    @Test
    fun insertLaunchList_getLaunchItem_success() = runBlocking {

        val launchList = createLaunchListTest(
            num = 1000,
            null
        )
        underTest.insertList(launchList)

        val cachedLaunchList = underTest.getAll()

        assertTrue { cachedLaunchList?.containsAll(launchList) == true }
    }

    @Test
    fun insert1000LaunchItems_confirmNumLaunchItemsInDb() = runBlocking {
        val currentNumLaunchItems = underTest.getTotalEntries()

        // insert 1000 launch items
        val launchList = createLaunchListTest(
            num = 1000,
            null
        )
        underTest.insertList(launchList)

        val cachedLaunchList = underTest.getTotalEntries()
        assertEquals(currentNumLaunchItems + 1000, cachedLaunchList)
    }

    @Test
    fun insertLaunch_deleteLaunch_confirmDeleted() = runBlocking {
        val newLaunchItem = createLaunchItem(
            id = "2",
            launchDate = UUID.randomUUID().toString(),
            launchDateLocalDateTime = LocalDateTime.now(),
            isLaunchSuccess = 1,
            launchSuccessIcon = R.drawable.ic_launch_success,
            launchYear = UUID.randomUUID().toString(),
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
        underTest.insert(newLaunchItem)

        var cachedLaunchItem = underTest.getById(newLaunchItem.id)
        assert(cachedLaunchItem == newLaunchItem)

        underTest.deleteById(newLaunchItem.id)
        cachedLaunchItem = underTest.getById(newLaunchItem.id)
        assert(cachedLaunchItem != newLaunchItem)
    }

    @Test
    fun deleteLaunchList_confirmDeleted() = runBlocking {
        val viewTypeList = mutableListOf(underTest.getAll() ?: emptyList())

        val launchList = viewTypeList.filterIsInstance<Launch>().toMutableList()

        // select some random launch for deleting
        val launchesToDelete: ArrayList<Launch> = ArrayList()

        // 1st
        var launchItemToDelete = launchList[Random.nextInt(0, launchList.size - 1) + 1]
        launchList.remove(launchItemToDelete)
        launchesToDelete.add(launchItemToDelete)

        // 2nd
        launchItemToDelete = launchList[Random.nextInt(0, launchList.size - 1) + 1]
        launchList.remove(launchItemToDelete)
        launchesToDelete.add(launchItemToDelete)

        // 3rd
        launchItemToDelete = launchList[Random.nextInt(0, launchList.size - 1) + 1]
        launchList.remove(launchItemToDelete)
        launchesToDelete.add(launchItemToDelete)

        // 4th
        launchItemToDelete = launchList[Random.nextInt(0, launchList.size - 1) + 1]
        launchList.remove(launchItemToDelete)
        launchesToDelete.add(launchItemToDelete)

        underTest.deleteList(launchesToDelete)

        // confirm they were deleted
        val searchResults = underTest.getAll()
        assertFalse { searchResults == launchesToDelete }
    }

    @Test
    fun orderAllLaunchItemsByDateASC_confirm() = runBlocking {

        val viewTypeList = underTest.filterLaunchList(
            year = null,
            order = ORDER_ASC,
            launchFilter = LAUNCH_SUCCESS,
            page = PAGE_ALL,
        )

        val launchList = viewTypeList!!.filterIsInstance<Launch>()

        assertTrue(launchList.isNotEmpty())
        checkDateOrderAscending(launchList)
    }

    @Test
    fun orderAllLaunchItemsByDateDESC_confirm() = runBlocking {

        val viewTypeList = underTest.filterLaunchList(
            year = null,
            order = ORDER_DESC,
            launchFilter = LAUNCH_SUCCESS,
            page = PAGE_ALL,
        )

        val launchList = viewTypeList!!.filterIsInstance<Launch>()

        assertTrue(viewTypeList.isNotEmpty())
        checkDateOrderDescending(launchList)
    }

    @Test
    fun filterLaunchItemsByYearAndDateOrderASC_success() = runBlocking {

        val launchYear = validLaunchYears[Random.nextInt(validLaunchYears.size)]

        val viewTypeList = underTest.filterLaunchList(
            year = launchYear,
            order = ORDER_ASC,
            launchFilter = null,
            page = PAGE_ALL,
        )

        val launchList = viewTypeList!!.filterIsInstance<Launch>()

        assertTrue(launchList.isNotEmpty())
        assertTrue { launchList.all { it.launchYear == launchYear } }
        checkDateOrderAscending(launchList)
    }

    @Test
    fun filterLaunchItemsByYearAndDateOrderDESC_success() = runBlocking {

        val launchYear = validLaunchYears[Random.nextInt(validLaunchYears.size)]

        val viewTypeList = underTest.filterLaunchList(
            year = launchYear,
            order = ORDER_DESC,
            launchFilter = null,
            page = PAGE_ALL,
        )

        val launchList = viewTypeList!!.filterIsInstance<Launch>()

        assertTrue(launchList.isNotEmpty())
        assertTrue { launchList.all { it.launchYear == launchYear } }
        checkDateOrderDescending(launchList)
    }

    @Test
    fun filterLaunchItemsByYear_noResultsFound() = runBlocking {

        val launchYear = "1000"

        val launchList = underTest.filterLaunchList(
            year = launchYear,
            order = ORDER_DESC,
            launchFilter = null,
            page = PAGE_ALL,
        )

        assertTrue(launchList?.isEmpty() == true)
    }

    @Test
    fun filterLaunchItemsByLaunchStatus_launchSuccess() = runBlocking {

        val viewTypeList = underTest.filterLaunchList(
            year = null,
            order = ORDER_DESC,
            launchFilter = LAUNCH_SUCCESS,
            page = PAGE_ALL,
        )

        val launchList = viewTypeList!!.filterIsInstance<Launch>()

        assertTrue(launchList.isNotEmpty())
        checkDateOrderDescending(launchList)
        assertTrue { launchList.all { it.isLaunchSuccess == LAUNCH_SUCCESS } }
    }

    @Test
    fun filterLaunchItemsByLaunchStatus_launchFailed() = runBlocking {

        val viewTypeList = underTest.filterLaunchList(
            year = null,
            order = ORDER_DESC,
            launchFilter = LAUNCH_FAILED,
            page = PAGE_ALL,
        )

        val launchList = viewTypeList!!.filterIsInstance<Launch>()

        assertTrue(launchList.isNotEmpty())
        checkDateOrderDescending(launchList)
        assertTrue { launchList.all { it.isLaunchSuccess == LAUNCH_FAILED } }
    }

    @Test
    fun filterLaunchItemsByLaunchStatus_launchAll() = runBlocking {

        val allLaunchItems = underTest.getAll()

        val viewTypeList = underTest.filterLaunchList(
            year = null,
            order = ORDER_DESC,
            launchFilter = null,
            page = PAGE_ALL,
        )

        val launchList = viewTypeList!!.filterIsInstance<Launch>()

        assertTrue(launchList.isNotEmpty())
        checkDateOrderDescending(launchList)
        assertTrue { viewTypeList.containsAll(allLaunchItems!!) }
    }

    @Test
    fun filterLaunchItemsByLaunchStatus_unknown() = runBlocking {

        val viewTypeList = underTest.filterLaunchList(
            year = null,
            order = ORDER_DESC,
            launchFilter = LAUNCH_UNKNOWN,
            page = PAGE_ALL,
        )

        val launchList = viewTypeList!!.filterIsInstance<Launch>()

        assertTrue(viewTypeList.isNotEmpty())
        checkDateOrderDescending(launchList)
        assertTrue { launchList.all { it.isLaunchSuccess == LAUNCH_UNKNOWN } }
    }

    @Test
    fun filterLaunchItemsByLaunchStatus_noResultsFound() = runBlocking {
        // Year set to 2006 as there were only launch failures that year
        val year = "0000"

        val launchList = underTest.filterLaunchList(
            year = year,
            order = ORDER_DESC,
            launchFilter = LAUNCH_SUCCESS,
            page = PAGE_ALL,
        )

        // No items should be returned with a launchYear of 2006 and LAUNCH_SUCCESS
        assertTrue { launchList?.isEmpty() == true }
    }

    @Test
    fun filterLaunchItemsFail_noResultsFound() = runBlocking {
        val FORCE_SEARCH_LAUNCH_EXCEPTION = "FORCE_SEARCH_LAUNCH_EXCEPTION"
        val launchList = underTest.filterLaunchList(
            year = FORCE_SEARCH_LAUNCH_EXCEPTION,
            order = ORDER_DESC,
            launchFilter = LAUNCH_SUCCESS,
            page = PAGE_ALL,
        )

        assertTrue { launchList?.isEmpty() == true }

        // confirm there are launch items in the cache
        val launchItemsInCache = underTest.getAll()
        if (launchItemsInCache != null) {
            assertTrue { launchItemsInCache.isNotEmpty() }
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

    private fun provideValidFilterYearDates() = listOf(
        "2022", "2021", "2020", "2019", "2018", "2017", "2016", "2015", "2014", "2013",
        "2012", "2010", "2009", "2008", "2007", "2006"
    ).shuffled()

    private fun createLaunchListTest(
        num: Int,
        id: String? = null
    ): List<Launch> {
        val list: ArrayList<Launch> = ArrayList()

        for (item in 0 until num) {
            // Generate a random launch state
            val randomLaunchState = when (Random.nextInt(3)) {
                0 -> LAUNCH_SUCCESS
                1 -> LAUNCH_FAILED
                else -> LAUNCH_UNKNOWN
            }

            val launchYear = provideValidFilterYearDates().first()

            list.add(
                createLaunchItem(
                    id = id ?: UUID.randomUUID().toString(),
                    launchDate = UUID.randomUUID().toString(),
                    launchDateLocalDateTime = LocalDateTime.now(),
                    isLaunchSuccess = randomLaunchState,
                    launchSuccessIcon = R.drawable.ic_launch_success, // You might want to change icon based on state
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

}