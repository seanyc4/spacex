package com.seancoyle.feature.launch.domain.model

data class Launch(
    val id: String,
    val url: String?,
    val missionName: String?,
    val lastUpdated: String?,
    val net: String?,
    val netPrecision: NetPrecision?,
    val status: LaunchStatus,
    val windowEnd: String?,
    val windowStart: String?,
    val image: Image,
    val infographic: String?,
    val probability: Int?,
    val weatherConcerns: String?,
    val failReason: String?,
    val launchServiceProvider: Agency?,
    val rocket: Rocket?,
    val mission: Mission?,
    val pad: Pad?,
    val webcastLive: Boolean?,
    val program: List<Program>?,
    val orbitalLaunchAttemptCount: Int?,
    val locationLaunchAttemptCount: Int?,
    val padLaunchAttemptCount: Int?,
    val agencyLaunchAttemptCount: Int?,
    val orbitalLaunchAttemptCountYear: Int?,
    val locationLaunchAttemptCountYear: Int?,
    val padLaunchAttemptCountYear: Int?,
    val agencyLaunchAttemptCountYear: Int?,
    val updates: List<LaunchUpdate>?,
    val infoUrls: List<InfoUrl>?,
    val vidUrls: List<VidUrl>?,
    val padTurnaround: String?,
    val missionPatches: List<MissionPatch>?,
)

data class NetPrecision(
    val id: Int?,
    val name: String?,
    val abbrev: String?,
    val description: String?
)

data class Image(
    val id: Int?,
    val name: String?,
    val imageUrl: String,
    val thumbnailUrl: String,
    val credit: String?
)

data class Agency(
    val id: Int?,
    val url: String?,
    val name: String?,
    val abbrev: String?,
    val type: String?,
    val featured: Boolean?,
    val country: List<Country>?,
    val description: String?,
    val administrator: String?,
    val foundingYear: Int?,
    val launchers: String?,
    val spacecraft: String?,
    val parent: String?,
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

data class Country(
    val id: Int?,
    val name: String?,
    val alpha2Code: String?,
    val alpha3Code: String?,
    val nationalityName: String?
)

data class Rocket(
    val id: Int?,
    val configuration: Configuration?
)

data class Configuration(
    val id: Int?,
    val url: String?,
    val name: String?,
    val fullName: String?,
    val variant: String?,
    val families: List<Family>?
)

data class Family(
    val id: Int?,
    val name: String?
)

data class Mission(
    val id: Int?,
    val name: String?,
    val type: String?,
    val description: String?,
    val orbit: Orbit?,
    val agencies: List<Agency?>?
)

data class Orbit(
    val id: Int?,
    val name: String?,
    val abbrev: String?
)

data class Pad(
    val id: Int?,
    val url: String?,
    val name: String?,
    val location: Location?,
    val latitude: Double?,
    val longitude: Double?,
    val mapUrl: String?,
    val totalLaunchCount: Int?
)

data class Location(
    val id: Int?,
    val url: String?,
    val name: String?,
    val country: Country?,
    val description: String?,
    val timezoneName: String?,
    val totalLaunchCount: Int?
)

data class Program(
    val id: Int?,
    val url: String?,
    val name: String?,
    val description: String?,
    val image: Image,
    val startDate: String?,
    val endDate: String?,
    val agencies: List<Agency?>?
)

data class LaunchUpdate(
    val id: Int?,
    val profileImage: String?,
    val comment: String?,
    val infoUrl: String?,
    val createdBy: String?,
    val createdOn: String?
)

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

data class MissionPatch(
    val id: Int?,
    val name: String?,
    val priority: Int?,
    val imageUrl: String?,
    val agency: Agency?
)

data class InfoUrl(
    val priority: Int?,
    val source: String?,
    val title: String?,
    val description: String?,
    val featureImage: String?,
    val url: String?
)
