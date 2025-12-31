package com.seancoyle.database.entities

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.TypeConverters
import com.seancoyle.database.util.AgencyListConverter
import com.seancoyle.database.util.CountryListConverter
import com.seancoyle.database.util.ProgramListConverter
import com.seancoyle.database.util.LaunchUpdateListConverter
import com.seancoyle.database.util.VidUrlListConverter
import com.seancoyle.database.util.MissionPatchListConverter
import com.seancoyle.database.util.InfoUrlListConverter
import kotlinx.serialization.Serializable

@Keep
@Entity(tableName = "launch", primaryKeys = ["id"])
data class LaunchEntity(

    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "url")
    val url: String?,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "last_updated")
    val lastUpdated: String?,

    @ColumnInfo(name = "net")
    val net: String?,

    @Embedded(prefix = "net_precision")
    val netPrecision: NetPrecisionEntity?,

    @ColumnInfo(name = "window_end")
    val windowEnd: String?,

    @ColumnInfo(name = "window_start")
    val windowStart: String?,

    @Embedded(prefix = "image")
    val image: ImageEntity,

    @ColumnInfo(name = "infographic")
    val infographic: String?,

    @ColumnInfo(name = "probability")
    val probability: Int?,

    @ColumnInfo(name = "weather_concerns")
    val weatherConcerns: String?,

    @ColumnInfo(name = "fail_reason")
    val failReason: String?,

    @Embedded(prefix = "agency")
    val launchServiceProvider: AgencyEntity?,

    @Embedded(prefix = "rocket")
    val rocket: RocketEntity?,

    @Embedded(prefix = "mission")
    val mission: MissionEntity?,

    @Embedded(prefix = "pad")
    val pad: PadEntity?,

    @ColumnInfo(name = "webcast_live")
    val webcastLive: Boolean?,

    @field:TypeConverters(ProgramListConverter::class)
    @ColumnInfo(name = "program")
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

    @field:TypeConverters(LaunchUpdateListConverter::class)
    @ColumnInfo(name = "updates")
    val updates: List<LaunchUpdateEntity>?,

    @field:TypeConverters(InfoUrlListConverter::class)
    @ColumnInfo(name = "info_urls")
    val infoUrls: List<InfoUrlEntity>?,

    @field:TypeConverters(VidUrlListConverter::class)
    @ColumnInfo(name = "vid_urls")
    val vidUrls: List<VidUrlEntity>?,

    @ColumnInfo(name = "pad_turnaround")
    val padTurnaround: String?,

    @field:TypeConverters(MissionPatchListConverter::class)
    @ColumnInfo(name = "mission_patches")
    val missionPatches: List<MissionPatchEntity>?,

    @Embedded(prefix = "status")
    val status: LaunchStatusEntity?,
)

@Keep
@Serializable
data class NetPrecisionEntity(
    @ColumnInfo(name = "id")
    val id: Int?,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "abbrev")
    val abbrev: String?,

    @ColumnInfo(name = "description")
    val description: String?
)

@Keep
@Serializable
data class ImageEntity(
    @ColumnInfo(name = "id")
    val id: Int?,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "image_url")
    val imageUrl: String,

    @ColumnInfo(name = "thumbnail_url")
    val thumbnailUrl: String,

    @ColumnInfo(name = "credit")
    val credit: String?
)

@Keep
@Serializable
data class AgencyEntity(
    @ColumnInfo(name = "id")
    val id: Int?,

    @ColumnInfo(name = "url")
    val url: String?,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "abbrev")
    val abbrev: String?,

    @ColumnInfo(name = "type")
    val type: String?,

    @ColumnInfo(name = "featured")
    val featured: Boolean?,

    @field:TypeConverters(CountryListConverter::class)
    @ColumnInfo(name = "country")
    val country: List<CountryEntity>?,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "administrator")
    val administrator: String?,

    @ColumnInfo(name = "founding_year")
    val foundingYear: Int?,

    @ColumnInfo(name = "launchers")
    val launchers: String?,

    @ColumnInfo(name = "spacecraft")
    val spacecraft: String?,

    @ColumnInfo(name = "parent")
    val parent: String?,

    @Embedded(prefix = "agency_image")
    val image: ImageEntity,

    @ColumnInfo(name = "total_launch_count")
    val totalLaunchCount: Int?,

    @ColumnInfo(name = "consecutive_successful_launches")
    val consecutiveSuccessfulLaunches: Int?,

    @ColumnInfo(name = "successful_launches")
    val successfulLaunches: Int?,

    @ColumnInfo(name = "failed_launches")
    val failedLaunches: Int?,

    @ColumnInfo(name = "pending_launches")
    val pendingLaunches: Int?,

    @ColumnInfo(name = "consecutive_successful_landings")
    val consecutiveSuccessfulLandings: Int?,

    @ColumnInfo(name = "successful_landings")
    val successfulLandings: Int?,

    @ColumnInfo(name = "failed_landings")
    val failedLandings: Int?,

    @ColumnInfo(name = "attempted_landings")
    val attemptedLandings: Int?,

    @ColumnInfo(name = "successful_landings_spacecraft")
    val successfulLandingsSpacecraft: Int?,

    @ColumnInfo(name = "failed_landings_spacecraft")
    val failedLandingsSpacecraft: Int?,

    @ColumnInfo(name = "attempted_landings_spacecraft")
    val attemptedLandingsSpacecraft: Int?,

    @ColumnInfo(name = "successful_landings_payload")
    val successfulLandingsPayload: Int?,

    @ColumnInfo(name = "failed_landings_payload")
    val failedLandingsPayload: Int?,

    @ColumnInfo(name = "attempted_landings_payload")
    val attemptedLandingsPayload: Int?,

    @ColumnInfo(name = "info_url")
    val infoUrl: String?,

    @ColumnInfo(name = "wiki_url")
    val wikiUrl: String?,
)

@Keep
@Serializable
data class CountryEntity(
    @ColumnInfo(name = "id")
    val id: Int?,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "alpha_2_code")
    val alpha2Code: String?,

    @ColumnInfo(name = "alpha_3_code")
    val alpha3Code: String?,

    @ColumnInfo(name = "nationality_name")
    val nationalityName: String?
)

@Keep
@Serializable
data class RocketEntity(
    @ColumnInfo(name = "id")
    val id: Int?,

    @ColumnInfo(name = "configuration_id")
    val configurationId: Int?,

    @ColumnInfo(name = "configuration_url")
    val configurationUrl: String?,

    @ColumnInfo(name = "configuration_name")
    val configurationName: String?,

    @ColumnInfo(name = "configuration_full_name")
    val configurationFullName: String?,

    @ColumnInfo(name = "variant")
    val variant: String?
)

@Keep
@Serializable
data class PadEntity(
    @ColumnInfo(name = "id")
    val id: Int?,

    @ColumnInfo(name = "url")
    val url: String?,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "location_id")
    val locationId: Int?,

    @ColumnInfo(name = "location_url")
    val locationUrl: String?,

    @ColumnInfo(name = "location_name")
    val locationName: String?,

    @ColumnInfo(name = "location_description")
    val locationDescription: String?,

    @ColumnInfo(name = "location_timezone")
    val locationTimezone: String?,

    @ColumnInfo(name = "location_total_launch_count")
    val locationTotalLaunchCount: Int?,

    @ColumnInfo(name = "latitude")
    val latitude: Double?,

    @ColumnInfo(name = "longitude")
    val longitude: Double?,

    @ColumnInfo(name = "map_url")
    val mapUrl: String?,

    @ColumnInfo(name = "total_launch_count")
    val totalLaunchCount: Int?
)

@Keep
@Serializable
data class MissionEntity(
    @ColumnInfo(name = "id")
    val id: Int?,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "type")
    val type: String?,

    @ColumnInfo(name = "description")
    val description: String?,

    @Embedded(prefix = "orbit")
    val orbit: OrbitEntity?,

    @field:TypeConverters(AgencyListConverter::class)
    @ColumnInfo(name = "agencies")
    val agencies: List<AgencyEntity>?
)

@Keep
@Serializable
data class OrbitEntity(
    @ColumnInfo(name = "id")
    val id: Int?,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "abbrev")
    val abbrev: String?
)

@Keep
@Serializable
data class ProgramEntity(
    @ColumnInfo(name = "id")
    val id: Int?,

    @ColumnInfo(name = "url")
    val url: String?,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "description")
    val description: String?,

    @Embedded(prefix = "image")
    val image: ImageEntity,

    @ColumnInfo(name = "start_date")
    val startDate: String?,

    @ColumnInfo(name = "end_date")
    val endDate: String?,

    @field:TypeConverters(AgencyListConverter::class)
    @ColumnInfo(name = "agencies")
    val agencies: List<AgencyEntity>?
)

@Keep
data class LaunchStatusEntity(
    @ColumnInfo(name = "id")
    val id: Int?,
    @ColumnInfo(name = "name")
    val name: String?,
    @ColumnInfo(name = "abbrev")
    val abbrev: String?,
    @ColumnInfo(name = "description")
    val description: String?,
)

@Keep
@Serializable
data class LaunchUpdateEntity(
    @ColumnInfo(name = "id")
    val id: Int?,

    @ColumnInfo(name = "profile_image")
    val profileImage: String?,

    @ColumnInfo(name = "comment")
    val comment: String?,

    @ColumnInfo(name = "info_url")
    val infoUrl: String?,

    @ColumnInfo(name = "created_by")
    val createdBy: String?,

    @ColumnInfo(name = "created_on")
    val createdOn: String?
)

@Keep
@Serializable
data class VidUrlEntity(
    @ColumnInfo(name = "priority")
    val priority: Int?,

    @ColumnInfo(name = "source")
    val source: String?,

    @ColumnInfo(name = "publisher")
    val publisher: String?,

    @ColumnInfo(name = "title")
    val title: String?,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "feature_image")
    val featureImage: String?,

    @ColumnInfo(name = "url")
    val url: String?,

    @ColumnInfo(name = "start_time")
    val startTime: String?,

    @ColumnInfo(name = "end_time")
    val endTime: String?,

    @ColumnInfo(name = "live")
    val live: Boolean?
)

@Keep
@Serializable
data class MissionPatchEntity(
    @ColumnInfo(name = "id")
    val id: Int?,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "priority")
    val priority: Int?,

    @ColumnInfo(name = "image_url")
    val imageUrl: String?,

    @Embedded(prefix = "agency")
    val agency: AgencyEntity?
)

@Keep
@Serializable
data class InfoUrlEntity(
    @ColumnInfo(name = "priority")
    val priority: Int?,
    @ColumnInfo(name = "source")
    val source: String?,
    @ColumnInfo(name = "title")
    val title: String?,
    @ColumnInfo(name = "description")
    val description: String?,
    @ColumnInfo(name = "feature_image")
    val featureImage: String?,
    @ColumnInfo(name = "url")
    val url: String?
)
