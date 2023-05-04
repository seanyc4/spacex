package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.testing.MainCoroutineRule
import com.seancoyle.launch.api.LaunchCacheDataSource
import com.seancoyle.launch.api.model.LaunchFactory
import com.seancoyle.launch.api.model.LaunchModel
import com.seancoyle.launch.api.usecase.GetLaunchItemByIdFromCacheUseCase
import com.seancoyle.launch.implementation.domain.GetLaunchItemByIdFromCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.GetLaunchItemByIdFromCacheUseCaseImpl.Companion.GET_LAUNCH_ITEM_BY_ID_SUCCESS
import com.seancoyle.launch.implementation.domain.LaunchDependencies
import com.seancoyle.launch.implementation.presentation.LaunchEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GeLaunchItemByIdFromCacheTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val launchDependencies: LaunchDependencies = LaunchDependencies()
    private lateinit var cacheDataSource: LaunchCacheDataSource
    private lateinit var factory: LaunchFactory
    private lateinit var underTest: GetLaunchItemByIdFromCacheUseCase

    @BeforeEach
    fun setup() {
        launchDependencies.build()
        cacheDataSource = launchDependencies.launchCacheDataSource
        factory = launchDependencies.launchFactory
        underTest =
            GetLaunchItemByIdFromCacheUseCaseImpl(
                ioDispatcher = mainCoroutineRule.testDispatcher,
                cacheDataSource = cacheDataSource
            )
    }

    @Test
    fun getLaunchItemsByIdFromCache_success_confirmCorrect() = runBlocking {

        val id = 1
        var retrievedLaunch: LaunchModel? = null

        underTest(
            id = id,
            stateEvent = LaunchEvent.GetLaunchItemFromCacheEvent(
                id = id
            )
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                GET_LAUNCH_ITEM_BY_ID_SUCCESS
            )

            value?.data?.launch?.let { item ->
                retrievedLaunch = item
            }
        }

        assertTrue { retrievedLaunch?.id == id }
    }
}
















