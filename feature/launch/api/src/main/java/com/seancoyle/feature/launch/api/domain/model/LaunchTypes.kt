package com.seancoyle.feature.launch.api.domain.model

import java.time.LocalDateTime

sealed interface LaunchTypes {

    data class Launch(
        val id: String,
        val url: String?,
        val name: String?,
        val responseMode: String?,
        val lastUpdated: String?,
        val net: String?,
        val netPrecision: NetPrecision?,
        val windowEnd: String?,
        val windowStart: String?,
        val image: Image?,
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
        val launchDate: String?,
        val launchDateLocalDateTime: LocalDateTime?,
        val launchYear: String?,
        val launchDateStatus: LaunchDateStatus?,
        val launchStatus: LaunchStatus,
        val launchDays: String?,
    ) : LaunchTypes
}

data class NetPrecision(
    val id: Int?,
    val name: String?,
    val abbrev: String?,
    val description: String?
)

data class Image(
    val id: Int?,
    val name: String?,
    val imageUrl: String?,
    val thumbnailUrl: String?,
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
    val image: Image?,
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
    val image: Image?,
    val startDate: String?,
    val endDate: String?,
    val agencies: List<Agency?>?
)
