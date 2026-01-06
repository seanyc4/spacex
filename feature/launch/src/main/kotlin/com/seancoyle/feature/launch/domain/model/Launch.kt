package com.seancoyle.feature.launch.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Launch(
    val id: String,
    val url: String?,
    val missionName: String,
    val lastUpdated: String?,
    val net: String,
    val netPrecision: NetPrecision?,
    val status: Status,
    val windowEnd: String?,
    val windowStart: String?,
    val image: Image,
    val infographic: String?,
    val probability: Int?,
    val weatherConcerns: String?,
    val failReason: String?,
    val launchServiceProvider: Agency?,
    val rocket: Rocket,
    val mission: Mission,
    val pad: Pad,
    val webcastLive: Boolean?,
    val program: List<Program>,
    val orbitalLaunchAttemptCount: Int?,
    val locationLaunchAttemptCount: Int?,
    val padLaunchAttemptCount: Int?,
    val agencyLaunchAttemptCount: Int?,
    val orbitalLaunchAttemptCountYear: Int?,
    val locationLaunchAttemptCountYear: Int?,
    val padLaunchAttemptCountYear: Int?,
    val agencyLaunchAttemptCountYear: Int?,
    val updates: List<LaunchUpdate>,
    val infoUrls: List<InfoUrl>,
    val vidUrls: List<VidUrl>,
    val padTurnaround: String?,
    val missionPatches: List<MissionPatch>,
)

@Immutable
data class NetPrecision(
    val id: Int?,
    val name: String?,
    val abbrev: String?,
    val description: String?
)

@Immutable
data class Image(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val thumbnailUrl: String,
    val credit: String
)

@Immutable
data class Status(
    val id: Int?,
    val name: String,
    val abbrev: String,
    val description: String?,
)

@Immutable
data class Agency(
    val id: Int?,
    val url: String?,
    val name: String,
    val abbrev: String,
    val type: String,
    val featured: Boolean?,
    val country: List<Country>,
    val description: String,
    val administrator: String?,
    val foundingYear: Int?,
    val launchers: String?,
    val spacecraft: String?,
    val image: Image,
    val totalLaunchCount: Int?,
    val consecutiveSuccessfulLaunches: Int?,
    val successfulLaunches: Int?,
    val failedLaunches: Int?,
    val pendingLaunches: Int?,
    val consecutiveSuccessfulLandings: Int?,
    val successfulLandings: Int?,
    val failedLandings: Int?,
    val attemptedLandings: Int?,
    val successfulLandingsSpacecraft: Int?,
    val failedLandingsSpacecraft: Int?,
    val attemptedLandingsSpacecraft: Int?,
    val successfulLandingsPayload: Int?,
    val failedLandingsPayload: Int?,
    val attemptedLandingsPayload: Int?,
    val infoUrl: String?,
    val wikiUrl: String?,
)

@Immutable
data class Country(
    val id: Int?,
    val name: String?,
    val alpha2Code: String?,
    val alpha3Code: String?,
    val nationalityName: String?
)

@Immutable
data class Rocket(
    val id: Int?,
    val configuration: Configuration,
    val launcherStage: List<LauncherStage>,
    val spacecraftStage: List<SpacecraftStage>
)

@Immutable
data class Configuration(
    val id: Int?,
    val url: String?,
    val name: String?,
    val fullName: String?,
    val variant: String?,
    val families: List<Family>,
    val manufacturer: Agency?,
    val image: Image?,
    val wikiUrl: String?,
    val description: String?,
    val alias: String?,
    val totalLaunchCount: Int?,
    val successfulLaunches: Int?,
    val failedLaunches: Int?,
    val length: Double?,
    val diameter: Double?,
    val launchMass: Double?,
    val maidenFlight: String?,
)

@Immutable
data class Family(
    val id: Int?,
    val name: String?,
    val manufacturer: List<Agency?>,
    val description: String?,
    val active: Boolean?,
    val maidenFlight: String?,
    val totalLaunchCount: Int?,
    val consecutiveSuccessfulLaunches: Int?,
    val successfulLaunches: Int?,
    val failedLaunches: Int?,
    val pendingLaunches: Int?,
    val attemptedLandings: Int?,
    val successfulLandings: Int?,
    val failedLandings: Int?,
    val consecutiveSuccessfulLandings: Int?
)

@Immutable
data class Mission(
    val id: Int?,
    val name: String?,
    val type: String?,
    val description: String?,
    val orbit: Orbit?,
    val agencies: List<Agency?>,
    val infoUrls: List<InfoUrl>,
    val vidUrls: List<VidUrl>
)

@Immutable
data class Orbit(
    val id: Int?,
    val name: String?,
    val abbrev: String?
)

@Immutable
data class Pad(
    val id: Int?,
    val url: String?,
    val agencies: List<Agency?>,
    val name: String?,
    val image: Image,
    val description: String?,
    val country: Country?,
    val latitude: Double?,
    val longitude: Double?,
    val mapUrl: String?,
    val mapImage: String?,
    val wikiUrl: String?,
    val infoUrl: String?,
    val totalLaunchCount: Int?,
    val orbitalLaunchAttemptCount: Int?,
    val fastestTurnaround: String?,
    val location: Location?,
)

@Immutable
data class Location(
    val id: Int?,
    val url: String?,
    val name: String?,
    val country: Country?,
    val description: String?,
    val image: Image?,
    val mapImage: String?,
    val longitude: Double?,
    val latitude: Double?,
    val timezoneName: String?,
    val totalLaunchCount: Int?,
    val totalLandingCount: Int?,
)

@Immutable
data class Program(
    val id: Int?,
    val url: String?,
    val name: String?,
    val description: String?,
    val image: Image,
    val startDate: String?,
    val endDate: String?,
    val agencies: List<Agency?>
)

@Immutable
data class LaunchUpdate(
    val id: Int?,
    val profileImage: String?,
    val comment: String?,
    val infoUrl: String?,
    val createdBy: String?,
    val createdOn: String?
)

@Immutable
data class VidUrl(
    val priority: Int?,
    val source: String?,
    val publisher: String?,
    val title: String?,
    val description: String?,
    val featureImage: String?,
    val url: String?,
    val startTime: String?,
    val endTime: String?,
    val live: Boolean?
)

@Immutable
data class MissionPatch(
    val id: Int?,
    val name: String?,
    val priority: Int?,
    val imageUrl: String?,
    val agency: Agency?
)

@Immutable
data class InfoUrl(
    val priority: Int?,
    val source: String?,
    val title: String?,
    val description: String?,
    val featureImage: String?,
    val url: String?
)

@Immutable
data class LauncherStage(
    val id: Int?,
    val type: String?,
    val reused: Boolean?,
    val launcherFlightNumber: Int?,
    val launcher: Launcher?,
    val landing: Landing?,
    val previousFlightDate: String?,
    val turnAroundTime: String?,
    val previousFlight: PreviousFlight?
)

@Immutable
data class Launcher(
    val id: Int?,
    val url: String?,
    val flightProven: Boolean?,
    val serialNumber: String?,
    val status: Status?,
    val details: String?,
    val image: Image,
    val successfulLandings: Int?,
    val attemptedLandings: Int?,
    val flights: Int?,
    val lastLaunchDate: String?,
    val firstLaunchDate: String?
)

@Immutable
data class Landing(
    val id: Int?,
    val attempt: Boolean?,
    val success: Boolean?,
    val description: String?,
    val location: LandingLocation?,
    val type: LandingType?
)

@Immutable
data class LandingLocation(
    val id: Int?,
    val name: String?,
    val abbrev: String?,
    val description: String?
)

@Immutable
data class LandingType(
    val id: Int?,
    val name: String?,
    val abbrev: String?,
    val description: String?
)

@Immutable
data class PreviousFlight(
    val id: String?,
    val name: String?
)

@Immutable
data class SpacecraftStage(
    val id: Int?,
    val url: String?,
    val destination: String?,
    val missionEnd: String?,
    val spacecraft: Spacecraft?,
    val landing: Landing?
)

@Immutable
data class Spacecraft(
    val id: Int?,
    val url: String?,
    val name: String?,
    val serialNumber: String?,
    val status: SpacecraftStatus?,
    val description: String?,
    val spacecraftConfig: SpacecraftConfig?
)

@Immutable
data class SpacecraftStatus(
    val id: Int?,
    val name: String?
)

@Immutable
data class SpacecraftConfig(
    val id: Int?,
    val url: String?,
    val name: String?,
    val type: SpacecraftType?,
    val agency: Agency?,
    val inUse: Boolean?,
    val capability: String?,
    val history: String?,
    val details: String?,
    val maidenFlight: String?,
    val height: Double?,
    val diameter: Double?,
    val humanRated: Boolean?,
    val crewCapacity: Int?
)

@Immutable
data class SpacecraftType(
    val id: Int?,
    val name: String?
)
