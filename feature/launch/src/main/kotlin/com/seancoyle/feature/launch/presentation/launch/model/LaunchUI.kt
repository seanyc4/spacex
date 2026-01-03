package com.seancoyle.feature.launch.presentation.launch.model

import com.seancoyle.feature.launch.domain.model.Agency
import com.seancoyle.feature.launch.domain.model.Image
import com.seancoyle.feature.launch.domain.model.InfoUrl
import com.seancoyle.feature.launch.domain.model.LaunchUpdate
import com.seancoyle.feature.launch.domain.model.Mission
import com.seancoyle.feature.launch.domain.model.MissionPatch
import com.seancoyle.feature.launch.domain.model.Pad
import com.seancoyle.feature.launch.domain.model.Rocket
import com.seancoyle.feature.launch.domain.model.VidUrl
import java.time.LocalDateTime

data class LaunchUI(
    val id: String,
    val missionName: String,
    val launchDate: String,
    val launchTime: String?,
    val launchDateTime: LocalDateTime?,
    val status: LaunchStatus,
    val windowEnd: String?,
    val windowStart: String?,
    val windowStartTime: String?,
    val windowEndTime: String?,
    val windowDuration: String?,
    val windowStartDateTime: LocalDateTime?,
    val windowEndDateTime: LocalDateTime?,
    val image: Image,
    val failReason: String?,
    val launchServiceProvider: Agency?,
    val rocket: Rocket?,
    val mission: Mission?,
    val pad: Pad?,
    val updates: List<LaunchUpdate>,
    val infoUrls: List<InfoUrl>,
    val vidUrls: List<VidUrl>,
    val missionPatches: List<MissionPatch>
)
