package com.seancoyle.feature.launch.util

import com.seancoyle.database.entities.AgencyEntity
import com.seancoyle.database.entities.ImageEntity
import com.seancoyle.database.entities.InfoUrlEntity
import com.seancoyle.database.entities.LaunchEntity
import com.seancoyle.database.entities.LaunchStatusEntity
import com.seancoyle.database.entities.MissionEntity
import com.seancoyle.database.entities.NetPrecisionEntity
import com.seancoyle.database.entities.PadEntity
import com.seancoyle.database.entities.ProgramEntity
import com.seancoyle.database.entities.RocketEntity
import com.seancoyle.feature.launch.domain.model.Agency
import com.seancoyle.feature.launch.domain.model.Image
import com.seancoyle.feature.launch.domain.model.Status
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
import com.seancoyle.database.entities.LaunchUpdateEntity
import com.seancoyle.database.entities.VidUrlEntity
import com.seancoyle.database.entities.MissionPatchEntity
import com.seancoyle.feature.launch.data.remote.InfoUrlDto
import com.seancoyle.feature.launch.data.remote.LaunchUpdateDto
import com.seancoyle.feature.launch.data.remote.MissionPatchDto
import com.seancoyle.feature.launch.data.remote.VidUrlDto
import com.seancoyle.feature.launch.domain.model.InfoUrl
import com.seancoyle.feature.launch.domain.model.Launch
import com.seancoyle.feature.launch.domain.model.LaunchUpdate
import com.seancoyle.feature.launch.domain.model.MissionPatch
import com.seancoyle.feature.launch.domain.model.VidUrl
import kotlin.random.Random

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
        agencyLaunchAttemptCountYear: Int? = null,
        updates: List<LaunchUpdateDto>? = null,
        infoUrls: List<InfoUrlDto>? = null,
        vidUrls: List<VidUrlDto>? = null,
        padTurnaround: String? = null,
        missionPatches: List<MissionPatchDto>? = null
    ) = LaunchDto(
        id = id,
        url = url,
        name = name,
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
        agencyLaunchAttemptCountYear = agencyLaunchAttemptCountYear,
        updates = updates,
        infoUrls = infoUrls,
        vidUrls = vidUrls,
        padTurnaround = padTurnaround,
        missionPatches = missionPatches
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

    fun createLaunchStatusEntity() = LaunchStatusEntity(
        id = 1,
        name = "Success",
        abbrev = "Success",
        description = "Current T-0 confirmed by official or reliable sources."
    )

    fun createImageEntity(
        id: Int? = 1296,
        name: String? = "Starlink night fairing",
        imageUrl: String = "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/falcon2520925_image_20221009234147.png",
        thumbnailUrl: String = "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305192320.png",
        credit: String? = "SpaceX"
    ) = ImageEntity(id, name, imageUrl, thumbnailUrl, credit)

    fun createLaunchEntity(
        id: String = "faf4a0bc-7dad-4842-b74c-73a9f648b5cc",
        url: String? = "https://lldev.thespacedevs.com/2.3.0/launches/faf4a0bc-7dad-4842-b74c-73a9f648b5cc/",
        name: String? = "Falcon 9 Block 5 | Starlink Group 15-12",
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
        status: LaunchStatusEntity? = null,
        updates: List<LaunchUpdateEntity>? = null,
        infoUrls: List<InfoUrlEntity>? = null,
        vidUrls: List<VidUrlEntity>? = null,
        padTurnaround: String? = null,
        missionPatches: List<MissionPatchEntity>? = null
    ) = LaunchEntity(
        id = id,
        url = url,
        name = name,
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
        updates = updates,
        infoUrls = infoUrls,
        vidUrls = vidUrls,
        padTurnaround = padTurnaround,
        missionPatches = missionPatches
    )

    // Domain Model Factory Functions

    fun createNetPrecision(
        id: Int? = 1,
        name: String? = "Minute",
        abbrev: String? = "MIN",
        description: String? = "The T-0 is accurate to the minute."
    ) = NetPrecision(id, name, abbrev, description)

    fun createStatus(
        id: Int? = 1,
        name: String? = "Success",
        abbrev: String? = "Success",
        description: String? = "Current T-0 confirmed by official or reliable sources."
    ) = Status(id, name, abbrev, description)

    fun createImage(
        id: Int? = 1296,
        name: String? = "Starlink night fairing",
        imageUrl: String = "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/falcon2520925_image_20221009234147.png",
        thumbnailUrl: String = "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305192320.png",
        credit: String? = "SpaceX"
    ) = Image(id, name, imageUrl, thumbnailUrl, credit)

    fun createLaunch(
        id: String = "faf4a0bc-7dad-4842-b74c-73a9f648b5cc",
        url: String? = "https://lldev.thespacedevs.com/2.3.0/launches/faf4a0bc-7dad-4842-b74c-73a9f648b5cc/",
        name: String? = "Falcon 9 Block 5 | Starlink Group 15-12",
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
        status: Status? = createStatus(),
        updates: List<LaunchUpdate>? = null,
        infoUrls: List<InfoUrl>? = null,
        vidUrls: List<VidUrl>? = null,
        padTurnaround: String? = null,
        missionPatches: List<MissionPatch>? = null
    ) = Launch(
        id = id,
        url = url,
        missionName = name,
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
        updates = updates,
        infoUrls = infoUrls,
        vidUrls = vidUrls,
        padTurnaround = padTurnaround,
        missionPatches = missionPatches
    )

    // Launch after all the business logic is applied from the use case
    fun createLaunchTransformed(
        id: String = "faf4a0bc-7dad-4842-b74c-73a9f648b5cc",
        url: String? = "https://lldev.thespacedevs.com/2.3.0/launches/faf4a0bc-7dad-4842-b74c-73a9f648b5cc/",
        name: String? = "Falcon 9 Block 5 | Starlink Group 15-12",
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
        status: Status? = createStatus(),
        updates: List<LaunchUpdate>? = null,
        infoUrls: List<InfoUrl>? = null,
        vidUrls: List<VidUrl>? = null,
        padTurnaround: String? = null,
        missionPatches: List<MissionPatch>? = null
    ) = Launch(
        id = id,
        url = url,
        missionName = name,
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
        updates = updates,
        infoUrls = infoUrls,
        vidUrls = vidUrls,
        padTurnaround = padTurnaround,
        missionPatches = missionPatches
    )

    fun createRandomLaunchList(num: Int): List<Launch> {
        val statusOptions = listOf(
            createStatus(id = 1, name = "Success", abbrev = "Success", description = "The launch was successful"),
            createStatus(id = 2, name = "Failed", abbrev = "Failed", description = "The launch failed"),
            createStatus(id = 3, name = "TBD", abbrev = "TBD", description = "To be determined"),
            createStatus(id = 4, name = "Go", abbrev = "Go", description = "Launch is go"),
        )
        return List(num) { i ->
            val status = statusOptions[Random.nextInt(statusOptions.size)]
            createLaunchTransformed(
                id = "launch_$i",
                name = "Launch $i",
                status = status
            )
        }
    }

    fun createLaunchUpdateEntity(
        id: Int = 1,
        profileImage: String = "https://example.com/profile.png",
        comment: String = "Test update comment",
        infoUrl: String = "https://example.com/info",
        createdBy: String = "TestUser",
        createdOn: String = "2025-12-31T12:00:00Z"
    ) = LaunchUpdateEntity(id, profileImage, comment, infoUrl, createdBy, createdOn)

    fun createVidUrlEntity(
        priority: Int = 10,
        source: String = "youtube.com",
        publisher: String = "TestPublisher",
        title: String = "Test Video",
        description: String = "Test video description",
        featureImage: String = "https://example.com/feature.jpg",
        url: String = "https://youtube.com/watch?v=123",
        startTime: String = "2025-12-31T12:00:00Z",
        endTime: String = "2025-12-31T13:00:00Z",
        live: Boolean = false
    ) = VidUrlEntity(
        priority = priority,
        source = source,
        publisher = publisher,
        title = title,
        description = description,
        featureImage = featureImage,
        url = url,
        startTime = startTime,
        endTime = endTime,
        live = live
    )

    fun createMissionPatchEntity(
        id: Int = 1,
        name: String = "PatchName",
        priority: Int = 10,
        imageUrl: String = "https://example.com/patch.png",
        agency: AgencyEntity? = null
    ) = MissionPatchEntity(id, name, priority, imageUrl, agency)

    fun createLaunchUpdate(
        id: Int = 1,
        profileImage: String = "https://example.com/profile.png",
        comment: String = "Test update comment",
        infoUrl: String = "https://example.com/info",
        createdBy: String = "TestUser",
        createdOn: String = "2025-12-31T12:00:00Z"
    ) = LaunchUpdate(
        id = id,
        profileImage = profileImage,
        comment = comment,
        infoUrl = infoUrl,
        createdBy = createdBy,
        createdOn = createdOn
    )

    fun createVidUrl(
        priority: Int = 10,
        source: String = "youtube.com",
        publisher: String = "TestPublisher",
        title: String = "Test Video",
        description: String = "Test video description",
        featureImage: String = "https://example.com/feature.jpg",
        url: String = "https://youtube.com/watch?v=123",
        startTime: String = "2025-12-31T12:00:00Z",
        endTime: String = "2025-12-31T13:00:00Z",
        live: Boolean = false
    ) = VidUrl(
        priority = priority,
        source = source,
        publisher = publisher,
        title = title,
        description = description,
        featureImage = featureImage,
        url = url,
        startTime = startTime,
        endTime = endTime,
        live = live
    )

    fun createMissionPatch(
        id: Int = 1,
        name: String = "PatchName",
        priority: Int = 10,
        imageUrl: String = "https://example.com/patch.png",
        agency: Agency? = null
    ) = MissionPatch(
        id = id,
        name = name,
        priority = priority,
        imageUrl = imageUrl,
        agency = agency
    )

    fun createMissionPatchDto(
        id: Int = 1,
        name: String = "PatchName",
        priority: Int = 10,
        imageUrl: String = "https://example.com/patch.png",
        agency: AgencyDto? = null
    ) = MissionPatchDto(
        id = id,
        name = name,
        priority = priority,
        imageUrl = imageUrl,
        agency = agency
    )

    fun createVidUrlDto(
        priority: Int = 10,
        source: String = "youtube.com",
        publisher: String = "TestPublisher",
        title: String = "Test Video",
        description: String = "Test video description",
        featureImage: String = "https://example.com/feature.jpg",
        url: String = "https://youtube.com/watch?v=123",
        startTime: String = "2025-12-31T12:00:00Z",
        endTime: String = "2025-12-31T13:00:00Z",
        live: Boolean = false
    ) = VidUrlDto(
        priority = priority,
        source = source,
        publisher = publisher,
        title = title,
        description = description,
        featureImage = featureImage,
        url = url,
        startTime = startTime,
        endTime = endTime,
        live = live
    )

    fun createLaunchUpdateDto(
        id: Int = 1,
        profileImage: String = "https://example.com/profile.png",
        comment: String = "Test update comment",
        infoUrl: String = "https://example.com/info",
        createdBy: String = "TestUser",
        createdOn: String = "2025-12-31T12:00:00Z"
    ) = LaunchUpdateDto(
        id = id,
        profileImage = profileImage,
        comment = comment,
        infoUrl = infoUrl,
        createdBy = createdBy,
        createdOn = createdOn
    )

    fun createInfoUrl(): InfoUrl {
        return InfoUrl(
            priority = 10,
            source = "spacex.com",
            title = "SpaceX",
            description = "SpaceX designs, manufactures and launches advanced rockets and spacecraft.",
            featureImage = null,
            url = "https://www.spacex.com/launches/cosmo-skymedfm3"
        )
    }

    fun createInfoUrlEntity(): InfoUrlEntity {
        return InfoUrlEntity(
            priority = 10,
            source = "spacex.com",
            title = "SpaceX",
            description = "SpaceX designs, manufactures and launches advanced rockets and spacecraft.",
            featureImage = null,
            url = "https://www.spacex.com/launches/cosmo-skymedfm3"
        )
    }

    fun createInfoUrlDto(): InfoUrlDto {
        return InfoUrlDto(
            priority = 10,
            source = "spacex.com",
            title = "SpaceX",
            description = "SpaceX designs, manufactures and launches advanced rockets and spacecraft.",
            featureImage = null,
            url = "https://www.spacex.com/launches/cosmo-skymedfm3"
        )
    }

}
