package com.seancoyle.feature.launch.presentation.launch

import com.seancoyle.feature.launch.domain.model.*

/**
 * Preview data for LaunchUI to be used in Composable previews
 */
internal fun previewData() = LaunchUI(
    id = "preview-id-123",
    missionName = "Starlink Group 7-12",
    launchDate = "Jan 15, 2026, 10:30 AM EST",
    status = LaunchStatus.SUCCESS,
    windowEnd = "Jan 15, 2026, 11:00 AM EST",
    windowStart = "Jan 15, 2026, 10:00 AM EST",
    image = Image(
        id = 1,
        name = "Falcon 9",
        imageUrl = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/launch_images/falcon_9_block__image_20240517131654.png",
        thumbnailUrl = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/launch_images/falcon_9_block__image_20240517131654.png",
        credit = "SpaceX"
    ),
    failReason = null,
    launchServiceProvider = Agency(
        id = 121,
        url = "https://ll.thespacedevs.com/2.2.0/agencies/121/",
        name = "SpaceX",
        abbrev = "SpX",
        type = "Commercial",
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
        description = "Space Exploration Technologies Corp., known as SpaceX, is an American aerospace manufacturer and space transport services company headquartered in Hawthorne, California.",
        administrator = "Elon Musk",
        foundingYear = 2002,
        launchers = null,
        spacecraft = null,
        parent = null,
        image = Image(
            id = 2,
            name = "SpaceX Logo",
            imageUrl = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/agency_images/spacex_image_20190207032501.jpeg",
            thumbnailUrl = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/agency_images/spacex_image_20190207032501.jpeg",
            credit = "SpaceX"
        ),
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
    ),
    rocket = Rocket(
        id = 123,
        configuration = Configuration(
            id = 164,
            url = "https://ll.thespacedevs.com/2.2.0/config/launcher/164/",
            name = "Falcon 9",
            fullName = "Falcon 9 Block 5",
            variant = "Block 5",
            families = null
        )
    ),
    mission = Mission(
        id = 456,
        name = "Starlink Group 7-12",
        description = "A batch of satellites for the Starlink mega-constellation - SpaceX's project for space-based Internet communication system.",
        type = "Communications",
        orbit = Orbit(
            id = 8,
            name = "Low Earth Orbit",
            abbrev = "LEO"
        ),
        agencies = emptyList()
    ),
    pad = Pad(
        id = 87,
        url = "https://ll.thespacedevs.com/2.2.0/pad/87/",
        name = "Space Launch Complex 40",
        location = Location(
            id = 12,
            url = "https://ll.thespacedevs.com/2.2.0/location/12/",
            name = "Cape Canaveral, FL, USA",
            description = "Cape Canaveral Space Force Station",
            timezoneName = "America/New_York",
            totalLaunchCount = null,
            country = Country(
                id = 1,
                name = "United States",
                alpha2Code = "US",
                alpha3Code = "USA",
                nationalityName = "American"
            )
        ),
        latitude = 28.56194122,
        longitude = -80.57735736,
        mapUrl = null,
        totalLaunchCount = null
    ),
    updates = emptyList(),
    infoUrls = emptyList(),
    vidUrls = listOf(
        VidUrl(
            priority = 1,
            source = "YouTube",
            publisher = "SpaceX",
            title = "Starlink Mission",
            description = "Live coverage of Falcon 9 launch",
            featureImage = null,
            url = "https://youtube.com/watch?v=dQw4w9WgXcQ",
            startTime = null,
            endTime = null,
            live = false
        )
    ),
    missionPatches = emptyList()
)

