package com.seancoyle.feature.launch.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class LaunchesDto(
    @SerialName("count")
    val count: Int?,

    @SerialName("next")
    val next: String?,

    @SerialName("previous")
    val previous: String?,

    @SerialName("results")
    val results: List<LaunchDto>?,
)

@Serializable
internal data class LaunchDto(
    @SerialName("id")
    val id: String?,

    @SerialName("url")
    val url: String?,

    @SerialName("name")
    val name: String?,

    @SerialName("response_mode")
    val responseMode: String?,

    @SerialName("status")
    val status: StatusDto?,

    @SerialName("last_updated")
    val lastUpdated: String?,

    @SerialName("net")
    val net: String?,

    @SerialName("net_precision")
    val netPrecision: NetPrecisionDto?,

    @SerialName("window_end")
    val windowEnd: String?,

    @SerialName("window_start")
    val windowStart: String?,

    @SerialName("image")
    val image: ImageDto?,

    @SerialName("infographic")
    val infographic: String?,

    @SerialName("probability")
    val probability: Int?,

    @SerialName("weather_concerns")
    val weatherConcerns: String?,

    @SerialName("failreason")
    val failReason: String?,

    @SerialName("launch_service_provider")
    val launchServiceProvider: AgencyDto?,

    @SerialName("rocket")
    val rocket: RocketDto?,

    @SerialName("mission")
    val mission: MissionDto?,

    @SerialName("pad")
    val pad: PadDto?,

    @SerialName("webcast_live")
    val webcastLive: Boolean?,

    @SerialName("program")
    val program: List<ProgramDto>?,

    @SerialName("orbital_launch_attempt_count")
    val orbitalLaunchAttemptCount: Int?,

    @SerialName("location_launch_attempt_count")
    val locationLaunchAttemptCount: Int?,

    @SerialName("pad_launch_attempt_count")
    val padLaunchAttemptCount: Int?,

    @SerialName("agency_launch_attempt_count")
    val agencyLaunchAttemptCount: Int?,

    @SerialName("orbital_launch_attempt_count_year")
    val orbitalLaunchAttemptCountYear: Int?,

    @SerialName("location_launch_attempt_count_year")
    val locationLaunchAttemptCountYear: Int?,

    @SerialName("pad_launch_attempt_count_year")
    val padLaunchAttemptCountYear: Int?,

    @SerialName("agency_launch_attempt_count_year")
    val agencyLaunchAttemptCountYear: Int?,
)

@Serializable
internal data class StatusDto(
    @SerialName("id")
    val id: Int?,

    @SerialName("name")
    val name: String?,

    @SerialName("abbrev")
    val abbrev: String?,

    @SerialName("description")
    val description: String?,
)

@Serializable
internal data class NetPrecisionDto(
    @SerialName("id")
    val id: Int?,

    @SerialName("name")
    val name: String?,

    @SerialName("abbrev")
    val abbrev: String?,

    @SerialName("description")
    val description: String?,
)

@Serializable
internal data class ImageDto(
    @SerialName("id")
    val id: Int?,

    @SerialName("name")
    val name: String?,

    @SerialName("image_url")
    val imageUrl: String?,

    @SerialName("thumbnail_url")
    val thumbnailUrl: String?,

    @SerialName("credit")
    val credit: String?,
)

@Serializable
internal data class AgencyDto(
    @SerialName("response_mode")
    val responseMode: String?,

    @SerialName("id")
    val id: Int?,

    @SerialName("url")
    val url: String?,

    @SerialName("name")
    val name: String?,

    @SerialName("abbrev")
    val abbrev: String?,

    @SerialName("type")
    val type: TypeDto?,

    @SerialName("featured")
    val featured: Boolean?,

    @SerialName("country")
    val country: List<CountryDto>?,

    @SerialName("description")
    val description: String?,

    @SerialName("administrator")
    val administrator: String?,

    @SerialName("founding_year")
    val foundingYear: Int?,

    @SerialName("launchers")
    val launchers: String?,

    @SerialName("spacecraft")
    val spacecraft: String?,

    @SerialName("parent")
    val parent: String?,

    @SerialName("image")
    val image: ImageDto?,

    @SerialName("total_launch_count")
    val totalLaunchCount: Int?,

    @SerialName("consecutive_successful_launches")
    val consecutiveSuccessfulLaunches: Int?,

    @SerialName("successful_launches")
    val successfulLaunches: Int?,

    @SerialName("failed_launches")
    val failedLaunches: Int?,

    @SerialName("pending_launches")
    val pendingLaunches: Int?,

    @SerialName("consecutive_successful_landings")
    val consecutiveSuccessfulLandings: Int?,

    @SerialName("successful_landings")
    val successfulLandings: Int?,

    @SerialName("failed_landings")
    val failedLandings: Int?,

    @SerialName("attempted_landings")
    val attemptedLandings: Int?,

    @SerialName("successful_landings_spacecraft")
    val successfulLandingsSpacecraft: Int?,

    @SerialName("failed_landings_spacecraft")
    val failedLandingsSpacecraft: Int?,

    @SerialName("attempted_landings_spacecraft")
    val attemptedLandingsSpacecraft: Int?,

    @SerialName("successful_landings_payload")
    val successfulLandingsPayload: Int?,

    @SerialName("failed_landings_payload")
    val failedLandingsPayload: Int?,

    @SerialName("attempted_landings_payload")
    val attemptedLandingsPayload: Int?,

    @SerialName("info_url")
    val infoUrl: String?,

    @SerialName("wiki_url")
    val wikiUrl: String?,
)

@Serializable
internal data class TypeDto(
    @SerialName("id")
    val id: Int?,

    @SerialName("name")
    val name: String?,
)

@Serializable
internal data class CountryDto(
    @SerialName("id")
    val id: Int?,

    @SerialName("name")
    val name: String?,

    @SerialName("alpha_2_code")
    val alpha2Code: String?,

    @SerialName("alpha_3_code")
    val alpha3Code: String?,

    @SerialName("nationality_name")
    val nationalityName: String?,

    @SerialName("nationality_name_composed")
    val nationalityNameComposed: String?,
)

@Serializable
internal data class RocketDto(
    @SerialName("id")
    val id: Int?,

    @SerialName("configuration")
    val configuration: ConfigurationDto?,
)

@Serializable
internal data class ConfigurationDto(
    @SerialName("response_mode")
    val responseMode: String?,

    @SerialName("id")
    val id: Int?,

    @SerialName("url")
    val url: String?,

    @SerialName("name")
    val name: String?,

    @SerialName("families")
    val families: List<FamilyDto>?,

    @SerialName("full_name")
    val fullName: String?,

    @SerialName("variant")
    val variant: String?,
)

@Serializable
internal data class FamilyDto(
    @SerialName("response_mode")
    val responseMode: String?,

    @SerialName("id")
    val id: Int?,

    @SerialName("name")
    val name: String?,
)

@Serializable
internal data class MissionDto(
    @SerialName("id")
    val id: Int?,

    @SerialName("name")
    val name: String?,

    @SerialName("type")
    val type: String?,

    @SerialName("description")
    val description: String?,

    @SerialName("orbit")
    val orbit: OrbitDto?,

    @SerialName("agencies")
    val agencies: List<AgencyDto>?,

    @SerialName("info_urls")
    val infoUrls: List<String>?,

    @SerialName("vid_urls")
    val vidUrls: List<String>?,
)

@Serializable
internal data class OrbitDto(
    @SerialName("id")
    val id: Int?,

    @SerialName("name")
    val name: String?,

    @SerialName("abbrev")
    val abbrev: String?,
)

@Serializable
internal data class PadDto(
    @SerialName("id")
    val id: Int?,

    @SerialName("url")
    val url: String?,

    @SerialName("active")
    val active: Boolean?,

    @SerialName("agencies")
    val agencies: List<AgencyDto>?,

    @SerialName("name")
    val name: String?,

    @SerialName("image")
    val image: ImageDto?,

    @SerialName("description")
    val description: String?,

    @SerialName("info_url")
    val infoUrl: String?,

    @SerialName("wiki_url")
    val wikiUrl: String?,

    @SerialName("map_url")
    val mapUrl: String?,

    @SerialName("latitude")
    val latitude: Double?,

    @SerialName("longitude")
    val longitude: Double?,

    @SerialName("country")
    val country: CountryDto?,

    @SerialName("map_image")
    val mapImage: String?,

    @SerialName("total_launch_count")
    val totalLaunchCount: Int?,

    @SerialName("orbital_launch_attempt_count")
    val orbitalLaunchAttemptCount: Int?,

    @SerialName("fastest_turnaround")
    val fastestTurnaround: String?,

    @SerialName("location")
    val location: LocationDto?,
)

@Serializable
internal data class LocationDto(
    @SerialName("response_mode")
    val responseMode: String?,

    @SerialName("id")
    val id: Int?,

    @SerialName("url")
    val url: String?,

    @SerialName("name")
    val name: String?,

    @SerialName("active")
    val active: Boolean?,

    @SerialName("country")
    val country: CountryDto?,

    @SerialName("description")
    val description: String?,

    @SerialName("image")
    val image: ImageDto?,

    @SerialName("map_image")
    val mapImage: String?,

    @SerialName("longitude")
    val longitude: Double?,

    @SerialName("latitude")
    val latitude: Double?,

    @SerialName("timezone_name")
    val timezoneName: String?,

    @SerialName("total_launch_count")
    val totalLaunchCount: Int?,

    @SerialName("total_landing_count")
    val totalLandingCount: Int?,
)

@Serializable
internal data class ProgramDto(
    @SerialName("response_mode")
    val responseMode: String?,

    @SerialName("id")
    val id: Int?,

    @SerialName("url")
    val url: String?,

    @SerialName("name")
    val name: String?,

    @SerialName("image")
    val image: ImageDto?,

    @SerialName("info_url")
    val infoUrl: String?,

    @SerialName("wiki_url")
    val wikiUrl: String?,

    @SerialName("description")
    val description: String?,

    @SerialName("agencies")
    val agencies: List<AgencyDto>?,

    @SerialName("start_date")
    val startDate: String?,

    @SerialName("end_date")
    val endDate: String?,

    @SerialName("mission_patches")
    val missionPatches: List<MissionPatchDto>?,

    @SerialName("type")
    val type: TypeDto?,
)

@Serializable
internal data class MissionPatchDto(
    @SerialName("id")
    val id: Int?,

    @SerialName("name")
    val name: String?,

    @SerialName("priority")
    val priority: Int?,

    @SerialName("image_url")
    val imageUrl: String?,

    @SerialName("agency")
    val agency: AgencyDto?,

    @SerialName("response_mode")
    val responseMode: String?,
)
