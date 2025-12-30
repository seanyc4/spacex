package com.seancoyle.feature.launch.domain.usecase.component

import androidx.paging.PagingData
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.domain.model.Launch
import com.seancoyle.feature.launch.domain.model.LaunchPrefs
import com.seancoyle.feature.launch.domain.model.LaunchesQuery
import com.seancoyle.feature.launch.domain.model.LaunchStatus
import com.seancoyle.feature.launch.domain.model.LaunchesType
import com.seancoyle.feature.launch.domain.usecase.launch.GetLaunchUseCase
import com.seancoyle.feature.launch.domain.usecase.launch.GetLaunchesPreferencesUseCase
import com.seancoyle.feature.launch.domain.usecase.launch.ObserveLaunchesUseCase
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
    private lateinit var observeLaunchesUseCase: ObserveLaunchesUseCase

    @MockK
    private lateinit var getLaunchUseCase: GetLaunchUseCase

    private lateinit var underTest: LaunchesComponent

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = LaunchesComponentImpl(
            saveLaunchesPreferencesUseCase = { saveLaunchesPreferencesUseCase },
            getLaunchesPreferencesUseCase = getLaunchesPreferencesUseCase,
            observeLaunchesUseCase = observeLaunchesUseCase,
            getLaunchUseCase = getLaunchUseCase
        )
    }

    @Test
    fun `observeLaunchesUseCase delegates to use case with query`() = runTest {
        val launchesQuery = LaunchesQuery(query = "Falcon")
        val pagingData = PagingData.empty<Launch>()
        val flow = flowOf(pagingData)
        every { observeLaunchesUseCase.invoke(launchesQuery) } returns flow

        val result = underTest.observeLaunchesUseCase(launchesQuery)
        assertNotNull(result)
        result.collect {}
    }

    @Test
    fun `getLaunchPreferencesUseCase delegates to use case`() = runTest {
        val preferences = LaunchPrefs(
            order = Order.ASC,
            launchStatus = LaunchStatus.ALL,
            query = ""
        )
        coEvery { getLaunchesPreferencesUseCase.invoke() } returns preferences

        val result = underTest.getLaunchPreferencesUseCase()

        assertEquals(preferences, result)
        coVerify { getLaunchesPreferencesUseCase.invoke() }
    }

    @Test
    fun `getLaunchPreferencesUseCase returns preferences with ASC order`() = runTest {
        val preferences = LaunchPrefs(
            order = Order.ASC,
            launchStatus = LaunchStatus.ALL,
            query = ""
        )
        coEvery { getLaunchesPreferencesUseCase.invoke() } returns preferences

        val result = underTest.getLaunchPreferencesUseCase()

        assertEquals(Order.ASC, result.order)
        assertEquals(LaunchStatus.ALL, result.launchStatus)
        coVerify { getLaunchesPreferencesUseCase.invoke() }
    }

    @Test
    fun `getLaunchPreferencesUseCase returns preferences with DESC order`() = runTest {
        val preferences = LaunchPrefs(
            order = Order.DESC,
            launchStatus = LaunchStatus.SUCCESS,
            query = "2024"
        )
        coEvery { getLaunchesPreferencesUseCase.invoke() } returns preferences

        val result = underTest.getLaunchPreferencesUseCase()

        assertEquals(Order.DESC, result.order)
        assertEquals(LaunchStatus.SUCCESS, result.launchStatus)
        assertEquals("2024", result.query)
        coVerify { getLaunchesPreferencesUseCase.invoke() }
    }

    @Test
    fun `getLaunchPreferencesUseCase invokes use case only once`() = runTest {
        val preferences = LaunchPrefs()
        coEvery { getLaunchesPreferencesUseCase.invoke() } returns preferences

        underTest.getLaunchPreferencesUseCase()

        coVerify(exactly = 1) { getLaunchesPreferencesUseCase.invoke() }
    }

    @Test
    fun `saveLaunchPreferencesUseCase delegates to lazy use case with ASC order`() = runTest {
        val order = Order.ASC
        coJustRun { saveLaunchesPreferencesUseCase.invoke(order) }

        underTest.saveLaunchPreferencesUseCase(order)

        coVerify { saveLaunchesPreferencesUseCase.invoke(order) }
    }

    @Test
    fun `saveLaunchPreferencesUseCase delegates to lazy use case with DESC order`() = runTest {
        val order = Order.DESC
        coJustRun { saveLaunchesPreferencesUseCase.invoke(order) }

        underTest.saveLaunchPreferencesUseCase(order)

        coVerify { saveLaunchesPreferencesUseCase.invoke(order) }
    }

    @Test
    fun `saveLaunchPreferencesUseCase uses lazy initialization`() = runTest {
        val order = Order.ASC
        coJustRun { saveLaunchesPreferencesUseCase.invoke(order) }

        underTest.saveLaunchPreferencesUseCase(order)

        coVerify { saveLaunchesPreferencesUseCase.invoke(order) }
    }

    @Test
    fun `saveLaunchPreferencesUseCase invokes use case only once per call`() = runTest {
        val order = Order.DESC
        coJustRun { saveLaunchesPreferencesUseCase.invoke(order) }

        underTest.saveLaunchPreferencesUseCase(order)

        coVerify(exactly = 1) { saveLaunchesPreferencesUseCase.invoke(order) }
    }


    @Test
    fun `component can handle multiple sequential calls`() = runTest {
        val launchesQuery = LaunchesQuery(query = "Test")
        val pagingData = PagingData.empty<Launch>()
        val flow = flowOf(pagingData)
        val preferences = LaunchPrefs(order = Order.ASC)

        every { observeLaunchesUseCase.invoke(launchesQuery) } returns flow
        coEvery { getLaunchesPreferencesUseCase.invoke() } returns preferences
        coJustRun { saveLaunchesPreferencesUseCase.invoke(Order.DESC) }

        underTest.observeLaunchesUseCase(launchesQuery).collect {}
        underTest.getLaunchPreferencesUseCase()
        underTest.saveLaunchPreferencesUseCase(Order.DESC)

        coVerify { getLaunchesPreferencesUseCase.invoke() }
        coVerify { saveLaunchesPreferencesUseCase.invoke(Order.DESC) }
    }

    @Test
    fun `component can observe launches multiple times with different queries`() = runTest {
        val query1 = LaunchesQuery(query = "Falcon")
        val query2 = LaunchesQuery(query = "Dragon")
        val pagingData = PagingData.empty<Launch>()
        val flow = flowOf(pagingData)

        every { observeLaunchesUseCase.invoke(query1) } returns flow
        every { observeLaunchesUseCase.invoke(query2) } returns flow

        underTest.observeLaunchesUseCase(query1).collect {}
        underTest.observeLaunchesUseCase(query2).collect {}
    }

    @Test
    fun `component can save preferences multiple times`() = runTest {
        coJustRun { saveLaunchesPreferencesUseCase.invoke(Order.ASC) }
        coJustRun { saveLaunchesPreferencesUseCase.invoke(Order.DESC) }

        underTest.saveLaunchPreferencesUseCase(Order.ASC)
        underTest.saveLaunchPreferencesUseCase(Order.DESC)

        coVerify { saveLaunchesPreferencesUseCase.invoke(Order.ASC) }
        coVerify { saveLaunchesPreferencesUseCase.invoke(Order.DESC) }
    }

    @Test
    fun `getLaunchUseCase delegates to use case and returns result`() = runTest {
        val launchId = "id123"
        val launchType = LaunchesType.UPCOMING
        val launches = TestData.createRandomLaunchList(1)
        val expectedResult = LaunchResult.Success(launches)
        coEvery { getLaunchUseCase.invoke(launchId, launchType) } returns expectedResult

        val result = underTest.getLaunchUseCase(launchId, launchType)

        assertEquals(expectedResult, result)
        coVerify { getLaunchUseCase.invoke(launchId, launchType) }
    }
}
