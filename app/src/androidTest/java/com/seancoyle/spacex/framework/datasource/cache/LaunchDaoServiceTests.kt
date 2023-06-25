package com.seancoyle.spacex.framework.datasource.cache

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.core.Constants.ORDER_ASC
import com.seancoyle.core.Constants.ORDER_DESC
import com.seancoyle.database.daos.LaunchDao
import com.seancoyle.launch.api.LaunchNetworkConstants.DEFAULT_LAUNCH_IMAGE
import com.seancoyle.launch.api.LaunchNetworkConstants.LAUNCH_FAILED
import com.seancoyle.launch.api.LaunchNetworkConstants.LAUNCH_SUCCESS
import com.seancoyle.launch.api.LaunchNetworkConstants.LAUNCH_UNKNOWN
import com.seancoyle.launch.api.data.LaunchCacheDataSource
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.api.domain.model.Links
import com.seancoyle.launch.api.domain.model.Rocket
import com.seancoyle.launch.api.domain.model.ViewType.Companion.TYPE_LIST
import com.seancoyle.launch.implementation.data.cache.LaunchCacheDataSourceImpl
import com.seancoyle.launch.implementation.data.cache.LaunchEntityMapper
import com.seancoyle.spacex.LaunchDataFactory
import com.seancoyle.spacex.R
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
    lateinit var launchDataFactory: LaunchDataFactory

    @Inject
    lateinit var launchEntityMapper: LaunchEntityMapper

    private lateinit var validLaunchYears: List<String>

    private lateinit var underTest: LaunchCacheDataSource

    @Before
    fun setup() {
        hiltRule.inject()
        insertTestData()
        underTest = LaunchCacheDataSourceImpl(
            dao = dao,
            entityMapper = launchEntityMapper
        )
        validLaunchYears = launchDataFactory.provideValidFilterYearDates()
    }


    private fun insertTestData() = runBlocking {
        val entityList = launchEntityMapper.mapDomainListToEntityList(
            launchDataFactory.parseJsonFile()
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

        val newLaunchItem = launchDataFactory.createLaunchItem(
            id = 1,
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

        val cachedLaunchItem = underTest.getById(1)
        assertEquals(cachedLaunchItem, newLaunchItem)
    }

    @Test
    fun insertLaunchList_getLaunchItem_success() = runBlocking {

        val launchList = launchDataFactory.createLaunchListTest(
            num = 10,
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
        val launchList = launchDataFactory.createLaunchListTest(
            num = 1000,
            null
        )
        underTest.insertList(launchList)

        val cachedLaunchList = underTest.getTotalEntries()
        assertEquals(currentNumLaunchItems + 1000, cachedLaunchList)
    }

    @Test
    fun insertLaunch_deleteLaunch_confirmDeleted() = runBlocking {
        val newLaunchItem = launchDataFactory.createLaunchItem(
            id = 2,
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
        val launchList: ArrayList<Launch> = ArrayList(underTest.getAll() ?: emptyList())

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

        val launchList = underTest.filterLaunchList(
            year = null,
            order = ORDER_ASC,
            launchFilter = LAUNCH_SUCCESS,
            page = PAGE_ALL,
        )

        assertTrue(!launchList.isNullOrEmpty())
        checkDateOrderAscending(launchList = launchList)
    }

    @Test
    fun orderAllLaunchItemsByDateDESC_confirm() = runBlocking {

        val launchList = underTest.filterLaunchList(
            year = null,
            order = ORDER_DESC,
            launchFilter = LAUNCH_SUCCESS,
            page = PAGE_ALL,
        )

        assertTrue(!launchList.isNullOrEmpty())
        checkDateOrderDescending(launchList = launchList)
    }

    @Test
    fun filterLaunchItemsByYearAndDateOrderASC_success() = runBlocking {

        val launchYear = validLaunchYears[Random.nextInt(validLaunchYears.size)]

        val launchList = underTest.filterLaunchList(
            year = launchYear,
            order = ORDER_ASC,
            launchFilter = null,
            page = PAGE_ALL,
        )

        assertTrue(!launchList.isNullOrEmpty())
        assertTrue { launchList.all { it.launchYear == launchYear } }
        checkDateOrderAscending(launchList = launchList)
    }

    @Test
    fun filterLaunchItemsByYearAndDateOrderDESC_success() = runBlocking {

        val launchYear = validLaunchYears[Random.nextInt(validLaunchYears.size)]

        val launchList = underTest.filterLaunchList(
            year = launchYear,
            order = ORDER_DESC,
            launchFilter = null,
            page = PAGE_ALL,
        )

        assertTrue(!launchList.isNullOrEmpty())
        assertTrue { launchList.all { it.launchYear == launchYear } }
        checkDateOrderDescending(launchList = launchList)
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

        val launchList = underTest.filterLaunchList(
            year = null,
            order = ORDER_DESC,
            launchFilter = LAUNCH_SUCCESS,
            page = PAGE_ALL,
        )

        assertTrue(!launchList.isNullOrEmpty())
        checkDateOrderDescending(launchList)
        assertTrue { launchList.all { it.isLaunchSuccess == LAUNCH_SUCCESS } }
    }

    @Test
    fun filterLaunchItemsByLaunchStatus_launchFailed() = runBlocking {

        val launchList = underTest.filterLaunchList(
            year = null,
            order = ORDER_DESC,
            launchFilter = LAUNCH_FAILED,
            page = PAGE_ALL,
        )

        assertTrue(!launchList.isNullOrEmpty())
        checkDateOrderDescending(launchList)
        assertTrue { launchList.all { it.isLaunchSuccess == LAUNCH_FAILED } }
    }

    @Test
    fun filterLaunchItemsByLaunchStatus_launchAll() = runBlocking {

        val allLaunchItems = underTest.getAll()

        val launchList = underTest.filterLaunchList(
            year = null,
            order = ORDER_DESC,
            launchFilter = null,
            page = PAGE_ALL,
        )

        assertTrue(!launchList.isNullOrEmpty())
        checkDateOrderDescending(launchList)
        assertTrue { launchList.containsAll(allLaunchItems!!) }
    }

    @Test
    fun filterLaunchItemsByLaunchStatus_unknown() = runBlocking {

        val launchList = underTest.filterLaunchList(
            year = null,
            order = ORDER_DESC,
            launchFilter = LAUNCH_UNKNOWN,
            page = PAGE_ALL,
        )

        assertTrue(!launchList.isNullOrEmpty())
        checkDateOrderDescending(launchList)
        assertTrue { launchList.all { it.isLaunchSuccess == LAUNCH_UNKNOWN } }
    }

    @Test
    fun filterLaunchItemsByLaunchStatus_noResultsFound() = runBlocking {
        // Year set to 2006 as there were only launch failures that year
        val year = "2006"

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


}














