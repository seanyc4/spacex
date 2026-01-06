package com.seancoyle.feature.launch.presentation

import com.seancoyle.feature.launch.domain.model.Agency
import com.seancoyle.feature.launch.domain.model.Configuration
import com.seancoyle.feature.launch.domain.model.Country
import com.seancoyle.feature.launch.domain.model.Image
import com.seancoyle.feature.launch.domain.model.InfoUrl
import com.seancoyle.feature.launch.domain.model.LaunchUpdate
import com.seancoyle.feature.launch.domain.model.Location
import com.seancoyle.feature.launch.domain.model.Mission
import com.seancoyle.feature.launch.domain.model.MissionPatch
import com.seancoyle.feature.launch.domain.model.Orbit
import com.seancoyle.feature.launch.domain.model.Pad
import com.seancoyle.feature.launch.domain.model.Rocket
import com.seancoyle.feature.launch.domain.model.VidUrl
import com.seancoyle.feature.launch.presentation.launch.model.LaunchStatus
import com.seancoyle.feature.launch.presentation.launch.model.LaunchUI
import com.seancoyle.feature.launch.presentation.launches.model.LaunchesUi
import java.time.LocalDateTime

/**
 * Test data factory for UI tests.
 * Provides realistic fake data for testing Compose UI components.
 */
object TestDataFactory {

    fun createLaunchesUi(
        id: String = "test-launch-id",
        missionName: String = "Starlink Mission",
        launchDate: String = "January 15, 2026",
        status: LaunchStatus = LaunchStatus.GO,
        imageUrl: String = "https://example.com/image.jpg"
    ): LaunchesUi = LaunchesUi(
        id = id,
        missionName = missionName,
        launchDate = launchDate,
        status = status,
        imageUrl = imageUrl
    )

    fun createLaunchUI(
        id: String = "test-launch-id",
        missionName: String = "Starlink Group 7-12",
        launchDate: String = "26 November 2026",
        launchTime: String = "10:30",
        launchDateTime: LocalDateTime = LocalDateTime.of(2026, 1, 15, 10, 30),
        status: LaunchStatus = LaunchStatus.SUCCESS,
        windowEnd: String? = "Jan 15, 2026, 11:00 AM EST",
        windowStart: String? = "Jan 15, 2026, 10:00 AM EST",
        windowStartTime: String? = "10:00",
        windowEndTime: String? = "11:00",
        windowDuration: String? = "1h",
        windowStartDateTime: LocalDateTime? = LocalDateTime.of(2026, 1, 15, 10, 0),
        windowEndDateTime: LocalDateTime? = LocalDateTime.of(2026, 1, 15, 11, 0),
        image: Image = createImage(),
        failReason: String? = null,
        launchServiceProvider: Agency? = createAgency(),
        rocket: Rocket = createRocket(),
        mission: Mission = createMission(),
        pad: Pad = createPad(),
        updates: List<LaunchUpdate> = emptyList(),
        infoUrls: List<InfoUrl> = emptyList(),
        vidUrls: List<VidUrl> = emptyList(),
        missionPatches: List<MissionPatch> = emptyList()
    ): LaunchUI = LaunchUI(
        id = id,
        missionName = missionName,
        launchDate = launchDate,
        launchTime = launchTime,
        launchDateTime = launchDateTime,
        status = status,
        windowEnd = windowEnd,
        windowStart = windowStart,
        windowStartTime = windowStartTime,
        windowEndTime = windowEndTime,
        windowDuration = windowDuration,
        windowStartDateTime = windowStartDateTime,
        windowEndDateTime = windowEndDateTime,
        image = image,
        failReason = failReason,
        launchServiceProvider = launchServiceProvider,
        rocket = rocket,
        mission = mission,
        pad = pad,
        updates = updates,
        infoUrls = infoUrls,
        vidUrls = vidUrls,
        missionPatches = missionPatches
    )

    fun createLaunchUIWithFailure(): LaunchUI = createLaunchUI(
        status = LaunchStatus.FAILED,
        failReason = "Failed to reach orbit due to engine malfunction"
    )

    fun createLaunchUIWithUpdates(): LaunchUI = createLaunchUI(
        updates = listOf(
            LaunchUpdate(
                id = 1,
                profileImage = null,
                comment = "Launch scrubbed due to weather conditions.",
                infoUrl = "https://spacex.com/updates/1",
                createdBy = "SpaceX",
                createdOn = "Jan 14, 2026 6:00 PM"
            ),
            LaunchUpdate(
                id = 2,
                profileImage = null,
                comment = "All systems are go for launch tomorrow.",
                infoUrl = "https://spacex.com/updates/2",
                createdBy = "Launch Director",
                createdOn = "Jan 15, 2026 8:00 AM"
            )
        )
    )

    fun createLaunchUIWithVideos(): LaunchUI = createLaunchUI(
        vidUrls = listOf(
            VidUrl(
                priority = 1,
                source = "YouTube",
                publisher = "SpaceX",
                title = "Starlink Mission Live",
                description = "Live coverage of Falcon 9 launch",
                featureImage = "https://i.ytimg.com/vi/dQw4w9WgXcQ/maxresdefault.jpg",
                url = "https://youtube.com/watch?v=dQw4w9WgXcQ",
                startTime = null,
                endTime = null,
                live = true
            )
        )
    )

    fun createLaunchUIWithoutOptionalData(): LaunchUI = createLaunchUI(
        windowEnd = null,
        windowStart = null,
        windowStartTime = null,
        windowEndTime = null,
        windowDuration = null,
        windowStartDateTime = null,
        windowEndDateTime = null,
        failReason = null,
        launchServiceProvider = null,
        updates = emptyList(),
        vidUrls = emptyList()
    )

    private fun createImage(
        id: Int = 1,
        name: String = "Falcon 9",
        imageUrl: String = "https://example.com/image.jpg",
        thumbnailUrl: String = "https://example.com/thumb.jpg",
        credit: String = "SpaceX"
    ): Image = Image(
        id = id,
        name = name,
        imageUrl = imageUrl,
        thumbnailUrl = thumbnailUrl,
        credit = credit
    )

    private fun createAgency(
        id: Int = 121,
        name: String = "SpaceX",
        abbrev: String = "SpX",
        type: String = "Commercial",
        description: String = "Space Exploration Technologies Corp."
    ): Agency = Agency(
        id = id,
        url = "https://ll.thespacedevs.com/2.2.0/agencies/$id/",
        name = name,
        abbrev = abbrev,
        type = type,
        featured = true,
        country = listOf(
            Country(
                id = 1,
                name = "United States",
                alpha2Code = "US",
                alpha3Code = "USA",
                nationalityName = "American"
            )
        ),
        description = description,
        administrator = "Elon Musk",
        foundingYear = 2002,
        launchers = "Falcon 9, Falcon Heavy, Starship",
        spacecraft = "Dragon, Crew Dragon, Starship",
        image = createImage(id = 2, name = "SpaceX Logo"),
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
            wikiUrl = "https://en.wikipedia.org/wiki/Falcon_9",
            description = "Falcon 9 is a reusable, two-stage rocket",
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
        name = "Starlink Group 7-12",
        description = "A batch of satellites for the Starlink mega-constellation",
        type = "Communications",
        orbit = Orbit(
            id = 8,
            name = "Low Earth Orbit",
            abbrev = "LEO"
        ),
        agencies = emptyList(),
        infoUrls = emptyList(),
        vidUrls = emptyList()
    )

    private fun createPad(): Pad = Pad(
        id = 87,
        url = "https://ll.thespacedevs.com/2.2.0/pad/87/",
        agencies = emptyList(),
        name = "Space Launch Complex 40",
        image = createImage(id = 3, name = "Pad Image"),
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
        mapUrl = "https://example.com/map",
        mapImage = "https://example.com/map_image.png",
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
            mapImage = "https://example.com/map_image.png",
            longitude = -80.57735736,
            latitude = 28.56194122,
            timezoneName = "America/New_York",
            totalLaunchCount = 778,
            totalLandingCount = 56
        )
    )
}
