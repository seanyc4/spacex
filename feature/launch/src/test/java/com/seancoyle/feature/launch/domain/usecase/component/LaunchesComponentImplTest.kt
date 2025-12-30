package com.seancoyle.feature.launch.domain.usecase.component

import androidx.paging.PagingData
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.domain.model.Launch
import com.seancoyle.feature.launch.domain.model.LaunchPrefs
import com.seancoyle.feature.launch.domain.model.LaunchQuery
import com.seancoyle.feature.launch.domain.model.LaunchStatus
import com.seancoyle.feature.launch.domain.usecase.launch.GetLaunchPreferencesUseCase
import com.seancoyle.feature.launch.domain.usecase.launch.ObserveLaunchesUseCase
import com.seancoyle.feature.launch.domain.usecase.launch.SaveLaunchPreferencesUseCase
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
    private lateinit var saveLaunchPreferencesUseCase: SaveLaunchPreferencesUseCase

    @MockK
    private lateinit var getLaunchPreferencesUseCase: GetLaunchPreferencesUseCase

    @MockK
    private lateinit var observeLaunchesUseCase: ObserveLaunchesUseCase

    private lateinit var underTest: LaunchesComponent

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = LaunchesComponentImpl(
            saveLaunchPreferencesUseCase = { saveLaunchPreferencesUseCase },
            getLaunchPreferencesUseCase = getLaunchPreferencesUseCase,
            observeLaunchesUseCase = observeLaunchesUseCase
        )
    }

    @Test
    fun `observeLaunchesUseCase delegates to use case with query`() = runTest {
        val launchQuery = LaunchQuery(query = "Falcon")
        val pagingData = PagingData.empty<Launch>()
        val flow = flowOf(pagingData)
        every { observeLaunchesUseCase.invoke(launchQuery) } returns flow

        val result = underTest.observeLaunchesUseCase(launchQuery)
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
        coEvery { getLaunchPreferencesUseCase.invoke() } returns preferences

        val result = underTest.getLaunchPreferencesUseCase()

        assertEquals(preferences, result)
        coVerify { getLaunchPreferencesUseCase.invoke() }
    }

    @Test
    fun `getLaunchPreferencesUseCase returns preferences with ASC order`() = runTest {
        val preferences = LaunchPrefs(
            order = Order.ASC,
            launchStatus = LaunchStatus.ALL,
            query = ""
        )
        coEvery { getLaunchPreferencesUseCase.invoke() } returns preferences

        val result = underTest.getLaunchPreferencesUseCase()

        assertEquals(Order.ASC, result.order)
        assertEquals(LaunchStatus.ALL, result.launchStatus)
        coVerify { getLaunchPreferencesUseCase.invoke() }
    }

    @Test
    fun `getLaunchPreferencesUseCase returns preferences with DESC order`() = runTest {
        val preferences = LaunchPrefs(
            order = Order.DESC,
            launchStatus = LaunchStatus.SUCCESS,
            query = "2024"
        )
        coEvery { getLaunchPreferencesUseCase.invoke() } returns preferences

        val result = underTest.getLaunchPreferencesUseCase()

        assertEquals(Order.DESC, result.order)
        assertEquals(LaunchStatus.SUCCESS, result.launchStatus)
        assertEquals("2024", result.query)
        coVerify { getLaunchPreferencesUseCase.invoke() }
    }

    @Test
    fun `getLaunchPreferencesUseCase invokes use case only once`() = runTest {
        val preferences = LaunchPrefs()
        coEvery { getLaunchPreferencesUseCase.invoke() } returns preferences

        underTest.getLaunchPreferencesUseCase()

        coVerify(exactly = 1) { getLaunchPreferencesUseCase.invoke() }
    }

    @Test
    fun `saveLaunchPreferencesUseCase delegates to lazy use case with ASC order`() = runTest {
        val order = Order.ASC
        coJustRun { saveLaunchPreferencesUseCase.invoke(order) }

        underTest.saveLaunchPreferencesUseCase(order)

        coVerify { saveLaunchPreferencesUseCase.invoke(order) }
    }

    @Test
    fun `saveLaunchPreferencesUseCase delegates to lazy use case with DESC order`() = runTest {
        val order = Order.DESC
        coJustRun { saveLaunchPreferencesUseCase.invoke(order) }

        underTest.saveLaunchPreferencesUseCase(order)

        coVerify { saveLaunchPreferencesUseCase.invoke(order) }
    }

    @Test
    fun `saveLaunchPreferencesUseCase uses lazy initialization`() = runTest {
        val order = Order.ASC
        coJustRun { saveLaunchPreferencesUseCase.invoke(order) }

        underTest.saveLaunchPreferencesUseCase(order)

        coVerify { saveLaunchPreferencesUseCase.invoke(order) }
    }

    @Test
    fun `saveLaunchPreferencesUseCase invokes use case only once per call`() = runTest {
        val order = Order.DESC
        coJustRun { saveLaunchPreferencesUseCase.invoke(order) }

        underTest.saveLaunchPreferencesUseCase(order)

        coVerify(exactly = 1) { saveLaunchPreferencesUseCase.invoke(order) }
    }


    @Test
    fun `component can handle multiple sequential calls`() = runTest {
        val launchQuery = LaunchQuery(query = "Test")
        val pagingData = PagingData.empty<Launch>()
        val flow = flowOf(pagingData)
        val preferences = LaunchPrefs(order = Order.ASC)

        every { observeLaunchesUseCase.invoke(launchQuery) } returns flow
        coEvery { getLaunchPreferencesUseCase.invoke() } returns preferences
        coJustRun { saveLaunchPreferencesUseCase.invoke(Order.DESC) }

        underTest.observeLaunchesUseCase(launchQuery).collect {}
        underTest.getLaunchPreferencesUseCase()
        underTest.saveLaunchPreferencesUseCase(Order.DESC)

        coVerify { getLaunchPreferencesUseCase.invoke() }
        coVerify { saveLaunchPreferencesUseCase.invoke(Order.DESC) }
    }

    @Test
    fun `component can observe launches multiple times with different queries`() = runTest {
        val query1 = LaunchQuery(query = "Falcon")
        val query2 = LaunchQuery(query = "Dragon")
        val pagingData = PagingData.empty<Launch>()
        val flow = flowOf(pagingData)

        every { observeLaunchesUseCase.invoke(query1) } returns flow
        every { observeLaunchesUseCase.invoke(query2) } returns flow

        underTest.observeLaunchesUseCase(query1).collect {}
        underTest.observeLaunchesUseCase(query2).collect {}
    }

    @Test
    fun `component can save preferences multiple times`() = runTest {
        coJustRun { saveLaunchPreferencesUseCase.invoke(Order.ASC) }
        coJustRun { saveLaunchPreferencesUseCase.invoke(Order.DESC) }

        underTest.saveLaunchPreferencesUseCase(Order.ASC)
        underTest.saveLaunchPreferencesUseCase(Order.DESC)

        coVerify { saveLaunchPreferencesUseCase.invoke(Order.ASC) }
        coVerify { saveLaunchPreferencesUseCase.invoke(Order.DESC) }
    }
}
