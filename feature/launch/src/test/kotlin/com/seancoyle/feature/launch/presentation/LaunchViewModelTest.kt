package com.seancoyle.feature.launch.presentation

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.core.test.TestCoroutineRule
import com.seancoyle.feature.launch.domain.model.Configuration
import com.seancoyle.feature.launch.domain.model.Country
import com.seancoyle.feature.launch.domain.model.Image
import com.seancoyle.feature.launch.domain.model.Launch
import com.seancoyle.feature.launch.domain.model.Location
import com.seancoyle.feature.launch.domain.model.Mission
import com.seancoyle.feature.launch.domain.model.Orbit
import com.seancoyle.feature.launch.domain.model.Pad
import com.seancoyle.feature.launch.domain.model.Rocket
import com.seancoyle.feature.launch.domain.model.Status
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
import io.mockk.MockKAnnotations
import io.mockk.coEvery
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

    @MockK()
    private lateinit var launchUiMapper: LaunchUiMapper

    private lateinit var underTest: LaunchViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        underTest = LaunchViewModel(
            launchesComponent = launchesComponent,
            uiMapper = launchUiMapper,
            launchId = "test-launch-id",
            launchType = LaunchesType.UPCOMING
        )
    }

    @Test
    fun `initial state is Loading`() = runTest {
        assertTrue(underTest.launchState.value is LaunchUiState.Loading)
    }

    @Test
    fun `launchState emits Success state when use case returns data`() = runTest {
        val testLaunch = createTestLaunch(missionName = "Starlink Mission")
        val testLaunchUI = createTestLaunchUI(missionName = "Starlink Mission")
        coEvery { launchesComponent.getLaunchUseCase(any(), any(), any()) } returns LaunchResult.Success(testLaunch)
        every { launchUiMapper.mapToLaunchUi(testLaunch) } returns testLaunchUI

        backgroundScope.launch(UnconfinedTestDispatcher()) { underTest.launchState.collect() }

        val state = underTest.launchState.value
        assertTrue(state is LaunchUiState.Success)
        assertEquals("Starlink Mission", (state as LaunchUiState.Success).launch.missionName)
    }

    @Test
    fun `launchState emits Error state when use case returns error`() = runTest {
        coEvery { launchesComponent.getLaunchUseCase(any(), any(), any()) } returns
            LaunchResult.Error(DataError.RemoteError.NETWORK_CONNECTION_FAILED)

        backgroundScope.launch(UnconfinedTestDispatcher()) { underTest.launchState.collect() }

        val state = underTest.launchState.value
        assertTrue(state is LaunchUiState.Error)
    }

    @Test
    fun `onEvent RetryFetch reloads launch data`() = runTest {
        val testLaunch = createTestLaunch(missionName = "Starlink Mission")
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

        backgroundScope.launch(UnconfinedTestDispatcher()) { underTest.launchState.collect() }

        assertTrue(underTest.launchState.value is LaunchUiState.Error)

        underTest.onEvent(LaunchEvent.RetryFetch)

        testScheduler.advanceUntilIdle()

        backgroundScope.launch(UnconfinedTestDispatcher()) { underTest.launchState.collect() }

        // Second call should succeed
        val secondState = underTest.launchState.value
        assertTrue(secondState is LaunchUiState.Success)
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
            launchServiceProvider = null, // AgencyUI? (can be null for test)
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
                name = "Starlink Mission",
                description = "A batch of satellites",
                type = "Communications",
                orbitName = "Low Earth Orbit"
            ),
            pad = PadUI(
                name = "Space Launch Complex 40",
                locationName = "Cape Canaveral, FL, USA",
                countryName = "United States",
                countryCode = "US",
                imageUrl = "https://example.com/pad.jpg",
                description = "Cape Canaveral SLC-40",
                latitude = "28.56194122",
                longitude = "-80.57735736",
                totalLaunchCount = "957",
                orbitalLaunchAttemptCount = "457",
                locationTotalLaunchCount = "778",
                locationTotalLandingCount = "56",
                mapUrl = null,
                mapImage = null
            ),
            updates = emptyList(),
            vidUrls = emptyList(),
            missionPatches = emptyList()
        )
    }

    private fun createTestLaunch(
        id: String = "test-launch-id",
        missionName: String = "Test Launch"
    ): Launch = Launch(
        id = id,
        url = null,
        missionName = missionName,
        lastUpdated = null,
        net = "2026-01-15T10:30:00Z",
        netPrecision = null,
        status = Status(id = 1, name = "Go", abbrev = "Go", description = null),
        windowEnd = null,
        windowStart = null,
        image = Image(
            id = 1,
            name = "Test Image",
            imageUrl = "https://example.com/image.jpg",
            thumbnailUrl = "https://example.com/thumb.jpg",
            credit = "Test"
        ),
        infographic = null,
        probability = null,
        weatherConcerns = null,
        failReason = null,
        launchServiceProvider = null,
        rocket = Rocket(
            id = 123,
            configuration = Configuration(
                id = 164,
                url = null,
                name = "Falcon 9",
                fullName = "Falcon 9 Block 5",
                variant = "Block 5",
                families = emptyList(),
                manufacturer = null,
                image = null,
                wikiUrl = null,
                description = "Falcon 9 rocket",
                alias = "F9",
                totalLaunchCount = 300,
                successfulLaunches = 290,
                failedLaunches = 10,
                length = 56.3,
                diameter = 3.35,
                maidenFlight = "2007-05-13",
                launchMass = 456.0
            ),
            launcherStage = emptyList(),
            spacecraftStage = emptyList()
        ),
        mission = Mission(
            id = 456,
            name = "Starlink Mission",
            description = "A batch of satellites",
            type = "Communications",
            orbit = Orbit(id = 8, name = "Low Earth Orbit", abbrev = "LEO"),
            agencies = emptyList(),
            infoUrls = emptyList(),
            vidUrls = emptyList()
        ),
        pad = Pad(
            id = 87,
            url = null,
            agencies = emptyList(),
            name = "Space Launch Complex 40",
            image = Image(
                id = 3,
                name = "Pad Image",
                imageUrl = "https://example.com/pad.jpg",
                thumbnailUrl = "https://example.com/pad_thumb.jpg",
                credit = "Test"
            ),
            description = "Cape Canaveral SLC-40",
            country = Country(
                id = 1,
                name = "United States",
                alpha2Code = "US",
                alpha3Code = "USA",
                nationalityName = "American"
            ),
            latitude = 28.56194122,
            longitude = -80.57735736,
            mapUrl = null,
            mapImage = null,
            wikiUrl = null,
            infoUrl = null,
            totalLaunchCount = 957,
            orbitalLaunchAttemptCount = 457,
            fastestTurnaround = null,
            location = Location(
                id = 12,
                url = null,
                name = "Cape Canaveral, FL, USA",
                country = Country(
                    id = 1,
                    name = "United States",
                    alpha2Code = "US",
                    alpha3Code = "USA",
                    nationalityName = "American"
                ),
                description = null,
                image = null,
                mapImage = null,
                longitude = -80.57735736,
                latitude = 28.56194122,
                timezoneName = "America/New_York",
                totalLaunchCount = 778,
                totalLandingCount = 56
            )
        ),
        webcastLive = null,
        program = emptyList(),
        orbitalLaunchAttemptCount = null,
        locationLaunchAttemptCount = null,
        padLaunchAttemptCount = null,
        agencyLaunchAttemptCount = null,
        orbitalLaunchAttemptCountYear = null,
        locationLaunchAttemptCountYear = null,
        padLaunchAttemptCountYear = null,
        agencyLaunchAttemptCountYear = null,
        updates = emptyList(),
        infoUrls = emptyList(),
        vidUrls = emptyList(),
        padTurnaround = null,
        missionPatches = emptyList()
    )
}
