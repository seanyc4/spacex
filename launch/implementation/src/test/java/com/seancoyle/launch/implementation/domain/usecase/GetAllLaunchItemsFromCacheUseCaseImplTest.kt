package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.testing.MainCoroutineRule
import com.seancoyle.launch.api.LaunchCacheDataSource
import com.seancoyle.launch.api.model.LaunchModel
import com.seancoyle.launch.api.usecase.GetAllLaunchItemsFromCacheUseCase
import com.seancoyle.launch.implementation.domain.GetAllLaunchItemsFromCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.GetAllLaunchItemsFromCacheUseCaseImpl.Companion.GET_ALL_LAUNCH_ITEMS_SUCCESS
import com.seancoyle.launch.implementation.domain.LaunchDependencies
import com.seancoyle.launch.implementation.presentation.LaunchEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetAllLaunchItemsFromCacheUseCaseImplTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val launchDependencies: LaunchDependencies = LaunchDependencies()
    private lateinit var cacheDataSource: LaunchCacheDataSource
    private lateinit var underTest: GetAllLaunchItemsFromCacheUseCase

    @BeforeEach
    fun init() {
        launchDependencies.build()
        cacheDataSource = launchDependencies.launchCacheDataSource
        underTest =
            GetAllLaunchItemsFromCacheUseCaseImpl(
                ioDispatcher = mainCoroutineRule.testDispatcher,
                cacheDataSource = cacheDataSource
            )
    }

    @Test
    fun `Get All Launch Items From Cache - success - confirm correct`() = runBlocking {

        val numTotal = cacheDataSource.getTotalEntries()
        var results = emptyList<LaunchModel>()

        underTest(
            stateEvent = LaunchEvent.GetAllLaunchItemsFromCacheEvent
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                GET_ALL_LAUNCH_ITEMS_SUCCESS
            )

            value?.data?.launchList?.let { list ->
                results = list
            }
        }

        assertTrue { results.isNotEmpty() }
        assertTrue { results.size == numTotal }
    }

    @Test
    fun `Check All Launch Items Have A Valid Patch Image URL`() = runBlocking {

        val launchList = cacheDataSource.getAll()
        assertTrue { !launchList.isNullOrEmpty() }
        assertTrue(launchList?.all { it.links.missionImage.isNotEmpty() } == true )
    }
}
















