package com.seancoyle.spacex.framework.datasource.cache.launch

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.spacex.R
import com.seancoyle.launch_domain.model.launch.LaunchModel
import com.seancoyle.launch_domain.model.launch.LaunchType.Companion.TYPE_LAUNCH
import com.seancoyle.launch_domain.model.launch.Links
import com.seancoyle.launch_domain.model.launch.Rocket
import com.seancoyle.spacex.framework.datasource.cache.abstraction.launch.LaunchDaoService
import com.seancoyle.launch_datasource.cache.dao.launch.LAUNCH_ORDER_ASC
import com.seancoyle.launch_datasource.cache.dao.launch.LAUNCH_ORDER_DESC
import com.seancoyle.launch_datasource.cache.dao.launch.LaunchDao
import com.seancoyle.spacex.framework.datasource.cache.implementation.launch.LaunchDaoServiceImpl
import com.seancoyle.launch_datasource.cache.mappers.launch.LaunchEntityMapper
import com.seancoyle.spacex.framework.datasource.data.LaunchDataFactory
import com.seancoyle.launch_datasource.network.mappers.launch.*
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

    // system in test
    private lateinit var launchDaoService: LaunchDaoService

    @Inject
    lateinit var dao: LaunchDao

    @Inject
    lateinit var launchDataFactory: LaunchDataFactory

    @Inject
    lateinit var launchEntityMapper: LaunchEntityMapper

    lateinit var validLaunchYears: List<String>

    @Before
    fun setup() {
        hiltRule.inject()
        insertTestData()
        launchDaoService = LaunchDaoServiceImpl(
            dao = dao,
            entityMapper = launchEntityMapper
        )
        validLaunchYears = launchDataFactory.provideValidFilterYearDates()
    }


    private fun insertTestData() = runBlocking {
        val entityList = launchEntityMapper.domainListToEntityList(
            launchDataFactory.produceListOfLaunches()
        )
        dao.insertList(entityList)
    }

    /**
     * This test runs first. Check to make sure the test data was inserted from
     * CacheTest class.
     */
    @Test
    fun a_searchLaunchTable_confirmDbNotEmpty() = runBlocking {

        val totalNum = launchDaoService.getTotalEntries()

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
                videoLink = "https://www.youtube.com",
                wikipedia = "https://www.wikipedia.com"
            ),
            missionName = UUID.randomUUID().toString(),
            rocket = Rocket(
                rocketNameAndType = UUID.randomUUID().toString()
            ),
            daysToFromTitle = UUID.randomUUID().hashCode(),
            launchDaysDifference = UUID.randomUUID().toString(),
            type = TYPE_LAUNCH,
        )

        launchDaoService.insert(newLaunchItem)

        val cachedLaunchItem = launchDaoService.getById(1)
        assertEquals(cachedLaunchItem, newLaunchItem)
    }

    @Test
    fun insertLaunchList_getLaunchItem_success() = runBlocking {

        val launchList = launchDataFactory.createLaunchListTest(
            num = 10,
            null
        )
        launchDaoService.insertList(launchList)

        val cachedLaunchList = launchDaoService.getAll()

        assertTrue { cachedLaunchList?.containsAll(launchList) == true }
    }

    @Test
    fun insert1000LaunchItems_confirmNumLaunchItemsInDb() = runBlocking {
        val currentNumLaunchItems = launchDaoService.getTotalEntries()

        // insert 1000 launch items
        val launchList = launchDataFactory.createLaunchListTest(
            num = 1000,
            null
        )
        launchDaoService.insertList(launchList)

        val cachedLaunchList = launchDaoService.getTotalEntries()
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
                videoLink = "https://www.youtube.com",
                wikipedia = "https://www.wikipedia.com"
            ),
            missionName = UUID.randomUUID().toString(),
            rocket = Rocket(
                rocketNameAndType = UUID.randomUUID().toString()
            ),
            daysToFromTitle = UUID.randomUUID().hashCode(),
            launchDaysDifference = UUID.randomUUID().toString(),
            type = TYPE_LAUNCH,
        )
        launchDaoService.insert(newLaunchItem)

        var cachedLaunchItem = launchDaoService.getById(newLaunchItem.id)
        assert(cachedLaunchItem == newLaunchItem)

        launchDaoService.deleteById(newLaunchItem.id)
        cachedLaunchItem = launchDaoService.getById(newLaunchItem.id)
        assert(cachedLaunchItem != newLaunchItem)
    }

    @Test
    fun deleteLaunchList_confirmDeleted() = runBlocking {
        val launchList: ArrayList<LaunchModel> = ArrayList(launchDaoService.getAll()!!)

        // select some random launch for deleting
        val launchesToDelete: ArrayList<LaunchModel> = ArrayList()

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

        launchDaoService.deleteList(launchesToDelete)

        // confirm they were deleted
        val searchResults = launchDaoService.getAll()
        assertFalse { searchResults == launchesToDelete }
    }

    @Test
    fun orderAllLaunchItemsByDateASC_confirm() = runBlocking {

        val launchList = launchDaoService.filterLaunchList(
            year = null,
            order = LAUNCH_ORDER_ASC,
            launchFilter = LAUNCH_SUCCESS,
            page = PAGE_ALL,
        )

        assertTrue(!launchList.isNullOrEmpty())
        checkDateOrderAscending(launchList = launchList)
    }

    @Test
    fun orderAllLaunchItemsByDateDESC_confirm() = runBlocking {

        val launchList = launchDaoService.filterLaunchList(
            year = null,
            order = LAUNCH_ORDER_DESC,
            launchFilter = LAUNCH_SUCCESS,
            page = PAGE_ALL,
        )

        assertTrue(!launchList.isNullOrEmpty())
        checkDateOrderDescending(launchList = launchList)
    }

    @Test
    fun filterLaunchItemsByYearAndDateOrderASC_success() = runBlocking {

        val launchYear = validLaunchYears[Random.nextInt(validLaunchYears.size)]

        val launchList = launchDaoService.filterLaunchList(
            year = launchYear,
            order = LAUNCH_ORDER_ASC,
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

        val launchList = launchDaoService.filterLaunchList(
            year = launchYear,
            order = LAUNCH_ORDER_DESC,
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

        val launchList = launchDaoService.filterLaunchList(
            year = launchYear,
            order = LAUNCH_ORDER_DESC,
            launchFilter = null,
            page = PAGE_ALL,
        )

        assertTrue(launchList?.isEmpty() == true)
    }

    @Test
    fun filterLaunchItemsByLaunchStatus_launchSuccess() = runBlocking {

        val launchList = launchDaoService.filterLaunchList(
            year = null,
            order = LAUNCH_ORDER_DESC,
            launchFilter = LAUNCH_SUCCESS,
            page = PAGE_ALL,
        )

        assertTrue(!launchList.isNullOrEmpty())
        checkDateOrderDescending(launchList)
        assertTrue { launchList.all { it.isLaunchSuccess == LAUNCH_SUCCESS } }
    }

    @Test
    fun filterLaunchItemsByLaunchStatus_launchFailed() = runBlocking {

        val launchList = launchDaoService.filterLaunchList(
            year = null,
            order = LAUNCH_ORDER_DESC,
            launchFilter = LAUNCH_FAILED,
            page = PAGE_ALL,
        )

        assertTrue(!launchList.isNullOrEmpty())
        checkDateOrderDescending(launchList)
        assertTrue { launchList.all { it.isLaunchSuccess == LAUNCH_FAILED } }
    }

    @Test
    fun filterLaunchItemsByLaunchStatus_launchAll() = runBlocking {

        val allLaunchItems = launchDaoService.getAll()

        val launchList = launchDaoService.filterLaunchList(
            year = null,
            order = LAUNCH_ORDER_DESC,
            launchFilter = null,
            page = PAGE_ALL,
        )

        assertTrue(!launchList.isNullOrEmpty())
        checkDateOrderDescending(launchList)
        assertTrue { launchList.containsAll(allLaunchItems!!) }
    }

    @Test
    fun filterLaunchItemsByLaunchStatus_unknown() = runBlocking {

        val launchList = launchDaoService.filterLaunchList(
            year = null,
            order = LAUNCH_ORDER_DESC,
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

        val launchList = launchDaoService.filterLaunchList(
            year = year,
            order = LAUNCH_ORDER_DESC,
            launchFilter = LAUNCH_SUCCESS,
            page = PAGE_ALL,
        )

        // No items should be returned with a launchYear of 2006 and LAUNCH_SUCCESS
        assertTrue { launchList?.isEmpty() == true }
    }

    @Test
    fun filterLaunchItemsFail_noResultsFound() = runBlocking {
        val FORCE_SEARCH_LAUNCH_EXCEPTION = "FORCE_SEARCH_LAUNCH_EXCEPTION"
        val launchList = launchDaoService.filterLaunchList(
            year = FORCE_SEARCH_LAUNCH_EXCEPTION,
            order = LAUNCH_ORDER_DESC,
            launchFilter = LAUNCH_SUCCESS,
            page = PAGE_ALL,
        )

        assertTrue { launchList?.isEmpty() == true }

        // confirm there are launch items in the cache
        val launchItemsInCache = launchDaoService.getAll()
        if (launchItemsInCache != null) {
            assertTrue { launchItemsInCache.isNotEmpty() }
        }
    }


    private fun checkDateOrderAscending(launchList: List<LaunchModel>) {
        // Check the list and verify the date is less than the next index
        for (item in 0..launchList.size.minus(2)) {
            assertTrue(
                launchList[item].launchDateLocalDateTime <=
                        launchList[item.plus(1)].launchDateLocalDateTime
            )
        }
    }

    private fun checkDateOrderDescending(launchList: List<LaunchModel>) {
        // Check the list and verify the date is greater than the next index
        for (item in 0..launchList.size.minus(2)) {
            assertTrue(
                launchList[item].launchDateLocalDateTime >=
                        launchList[item.plus(1)].launchDateLocalDateTime
            )
        }
    }


}














