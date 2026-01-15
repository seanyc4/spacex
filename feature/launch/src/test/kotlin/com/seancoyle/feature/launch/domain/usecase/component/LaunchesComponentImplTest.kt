package com.seancoyle.feature.launch.domain.usecase.component

import androidx.paging.PagingData
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.domain.model.LaunchPrefs
import com.seancoyle.feature.launch.domain.model.LaunchSummary
import com.seancoyle.feature.launch.domain.model.LaunchesQuery
import com.seancoyle.feature.launch.domain.usecase.launch.GetLaunchUseCase
import com.seancoyle.feature.launch.domain.usecase.launch.GetLaunchesPreferencesUseCase
import com.seancoyle.feature.launch.domain.usecase.launch.ObservePastLaunchesUseCase
import com.seancoyle.feature.launch.domain.usecase.launch.ObserveUpcomingLaunchesUseCase
import com.seancoyle.feature.launch.domain.usecase.launch.SaveLaunchesPreferencesUseCase
import com.seancoyle.feature.launch.util.TestData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class LaunchesComponentImplTest {

    @MockK
    private lateinit var saveLaunchesPreferencesUseCase: SaveLaunchesPreferencesUseCase

    @MockK
    private lateinit var getLaunchesPreferencesUseCase: GetLaunchesPreferencesUseCase

    @MockK
    private lateinit var observePastLaunchesUseCase: ObservePastLaunchesUseCase

    @MockK
    private lateinit var observeUpcomingLaunchesUseCase: ObserveUpcomingLaunchesUseCase

    @MockK
    private lateinit var getLaunchUseCase: GetLaunchUseCase

    private lateinit var underTest: LaunchesComponent

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = LaunchesComponentImpl(
            saveLaunchesPreferencesUseCase = { saveLaunchesPreferencesUseCase },
            getLaunchesPreferencesUseCase = getLaunchesPreferencesUseCase,
            observePastLaunchesUseCase = observePastLaunchesUseCase,
            observeUpcomingLaunchesUseCase = observeUpcomingLaunchesUseCase,
            getLaunchUseCase = getLaunchUseCase
        )
    }

    @Test
    fun `component delegates to observeUpcomingLaunches`() = runTest {
        val launchesQuery = LaunchesQuery(query = "Falcon")
        val pagingData = PagingData.empty<LaunchSummary>()
        val flow = flowOf(pagingData)
        every { observeUpcomingLaunchesUseCase.invoke(launchesQuery) } returns flow

        val result = underTest.observeUpcomingLaunches(launchesQuery)
        assertNotNull(result)
        result.collect {}
    }

    @Test
    fun `component delegates to observePastLaunches`() = runTest {
        val launchesQuery = LaunchesQuery(query = "Falcon")
        val pagingData = PagingData.empty<LaunchSummary>()
        val flow = flowOf(pagingData)
        every { observePastLaunchesUseCase.invoke(launchesQuery) } returns flow

        val result = underTest.observePastLaunches(launchesQuery)
        assertNotNull(result)
        result.collect {}
    }

    @Test
    fun `component delegates to getLaunchPreferencesUseCase`() = runTest {
        val preferences = LaunchPrefs(order = Order.ASC)
        coEvery { getLaunchesPreferencesUseCase.invoke() } returns preferences

        val result = underTest.getLaunchPreferencesUseCase()

        assertEquals(Order.ASC, result.order)
        coVerify { getLaunchesPreferencesUseCase.invoke() }
        coVerify(exactly = 1) { getLaunchesPreferencesUseCase.invoke() }
    }

    @Test
    fun `component delegates to saveLaunchPreferencesUseCase`() = runTest {
        val order = Order.ASC
        coJustRun { saveLaunchesPreferencesUseCase.invoke(order) }

        underTest.saveLaunchPreferencesUseCase(order)

        coVerify { saveLaunchesPreferencesUseCase.invoke(order) }
        coVerify(exactly = 1) { saveLaunchesPreferencesUseCase.invoke(order) }
    }

    @Test
    fun `component delegates to getLaunchUseCase`() = runTest {
        val launchId = "id123"
        val launchType = LaunchesType.UPCOMING
        val launches = TestData.createLaunch(launchId)
        val expectedResult = LaunchResult.Success(launches)
        coEvery { getLaunchUseCase.invoke(launchId, launchType) } returns expectedResult

        val result = underTest.getLaunchUseCase(launchId, launchType)

        assertEquals(expectedResult, result)
        coVerify { getLaunchUseCase.invoke(launchId, launchType) }
    }
}
