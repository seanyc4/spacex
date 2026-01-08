package com.seancoyle.feature.launch.presentation.launch.components

import com.seancoyle.feature.launch.presentation.launch.model.AgencyUI
import com.seancoyle.feature.launch.presentation.launch.model.ConfigurationUI
import com.seancoyle.feature.launch.presentation.launch.model.LandingUI
import com.seancoyle.feature.launch.presentation.launch.model.LaunchStatus
import com.seancoyle.feature.launch.presentation.launch.model.LaunchUI
import com.seancoyle.feature.launch.presentation.launch.model.LaunchUpdateUI
import com.seancoyle.feature.launch.presentation.launch.model.LauncherStageUI
import com.seancoyle.feature.launch.presentation.launch.model.LauncherUI
import com.seancoyle.feature.launch.presentation.launch.model.ManufacturerUI
import com.seancoyle.feature.launch.presentation.launch.model.MissionPatchUI
import com.seancoyle.feature.launch.presentation.launch.model.MissionUI
import com.seancoyle.feature.launch.presentation.launch.model.PadUI
import com.seancoyle.feature.launch.presentation.launch.model.RocketFamilyUI
import com.seancoyle.feature.launch.presentation.launch.model.RocketUI
import com.seancoyle.feature.launch.presentation.launch.model.SpacecraftConfigUI
import com.seancoyle.feature.launch.presentation.launch.model.SpacecraftStageUI
import com.seancoyle.feature.launch.presentation.launch.model.SpacecraftStatusUI
import com.seancoyle.feature.launch.presentation.launch.model.SpacecraftUI
import com.seancoyle.feature.launch.presentation.launch.model.VidUrlUI

/**
 * Preview data for LaunchUI to be used in Composable previews
 */
internal fun previewData() = LaunchUI(
    missionName = "Starlink Group 7-12",
    launchDate = "26 November 2026",
    launchTime = "10:30",
    status = LaunchStatus.SUCCESS,
    windowStartTime = "10:00",
    windowEndTime = "11:00",
    windowDuration = "1h",
    launchWindowPosition = 0.5f,
    imageUrl = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/launch_images/falcon_9_block__image_20240517131654.png",
    failReason = "The flight experienced a minor anomaly during stage separation.",
    launchServiceProvider = AgencyUI(
        name = "SpaceX",
        abbrev = "SpX",
        description = "Space Exploration Technologies Corp., known as SpaceX, is an American aerospace manufacturer and space transport services company headquartered in Hawthorne, California.",
        type = "Commercial",
        imageUrl = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/agency_images/spacex_image_20190207032501.jpeg",
        foundingYear = "2002",
        countryNames = "United States"
    ),
    rocket = RocketUI(
        configuration = ConfigurationUI(
            name = "Falcon 9",
            fullName = "Falcon 9 Block 5",
            variant = "Block 5",
            alias = "F9",
            description = "Falcon 9 is a reusable, two-stage rocket designed and manufactured by SpaceX",
            imageUrl = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/launch_images/falcon_9_block__image_20240517131654.png",
            totalLaunchCount = "300",
            successfulLaunches = "290",
            failedLaunches = "10",
            length = "56.3 m",
            diameter = "3.35 m",
            launchMass = "456 kg",
            maidenFlight = "2007-05-13",
            manufacturer = ManufacturerUI(
                name = "SpaceX",
                countryName = "United States",
                foundingYear = "2002",
                wikiUrl = "https://en.wikipedia.org/wiki/SpaceX",
                infoUrl = "https://www.spacex.com"
            ),
            families = listOf(
                RocketFamilyUI(
                    name = "Falcon",
                    description = "Falcon rocket family developed by SpaceX",
                    active = "Active",
                    maidenFlight = "2010-06-04",
                    totalLaunches = "300",
                    successfulLaunches = "290",
                    failedLaunches = "10"
                )
            ),
            wikiUrl = "https://en.wikipedia.org/wiki/Falcon_9"
        ),
        launcherStages = listOf(
            LauncherStageUI(
                type = "Core",
                reused = "Reused",
                flightNumber = "6",
                serialNumber = "B1062",
                landingSuccess = "Success",
                landingLocation = "JRTI",
                launcherUI = LauncherUI(
                    id = 187,
                    url = "https://lldev.thespacedevs.com/2.3.0/launchers/187/",
                    flightProven = true,
                    serialNumber = "B1062",
                    details = "Falcon 9 Block 5 booster",
                    image = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/launcher_images/falcon25209_image_20230807133459.jpeg",
                    successfulLandings = "6",
                    attemptedLandings = "6",
                    flights = "6",
                    lastLaunchDate = "2025-12-20",
                    firstLaunchDate = "2024-10-23",
                    status = "Active"
                ),
                landing = LandingUI(
                    attempt = true,
                    success = true,
                    description = "The Falcon 9 booster B1062 has landed on the droneship.",
                    location = "JRTI",
                    type = "ASDS"
                )
            )
        ),
        spacecraftStages = listOf(
            SpacecraftStageUI(
                url = "https://ll.thespacedevs.com/2.3.0/spacecraft_stage/1/",
                destination = "International Space Station",
                missionEnd = "2026-01-16",
                spacecraft = SpacecraftUI(
                    url = "https://ll.thespacedevs.com/2.3.0/spacecraft/1/",
                    name = "Dragon C208",
                    serialNumber = "C208",
                    status = SpacecraftStatusUI(id = 1, name = "Active"),
                    description = "Dragon spacecraft for cargo missions",
                    spacecraftConfig = SpacecraftConfigUI(
                        name = "Dragon 2",
                        type = "Cargo",
                        agencyName = "SpaceX",
                        inUse = true,
                        capability = "Cargo and Crew Transport",
                        history = "Dragon 2 is a class of reusable spacecraft developed and manufactured by SpaceX.",
                        details = "Dragon 2 can carry up to 7 passengers to and from Earth orbit, and beyond.",
                        maidenFlight = "2019-03-02",
                        height = "8.1",
                        diameter = "4.0",
                        humanRated = true,
                        crewCapacity = "7"
                    )
                ),
                landing = LandingUI(
                    attempt = false,
                    success = false,
                    description = "N/A",
                    location = "N/A",
                    type = "N/A"
                )
            )
        )
    ),
    mission = MissionUI(
        name = "Starlink Group 7-12",
        description = "A batch of satellites for the Starlink mega-constellation - SpaceX's project for space-based Internet communication system.",
        type = "Communications",
        orbitName = "Low Earth Orbit",
        orbitAbbrev = "LEO"
    ),
    pad = PadUI(
        name = "Space Launch Complex 40",
        locationName = "Cape Canaveral, FL, USA",
        countryName = "United States",
        countryCode = "US",
        imageUrl = "https://example.com/pad_image.png",
        description = "Cape Canaveral SLC-40",
        latitude = 28.56194122,
        longitude = -80.57735736,
        totalLaunchCount = "957",
        orbitalLaunchAttemptCount = "457",
        locationTotalLaunchCount = "778",
        locationTotalLandingCount = "56",
        wikiUrl = null,
        mapUrl = "https://example.com/pad_image.png",
        mapImage = "https://example.com/pad_image.png"
    ),
    updates = listOf(
        LaunchUpdateUI(
            comment = "Launch scrubbed due to weather conditions. New T-0 is in 24 hours.",
            createdBy = "SpaceX",
            createdOn = "Jan 14, 2026 6:00 PM",
            profileImage = ""
        ),
        LaunchUpdateUI(
            comment = "All systems are go for launch tomorrow.",
            createdBy = "Launch Director",
            createdOn = "Jan 15, 2026 8:00 AM",
            profileImage = ""
        )
    ),
    vidUrls = listOf(
        VidUrlUI(
            title = "Starlink Mission",
            description = "Live coverage of Falcon 9 launch",
            url = "https://youtube.com/watch?v=dQw4w9WgXcQ",
            thumbnailUrl = "https://i.ytimg.com/vi/dQw4w9WgXcQ/maxresdefault.jpg",
            publisher = "SpaceX",
            isLive = false
        )
    ),
    missionPatches = listOf(
        MissionPatchUI(
            name = "Starlink Mission Patch",
            imageUrl = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/launch_images/falcon_9_block__image_20240517131654.png",
            agencyName = "SpaceX"
        ),
        MissionPatchUI(
            name = "Falcon 9 Mission Patch",
            imageUrl = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/launch_images/falcon_9_block__image_20240517131654.png",
            agencyName = "SpaceX"
        )
    )
)

