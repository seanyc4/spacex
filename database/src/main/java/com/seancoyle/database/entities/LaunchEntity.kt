package com.seancoyle.database.entities

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.TypeConverters
import com.seancoyle.database.util.AgencyListConverter
import com.seancoyle.database.util.CountryListConverter
import com.seancoyle.database.util.LocalDateTimeConverter
import com.seancoyle.database.util.ProgramListConverter
import java.time.LocalDateTime

@Keep
@Entity(tableName = "launch", primaryKeys = ["id"])
data class LaunchEntity(

    @ColumnInfo(name="id")
    val id: String,

    @ColumnInfo(name="count")
    val count: Int,

    @ColumnInfo(name="url")
    val url: String?,

    @ColumnInfo(name="name")
    val name: String?,

    @ColumnInfo(name="response_mode")
    val responseMode: String?,

    @ColumnInfo(name="last_updated")
    val lastUpdated: String?,

    @ColumnInfo(name="net")
    val net: String?,

    @Embedded(prefix = "net_precision")
    val netPrecision: NetPrecisionEntity?,

    @ColumnInfo(name="window_end")
    val windowEnd: String?,

    @ColumnInfo(name="window_start")
    val windowStart: String?,

    @Embedded(prefix = "image")
    val image: ImageEntity?,

    @ColumnInfo(name="infographic")
    val infographic: String?,

    @ColumnInfo(name="probability")
    val probability: Int?,

    @ColumnInfo(name="weather_concerns")
    val weatherConcerns: String?,

    @ColumnInfo(name="fail_reason")
    val failReason: String?,

    @Embedded(prefix = "agency")
    val launchServiceProvider: AgencyEntity?,

    @Embedded(prefix = "rocket")
    val rocket: RocketEntity?,

    @Embedded(prefix = "mission")
    val mission: MissionEntity?,

    @Embedded(prefix = "pad")
    val pad: PadEntity?,

    @ColumnInfo(name="webcast_live")
    val webcastLive: Boolean?,

    @field:TypeConverters(ProgramListConverter::class)
    @ColumnInfo(name="program")
    val program: List<ProgramEntity>?,

    @ColumnInfo("orbital_launch_attempt_count")
    val orbitalLaunchAttemptCount: Int?,

    @ColumnInfo("location_launch_attempt_count")
    val locationLaunchAttemptCount: Int?,

    @ColumnInfo("pad_launch_attempt_count")
    val padLaunchAttemptCount: Int?,

    @ColumnInfo("agency_launch_attempt_count")
    val agencyLaunchAttemptCount: Int?,

    @ColumnInfo("orbital_launch_attempt_count_year")
    val orbitalLaunchAttemptCountYear: Int?,

    @ColumnInfo("location_launch_attempt_count_year")
    val locationLaunchAttemptCountYear: Int?,

    @ColumnInfo("pad_launch_attempt_count_year")
    val padLaunchAttemptCountYear: Int?,

    @ColumnInfo("agency_launch_attempt_count_year")
    val agencyLaunchAttemptCountYear: Int?,

    @ColumnInfo(name="launch_date")
    val launchDate: String?,

    @field:TypeConverters(LocalDateTimeConverter::class)
    @ColumnInfo(name="launch_date_local")
    val launchDateLocalDateTime: LocalDateTime?,

    @ColumnInfo(name="launch_year")
    val launchYear: String?,

    @ColumnInfo(name="launch_date_status")
    val launchDateStatus: LaunchDateStatusEntity?,

    @ColumnInfo(name="launch_status")
    val launchStatus: LaunchStatusEntity,

    @ColumnInfo(name="launch_days_difference")
    val launchDays: String?,
)

@Keep
data class NetPrecisionEntity(
    @ColumnInfo(name="id")
    val id: Int?,

    @ColumnInfo(name="name")
    val name: String?,

    @ColumnInfo(name="abbrev")
    val abbrev: String?,

    @ColumnInfo(name="description")
    val description: String?
)

@Keep
data class ImageEntity(
    @ColumnInfo(name="id")
    val id: Int?,

    @ColumnInfo(name="name")
    val name: String?,

    @ColumnInfo(name="image_url")
    val imageUrl: String?,

    @ColumnInfo(name="thumbnail_url")
    val thumbnailUrl: String?,

    @ColumnInfo(name="credit")
    val credit: String?
)

@Keep
data class AgencyEntity(
    @ColumnInfo(name="id")
    val id: Int?,

    @ColumnInfo(name="url")
    val url: String?,

    @ColumnInfo(name="name")
    val name: String?,

    @ColumnInfo(name="abbrev")
    val abbrev: String?,

    @ColumnInfo(name="type")
    val type: String?,

    @ColumnInfo(name="featured")
    val featured: Boolean?,

    @field:TypeConverters(CountryListConverter::class)
    @ColumnInfo(name="country")
    val country: List<CountryEntity>?,

    @ColumnInfo(name="description")
    val description: String?,

    @ColumnInfo(name="administrator")
    val administrator: String?,

    @ColumnInfo(name="founding_year")
    val foundingYear: Int?,

    @ColumnInfo(name="launchers")
    val launchers: String?,

    @ColumnInfo(name="spacecraft")
    val spacecraft: String?,

    @ColumnInfo(name="parent")
    val parent: String?,

    @Embedded(prefix = "agency_image")
    val image: ImageEntity?,

    @ColumnInfo(name="total_launch_count")
    val totalLaunchCount: Int?,

    @ColumnInfo(name="consecutive_successful_launches")
    val consecutiveSuccessfulLaunches: Int?,

    @ColumnInfo(name="successful_launches")
    val successfulLaunches: Int?,

    @ColumnInfo(name="failed_launches")
    val failedLaunches: Int?,

    @ColumnInfo(name="pending_launches")
    val pendingLaunches: Int?,

    @ColumnInfo(name="consecutive_successful_landings")
    val consecutiveSuccessfulLandings: Int?,

    @ColumnInfo(name="successful_landings")
    val successfulLandings: Int?,

    @ColumnInfo(name="failed_landings")
    val failedLandings: Int?,

    @ColumnInfo(name="attempted_landings")
    val attemptedLandings: Int?,

    @ColumnInfo(name="successful_landings_spacecraft")
    val successfulLandingsSpacecraft: Int?,

    @ColumnInfo(name="failed_landings_spacecraft")
    val failedLandingsSpacecraft: Int?,

    @ColumnInfo(name="attempted_landings_spacecraft")
    val attemptedLandingsSpacecraft: Int?,

    @ColumnInfo(name="successful_landings_payload")
    val successfulLandingsPayload: Int?,

    @ColumnInfo(name="failed_landings_payload")
    val failedLandingsPayload: Int?,

    @ColumnInfo(name="attempted_landings_payload")
    val attemptedLandingsPayload: Int?,

    @ColumnInfo(name="info_url")
    val infoUrl: String?,

    @ColumnInfo(name="wiki_url")
    val wikiUrl: String?,
)

@Keep
data class CountryEntity(
    @ColumnInfo(name="id")
    val id: Int?,

    @ColumnInfo(name="name")
    val name: String?,

    @ColumnInfo(name="alpha_2_code")
    val alpha2Code: String?,

    @ColumnInfo(name="alpha_3_code")
    val alpha3Code: String?,

    @ColumnInfo(name="nationality_name")
    val nationalityName: String?
)

@Keep
data class RocketEntity(
    @ColumnInfo(name="id")
    val id: Int?,

    @ColumnInfo(name="configuration_id")
    val configurationId: Int?,

    @ColumnInfo(name="configuration_url")
    val configurationUrl: String?,

    @ColumnInfo(name="configuration_name")
    val configurationName: String?,

    @ColumnInfo(name="configuration_full_name")
    val configurationFullName: String?,

    @ColumnInfo(name="variant")
    val variant: String?
)

@Keep
data class PadEntity(
    @ColumnInfo(name="id")
    val id: Int?,

    @ColumnInfo(name="url")
    val url: String?,

    @ColumnInfo(name="name")
    val name: String?,

    @ColumnInfo(name="location_id")
    val locationId: Int?,

    @ColumnInfo(name="location_url")
    val locationUrl: String?,

    @ColumnInfo(name="location_name")
    val locationName: String?,

    @ColumnInfo(name="location_description")
    val locationDescription: String?,

    @ColumnInfo(name="location_timezone")
    val locationTimezone: String?,

    @ColumnInfo(name="location_total_launch_count")
    val locationTotalLaunchCount: Int?,

    @ColumnInfo(name="latitude")
    val latitude: Double?,

    @ColumnInfo(name="longitude")
    val longitude: Double?,

    @ColumnInfo(name="map_url")
    val mapUrl: String?,

    @ColumnInfo(name="total_launch_count")
    val totalLaunchCount: Int?
)

@Keep
data class MissionEntity(
    @ColumnInfo(name="id")
    val id: Int?,

    @ColumnInfo(name="name")
    val name: String?,

    @ColumnInfo(name="type")
    val type: String?,

    @ColumnInfo(name="description")
    val description: String?,

    @Embedded(prefix = "orbit")
    val orbit: OrbitEntity?,

    @field:TypeConverters(AgencyListConverter::class)
    @ColumnInfo(name="agencies")
    val agencies: List<AgencyEntity>?
)

@Keep
data class OrbitEntity(
    @ColumnInfo(name="id")
    val id: Int?,

    @ColumnInfo(name="name")
    val name: String?,

    @ColumnInfo(name="abbrev")
    val abbrev: String?
)

@Keep
data class ProgramEntity(
    @ColumnInfo(name="id")
    val id: Int?,

    @ColumnInfo(name="url")
    val url: String?,

    @ColumnInfo(name="name")
    val name: String?,

    @ColumnInfo(name="description")
    val description: String?,

    @Embedded(prefix = "image_")
    val image: ImageEntity?,

    @ColumnInfo(name="start_date")
    val startDate: String?,

    @ColumnInfo(name="end_date")
    val endDate: String?,

    @field:TypeConverters(AgencyListConverter::class)
    @ColumnInfo(name="agencies")
    val agencies: List<AgencyEntity>?
)

@Keep
enum class LaunchDateStatusEntity {
    PAST, FUTURE
}

@Keep
enum class LaunchStatusEntity {
    SUCCESS, FAILED, UNKNOWN, ALL
}
