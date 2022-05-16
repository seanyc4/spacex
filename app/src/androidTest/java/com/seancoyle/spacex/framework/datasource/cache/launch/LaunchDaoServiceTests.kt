package com.seancoyle.spacex.framework.datasource.cache.launch

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.spacex.BaseTest
import com.seancoyle.spacex.R
import com.seancoyle.spacex.business.domain.model.launch.LaunchDomainEntity
import com.seancoyle.spacex.business.domain.model.launch.LaunchFactory
import com.seancoyle.spacex.business.domain.model.launch.LaunchType.Companion.TYPE_LAUNCH
import com.seancoyle.spacex.business.domain.model.launch.Links
import com.seancoyle.spacex.business.domain.model.launch.Rocket
import com.seancoyle.spacex.di.AppModule
import com.seancoyle.spacex.di.LaunchModule
import com.seancoyle.spacex.di.ProductionModule
import com.seancoyle.spacex.framework.datasource.cache.abstraction.launch.LaunchDaoService
import com.seancoyle.spacex.framework.datasource.cache.dao.launch.LaunchDao
import com.seancoyle.spacex.framework.datasource.cache.implementation.launch.LaunchDaoServiceImpl
import com.seancoyle.spacex.framework.datasource.cache.mappers.launch.LaunchCacheMapper
import com.seancoyle.spacex.framework.datasource.data.launch.LaunchDataFactory
import com.seancoyle.spacex.framework.datasource.network.mappers.launch.DEFAULT_LAUNCH_IMAGE
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
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
import kotlin.collections.ArrayList
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/*
    LEGEND:
    1. CBS = "Confirm by searching"

    Test cases:
    1. confirm launch database is empty to start (should be test data inserted from CacheTest.kt)
    2. insert a launch, CBS
    3. insert a list of launches, CBS
    4. insert 1000 new launches, confirm db size increased
    5. delete new launch, confirm deleted
    6. delete list of launches, CBS

 */
@ExperimentalCoroutinesApi
@FlowPreview
@HiltAndroidTest
@UninstallModules(
    LaunchModule::class,
    AppModule::class,
    ProductionModule::class
)
@RunWith(AndroidJUnit4ClassRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class LaunchDaoServiceTests : BaseTest() {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    // system in test
    private lateinit var launchDaoService: LaunchDaoService

    // dependencies
    @Inject
    lateinit var dao: LaunchDao

    @Inject
    lateinit var launchDataFactory: LaunchDataFactory

    @Inject
    lateinit var launchFactory: LaunchFactory

    @Inject
    lateinit var launchCacheMapper: LaunchCacheMapper

    @Before
    fun setup() {
        hiltRule.inject()
        insertTestData()
        launchDaoService = LaunchDaoServiceImpl(
            dao = dao,
            cacheMapper = launchCacheMapper
        )
    }


    private fun insertTestData() = runBlocking {
        val entityList = launchCacheMapper.domainListToEntityList(
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
    fun insertLaunchItem_CBS() = runBlocking {

        val newLaunchItem = launchFactory.createLaunchItem(
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
        assert(cachedLaunchItem == newLaunchItem)
    }

    @Test
    fun insertLaunchList_CBS() = runBlocking {

        val launchList = launchFactory.createLaunchListTest(
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
        val launchList = launchFactory.createLaunchListTest(
            num = 1000,
            null
        )
        launchDaoService.insertList(launchList)

        val cachedLaunchList = launchDaoService.getTotalEntries()
        assertEquals(currentNumLaunchItems + 1000, cachedLaunchList)
    }

    @Test
    fun insertLaunch_deleteLaunch_confirmDeleted() = runBlocking {
        val newLaunchItem = launchFactory.createLaunchItem(
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
        val launchList: ArrayList<LaunchDomainEntity> = ArrayList(launchDaoService.getAll())

        // select some random launch for deleting
        val launchesToDelete: ArrayList<LaunchDomainEntity> = ArrayList()

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

}














