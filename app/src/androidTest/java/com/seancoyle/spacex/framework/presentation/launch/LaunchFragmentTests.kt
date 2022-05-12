package com.seancoyle.spacex.framework.presentation.launch

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.spacex.BaseTest
import com.seancoyle.spacex.R
import com.seancoyle.spacex.di.*
import com.seancoyle.spacex.framework.datasource.cache.dao.launch.LaunchDao
import com.seancoyle.spacex.framework.datasource.cache.mappers.launch.LaunchCacheMapper
import com.seancoyle.spacex.framework.datasource.cache.model.launch.LaunchCacheEntity
import com.seancoyle.spacex.framework.datasource.data.launch.LaunchDataFactory
import com.seancoyle.spacex.framework.presentation.TestSpaceXFragmentFactory
import com.seancoyle.spacex.framework.presentation.UIController
import com.seancoyle.spacex.framework.presentation.launch.adapter.LaunchAdapter.LaunchViewHolder
import com.seancoyle.spacex.util.EspressoIdlingResourceRule
import com.seancoyle.spacex.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

/*
    --Test cases:

    1) check contents of launch list is displayed on screen
    2) scroll to the end of the launch list
    3) select a launch item and check the bottom navigation gets displayed
    4)

*/
@MediumTest
@ExperimentalCoroutinesApi
@FlowPreview
@HiltAndroidTest
@UninstallModules(
    LaunchModule::class,
    CompanyInfoModule::class,
    ProductionModule:: class,
    AppModule::class,
    SpaceXFragmentFactoryModule::class
)
@RunWith(AndroidJUnit4ClassRunner::class)
class LaunchFragmentTests: BaseTest() {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val espressoIdlingResourceRule = EspressoIdlingResourceRule()

    @Inject
    lateinit var launchCacheMapper: LaunchCacheMapper

    @Inject
    lateinit var launchDataFactory: LaunchDataFactory

    @Inject
    lateinit var dao: LaunchDao

    @Inject
    lateinit var fragmentFactory: TestSpaceXFragmentFactory

    private lateinit var testLaunchList: List<LaunchCacheEntity>
    private val uiController = mockk<UIController>(relaxed = true)
    private val navController = mockk<NavController>(relaxed = true)

    @Before
    fun before(){
        hiltRule.inject()
        testLaunchList = launchCacheMapper.domainListToEntityList(
            launchDataFactory.produceListOfLaunches()
        )
        prepareDataSet(testLaunchList)
        setupUIController()
    }

    private fun setupUIController(){
        fragmentFactory.uiController = uiController
    }

    // ** Must clear cache so there is no previous state issues **
    private fun prepareDataSet(testData: List<LaunchCacheEntity>) = runBlocking{
        // clear any existing data so recyclerview isn't overwhelmed
        dao.deleteAll()
        dao.insertList(testData)
    }

    /**
     * I decided to write a single large test when testing fragments in isolation.
     * Because if I make multiple tests, they have issues sharing state. I can solve
     * that issue by using test orchestrator, but that will prevent me from getting
     * reports.
     */
    @Test
    fun launchListFragmentTest(): Unit = runBlocking{

        // setup
        launchFragmentInHiltContainer<LaunchFragment>(
            fragmentFactory = fragmentFactory
        ){
            this.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(this.requireView(), navController)
                }
            }
        }

        // check contents of the launch list is displayed on screen
        val recyclerView = onView(withId(R.id.rv_launch))
        recyclerView.check(matches(isDisplayed()))

        // test scrolling to the bottom of the list
        recyclerView.perform(
            scrollToPosition<LaunchViewHolder>(testLaunchList.size.minus(1))
        )
    }
}























