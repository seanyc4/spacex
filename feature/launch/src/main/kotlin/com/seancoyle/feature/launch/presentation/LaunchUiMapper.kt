package com.seancoyle.feature.launch.presentation

import com.seancoyle.core.common.dataformatter.DateFormatConstants
import com.seancoyle.core.common.dataformatter.DateTransformer
import com.seancoyle.core.ui.components.videoplayer.extractYouTubeVideoId
import com.seancoyle.feature.launch.domain.model.Agency
import com.seancoyle.feature.launch.domain.model.Configuration
import com.seancoyle.feature.launch.domain.model.Family
import com.seancoyle.feature.launch.domain.model.Launch
import com.seancoyle.feature.launch.domain.model.LaunchSummary
import com.seancoyle.feature.launch.domain.model.LaunchUpdate
import com.seancoyle.feature.launch.domain.model.Launcher
import com.seancoyle.feature.launch.domain.model.LauncherStage
import com.seancoyle.feature.launch.domain.model.Mission
import com.seancoyle.feature.launch.domain.model.MissionPatch
import com.seancoyle.feature.launch.domain.model.Pad
import com.seancoyle.feature.launch.domain.model.Rocket
import com.seancoyle.feature.launch.domain.model.SpacecraftStage
import com.seancoyle.feature.launch.domain.model.Status
import com.seancoyle.feature.launch.domain.model.VidUrl
import com.seancoyle.feature.launch.presentation.LaunchesConstants.DEFAULT_LAUNCH_IMAGE
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
import com.seancoyle.feature.launch.presentation.launches.model.LaunchesUi
import java.time.Duration
import java.time.LocalDateTime
import javax.inject.Inject

private const val NA = "N/A"

class LaunchUiMapper @Inject constructor(
    private val dateFormatter: DateTransformer
) {

    fun mapToLaunchUi(launch: Launch): LaunchUI {
        with(launch) {
            val launchDateTime = dateFormatter.formatDate(net)
            val windowStartDateTime = windowStart?.let { dateFormatter.formatDate(it) }
            val windowEndDateTime = windowEnd?.let { dateFormatter.formatDate(it) }
            val duration = calculateDuration(windowStartDateTime, windowEndDateTime)
            val windowPosition = calculateLaunchWindowPosition(
                windowStartDateTime,
                windowEndDateTime,
                launchDateTime
            )

            return LaunchUI(
                missionName = missionName.substringBefore("|").trim(),
                status = status.toDomain(),
                launchDate = formatDate(launchDateTime),
                launchTime = formatDate(launchDateTime, DateFormatConstants.HH_MM),
                windowStartTime = windowStartDateTime?.let { formatDate(it, DateFormatConstants.HH_MM) } ?: "00:00",
                windowEndTime = windowEndDateTime?.let { formatDate(it, DateFormatConstants.HH_MM) } ?: "00:00",
                windowDuration = duration ?: "00:00",
                launchWindowPosition = windowPosition,
                imageUrl = image.imageUrl,
                failReason = failReason,
                launchServiceProvider = launchServiceProvider?.toUI(),
                rocket = rocket.toUI(),
                mission = mission.toUI(),
                pad = pad.toUI(),
                updates = updates.map { it.toUI() },
                vidUrls = vidUrls.map { it.toUI() }.filter { it.videoId != null },
                missionPatches = missionPatches.map { it.toUI() }
            )
        }
    }

    private fun Rocket.toUI() = RocketUI(
        configuration = configuration.toUI(),
        launcherStages = launcherStage.map { it.toUI() },
        spacecraftStages = spacecraftStage.map { it.toUI() }
    )

    private fun Configuration.toUI() = ConfigurationUI(
        name = name ?: NA,
        fullName = fullName ?: name ?: NA,
        variant = variant ?: NA,
        alias = alias ?: NA,
        description = description ?: NA,
        imageUrl = image?.imageUrl ?: NA,
        totalLaunchCount = totalLaunchCount?.toString() ?: NA,
        successfulLaunches = successfulLaunches?.toString() ?: NA,
        failedLaunches = failedLaunches?.toString() ?: NA,
        length = length?.let { if (it > 0) String.format("%.1f m", it) else NA } ?: NA,
        diameter = diameter?.let { if (it > 0) String.format("%.1f m", it) else NA } ?: NA,
        launchMass = launchMass?.let { if (it > 0) String.format("%.0f kg", it) else NA } ?: NA,
        maidenFlight = maidenFlight ?: NA,
        manufacturer = manufacturer?.let { agency ->
            ManufacturerUI(
                name = agency.name,
                countryName = agency.country.firstOrNull()?.name ?: NA,
                foundingYear = agency.foundingYear?.toString() ?: NA,
                wikiUrl = agency.wikiUrl,
                infoUrl = agency.infoUrl
            )
        },
        families = families.map { it.toUI() },
        wikiUrl = wikiUrl
    )

    private fun Family.toUI() = RocketFamilyUI(
        name = name ?: NA,
        description = description ?: NA,
        active = active?.let { if (it) "Active" else "Inactive" } ?: NA,
        maidenFlight = maidenFlight ?: NA,
        totalLaunches = totalLaunchCount?.toString() ?: NA,
        successfulLaunches = successfulLaunches?.toString() ?: NA,
    )

    private fun Mission.toUI() = MissionUI(
        name = name ?: NA,
        description = description,
        type = type,
        orbitName = orbit?.name,
    )

    private fun Pad.toUI() = PadUI(
        name = name ?: NA,
        locationName = location?.name.orEmpty(),
        countryName = country?.name ?: location?.country?.name.orEmpty(),
        countryCode = country?.alpha2Code ?: location?.country?.alpha2Code.orEmpty(),
        imageUrl = image.imageUrl,
        description = description.orEmpty(),
        latitude = latitude,
        longitude = longitude,
        totalLaunchCount = totalLaunchCount?.toString() ?: NA,
        orbitalLaunchAttemptCount = orbitalLaunchAttemptCount?.toString() ?: NA,
        locationTotalLaunchCount = location?.totalLaunchCount?.toString() ?: NA,
        locationTotalLandingCount = location?.totalLandingCount?.toString() ?: NA,
        mapUrl = mapUrl,
        mapImage = mapImage ?: location?.mapImage
    )

    private fun Agency.toUI() = AgencyUI(
        name = name,
        abbrev = abbrev,
        description = description,
        type = type
    )

    private fun LaunchUpdate.toUI() = LaunchUpdateUI(
        comment = comment ?: NA,
        createdBy = createdBy ?: NA,
        createdOn = createdOn ?: NA,
    )

    private fun VidUrl.toUI() = VidUrlUI(
        title = title.orEmpty(),
        url = url!!, // we filter out null urls in the domain layer
        publisher = publisher.orEmpty(),
        isLive = live ?: false,
        videoId = extractYouTubeVideoId(url)
    )

    private fun LauncherStage.toUI() = LauncherStageUI(
        type = type ?: NA,
        reused = reused?.let { if (it) "Reused" else "New" } ?: NA,
        flightNumber = launcherFlightNumber?.toString() ?: NA,
        serialNumber = launcher?.serialNumber ?: NA,
        launcherUI = launcher?.toUI() ?: LauncherUI(
            flightProven = false,
            serialNumber = NA,
            image = DEFAULT_LAUNCH_IMAGE,
            successfulLandings = NA,
            attemptedLandings = NA,
            status = NA
        ),
        landing = LandingUI(
            attempt = landing?.attempt ?: false,
            success = landing?.success ?: false,
            description = landing?.description ?: NA,
            location = landing?.location?.name ?: NA,
            type = landing?.type?.name ?: NA
        )

    )

    private fun Launcher.toUI() = LauncherUI(
        flightProven = flightProven ?: false,
        serialNumber = serialNumber ?: NA,
        image = image.imageUrl,
        successfulLandings = successfulLandings?.toString() ?: NA,
        attemptedLandings = attemptedLandings?.toString() ?: NA,
        status = status?.name ?: NA
    )

    private fun SpacecraftStage.toUI() = SpacecraftStageUI(
        destination = destination ?: NA,
        landing = LandingUI(
            attempt = landing?.attempt ?: false,
            success = landing?.success ?: false,
            description = landing?.description ?: NA,
            location = landing?.location?.name ?: NA,
            type = landing?.type?.name ?: NA
        ),
        spacecraft = SpacecraftUI(
            name = this.spacecraft?.name ?: NA,
            serialNumber = this.spacecraft?.serialNumber ?: NA,
            spacecraftStatus = SpacecraftStatusUI(
                name = this.spacecraft?.status?.name ?: NA,
            ),
            spacecraftConfig = SpacecraftConfigUI(
                type = this.spacecraft?.spacecraftConfig?.type?.name ?: NA,
                capability = this.spacecraft?.spacecraftConfig?.capability ?: NA,
                details = this.spacecraft?.spacecraftConfig?.details ?: NA,
                humanRated = this.spacecraft?.spacecraftConfig?.humanRated ?: false,
                crewCapacity = this.spacecraft?.spacecraftConfig?.crewCapacity?.toString() ?: NA,
            )
        )
    )

    private fun calculateDuration(startTime: LocalDateTime?, endTime: LocalDateTime?): String? {
        if (startTime == null || endTime == null) return null

        // If times are the same, it's an instantaneous launch
        if (startTime == endTime) return "Instantaneous"

        val duration = Duration.between(startTime, endTime)
        val hours = duration.toHours()
        val minutes = duration.toMinutes() % 60
        val seconds = duration.seconds % 60

        return buildString {
            if (hours > 0) {
                append("${hours}h")
                if (minutes > 0 || seconds > 0) append(" ")
            }
            if (minutes > 0) {
                append("${minutes}m")
                if (seconds > 0) append(" ")
            }
            if (seconds > 0 || (hours == 0L && minutes == 0L)) {
                append("${seconds}s")
            }
        }.trim()
    }

    private fun calculateLaunchWindowPosition(
        windowStart: LocalDateTime?,
        windowEnd: LocalDateTime?,
        launchTime: LocalDateTime?
    ): Float {
        if (windowStart == null || windowEnd == null || launchTime == null) return 0f

        // If launch time is before window, show at start
        if (launchTime.isBefore(windowStart)) return 0f

        // If launch time is after window, show at end
        if (launchTime.isAfter(windowEnd)) return 1f

        // Calculate position of launch time within the window
        val totalDuration = Duration.between(windowStart, windowEnd).toMillis().toFloat()
        val launchOffset = Duration.between(windowStart, launchTime).toMillis().toFloat()

        // Avoid division by zero & show indicator when the launch is exactly at the start
        if (totalDuration == 0f) return 0.1f
        if (launchOffset == 0f) return 0.1f

        // Return position between 0 and 1
        return (launchOffset / totalDuration).coerceIn(0f, 1f)
    }

    private fun MissionPatch.toUI() = MissionPatchUI(
        name = name ?: NA,
        imageUrl = imageUrl ?: "",
        agencyName = agency?.name ?: NA
    )

    fun mapToLaunchesUi(launch: LaunchSummary): LaunchesUi {
        with(launch) {
            val locateDateTime = dateFormatter.formatDate(net)
            return LaunchesUi(
                id = id,
                launchDate = formatDate(locateDateTime),
                missionName = missionName.substringBefore("|").trim(),
                status = status.toDomain(),
                imageUrl = imageUrl
            )
        }
    }

    private fun formatDate(
        date: LocalDateTime?,
        format: String = DateFormatConstants.DD_MMMM_YYYY
    ): String {
        return dateFormatter.formatDateTimeToString(date, format)
    }

    fun Status.toDomain(): LaunchStatus =
        when {
            this.abbrev.contains("Success", ignoreCase = true) -> LaunchStatus.SUCCESS
            this.abbrev.contains("Go", ignoreCase = true) -> LaunchStatus.GO
            this.abbrev.contains("Fail", ignoreCase = true) -> LaunchStatus.FAILED
            this.abbrev.contains("To Be Confirmed", ignoreCase = true) -> LaunchStatus.TBC
            this.abbrev.contains("To Be Determined", ignoreCase = true) -> LaunchStatus.TBD
            else -> LaunchStatus.TBD
        }
}
