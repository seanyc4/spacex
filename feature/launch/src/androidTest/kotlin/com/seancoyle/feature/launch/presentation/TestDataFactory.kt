package com.seancoyle.feature.launch.presentation

import com.seancoyle.feature.launch.presentation.launch.model.AgencyUI
import com.seancoyle.feature.launch.presentation.launch.model.ConfigurationUI
import com.seancoyle.feature.launch.presentation.launch.model.LaunchStatus
import com.seancoyle.feature.launch.presentation.launch.model.LaunchUI
import com.seancoyle.feature.launch.presentation.launch.model.LaunchUpdateUI
import com.seancoyle.feature.launch.presentation.launch.model.MissionPatchUI
import com.seancoyle.feature.launch.presentation.launch.model.MissionUI
import com.seancoyle.feature.launch.presentation.launch.model.PadUI
import com.seancoyle.feature.launch.presentation.launch.model.RocketUI
import com.seancoyle.feature.launch.presentation.launch.model.VidUrlUI
import com.seancoyle.feature.launch.presentation.launches.model.LaunchesUi

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
        missionName: String = "Starlink Group 7-12",
        launchDate: String = "26 November 2026",
        launchTime: String = "10:30",
        status: LaunchStatus = LaunchStatus.SUCCESS,
        windowStartTime: String = "10:00",
        windowEndTime: String = "11:00",
        windowDuration: String = "1h",
        imageUrl: String = "https://example.com/image.jpg",
        failReason: String? = null,
        launchServiceProvider: AgencyUI? = createAgency(),
        rocket: RocketUI = createRocket(),
        mission: MissionUI = createMission(),
        pad: PadUI = createPad(),
        updates: List<LaunchUpdateUI> = emptyList(),
        vidUrls: List<VidUrlUI> = emptyList(),
        missionPatches: List<MissionPatchUI> = emptyList()
    ): LaunchUI = LaunchUI(
        missionName = missionName,
        launchDate = launchDate,
        launchTime = launchTime,
        status = status,
        windowStartTime = windowStartTime,
        windowEndTime = windowEndTime,
        windowDuration = windowDuration,
        imageUrl = imageUrl,
        failReason = failReason,
        launchServiceProvider = launchServiceProvider,
        rocket = rocket,
        mission = mission,
        pad = pad,
        updates = updates,
        vidUrls = vidUrls,
        missionPatches = missionPatches,
        launchWindowPosition = 0.5f
    )

    fun createLaunchUIWithFailure(): LaunchUI = createLaunchUI(
        status = LaunchStatus.FAILED,
        failReason = "Failed to reach orbit due to engine malfunction"
    )

    fun createLaunchUIWithUpdates(): LaunchUI = createLaunchUI(
        updates = listOf(
            LaunchUpdateUI(
                comment = "Launch scrubbed due to weather conditions.",
                createdBy = "SpaceX",
                createdOn = "Jan 14, 2026 6:00 PM"
            ),
            LaunchUpdateUI(
                comment = "All systems are go for launch tomorrow.",
                createdBy = "Launch Director",
                createdOn = "Jan 15, 2026 8:00 AM"
            )
        )
    )

    fun createLaunchUIWithVideos(): LaunchUI = createLaunchUI(
        vidUrls = listOf(
            VidUrlUI(
                title = "Starlink Mission Live",
                url = "https://youtube.com/watch?v=dQw4w9WgXcQ",
                isLive = true,
                videoId = "dQw4w9WgXcQ",
                publisher = "SpaceX"
            )
        )
    )

    fun createLaunchUIWithoutOptionalData(): LaunchUI = createLaunchUI(
        failReason = null,
        launchServiceProvider = null,
        updates = emptyList(),
        vidUrls = emptyList()
    )

    private fun createAgency(
        name: String = "SpaceX",
        abbrev: String = "SpX",
        type: String = "Commercial",
        description: String = "Space Exploration Technologies Corp."
    ): AgencyUI = AgencyUI(
        name = name,
        abbrev = abbrev,
        type = type,
        description = description
    )

    private fun createRocket(): RocketUI = RocketUI(
        configuration = ConfigurationUI(
            name = "Falcon 9",
            imageUrl = "https://example.com/image.jpg",
            fullName = "Falcon 9 Block 5",
            variant = "Block 5",
            families = emptyList(),
            manufacturer = null,
            wikiUrl = "https://en.wikipedia.org/wiki/Falcon_9",
            description = "Falcon 9 is a reusable, two-stage rocket",
            alias = "F9",
            totalLaunchCount = "300",
            successfulLaunches = "290",
            failedLaunches = "10",
            length = "56.3",
            diameter = "3.35",
            maidenFlight = "2007-05-13",
            launchMass = "456.0"
        ),
        launcherStages = emptyList(),
        spacecraftStages = emptyList()
    )

    private fun createMission(): MissionUI = MissionUI(
        name = "Starlink Group 7-12",
        description = "A batch of satellites for the Starlink mega-constellation",
        type = "Communications",
        orbitName = "Low Earth Orbit"
    )

    private fun createPad(): PadUI = PadUI(
        name = "Space Launch Complex 40",
        locationName = "Cape Canaveral, FL, USA",
        countryName = "United States",
        countryCode = "USA",
        imageUrl = "https://example.com/pad_image.png",
        description = "Cape Canaveral SLC-40",
        latitude = "28.56194122",
        longitude = "-80.57735736",
        totalLaunchCount = "100",
        orbitalLaunchAttemptCount = "95",
        locationTotalLaunchCount = "1500",
        locationTotalLandingCount = "300",
        mapUrl = "https://example.com/map",
        mapImage = "https://example.com/map_image.png"
    )
}
