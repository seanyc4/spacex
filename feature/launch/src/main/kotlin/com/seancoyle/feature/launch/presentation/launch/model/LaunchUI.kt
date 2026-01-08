package com.seancoyle.feature.launch.presentation.launch.model

import androidx.compose.runtime.Immutable

@Immutable
data class LaunchUI(
    val missionName: String,
    val status: LaunchStatus,
    val launchDate: String,
    val launchTime: String,
    val windowStartTime: String,
    val windowEndTime: String,
    val windowDuration: String,
    val launchWindowPosition: Float,
    val imageUrl: String,
    val failReason: String?,
    val launchServiceProvider: AgencyUI?,
    val rocket: RocketUI,
    val mission: MissionUI,
    val pad: PadUI,
    val updates: List<LaunchUpdateUI>,
    val vidUrls: List<VidUrlUI>,
    val missionPatches: List<MissionPatchUI>
)

@Immutable
data class RocketUI(
    val configuration: ConfigurationUI,
    val launcherStages: List<LauncherStageUI>,
    val spacecraftStages: List<SpacecraftStageUI>
)

@Immutable
data class ConfigurationUI(
    val name: String,
    val fullName: String,
    val variant: String,
    val alias: String,
    val description: String,
    val imageUrl: String,
    val totalLaunchCount: String,
    val successfulLaunches: String,
    val failedLaunches: String,
    val length: String,
    val diameter: String,
    val launchMass: String,
    val maidenFlight: String,
    val manufacturer: ManufacturerUI?,
    val families: List<RocketFamilyUI>,
    val wikiUrl: String?,
)

@Immutable
data class ManufacturerUI(
    val name: String,
    val countryName: String,
    val foundingYear: String,
    val wikiUrl: String?,
    val infoUrl: String?
)

@Immutable
data class RocketFamilyUI(
    val name: String,
    val description: String,
    val active: String,
    val maidenFlight: String,
    val totalLaunches: String,
    val successfulLaunches: String,
)

@Immutable
data class MissionUI(
    val name: String,
    val description: String?,
    val type: String?,
    val orbitName: String?,
)

@Immutable
data class PadUI(
    val name: String,
    val locationName: String?,
    val countryName: String?,
    val countryCode: String?,
    val imageUrl: String,
    val description: String,
    val latitude: Double?,
    val longitude: Double?,
    val totalLaunchCount: String,
    val orbitalLaunchAttemptCount: String,
    val locationTotalLaunchCount: String,
    val locationTotalLandingCount: String,
    val mapUrl: String?,
    val mapImage: String?
)

@Immutable
data class AgencyUI(
    val name: String,
    val abbrev: String,
    val description: String,
    val type: String,
)

@Immutable
data class LaunchUpdateUI(
    val comment: String,
    val createdBy: String,
    val createdOn: String,
)

@Immutable
data class VidUrlUI(
    val title: String,
    val url: String,
    val publisher: String,
    val isLive: Boolean,
    val videoId: String?
)

@Immutable
data class LauncherStageUI(
    val type: String,
    val reused: String,
    val flightNumber: String,
    val launcherUI: LauncherUI,
    val landing: LandingUI,
    val serialNumber: String,
)

@Immutable
data class LauncherUI(
    val flightProven: Boolean,
    val serialNumber: String,
    val image: String,
    val successfulLandings: String,
    val attemptedLandings: String,
    val status: String
)

@Immutable
data class SpacecraftStageUI(
    val destination: String,
    val spacecraft: SpacecraftUI,
    val landing: LandingUI
)

@Immutable
data class LandingUI(
    val attempt: Boolean,
    val success: Boolean,
    val description: String,
    val location: String,
    val type: String
)


@Immutable
data class SpacecraftUI(
    val name: String,
    val serialNumber: String,
    val spacecraftStatus: SpacecraftStatusUI,
    val spacecraftConfig: SpacecraftConfigUI,
)

@Immutable
data class SpacecraftStatusUI(
    val name: String
)

@Immutable
data class SpacecraftConfigUI(
    val type: String,
    val agencyName: String,
    val inUse: Boolean,
    val capability: String,
    val history: String,
    val details: String,
    val maidenFlight: String,
    val humanRated: Boolean,
    val crewCapacity: String
)

@Immutable
data class MissionPatchUI(
    val name: String,
    val imageUrl: String,
    val agencyName: String
)
