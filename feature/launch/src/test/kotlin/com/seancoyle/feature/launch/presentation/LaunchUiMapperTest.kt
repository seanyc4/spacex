package com.seancoyle.feature.launch.presentation

import com.seancoyle.core.common.dataformatter.DateTransformer
import com.seancoyle.feature.launch.domain.model.Agency
import com.seancoyle.feature.launch.domain.model.Configuration
import com.seancoyle.feature.launch.domain.model.Country
import com.seancoyle.feature.launch.domain.model.Image
import com.seancoyle.feature.launch.domain.model.InfoUrl
import com.seancoyle.feature.launch.domain.model.Launch
import com.seancoyle.feature.launch.domain.model.LaunchSummary
import com.seancoyle.feature.launch.domain.model.LaunchUpdate
import com.seancoyle.feature.launch.domain.model.Location
import com.seancoyle.feature.launch.domain.model.Mission
import com.seancoyle.feature.launch.domain.model.MissionPatch
import com.seancoyle.feature.launch.domain.model.Orbit
import com.seancoyle.feature.launch.domain.model.Pad
import com.seancoyle.feature.launch.domain.model.Rocket
import com.seancoyle.feature.launch.domain.model.Status
import com.seancoyle.feature.launch.domain.model.VidUrl
import com.seancoyle.feature.launch.presentation.launch.model.LaunchStatus
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class LaunchUiMapperTest {

    private lateinit var dateTransformer: DateTransformer
    private lateinit var mapper: LaunchUiMapper

    @Before
    fun setup() {
        dateTransformer = mockk()
        mapper = LaunchUiMapper(dateTransformer)
    }

    @Test
    fun `mapToLaunchUi maps success status correctly`() = runTest {
        val launch = createTestLaunch(
            status = createStatus(abbrev = "Success")
        )
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals(LaunchStatus.SUCCESS, result.status)
    }

    @Test
    fun `mapToLaunchUi maps partial success status correctly`() = runTest {
        val launch = createTestLaunch(
            status = createStatus(abbrev = "Partial Success")
        )
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals(LaunchStatus.SUCCESS, result.status)
    }

    @Test
    fun `mapToLaunchUi maps failed status correctly`() = runTest {
        val launch = createTestLaunch(
            status = createStatus(abbrev = "Failure")
        )
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals(LaunchStatus.FAILED, result.status)
    }

    @Test
    fun `mapToLaunchUi maps go status correctly`() = runTest {
        val launch = createTestLaunch(
            status = createStatus(abbrev = "Go")
        )
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals(LaunchStatus.GO, result.status)
    }

    @Test
    fun `mapToLaunchUi maps TBD status correctly`() = runTest {
        val launch = createTestLaunch(
            status = createStatus(abbrev = "To Be Determined")
        )
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals(LaunchStatus.TBD, result.status)
    }

    @Test
    fun `mapToLaunchUi maps TBC status correctly`() = runTest {
        val launch = createTestLaunch(
            status = createStatus(abbrev = "To Be Confirmed")
        )
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals(LaunchStatus.TBC, result.status)
    }

    @Test
    fun `mapToLaunchUi maps unknown status to TBD`() = runTest {
        val launch = createTestLaunch(
            status = createStatus(abbrev = "Unknown Status")
        )
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals(LaunchStatus.TBD, result.status)
    }

    @Test
    fun `mapToLaunchUi handles missing status abbrev`() = runTest {
        val launch = createTestLaunch(status = Status(0, "", "", ""))
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals(LaunchStatus.TBD, result.status)
    }

    @Test
    fun `mapToLaunchUi formats launch date correctly`() = runTest {
        val expectedDate = "26 November 2026"
        val launch = createTestLaunch()
        every { dateTransformer.formatDate(any()) } returns LocalDateTime.of(2026, 11, 26, 10, 30)
        every { dateTransformer.formatDateTimeToString(any(), any()) } returns expectedDate

        val result = mapper.mapToLaunchUi(launch)

        assertEquals(expectedDate, result.launchDate)
    }

    @Test
    fun `mapToLaunchUi formats launch time correctly`() = runTest {
        val expectedTime = "10:30"
        val launch = createTestLaunch()
        every { dateTransformer.formatDate(any()) } returns LocalDateTime.of(2026, 1, 15, 10, 30)
        every { dateTransformer.formatDateTimeToString(any(), any()) } returns expectedTime

        val result = mapper.mapToLaunchUi(launch)

        assertEquals(expectedTime, result.launchTime)
    }

    @Test
    fun `mapToLaunchUi maps id correctly`() = runTest {
        val expectedId = "test-launch-id-123"
        val launch = createTestLaunch(id = expectedId)
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals(expectedId, result.id)
    }

    @Test
    fun `mapToLaunchUi maps mission name correctly and trims pipe suffix`() = runTest {
        val launch = createTestLaunch(missionName = "Falcon 9 Block 5 | Starlink Group 15-12")
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals("Falcon 9 Block 5", result.missionName)
    }

    @Test
    fun `mapToLaunchUi handles mission name without pipe`() = runTest {
        val launch = createTestLaunch(missionName = "Starlink Group 7-12")
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals("Starlink Group 7-12", result.missionName)
    }

    @Test
    fun `mapToLaunchUi maps image correctly`() = runTest {
        val expectedImageUrl = "https://example.com/image.jpg"
        val launch = createTestLaunch(
            image = createImage(imageUrl = expectedImageUrl)
        )
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals(expectedImageUrl, result.image.imageUrl)
    }

    @Test
    fun `mapToLaunchUi maps failReason correctly when present`() = runTest {
        val expectedFailReason = "Engine malfunction during ascent"
        val launch = createTestLaunch(failReason = expectedFailReason)
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals(expectedFailReason, result.failReason)
    }

    @Test
    fun `mapToLaunchUi handles null failReason`() = runTest {
        val launch = createTestLaunch(failReason = null)
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertNull(result.failReason)
    }

    @Test
    fun `mapToLaunchUi handles null launchServiceProvider`() = runTest {
        val launch = createTestLaunch(launchServiceProvider = null)
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertNull(result.launchServiceProvider)
    }

    @Test
    fun `mapToLaunchUi maps launchServiceProvider correctly when present`() = runTest {
        val expectedAgency = createAgency(name = "SpaceX")
        val launch = createTestLaunch(launchServiceProvider = expectedAgency)
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals("SpaceX", result.launchServiceProvider?.name)
    }

    @Test
    fun `mapToLaunchUi maps rocket correctly`() = runTest {
        val launch = createTestLaunch()
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals("Falcon 9", result.rocket.configuration.name)
    }

    @Test
    fun `mapToLaunchUi maps mission correctly`() = runTest {
        val launch = createTestLaunch()
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals("Starlink Mission", result.mission.name)
    }

    @Test
    fun `mapToLaunchUi maps pad correctly`() = runTest {
        val launch = createTestLaunch()
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals("Space Launch Complex 40", result.pad.name)
    }

    @Test
    fun `mapToLaunchUi calculates window duration for 1 hour window`() = runTest {
        val windowStart = "2026-01-15T10:00:00Z"
        val windowEnd = "2026-01-15T11:00:00Z"
        val launch = createTestLaunch(windowStart = windowStart, windowEnd = windowEnd)

        val startDateTime = LocalDateTime.of(2026, 1, 15, 10, 0)
        val endDateTime = LocalDateTime.of(2026, 1, 15, 11, 0)
        every { dateTransformer.formatDate("2026-01-15T10:30:00Z") } returns LocalDateTime.of(
            2026,
            1,
            15,
            10,
            30
        )
        every { dateTransformer.formatDate(windowStart) } returns startDateTime
        every { dateTransformer.formatDate(windowEnd) } returns endDateTime
        every { dateTransformer.formatDateTimeToString(any(), any()) } returns "Formatted Date"

        val result = mapper.mapToLaunchUi(launch)

        assertEquals("1h", result.windowDuration)
    }

    @Test
    fun `mapToLaunchUi calculates window duration for hours and minutes`() = runTest {
        val windowStart = "2026-01-15T10:00:00Z"
        val windowEnd = "2026-01-15T11:30:00Z"
        val launch = createTestLaunch(windowStart = windowStart, windowEnd = windowEnd)

        val startDateTime = LocalDateTime.of(2026, 1, 15, 10, 0)
        val endDateTime = LocalDateTime.of(2026, 1, 15, 11, 30)
        every { dateTransformer.formatDate("2026-01-15T10:30:00Z") } returns LocalDateTime.of(
            2026,
            1,
            15,
            10,
            30
        )
        every { dateTransformer.formatDate(windowStart) } returns startDateTime
        every { dateTransformer.formatDate(windowEnd) } returns endDateTime
        every { dateTransformer.formatDateTimeToString(any(), any()) } returns "Formatted Date"

        val result = mapper.mapToLaunchUi(launch)

        assertEquals("1h 30m", result.windowDuration)
    }

    @Test
    fun `mapToLaunchUi shows instantaneous for same start and end time`() = runTest {
        val windowStart = "2026-01-15T10:00:00Z"
        val windowEnd = "2026-01-15T10:00:00Z"
        val launch = createTestLaunch(windowStart = windowStart, windowEnd = windowEnd)

        val sameDateTime = LocalDateTime.of(2026, 1, 15, 10, 0)
        every { dateTransformer.formatDate("2026-01-15T10:30:00Z") } returns LocalDateTime.of(
            2026,
            1,
            15,
            10,
            30
        )
        every { dateTransformer.formatDate(windowStart) } returns sameDateTime
        every { dateTransformer.formatDate(windowEnd) } returns sameDateTime
        every { dateTransformer.formatDateTimeToString(any(), any()) } returns "Formatted Date"

        val result = mapper.mapToLaunchUi(launch)

        assertEquals("Instantaneous", result.windowDuration)
    }

    @Test
    fun `mapToLaunchUi handles null windowStart and windowEnd`() = runTest {
        val launch = createTestLaunch(windowStart = null, windowEnd = null)
        every { dateTransformer.formatDate("2026-01-15T10:30:00Z") } returns LocalDateTime.of(
            2026,
            1,
            15,
            10,
            30
        )
        every { dateTransformer.formatDateTimeToString(any(), any()) } returns "Formatted Date"

        val result = mapper.mapToLaunchUi(launch)

        assertNull(result.windowDuration)
    }

    @Test
    fun `mapToLaunchUi maps empty updates list`() = runTest {
        val launch = createTestLaunch(updates = emptyList())
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals(0, result.updates.size)
    }

    @Test
    fun `mapToLaunchUi maps null updates to empty list`() = runTest {
        val launch = createTestLaunch(updates = null)
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals(0, result.updates.size)
    }

    @Test
    fun `mapToLaunchUi maps empty vidUrls list`() = runTest {
        val launch = createTestLaunch(vidUrls = emptyList())
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals(0, result.vidUrls.size)
    }

    @Test
    fun `mapToLaunchUi maps empty infoUrls list`() = runTest {
        val launch = createTestLaunch(infoUrls = emptyList())
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals(0, result.infoUrls.size)
    }

    @Test
    fun `mapToLaunchUi maps empty missionPatches list`() = runTest {
        val launch = createTestLaunch(missionPatches = emptyList())
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals(0, result.missionPatches.size)
    }

    @Test
    fun `mapToLaunchesUi maps id correctly`() = runTest {
        val expectedId = "launch-summary-id"
        val launchSummary = createTestLaunchSummary(id = expectedId)
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchesUi(launchSummary)

        assertEquals(expectedId, result.id)
    }

    @Test
    fun `mapToLaunchesUi maps mission name and trims pipe suffix`() = runTest {
        val launchSummary = createTestLaunchSummary(
            missionName = "Falcon 9 Block 5 | Starlink Mission"
        )
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchesUi(launchSummary)

        assertEquals("Falcon 9 Block 5", result.missionName)
    }

    @Test
    fun `mapToLaunchesUi maps status correctly`() = runTest {
        val launchSummary = createTestLaunchSummary(
            status = createStatus(abbrev = "Go")
        )
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchesUi(launchSummary)

        assertEquals(LaunchStatus.GO, result.status)
    }

    @Test
    fun `mapToLaunchesUi maps imageUrl correctly`() = runTest {
        val expectedImageUrl = "https://example.com/summary-image.jpg"
        val launchSummary = createTestLaunchSummary(imageUrl = expectedImageUrl)
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchesUi(launchSummary)

        assertEquals(expectedImageUrl, result.imageUrl)
    }

    private fun setupDateFormatterDefaults() {
        every { dateTransformer.formatDate(any()) } returns LocalDateTime.of(2026, 1, 15, 10, 30)
        every { dateTransformer.formatDateTimeToString(any(), any()) } returns "Formatted Date"
    }

    private fun createTestLaunch(
        id: String = "test-launch-id",
        missionName: String = "Test Launch",
        net: String = "2026-01-15T10:30:00Z",
        status: Status? = createStatus(),
        image: Image = createImage(),
        failReason: String? = null,
        launchServiceProvider: Agency? = createAgency(),
        rocket: Rocket = createRocket(),
        mission: Mission = createMission(),
        pad: Pad = createPad(),
        windowStart: String? = "2026-01-15T10:00:00Z",
        windowEnd: String? = "2026-01-15T11:00:00Z",
        updates: List<LaunchUpdate>? = emptyList(),
        vidUrls: List<VidUrl>? = emptyList(),
        infoUrls: List<InfoUrl>? = emptyList(),
        missionPatches: List<MissionPatch>? = emptyList()
    ): Launch = Launch(
        id = id,
        url = null,
        missionName = missionName,
        lastUpdated = null,
        net = net,
        netPrecision = null,
        status = status ?: createStatus(),
        windowEnd = windowEnd,
        windowStart = windowStart,
        image = image,
        infographic = null,
        probability = null,
        weatherConcerns = null,
        failReason = failReason,
        launchServiceProvider = launchServiceProvider,
        rocket = rocket,
        mission = mission,
        pad = pad,
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
        updates = updates,
        infoUrls = infoUrls,
        vidUrls = vidUrls,
        padTurnaround = null,
        missionPatches = missionPatches
    )

    private fun createTestLaunchSummary(
        id: String = "test-summary-id",
        missionName: String = "Test Summary",
        net: String = "2026-01-15T10:30:00Z",
        status: Status = createStatus(),
        imageUrl: String = "https://example.com/image.jpg"
    ): LaunchSummary = LaunchSummary(
        id = id,
        missionName = missionName,
        net = net,
        status = status,
        imageUrl = imageUrl
    )

    private fun createStatus(
        id: Int = 3,
        name: String = "Success",
        abbrev: String = "Success"
    ): Status = Status(
        id = id,
        name = name,
        abbrev = abbrev,
        description = "Status description"
    )

    private fun createImage(
        imageUrl: String = "https://example.com/image.jpg"
    ): Image = Image(
        id = 1,
        name = "Test Image",
        imageUrl = imageUrl,
        thumbnailUrl = "https://example.com/thumb.jpg",
        credit = "Test Credit"
    )

    private fun createAgency(
        name: String = "SpaceX"
    ): Agency = Agency(
        id = 121,
        url = "https://ll.thespacedevs.com/2.2.0/agencies/121/",
        name = name,
        abbrev = "SpX",
        type = "Commercial",
        featured = true,
        country = emptyList(),
        description = "Space Exploration Technologies Corp.",
        administrator = "Elon Musk",
        foundingYear = 2002,
        launchers = "Falcon 9",
        spacecraft = "Dragon",
        image = createImage(imageUrl = "https://example.com/agency.jpg"),
        totalLaunchCount = 200,
        consecutiveSuccessfulLaunches = 150,
        successfulLaunches = 190,
        failedLaunches = 10,
        pendingLaunches = 50,
        consecutiveSuccessfulLandings = 100,
        successfulLandings = 180,
        failedLandings = 20,
        attemptedLandings = 200,
        successfulLandingsSpacecraft = 50,
        failedLandingsSpacecraft = 5,
        attemptedLandingsSpacecraft = 55,
        successfulLandingsPayload = 30,
        failedLandingsPayload = 3,
        attemptedLandingsPayload = 33,
        infoUrl = "https://www.spacex.com",
        wikiUrl = "https://en.wikipedia.org/wiki/SpaceX"
    )

    private fun createRocket(): Rocket = Rocket(
        id = 123,
        configuration = Configuration(
            id = 164,
            url = "https://ll.thespacedevs.com/2.3.0/config/launcher/164/",
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
    )

    private fun createMission(): Mission = Mission(
        id = 456,
        name = "Starlink Mission",
        description = "A batch of satellites",
        type = "Communications",
        orbit = Orbit(id = 8, name = "Low Earth Orbit", abbrev = "LEO"),
        agencies = emptyList(),
        infoUrls = emptyList(),
        vidUrls = emptyList()
    )

    private fun createPad(): Pad = Pad(
        id = 87,
        url = "https://ll.thespacedevs.com/2.2.0/pad/87/",
        agencies = emptyList(),
        name = "Space Launch Complex 40",
        image = createImage(imageUrl = "https://example.com/pad.jpg"),
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
            url = "https://ll.thespacedevs.com/2.2.0/location/12/",
            name = "Cape Canaveral, FL, USA",
            country = Country(
                id = 1,
                name = "United States",
                alpha2Code = "US",
                alpha3Code = "USA",
                nationalityName = "American"
            ),
            description = "Cape Canaveral Space Force Station",
            image = null,
            mapImage = null,
            longitude = -80.57735736,
            latitude = 28.56194122,
            timezoneName = "America/New_York",
            totalLaunchCount = 778,
            totalLandingCount = 56
        )
    )
}
