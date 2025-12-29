package com.seancoyle.feature.launch.util

import com.seancoyle.database.entities.AgencyEntity
import com.seancoyle.database.entities.ImageEntity
import com.seancoyle.database.entities.LaunchEntity
import com.seancoyle.database.entities.LaunchStatusEntity
import com.seancoyle.database.entities.MissionEntity
import com.seancoyle.database.entities.NetPrecisionEntity
import com.seancoyle.database.entities.PadEntity
import com.seancoyle.database.entities.ProgramEntity
import com.seancoyle.database.entities.RocketEntity
import com.seancoyle.feature.launch.domain.model.Agency
import com.seancoyle.feature.launch.domain.model.Image
import com.seancoyle.feature.launch.domain.model.LaunchStatus
import com.seancoyle.feature.launch.domain.model.Mission
import com.seancoyle.feature.launch.domain.model.NetPrecision
import com.seancoyle.feature.launch.domain.model.Pad
import com.seancoyle.feature.launch.domain.model.Program
import com.seancoyle.feature.launch.domain.model.Rocket
import com.seancoyle.feature.launch.data.remote.AgencyDto
import com.seancoyle.feature.launch.data.remote.ImageDto
import com.seancoyle.feature.launch.data.remote.LaunchDto
import com.seancoyle.feature.launch.data.remote.LaunchesDto
import com.seancoyle.feature.launch.data.remote.MissionDto
import com.seancoyle.feature.launch.data.remote.NetPrecisionDto
import com.seancoyle.feature.launch.data.remote.PadDto
import com.seancoyle.feature.launch.data.remote.ProgramDto
import com.seancoyle.feature.launch.data.remote.RocketDto
import com.seancoyle.feature.launch.data.remote.StatusDto
import com.seancoyle.feature.launch.domain.model.Launch
import java.time.LocalDateTime

internal object TestData {

    fun createStatusDto(
        id: Int? = 1,
        name: String? = "Success",
        abbrev: String? = "Success",
        description: String? = "Current T-0 confirmed by official or reliable sources."
    ) = StatusDto(id, name, abbrev, description)

    fun createNetPrecisionDto(
        id: Int? = 1,
        name: String? = "Minute",
        abbrev: String? = "MIN",
        description: String? = "The T-0 is accurate to the minute."
    ) = NetPrecisionDto(id, name, abbrev, description)

    fun createImageDto(
        id: Int? = 1296,
        name: String? = "Starlink night fairing",
        imageUrl: String? = "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/falcon2520925_image_20221009234147.png",
        thumbnailUrl: String? = "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305192320.png",
        credit: String? = "SpaceX"
    ) = ImageDto(id, name, imageUrl, thumbnailUrl, credit)

    fun createLaunchDto(
        id: String? = "faf4a0bc-7dad-4842-b74c-73a9f648b5cc",
        url: String? = "https://lldev.thespacedevs.com/2.3.0/launches/faf4a0bc-7dad-4842-b74c-73a9f648b5cc/",
        name: String? = "Falcon 9 Block 5 | Starlink Group 15-12",
        responseMode: String? = "list",
        status: StatusDto? = createStatusDto(),
        lastUpdated: String? = "2025-12-05T18:39:36Z",
        net: String? = "2025-12-13T05:34:00Z",
        netPrecision: NetPrecisionDto? = createNetPrecisionDto(),
        windowEnd: String? = "2025-12-13T09:34:00Z",
        windowStart: String? = "2025-12-13T05:34:00Z",
        image: ImageDto? = createImageDto(),
        infographic: String? = null,
        probability: Int? = null,
        weatherConcerns: String? = null,
        failReason: String? = null,
        launchServiceProvider: AgencyDto? = null,
        rocket: RocketDto? = null,
        mission: MissionDto? = null,
        pad: PadDto? = null,
        webcastLive: Boolean? = false,
        program: List<ProgramDto>? = null,
        orbitalLaunchAttemptCount: Int? = null,
        locationLaunchAttemptCount: Int? = null,
        padLaunchAttemptCount: Int? = null,
        agencyLaunchAttemptCount: Int? = null,
        orbitalLaunchAttemptCountYear: Int? = null,
        locationLaunchAttemptCountYear: Int? = null,
        padLaunchAttemptCountYear: Int? = null,
        agencyLaunchAttemptCountYear: Int? = null
    ) = LaunchDto(
        id = id,
        url = url,
        name = name,
        responseMode = responseMode,
        status = status,
        lastUpdated = lastUpdated,
        net = net,
        netPrecision = netPrecision,
        windowEnd = windowEnd,
        windowStart = windowStart,
        image = image,
        infographic = infographic,
        probability = probability,
        weatherConcerns = weatherConcerns,
        failReason = failReason,
        launchServiceProvider = launchServiceProvider,
        rocket = rocket,
        mission = mission,
        pad = pad,
        webcastLive = webcastLive,
        program = program,
        orbitalLaunchAttemptCount = orbitalLaunchAttemptCount,
        locationLaunchAttemptCount = locationLaunchAttemptCount,
        padLaunchAttemptCount = padLaunchAttemptCount,
        agencyLaunchAttemptCount = agencyLaunchAttemptCount,
        orbitalLaunchAttemptCountYear = orbitalLaunchAttemptCountYear,
        locationLaunchAttemptCountYear = locationLaunchAttemptCountYear,
        padLaunchAttemptCountYear = padLaunchAttemptCountYear,
        agencyLaunchAttemptCountYear = agencyLaunchAttemptCountYear
    )

    fun createLaunchesDto(
        count: Int? = 109,
        next: String? = "https://lldev.thespacedevs.com/2.3.0/launches/upcoming/?limit=10&mode=list&offset=10&ordering=net",
        previous: String? = null,
        results: List<LaunchDto>? = listOf(createLaunchDto())
    ) = LaunchesDto(count, next, previous, results)


    // Entity Factory Functions

    fun createNetPrecisionEntity(
        id: Int? = 1,
        name: String? = "Minute",
        abbrev: String? = "MIN",
        description: String? = "The T-0 is accurate to the minute."
    ) = NetPrecisionEntity(id, name, abbrev, description)

    fun createImageEntity(
        id: Int? = 1296,
        name: String? = "Starlink night fairing",
        imageUrl: String = "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/falcon2520925_image_20221009234147.png",
        thumbnailUrl: String = "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305192320.png",
        credit: String? = "SpaceX"
    ) = ImageEntity(id, name, imageUrl, thumbnailUrl, credit)

    fun createLaunchEntity(
        id: String = "faf4a0bc-7dad-4842-b74c-73a9f648b5cc",
        count: Int = 100,
        url: String? = "https://lldev.thespacedevs.com/2.3.0/launches/faf4a0bc-7dad-4842-b74c-73a9f648b5cc/",
        name: String? = "Falcon 9 Block 5 | Starlink Group 15-12",
        responseMode: String? = "list",
        lastUpdated: String? = "2025-12-05T18:39:36Z",
        net: String? = "2025-12-13T05:34:00Z",
        netPrecision: NetPrecisionEntity? = createNetPrecisionEntity(),
        windowEnd: String? = "2025-12-13T09:34:00Z",
        windowStart: String? = "2025-12-13T05:34:00Z",
        image: ImageEntity = createImageEntity(),
        infographic: String? = null,
        probability: Int? = null,
        weatherConcerns: String? = null,
        failReason: String? = null,
        launchServiceProvider: AgencyEntity? = null,
        rocket: RocketEntity? = null,
        mission: MissionEntity? = null,
        pad: PadEntity? = null,
        webcastLive: Boolean? = false,
        program: List<ProgramEntity>? = null,
        orbitalLaunchAttemptCount: Int? = null,
        locationLaunchAttemptCount: Int? = null,
        padLaunchAttemptCount: Int? = null,
        agencyLaunchAttemptCount: Int? = null,
        orbitalLaunchAttemptCountYear: Int? = null,
        locationLaunchAttemptCountYear: Int? = null,
        padLaunchAttemptCountYear: Int? = null,
        agencyLaunchAttemptCountYear: Int? = null,
        status: LaunchStatusEntity = LaunchStatusEntity.SUCCESS,
    ) = LaunchEntity(
        id = id,
        count = count,
        url = url,
        name = name,
        responseMode = responseMode,
        lastUpdated = lastUpdated,
        net = net,
        netPrecision = netPrecision,
        windowEnd = windowEnd,
        windowStart = windowStart,
        image = image,
        infographic = infographic,
        probability = probability,
        weatherConcerns = weatherConcerns,
        failReason = failReason,
        launchServiceProvider = launchServiceProvider,
        rocket = rocket,
        mission = mission,
        pad = pad,
        webcastLive = webcastLive,
        program = program,
        orbitalLaunchAttemptCount = orbitalLaunchAttemptCount,
        locationLaunchAttemptCount = locationLaunchAttemptCount,
        padLaunchAttemptCount = padLaunchAttemptCount,
        agencyLaunchAttemptCount = agencyLaunchAttemptCount,
        orbitalLaunchAttemptCountYear = orbitalLaunchAttemptCountYear,
        locationLaunchAttemptCountYear = locationLaunchAttemptCountYear,
        padLaunchAttemptCountYear = padLaunchAttemptCountYear,
        agencyLaunchAttemptCountYear = agencyLaunchAttemptCountYear,
        status = status,
    )

    // Domain Model Factory Functions

    fun createNetPrecision(
        id: Int? = 1,
        name: String? = "Minute",
        abbrev: String? = "MIN",
        description: String? = "The T-0 is accurate to the minute."
    ) = NetPrecision(id, name, abbrev, description)

    fun createImage(
        id: Int? = 1296,
        name: String? = "Starlink night fairing",
        imageUrl: String = "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/falcon2520925_image_20221009234147.png",
        thumbnailUrl: String = "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305192320.png",
        credit: String? = "SpaceX"
    ) = Image(id, name, imageUrl, thumbnailUrl, credit)

    fun createLaunch(
        id: String = "faf4a0bc-7dad-4842-b74c-73a9f648b5cc",
        count: Int = 100,
        url: String? = "https://lldev.thespacedevs.com/2.3.0/launches/faf4a0bc-7dad-4842-b74c-73a9f648b5cc/",
        name: String? = "Falcon 9 Block 5 | Starlink Group 15-12",
        responseMode: String? = "list",
        lastUpdated: String? = "2025-12-05T18:39:36Z",
        net: String? = "2025-12-13T05:34:00Z",
        netPrecision: NetPrecision? = createNetPrecision(),
        windowEnd: String? = "2025-12-13T09:34:00Z",
        windowStart: String? = "2025-12-13T05:34:00Z",
        image: Image = createImage(),
        infographic: String? = null,
        probability: Int? = null,
        weatherConcerns: String? = null,
        failReason: String? = null,
        launchServiceProvider: Agency? = null,
        rocket: Rocket? = null,
        mission: Mission? = null,
        pad: Pad? = null,
        webcastLive: Boolean? = false,
        program: List<Program>? = null,
        orbitalLaunchAttemptCount: Int? = null,
        locationLaunchAttemptCount: Int? = null,
        padLaunchAttemptCount: Int? = null,
        agencyLaunchAttemptCount: Int? = null,
        orbitalLaunchAttemptCountYear: Int? = null,
        locationLaunchAttemptCountYear: Int? = null,
        padLaunchAttemptCountYear: Int? = null,
        agencyLaunchAttemptCountYear: Int? = null,
        status: LaunchStatus = LaunchStatus.UNKNOWN,
    ) = Launch(
        id = id,
        count = count,
        url = url,
        name = name,
        responseMode = responseMode,
        lastUpdated = lastUpdated,
        net = net,
        netPrecision = netPrecision,
        windowEnd = windowEnd,
        windowStart = windowStart,
        image = image,
        infographic = infographic,
        probability = probability,
        weatherConcerns = weatherConcerns,
        failReason = failReason,
        launchServiceProvider = launchServiceProvider,
        rocket = rocket,
        mission = mission,
        pad = pad,
        webcastLive = webcastLive,
        program = program,
        orbitalLaunchAttemptCount = orbitalLaunchAttemptCount,
        locationLaunchAttemptCount = locationLaunchAttemptCount,
        padLaunchAttemptCount = padLaunchAttemptCount,
        agencyLaunchAttemptCount = agencyLaunchAttemptCount,
        orbitalLaunchAttemptCountYear = orbitalLaunchAttemptCountYear,
        locationLaunchAttemptCountYear = locationLaunchAttemptCountYear,
        padLaunchAttemptCountYear = padLaunchAttemptCountYear,
        agencyLaunchAttemptCountYear = agencyLaunchAttemptCountYear,
        status = status,
    )

    // Launch after all the business logic is applied from the use case
    fun createLaunchTransformed(
        id: String = "faf4a0bc-7dad-4842-b74c-73a9f648b5cc",
        count: Int = 100,
        url: String? = "https://lldev.thespacedevs.com/2.3.0/launches/faf4a0bc-7dad-4842-b74c-73a9f648b5cc/",
        name: String? = "Falcon 9 Block 5 | Starlink Group 15-12",
        responseMode: String? = "list",
        lastUpdated: String? = "2025-12-05T18:39:36Z",
        net: String? = "2025-12-13T05:34:00Z",
        netPrecision: NetPrecision? = createNetPrecision(),
        windowEnd: String? = "2025-12-13T09:34:00Z",
        windowStart: String? = "2025-12-13T05:34:00Z",
        image: Image = createImage(),
        infographic: String? = null,
        probability: Int? = null,
        weatherConcerns: String? = null,
        failReason: String? = null,
        launchServiceProvider: Agency? = null,
        rocket: Rocket? = null,
        mission: Mission? = null,
        pad: Pad? = null,
        webcastLive: Boolean? = false,
        program: List<Program>? = null,
        orbitalLaunchAttemptCount: Int? = null,
        locationLaunchAttemptCount: Int? = null,
        padLaunchAttemptCount: Int? = null,
        agencyLaunchAttemptCount: Int? = null,
        orbitalLaunchAttemptCountYear: Int? = null,
        locationLaunchAttemptCountYear: Int? = null,
        padLaunchAttemptCountYear: Int? = null,
        agencyLaunchAttemptCountYear: Int? = null,
        status: LaunchStatus = LaunchStatus.SUCCESS,
    ) = Launch(
        id = id,
        count = count,
        url = url,
        name = name,
        responseMode = responseMode,
        lastUpdated = lastUpdated,
        net = net,
        netPrecision = netPrecision,
        windowEnd = windowEnd,
        windowStart = windowStart,
        image = image,
        infographic = infographic,
        probability = probability,
        weatherConcerns = weatherConcerns,
        failReason = failReason,
        launchServiceProvider = launchServiceProvider,
        rocket = rocket,
        mission = mission,
        pad = pad,
        webcastLive = webcastLive,
        program = program,
        orbitalLaunchAttemptCount = orbitalLaunchAttemptCount,
        locationLaunchAttemptCount = locationLaunchAttemptCount,
        padLaunchAttemptCount = padLaunchAttemptCount,
        agencyLaunchAttemptCount = agencyLaunchAttemptCount,
        orbitalLaunchAttemptCountYear = orbitalLaunchAttemptCountYear,
        locationLaunchAttemptCountYear = locationLaunchAttemptCountYear,
        padLaunchAttemptCountYear = padLaunchAttemptCountYear,
        agencyLaunchAttemptCountYear = agencyLaunchAttemptCountYear,
        status = status,
    )

}
