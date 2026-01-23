package com.seancoyle.database.entities

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.TypeConverters
import com.seancoyle.database.util.FamilyListConverter
import com.seancoyle.database.util.InfoUrlListConverter
import com.seancoyle.database.util.LaunchUpdateListConverter
import com.seancoyle.database.util.MissionPatchListConverter
import com.seancoyle.database.util.ProgramListConverter
import com.seancoyle.database.util.VidUrlListConverter

@Keep
@Entity(tableName = "past_detail", primaryKeys = ["id"])
data class PastDetailEntity(

    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "url")
    val url: String?,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "last_updated")
    val lastUpdated: String?,

    @ColumnInfo(name = "net")
    val net: String,

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
    val rocket: RocketEntity,

    @Embedded(prefix = "mission")
    val mission: MissionEntity,

    @Embedded(prefix = "pad")
    val pad: PadEntity,

    @ColumnInfo(name = "webcast_live")
    val webcastLive: Boolean?,

    @field:TypeConverters(ProgramListConverter::class)
    @ColumnInfo(name = "program")
    val program: List<ProgramEntity>,

    @ColumnInfo(name = "orbital_launch_attempt_count")
    val orbitalLaunchAttemptCount: Int?,

    @ColumnInfo(name = "location_launch_attempt_count")
    val locationLaunchAttemptCount: Int?,

    @ColumnInfo(name = "pad_launch_attempt_count")
    val padLaunchAttemptCount: Int?,

    @ColumnInfo(name = "agency_launch_attempt_count")
    val agencyLaunchAttemptCount: Int?,

    @ColumnInfo(name = "orbital_launch_attempt_count_year")
    val orbitalLaunchAttemptCountYear: Int?,

    @ColumnInfo(name = "location_launch_attempt_count_year")
    val locationLaunchAttemptCountYear: Int?,

    @ColumnInfo(name = "pad_launch_attempt_count_year")
    val padLaunchAttemptCountYear: Int?,

    @ColumnInfo(name = "agency_launch_attempt_count_year")
    val agencyLaunchAttemptCountYear: Int?,

    @field:TypeConverters(LaunchUpdateListConverter::class)
    @ColumnInfo(name = "updates")
    val updates: List<LaunchUpdateEntity>,

    @field:TypeConverters(InfoUrlListConverter::class)
    @ColumnInfo(name = "info_urls")
    val infoUrls: List<InfoUrlEntity>,

    @field:TypeConverters(VidUrlListConverter::class)
    @ColumnInfo(name = "vid_urls")
    val vidUrls: List<VidUrlEntity>,

    @ColumnInfo(name = "pad_turnaround")
    val padTurnaround: String?,

    @field:TypeConverters(MissionPatchListConverter::class)
    @ColumnInfo(name = "mission_patches")
    val missionPatches: List<MissionPatchEntity>,

    @Embedded(prefix = "status")
    val status: LaunchStatusEntity,

    @Embedded(prefix = "configuration")
    val configuration: ConfigurationEntity?,

    @ColumnInfo(name = "family")
    @field:TypeConverters(FamilyListConverter::class)
    val families: List<FamilyEntity>,
)
