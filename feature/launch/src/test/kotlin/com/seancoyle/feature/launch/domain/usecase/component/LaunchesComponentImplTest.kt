package com.seancoyle.feature.launch.domain.usecase.component

import androidx.paging.PagingData
import com.seancoyle.core.common.result.DataError.RemoteError
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
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

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
    fun `GIVEN query WHEN observeUpcomingLaunches called THEN delegates to use case`() = runTest {
        val launchesQuery = LaunchesQuery(query = "Falcon")
        val pagingData = PagingData.empty<LaunchSummary>()
        val flow = flowOf(pagingData)
        every { observeUpcomingLaunchesUseCase.invoke(launchesQuery) } returns flow

        val result = underTest.observeUpcomingLaunches(launchesQuery)

        assertNotNull(result)
        result.collect {}
        verify { observeUpcomingLaunchesUseCase.invoke(launchesQuery) }
    }

    @Test
    fun `GIVEN query WHEN observePastLaunches called THEN delegates to use case`() = runTest {
        val launchesQuery = LaunchesQuery(query = "Falcon")
        val pagingData = PagingData.empty<LaunchSummary>()
        val flow = flowOf(pagingData)
        every { observePastLaunchesUseCase.invoke(launchesQuery) } returns flow

        val result = underTest.observePastLaunches(launchesQuery)

        assertNotNull(result)
        result.collect {}
        verify { observePastLaunchesUseCase.invoke(launchesQuery) }
    }

    @Test
    fun `GIVEN preferences exist WHEN getLaunchPreferencesUseCase called THEN returns preferences`() = runTest {
        val preferences = LaunchPrefs(order = Order.ASC)
        coEvery { getLaunchesPreferencesUseCase.invoke() } returns preferences

        val result = underTest.getLaunchPreferencesUseCase()

        assertEquals(Order.ASC, result.order)
        coVerify(exactly = 1) { getLaunchesPreferencesUseCase.invoke() }
    }

    @Test
    fun `GIVEN order WHEN saveLaunchPreferencesUseCase called THEN delegates to use case`() = runTest {
        val order = Order.ASC
        coJustRun { saveLaunchesPreferencesUseCase.invoke(order) }

        underTest.saveLaunchPreferencesUseCase(order)

        coVerify(exactly = 1) { saveLaunchesPreferencesUseCase.invoke(order) }
    }

    @Test
    fun `GIVEN launch exists WHEN getLaunchUseCase with isRefresh false THEN delegates with isRefresh false`() = runTest {
        val launchId = "id123"
        val launchType = LaunchesType.UPCOMING
        val launch = TestData.createLaunch(id = launchId)
        val expectedResult = LaunchResult.Success(launch)
        coEvery { getLaunchUseCase.invoke(launchId, launchType, isRefresh = false) } returns expectedResult

        val result = underTest.getLaunchUseCase(launchId, launchType, isRefresh = false)

        assertEquals(expectedResult, result)
        coVerify(exactly = 1) { getLaunchUseCase.invoke(launchId, launchType, isRefresh = false) }
    }

    @Test
    fun `GIVEN launch exists WHEN getLaunchUseCase with isRefresh true THEN delegates with isRefresh true`() = runTest {
        val launchId = "id123"
        val launchType = LaunchesType.UPCOMING
        val launch = TestData.createLaunch(id = launchId)
        val expectedResult = LaunchResult.Success(launch)
        coEvery { getLaunchUseCase.invoke(launchId, launchType, isRefresh = true) } returns expectedResult

        val result = underTest.getLaunchUseCase(launchId, launchType, isRefresh = true)

        assertEquals(expectedResult, result)
        coVerify(exactly = 1) { getLaunchUseCase.invoke(launchId, launchType, isRefresh = true) }
    }

    @Test
    fun `GIVEN PAST launch type WHEN getLaunchUseCase called THEN delegates with PAST type`() = runTest {
        val launchId = "id456"
        val launchType = LaunchesType.PAST
        val launch = TestData.createLaunch(id = launchId)
        val expectedResult = LaunchResult.Success(launch)
        coEvery { getLaunchUseCase.invoke(launchId, launchType, isRefresh = false) } returns expectedResult

        val result = underTest.getLaunchUseCase(launchId, launchType, isRefresh = false)

        assertEquals(expectedResult, result)
        coVerify(exactly = 1) { getLaunchUseCase.invoke(launchId, launchType, isRefresh = false) }
    }

    @Test
    fun `GIVEN use case returns error WHEN getLaunchUseCase called THEN returns error`() = runTest {
        val launchId = "id789"
        val launchType = LaunchesType.UPCOMING
        val error = RemoteError.NETWORK_CONNECTION_FAILED
        val expectedResult = LaunchResult.Error(error)
        coEvery { getLaunchUseCase.invoke(launchId, launchType, isRefresh = false) } returns expectedResult

        val result = underTest.getLaunchUseCase(launchId, launchType, isRefresh = false)

        assertTrue(result is LaunchResult.Error)
        assertEquals(error, result.error)
    }

    @Test
    fun `GIVEN DESC order WHEN saveLaunchPreferencesUseCase called THEN saves DESC order`() = runTest {
        val order = Order.DESC
        coJustRun { saveLaunchesPreferencesUseCase.invoke(order) }

        underTest.saveLaunchPreferencesUseCase(order)

        coVerify(exactly = 1) { saveLaunchesPreferencesUseCase.invoke(Order.DESC) }
    }

    @Test
    fun `GIVEN preferences with DESC order WHEN getLaunchPreferencesUseCase called THEN returns DESC order`() = runTest {
        val preferences = LaunchPrefs(order = Order.DESC)
        coEvery { getLaunchesPreferencesUseCase.invoke() } returns preferences

        val result = underTest.getLaunchPreferencesUseCase()

        assertEquals(Order.DESC, result.order)
    }
}
