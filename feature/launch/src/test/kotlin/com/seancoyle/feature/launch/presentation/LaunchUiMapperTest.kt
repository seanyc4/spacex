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
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class LaunchUiMapperTest {

    @MockK
    private lateinit var dateTransformer: DateTransformer
    private lateinit var mapper: LaunchUiMapper

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mapper = LaunchUiMapper(dateTransformer)
    }

    @Test
    fun `GIVEN launch with success status WHEN mapToLaunchUi THEN maps to SUCCESS status`() = runTest {
        val launch = createTestLaunch(
            status = createStatus(abbrev = "Success")
        )
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals(LaunchStatus.SUCCESS, result.status)
    }

    @Test
    fun `GIVEN launch with partial success status WHEN mapToLaunchUi THEN maps to SUCCESS status`() = runTest {
        val launch = createTestLaunch(
            status = createStatus(abbrev = "Partial Success")
        )
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals(LaunchStatus.SUCCESS, result.status)
    }

    @Test
    fun `GIVEN launch with failure status WHEN mapToLaunchUi THEN maps to FAILED status`() = runTest {
        val launch = createTestLaunch(
            status = createStatus(abbrev = "Failure")
        )
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals(LaunchStatus.FAILED, result.status)
    }

    @Test
    fun `GIVEN launch with go status WHEN mapToLaunchUi THEN maps to GO status`() = runTest {
        val launch = createTestLaunch(
            status = createStatus(abbrev = "Go")
        )
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals(LaunchStatus.GO, result.status)
    }

    @Test
    fun `GIVEN launch with TBD status WHEN mapToLaunchUi THEN maps to TBD status`() = runTest {
        val launch = createTestLaunch(
            status = createStatus(abbrev = "To Be Determined")
        )
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals(LaunchStatus.TBD, result.status)
    }

    @Test
    fun `GIVEN launch with TBC status WHEN mapToLaunchUi THEN maps to TBC status`() = runTest {
        val launch = createTestLaunch(
            status = createStatus(abbrev = "To Be Confirmed")
        )
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals(LaunchStatus.TBC, result.status)
    }

    @Test
    fun `GIVEN launch with unknown status WHEN mapToLaunchUi THEN maps to TBD status`() = runTest {
        val launch = createTestLaunch(
            status = createStatus(abbrev = "Unknown Status")
        )
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals(LaunchStatus.TBD, result.status)
    }

    @Test
    fun `GIVEN launch with missing status abbrev WHEN mapToLaunchUi THEN maps to TBD status`() = runTest {
        val launch = createTestLaunch(status = Status(0, "", "", ""))
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals(LaunchStatus.TBD, result.status)
    }

    @Test
    fun `GIVEN launch with date WHEN mapToLaunchUi THEN formats launch date correctly`() = runTest {
        val expectedDate = "26 November 2026"
        val launch = createTestLaunch()
        every { dateTransformer.formatDate(any()) } returns LocalDateTime.of(2026, 11, 26, 10, 30)
        every { dateTransformer.formatDateTimeToString(any(), any()) } returns expectedDate

        val result = mapper.mapToLaunchUi(launch)

        assertEquals(expectedDate, result.launchDate)
    }

    @Test
    fun `GIVEN launch with time WHEN mapToLaunchUi THEN formats launch time correctly`() = runTest {
        val expectedTime = "10:30"
        val launch = createTestLaunch()
        every { dateTransformer.formatDate(any()) } returns LocalDateTime.of(2026, 1, 15, 10, 30)
        every { dateTransformer.formatDateTimeToString(any(), any()) } returns expectedTime

        val result = mapper.mapToLaunchUi(launch)

        assertEquals(expectedTime, result.launchTime)
    }

    @Test
    fun `GIVEN launch with mission name containing pipe WHEN mapToLaunchUi THEN trims after pipe`() = runTest {
        val launch = createTestLaunch(missionName = "Falcon 9 Block 5 | Starlink Group 15-12")
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals("Falcon 9 Block 5", result.missionName)
    }

    @Test
    fun `GIVEN launch with mission name without pipe WHEN mapToLaunchUi THEN keeps full name`() = runTest {
        val launch = createTestLaunch(missionName = "Starlink Group 7-12")
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals("Starlink Group 7-12", result.missionName)
    }

    @Test
    fun `GIVEN launch with image WHEN mapToLaunchUi THEN maps image correctly`() = runTest {
        val expectedImageUrl = "https://example.com/image.jpg"
        val launch = createTestLaunch(
            image = createImage(imageUrl = expectedImageUrl)
        )
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals(expectedImageUrl, result.imageUrl)
    }

    @Test
    fun `GIVEN launch with failReason WHEN mapToLaunchUi THEN maps failReason correctly`() = runTest {
        val expectedFailReason = "Engine malfunction during ascent"
        val launch = createTestLaunch(failReason = expectedFailReason)
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals(expectedFailReason, result.failReason)
    }

    @Test
    fun `GIVEN launch with null failReason WHEN mapToLaunchUi THEN returns null failReason`() = runTest {
        val launch = createTestLaunch(failReason = null)
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertNull(result.failReason)
    }

    @Test
    fun `GIVEN launch with null launchServiceProvider WHEN mapToLaunchUi THEN returns null provider`() = runTest {
        val launch = createTestLaunch(launchServiceProvider = null)
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertNull(result.launchServiceProvider)
    }

    @Test
    fun `GIVEN launch with launchServiceProvider WHEN mapToLaunchUi THEN maps provider correctly`() = runTest {
        val expectedAgency = createAgency(name = "SpaceX")
        val launch = createTestLaunch(launchServiceProvider = expectedAgency)
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals("SpaceX", result.launchServiceProvider?.name)
    }

    @Test
    fun `GIVEN launch with rocket WHEN mapToLaunchUi THEN maps rocket correctly`() = runTest {
        val launch = createTestLaunch()
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals("Falcon 9", result.rocket.configuration.name)
    }

    @Test
    fun `GIVEN launch with mission WHEN mapToLaunchUi THEN maps mission correctly`() = runTest {
        val launch = createTestLaunch()
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals("Starlink Mission", result.mission.name)
    }

    @Test
    fun `GIVEN launch with pad WHEN mapToLaunchUi THEN maps pad correctly`() = runTest {
        val launch = createTestLaunch()
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals("Space Launch Complex 40", result.pad.name)
    }

    @Test
    fun `GIVEN pad with location WHEN mapToLaunchUi THEN maps location name and counts correctly`() = runTest {
        val location = Location(
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
        val pad = createPad().copy(location = location)
        val launch = createTestLaunch(pad = pad)
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals("Cape Canaveral, FL, USA", result.pad.locationName)
        assertEquals("778", result.pad.locationTotalLaunchCount)
        assertEquals("56", result.pad.locationTotalLandingCount)
    }

    @Test
    fun `GIVEN pad with location counts WHEN mapToLaunchUi THEN formats counts as strings correctly`() = runTest {
        val location = Location(
            id = 12,
            url = "https://ll.thespacedevs.com/2.2.0/location/12/",
            name = "Kennedy Space Center, FL, USA",
            country = null,
            description = null,
            image = null,
            mapImage = null,
            longitude = null,
            latitude = null,
            timezoneName = "America/New_York",
            totalLaunchCount = 1543,
            totalLandingCount = 89
        )
        val pad = createPad().copy(location = location)
        val launch = createTestLaunch(pad = pad)
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals("1543", result.pad.locationTotalLaunchCount)
        assertEquals("89", result.pad.locationTotalLandingCount)
    }


    @Test
    fun `mapToLaunchUi calculates window duration for 1 hour window`() = runTest {
        val windowStart = "2026-01-15T10:00:00Z"
        val windowEnd = "2026-01-15T11:00:00Z"
        val launch = createTestLaunch(windowStart = windowStart, windowEnd = windowEnd)

        val startDateTime = LocalDateTime.of(2026, 1, 15, 10, 0)
        val endDateTime = LocalDateTime.of(2026, 1, 15, 11, 0)
        every { dateTransformer.formatDate("2026-01-15T10:30:00Z") } returns LocalDateTime.of(2026, 1, 15, 10, 30)
        every { dateTransformer.formatDate("2007-05-13") } returns LocalDateTime.of(2007, 5, 13, 0, 0)
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
        every { dateTransformer.formatDate("2026-01-15T10:30:00Z") } returns LocalDateTime.of(2026, 1, 15, 10, 30)
        every { dateTransformer.formatDate("2007-05-13") } returns LocalDateTime.of(2007, 5, 13, 0, 0)
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
        every { dateTransformer.formatDate("2026-01-15T10:30:00Z") } returns LocalDateTime.of(2026, 1, 15, 10, 30)
        every { dateTransformer.formatDate("2007-05-13") } returns LocalDateTime.of(2007, 5, 13, 0, 0)
        every { dateTransformer.formatDate(windowStart) } returns sameDateTime
        every { dateTransformer.formatDate(windowEnd) } returns sameDateTime
        every { dateTransformer.formatDateTimeToString(any(), any()) } returns "Formatted Date"

        val result = mapper.mapToLaunchUi(launch)

        assertEquals("Instantaneous", result.windowDuration)
    }

    @Test
    fun `mapToLaunchUi handles null windowStart and windowEnd`() = runTest {
        val launch = createTestLaunch(windowStart = null, windowEnd = null)
        every { dateTransformer.formatDate("2026-01-15T10:30:00Z") } returns LocalDateTime.of(2026, 1, 15, 10, 30)
        every { dateTransformer.formatDate("2007-05-13") } returns LocalDateTime.of(2007, 5, 13, 0, 0)
        every { dateTransformer.formatDateTimeToString(any(), any()) } returns "Formatted Date"

        val result = mapper.mapToLaunchUi(launch)

        assertEquals("00:00",result.windowDuration)
    }

    @Test
    fun `mapToLaunchUi maps empty updates list`() = runTest {
        val launch = createTestLaunch(updates = emptyList())
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
    fun `mapToLaunchUi maps empty missionPatches list`() = runTest {
        val launch = createTestLaunch(missionPatches = emptyList())
        setupDateFormatterDefaults()

        val result = mapper.mapToLaunchUi(launch)

        assertEquals(0, result.missionPatches.size)
    }

    @Test
    fun `mapToLaunchUi formats maidenFlight date correctly`() = runTest {
        val expectedMaidenFlight = "13 May 2007"
        val launch = createTestLaunch()
        every { dateTransformer.formatDate("2026-01-15T10:30:00Z") } returns LocalDateTime.of(2026, 1, 15, 10, 30)
        every { dateTransformer.formatDate("2026-01-15T10:00:00Z") } returns LocalDateTime.of(2026, 1, 15, 10, 0)
        every { dateTransformer.formatDate("2026-01-15T11:00:00Z") } returns LocalDateTime.of(2026, 1, 15, 11, 0)
        every { dateTransformer.formatDate("2007-05-13") } returns LocalDateTime.of(2007, 5, 13, 0, 0)
        every { dateTransformer.formatDateTimeToString(any(), any()) } answers {
            if (firstArg<LocalDateTime>().year == 2007) expectedMaidenFlight else "Formatted Date"
        }

        val result = mapper.mapToLaunchUi(launch)

        assertEquals(expectedMaidenFlight, result.rocket.configuration.maidenFlight)
    }

    @Test
    fun `mapToLaunchUi handles null maidenFlight gracefully`() = runTest {
        val rocket = createRocket().copy(
            configuration = createRocket().configuration.copy(maidenFlight = null)
        )
        val launch = createTestLaunch(rocket = rocket)
        every { dateTransformer.formatDate(any()) } returns LocalDateTime.now()
        every { dateTransformer.formatDateTimeToString(any(), any()) } returns "Formatted Date"

        val result = mapper.mapToLaunchUi(launch)

        // Should use fallback date (current time) since formatDate now handles nulls
        assertEquals("Formatted Date", result.rocket.configuration.maidenFlight)
    }

    @Test
    fun `mapToLaunchUi formats LaunchUpdate createdOn correctly`() = runTest {
        val expectedUpdateDate = "20 January 2026 at 14:30"
        val launchUpdate = LaunchUpdate(
            id = 1,
            profileImage = null,
            comment = "Launch delayed",
            infoUrl = null,
            createdBy = "Launch Director",
            createdOn = "2026-01-20T14:30:00Z"
        )
        val launch = createTestLaunch(updates = listOf(launchUpdate))

        every { dateTransformer.formatDate("2026-01-15T10:30:00Z") } returns LocalDateTime.of(2026, 1, 15, 10, 30)
        every { dateTransformer.formatDate("2026-01-15T10:00:00Z") } returns LocalDateTime.of(2026, 1, 15, 10, 0)
        every { dateTransformer.formatDate("2026-01-15T11:00:00Z") } returns LocalDateTime.of(2026, 1, 15, 11, 0)
        every { dateTransformer.formatDate("2007-05-13") } returns LocalDateTime.of(2007, 5, 13, 0, 0)
        every { dateTransformer.formatDate("2026-01-20T14:30:00Z") } returns LocalDateTime.of(2026, 1, 20, 14, 30)
        every { dateTransformer.formatDateTimeToString(any(), any()) } answers {
            val dateTime = firstArg<LocalDateTime>()
            when {
                dateTime.year == 2026 && dateTime.monthValue == 1 && dateTime.dayOfMonth == 20 -> expectedUpdateDate
                else -> "Formatted Date"
            }
        }

        val result = mapper.mapToLaunchUi(launch)

        assertEquals(1, result.updates.size)
        assertEquals(expectedUpdateDate, result.updates[0].createdOn)
        assertEquals("Launch Director", result.updates[0].createdBy)
    }

    @Test
    fun `mapToLaunchUi handles null LaunchUpdate createdOn gracefully`() = runTest {
        val launchUpdate = LaunchUpdate(
            id = 1,
            profileImage = null,
            comment = "Update comment",
            infoUrl = null,
            createdBy = "Admin",
            createdOn = null
        )
        val launch = createTestLaunch(updates = listOf(launchUpdate))
        every { dateTransformer.formatDate(any()) } returns LocalDateTime.now()
        every { dateTransformer.formatDateTimeToString(any(), any()) } returns "Formatted Date"

        val result = mapper.mapToLaunchUi(launch)

        assertEquals(1, result.updates.size)
        // Should use fallback date since formatDate now handles nulls
        assertEquals("Formatted Date", result.updates[0].createdOn)
    }

    @Test
    fun `mapToLaunchUi handles malformed date strings without crashing`() = runTest {
        val launchUpdate = LaunchUpdate(
            id = 1,
            profileImage = null,
            comment = "Update",
            infoUrl = null,
            createdBy = "User",
            createdOn = "invalid-date-format"
        )
        val rocket = createRocket().copy(
            configuration = createRocket().configuration.copy(maidenFlight = "also-invalid")
        )
        val launch = createTestLaunch(
            updates = listOf(launchUpdate),
            rocket = rocket
        )

        // formatDate now returns current time as fallback for unparseable dates
        every { dateTransformer.formatDate(any()) } returns LocalDateTime.now()
        every { dateTransformer.formatDateTimeToString(any(), any()) } returns "Formatted Date"

        // Should not throw exception
        val result = mapper.mapToLaunchUi(launch)

        assertEquals("Formatted Date", result.rocket.configuration.maidenFlight)
        assertEquals("Formatted Date", result.updates[0].createdOn)
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
        // Mock formatDate for all possible inputs (net, windowStart, windowEnd, maidenFlight, createdOn)
        every { dateTransformer.formatDate(any()) } returns LocalDateTime.of(2026, 1, 15, 10, 30)
        // Mock formatDateTimeToString for all format outputs (dates, times, updates)
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
        updates: List<LaunchUpdate> = emptyList(),
        vidUrls: List<VidUrl> = emptyList(),
        infoUrls: List<InfoUrl> = emptyList(),
        missionPatches: List<MissionPatch> = emptyList()
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
        imageUrl: String = "https://example.com/image.jpg",
        location: String = "Kennedy Space Center, FL, USA"
    ): LaunchSummary = LaunchSummary(
        id = id,
        missionName = missionName,
        net = net,
        status = status,
        imageUrl = imageUrl,
        location = location
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
