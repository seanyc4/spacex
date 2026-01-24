package com.seancoyle.feature.launch.presentation

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.core.test.TestCoroutineRule
import com.seancoyle.feature.launch.domain.usecase.component.LaunchesComponent
import com.seancoyle.feature.launch.presentation.launch.LaunchViewModel
import com.seancoyle.feature.launch.presentation.launch.model.ConfigurationUI
import com.seancoyle.feature.launch.presentation.launch.model.LaunchStatus
import com.seancoyle.feature.launch.presentation.launch.model.LaunchUI
import com.seancoyle.feature.launch.presentation.launch.model.MissionUI
import com.seancoyle.feature.launch.presentation.launch.model.PadUI
import com.seancoyle.feature.launch.presentation.launch.model.RocketUI
import com.seancoyle.feature.launch.presentation.launch.state.LaunchEvent
import com.seancoyle.feature.launch.presentation.launch.state.LaunchUiState
import com.seancoyle.feature.launch.util.TestData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LaunchViewModelTest {

    @get:Rule
    val dispatcherRule = TestCoroutineRule()

    @MockK
    private lateinit var launchesComponent: LaunchesComponent

    @MockK
    private lateinit var launchUiMapper: LaunchUiMapper

    private lateinit var underTest: LaunchViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    private fun createViewModel(
        launchId: String = "test-launch-id",
        launchType: LaunchesType = LaunchesType.UPCOMING
    ): LaunchViewModel {
        return LaunchViewModel(
            launchesComponent = launchesComponent,
            uiMapper = launchUiMapper,
            launchId = launchId,
            launchType = launchType
        )
    }

    @Test
    fun `GIVEN ViewModel created WHEN initial state observed THEN state is Loading`() = runTest {
        underTest = createViewModel()

        assertTrue(underTest.launchState.value is LaunchUiState.Loading)
    }

    @Test
    fun `GIVEN use case returns launch WHEN state collected THEN emits Success state`() = runTest {
        val testLaunch = TestData.createLaunch(name = "Starlink Mission")
        val testLaunchUI = createTestLaunchUI(missionName = "Starlink Mission")
        coEvery { launchesComponent.getLaunchUseCase(any(), any(), any()) } returns LaunchResult.Success(testLaunch)
        every { launchUiMapper.mapToLaunchUi(testLaunch) } returns testLaunchUI
        underTest = createViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher()) { underTest.launchState.collect() }

        val state = underTest.launchState.value
        assertTrue(state is LaunchUiState.Success)
        assertEquals("Starlink Mission", (state as LaunchUiState.Success).launch.missionName)
    }

    @Test
    fun `GIVEN use case returns error WHEN state collected THEN emits Error state`() = runTest {
        coEvery { launchesComponent.getLaunchUseCase(any(), any(), any()) } returns
            LaunchResult.Error(DataError.RemoteError.NETWORK_CONNECTION_FAILED)
        underTest = createViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher()) { underTest.launchState.collect() }

        val state = underTest.launchState.value
        assertTrue(state is LaunchUiState.Error)
    }

    @Test
    fun `GIVEN Error state WHEN RetryFetch event THEN reloads launch data`() = runTest {
        val testLaunch = TestData.createLaunch(name = "Starlink Mission")
        val testLaunchUI = createTestLaunchUI(missionName = "Starlink Mission")
        every { launchUiMapper.mapToLaunchUi(testLaunch) } returns testLaunchUI
        var callCount = 0
        coEvery { launchesComponent.getLaunchUseCase(any(), any(), any()) } answers {
            callCount++
            if (callCount == 1) {
                LaunchResult.Error(DataError.RemoteError.NETWORK_CONNECTION_FAILED)
            } else {
                LaunchResult.Success(testLaunch)
            }
        }
        underTest = createViewModel()
        backgroundScope.launch(UnconfinedTestDispatcher()) { underTest.launchState.collect() }
        assertTrue(underTest.launchState.value is LaunchUiState.Error)

        underTest.onEvent(LaunchEvent.Retry)
        testScheduler.advanceUntilIdle()
        backgroundScope.launch(UnconfinedTestDispatcher()) { underTest.launchState.collect() }

        val state = underTest.launchState.value
        assertTrue(state is LaunchUiState.Success)
    }

    @Test
    fun `GIVEN Success state WHEN PullToRefreshEvent THEN triggers refresh with isRefresh true`() = runTest {
        val testLaunch = TestData.createLaunch()
        val testLaunchUI = createTestLaunchUI()
        coEvery { launchesComponent.getLaunchUseCase(any(), any(), any()) } returns LaunchResult.Success(testLaunch)
        every { launchUiMapper.mapToLaunchUi(testLaunch) } returns testLaunchUI
        underTest = createViewModel()
        backgroundScope.launch(UnconfinedTestDispatcher()) { underTest.launchState.collect() }

        underTest.onEvent(LaunchEvent.PullToRefresh)
        testScheduler.advanceUntilIdle()

        coVerify(atLeast = 1) { launchesComponent.getLaunchUseCase(any(), any(), isRefresh = true) }
    }

    @Test
    fun `GIVEN Error state WHEN RetryFetch THEN triggers reload with isRefresh true`() = runTest {
        val testLaunch = TestData.createLaunch()
        val testLaunchUI = createTestLaunchUI()
        coEvery { launchesComponent.getLaunchUseCase(any(), any(), any()) } returns LaunchResult.Success(testLaunch)
        every { launchUiMapper.mapToLaunchUi(testLaunch) } returns testLaunchUI
        underTest = createViewModel()
        backgroundScope.launch(UnconfinedTestDispatcher()) { underTest.launchState.collect() }

        underTest.onEvent(LaunchEvent.Retry)
        testScheduler.advanceUntilIdle()

        coVerify(atLeast = 1) { launchesComponent.getLaunchUseCase(any(), any(), isRefresh = true) }
    }

    @Test
    fun `GIVEN initial load WHEN state collected THEN triggers with isRefresh false`() = runTest {
        val testLaunch = TestData.createLaunch()
        val testLaunchUI = createTestLaunchUI()
        coEvery { launchesComponent.getLaunchUseCase(any(), any(), isRefresh = false) } returns LaunchResult.Success(testLaunch)
        every { launchUiMapper.mapToLaunchUi(testLaunch) } returns testLaunchUI
        underTest = createViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher()) { underTest.launchState.collect() }

        coVerify { launchesComponent.getLaunchUseCase(any(), any(), isRefresh = false) }
    }

    @Test
    fun `GIVEN UPCOMING launch type WHEN ViewModel created THEN delegates to component with UPCOMING type`() = runTest {
        val testLaunch = TestData.createLaunch()
        val testLaunchUI = createTestLaunchUI()
        coEvery { launchesComponent.getLaunchUseCase("test-id", LaunchesType.UPCOMING, any()) } returns LaunchResult.Success(testLaunch)
        every { launchUiMapper.mapToLaunchUi(testLaunch) } returns testLaunchUI
        underTest = createViewModel(launchId = "test-id", launchType = LaunchesType.UPCOMING)

        backgroundScope.launch(UnconfinedTestDispatcher()) { underTest.launchState.collect() }

        coVerify { launchesComponent.getLaunchUseCase("test-id", LaunchesType.UPCOMING, any()) }
    }

    @Test
    fun `GIVEN PAST launch type WHEN ViewModel created THEN delegates to component with PAST type`() = runTest {
        val testLaunch = TestData.createLaunch()
        val testLaunchUI = createTestLaunchUI()
        coEvery { launchesComponent.getLaunchUseCase("test-id", LaunchesType.PAST, any()) } returns LaunchResult.Success(testLaunch)
        every { launchUiMapper.mapToLaunchUi(testLaunch) } returns testLaunchUI
        underTest = createViewModel(launchId = "test-id", launchType = LaunchesType.PAST)

        backgroundScope.launch(UnconfinedTestDispatcher()) { underTest.launchState.collect() }

        coVerify { launchesComponent.getLaunchUseCase("test-id", LaunchesType.PAST, any()) }
    }

    @Test
    fun `GIVEN multiple PullToRefresh events WHEN onEvent called THEN each triggers network fetch`() = runTest {
        val testLaunch = TestData.createLaunch()
        val testLaunchUI = createTestLaunchUI()
        coEvery { launchesComponent.getLaunchUseCase(any(), any(), any()) } returns LaunchResult.Success(testLaunch)
        every { launchUiMapper.mapToLaunchUi(testLaunch) } returns testLaunchUI
        underTest = createViewModel()
        backgroundScope.launch(UnconfinedTestDispatcher()) { underTest.launchState.collect() }

        underTest.onEvent(LaunchEvent.PullToRefresh)
        testScheduler.advanceUntilIdle()
        underTest.onEvent(LaunchEvent.PullToRefresh)
        testScheduler.advanceUntilIdle()

        coVerify(atLeast = 2) { launchesComponent.getLaunchUseCase(any(), any(), isRefresh = true) }
    }

    @Test
    fun `GIVEN network error WHEN PullToRefresh THEN emits Error state`() = runTest {
        var callCount = 0
        val testLaunch = TestData.createLaunch()
        val testLaunchUI = createTestLaunchUI()
        every { launchUiMapper.mapToLaunchUi(testLaunch) } returns testLaunchUI
        coEvery { launchesComponent.getLaunchUseCase(any(), any(), any()) } answers {
            callCount++
            if (callCount == 1) {
                LaunchResult.Success(testLaunch)
            } else {
                LaunchResult.Error(DataError.RemoteError.NETWORK_CONNECTION_FAILED)
            }
        }
        underTest = createViewModel()
        backgroundScope.launch(UnconfinedTestDispatcher()) { underTest.launchState.collect() }
        assertTrue(underTest.launchState.value is LaunchUiState.Success)

        underTest.onEvent(LaunchEvent.PullToRefresh)
        testScheduler.advanceUntilIdle()

        assertTrue(underTest.launchState.value is LaunchUiState.Error)
    }

    @Test
    fun `GIVEN launch ID WHEN ViewModel created THEN uses provided launch ID`() = runTest {
        val launchId = "specific-launch-id"
        val testLaunch = TestData.createLaunch(id = launchId)
        val testLaunchUI = createTestLaunchUI()
        coEvery { launchesComponent.getLaunchUseCase(launchId, any(), any()) } returns LaunchResult.Success(testLaunch)
        every { launchUiMapper.mapToLaunchUi(testLaunch) } returns testLaunchUI
        underTest = createViewModel(launchId = launchId)

        backgroundScope.launch(UnconfinedTestDispatcher()) { underTest.launchState.collect() }

        coVerify { launchesComponent.getLaunchUseCase(launchId, any(), any()) }
    }

    private fun createTestLaunchUI(
        missionName: String = "Test Launch"
    ): LaunchUI {
        return LaunchUI(
            missionName = missionName,
            status = LaunchStatus.GO,
            launchDate = "15 January 2026",
            launchTime = "10:30",
            windowStartTime = "09:00",
            windowEndTime = "12:00",
            windowDuration = "3 hrs",
            launchWindowPosition = 0.5f,
            imageUrl = "https://example.com/image.jpg",
            failReason = null,
            launchServiceProvider = null,
            rocket = RocketUI(
                configuration = ConfigurationUI(
                    name = "Falcon 9",
                    fullName = "Falcon 9 Block 5",
                    variant = "Block 5",
                    alias = "F9",
                    description = "Falcon 9 rocket",
                    imageUrl = "https://example.com/rocket.jpg",
                    totalLaunchCount = "300",
                    successfulLaunches = "290",
                    failedLaunches = "10",
                    length = "56.3",
                    diameter = "3.35",
                    launchMass = "456.0",
                    maidenFlight = "2007-05-13",
                    manufacturer = null,
                    families = emptyList(),
                    wikiUrl = null
                ),
                launcherStages = emptyList(),
                spacecraftStages = emptyList()
            ),
            mission = MissionUI(
                name = "Test Mission",
                description = "Test description",
                type = "Communications",
                orbitName = "Low Earth Orbit"
            ),
            pad = PadUI(
                name = "Launch Complex 39A",
                locationName = "Kennedy Space Center",
                countryName = "United States",
                countryCode = "US",
                imageUrl = "https://example.com/pad.jpg",
                description = "Test pad",
                latitude = "28.56",
                longitude = "-80.57",
                totalLaunchCount = "100",
                orbitalLaunchAttemptCount = "95",
                locationTotalLaunchCount = "200",
                locationTotalLandingCount = "50",
                mapUrl = null,
                mapImage = null
            ),
            updates = emptyList(),
            vidUrls = emptyList(),
            missionPatches = emptyList()
        )
    }
}
