package com.seancoyle.feature.launch.presentation.launch.components

import com.seancoyle.feature.launch.domain.model.*
import com.seancoyle.feature.launch.presentation.launch.model.LaunchStatus
import com.seancoyle.feature.launch.presentation.launch.model.LaunchUI

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
    failReason = "Failed to reach orbit",
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
            url = "https://ll.thespacedevs.com/2.3.0/config/launcher/164/",
            name = "Falcon 9",
            fullName = "Falcon 9 Block 5",
            variant = "Block 5",
            families = listOf(Family(id = 1, name = "Falcon"))
        ),
        launcherStage = listOf(
            LauncherStage(
                id = 826,
                type = "Core",
                reused = true,
                launcherFlightNumber = 6,
                launcher = Launcher(
                    id = 187,
                    url = "https://lldev.thespacedevs.com/2.3.0/launchers/187/",
                    flightProven = true,
                    serialNumber = "B1062",
                    status = Status(id = 3, name = "active", abbrev = "ACT", description = "Active booster"),
                    details = "Falcon 9 Block 5 booster",
                    image = Image(
                        id = 1,
                        name = "Falcon 9",
                        imageUrl = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/launcher_images/falcon25209_image_20230807133459.jpeg",
                        thumbnailUrl = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/launcher_images/falcon25209_image_20230807133459.jpeg",
                        credit = "SpaceX"
                    ),
                    successfulLandings = 6,
                    attemptedLandings = 6,
                    flights = 6,
                    lastLaunchDate = "2025-12-20T14:15:00Z",
                    firstLaunchDate = "2024-10-23T15:27:00Z"
                ),
                landing = Landing(
                    id = 1719,
                    attempt = true,
                    success = true,
                    description = "The Falcon 9 booster B1062 has landed on the droneship.",
                    location = LandingLocation(
                        id = 11,
                        name = "ASDS - Just Read the Instructions",
                        abbrev = "JRTI",
                        description = "Autonomous spaceport drone ship"
                    ),
                    type = LandingType(
                        id = 1,
                        name = "Autonomous Spaceport Drone Ship",
                        abbrev = "ASDS",
                        description = "Landing on a floating platform"
                    )
                ),
                previousFlightDate = "2025-09-18T13:00:00Z",
                turnAroundTime = "P93DT1H15M",
                previousFlight = PreviousFlight(
                    id = "92ec4610-4576-4077-b538-65272a5d6491",
                    name = "Falcon 9 Block 5 | Starlink Group 15-11"
                )
            )
        ),
        spacecraftStage = listOf(
            SpacecraftStage(
                id = 1,
                url = "https://ll.thespacedevs.com/2.3.0/spacecraft_stage/1/",
                destination = "International Space Station",
                missionEnd = "2026-01-16T12:00:00Z",
                spacecraft = Spacecraft(
                    id = 1,
                    url = "https://ll.thespacedevs.com/2.3.0/spacecraft/1/",
                    name = "Dragon C208",
                    serialNumber = "C208",
                    status = SpacecraftStatus(id = 1, name = "Active"),
                    description = "Dragon spacecraft for cargo missions",
                    spacecraftConfig = SpacecraftConfig(
                        id = 1,
                        url = "https://ll.thespacedevs.com/2.3.0/config/spacecraft/1/",
                        name = "Dragon 2",
                        type = SpacecraftType(id = 1, name = "Cargo"),
                        agency = null,
                        inUse = true,
                        capability = "Cargo and Crew Transport",
                        history = "Dragon 2 is a class of reusable spacecraft developed and manufactured by SpaceX.",
                        details = "Dragon 2 can carry up to 7 passengers to and from Earth orbit, and beyond.",
                        maidenFlight = "2019-03-02T07:49:00Z",
                        height = 8.1,
                        diameter = 4.0,
                        humanRated = true,
                        crewCapacity = 7
                    )
                ),
                landing = null
            )
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
        agencies = emptyList(),
        infoUrls = emptyList(),
        vidUrls = emptyList()
    ),
    pad = Pad(
        id = 87,
        url = "https://ll.thespacedevs.com/2.2.0/pad/87/",
        agencies = emptyList(),
        name = "Space Launch Complex 40",
        image = Image(
            id = 3,
            name = "Pad Image",
            imageUrl = "https://example.com/pad_image.png",
            thumbnailUrl = "https://example.com/pad_image_thumb.png",
            credit = "SpaceX"
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
        totalLaunchCount = null,
        orbitalLaunchAttemptCount = null,
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
            totalLaunchCount = null,
            totalLandingCount = null
        )
    ),
    updates = listOf(
        LaunchUpdate(
            id = 1,
            profileImage = null,
            comment = "Launch scrubbed due to weather conditions. New T-0 is in 24 hours.",
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
    ),
    infoUrls = listOf(
        InfoUrl(
            priority = 1,
            title = "SpaceX Official Site",
            description = "Learn more about SpaceX launches.",
            url = "https://www.spacex.com/launches/",
            source = "SpaceX",
            featureImage = "https://www.spacex.com/static/images/share.jpg"
        ),
        InfoUrl(
            priority = 2,
            title = "Wikipedia",
            description = "Falcon 9 Wikipedia page.",
            url = "https://en.wikipedia.org/wiki/Falcon_9",
            source = "Wikipedia",
            featureImage = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/59/F9_maiden_launch_cropped.jpg/800px-F9_maiden_launch_cropped.jpg"
        )
    ),
    vidUrls = listOf(
        VidUrl(
            priority = 1,
            source = "YouTube",
            publisher = "SpaceX",
            title = "Starlink Mission",
            description = "Live coverage of Falcon 9 launch",
            featureImage = "https://i.ytimg.com/vi/dQw4w9WgXcQ/maxresdefault.jpg",
            url = "https://youtube.com/watch?v=dQw4w9WgXcQ",
            startTime = "2026-01-15T10:00:00Z",
            endTime = "2026-01-15T12:00:00Z",
            live = false
        )
    ),
    missionPatches = listOf(
        MissionPatch(
            id = 1,
            name = "Starlink Mission Patch",
            priority = 1,
            imageUrl = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/launch_images/falcon_9_block__image_20240517131654.png",
            agency = Agency(
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
                description = "Space Exploration Technologies Corp.",
                administrator = "Elon Musk",
                foundingYear = 2002,
                launchers = "Falcon 9, Falcon Heavy, Starship",
                spacecraft = "Dragon, Crew Dragon, Starship",
                parent = "SpaceX Corporation",
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
            )
        ),
        MissionPatch(
            id = 2,
            name = "Falcon 9 Mission Patch",
            priority = 2,
            imageUrl = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/launch_images/falcon_9_block__image_20240517131654.png",
            agency = Agency(
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
                description = "Space Exploration Technologies Corp.",
                administrator = "Elon Musk",
                foundingYear = 2002,
                launchers = "Falcon 9, Falcon Heavy, Starship",
                spacecraft = "Dragon, Crew Dragon, Starship",
                parent = "SpaceX Corporation",
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
            )
        )
    )
)
