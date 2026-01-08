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
    val failedLaunches: String
)

@Immutable
data class MissionUI(
    val name: String,
    val description: String?,
    val type: String?,
    val orbitName: String?,
    val orbitAbbrev: String
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
    val wikiUrl: String?,
    val mapUrl: String?,
    val mapImage: String?
)

@Immutable
data class AgencyUI(
    val name: String,
    val abbrev: String,
    val description: String,
    val type: String,
    val imageUrl: String,
    val foundingYear: String,
    val countryNames: String
)

@Immutable
data class LaunchUpdateUI(
    val comment: String,
    val createdBy: String,
    val createdOn: String,
    val profileImage: String
)

@Immutable
data class VidUrlUI(
    val title: String,
    val description: String,
    val url: String,
    val thumbnailUrl: String,
    val publisher: String,
    val isLive: Boolean
)

@Immutable
data class LauncherStageUI(
    val type: String,
    val reused: String,
    val flightNumber: String,
    val launcherUI: LauncherUI,
    val landing: LandingUI,
    val serialNumber: String,
    val landingSuccess: String,
    val landingLocation: String,
)

@Immutable
data class LauncherUI(
    val id: Int,
    val url: String,
    val flightProven: Boolean,
    val serialNumber: String,
    val details: String,
    val image: String,
    val successfulLandings: String,
    val attemptedLandings: String,
    val flights: String,
    val lastLaunchDate: String,
    val firstLaunchDate: String,
    val status: String
)

@Immutable
data class SpacecraftStageUI(
    val url: String,
    val destination: String,
    val missionEnd: String,
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
    val url: String,
    val name: String,
    val serialNumber: String,
    val status: SpacecraftStatusUI,
    val description: String,
    val spacecraftConfig: SpacecraftConfigUI,
)

@Immutable
data class SpacecraftStatusUI(
    val id: Int,
    val name: String
)

@Immutable
data class SpacecraftConfigUI(
    val name: String,
    val type: String,
    val agencyName: String,
    val inUse: Boolean,
    val capability: String,
    val history: String,
    val details: String,
    val maidenFlight: String,
    val height: String,
    val diameter: String,
    val humanRated: Boolean,
    val crewCapacity: String
)

@Immutable
data class MissionPatchUI(
    val name: String,
    val imageUrl: String,
    val agencyName: String
)

