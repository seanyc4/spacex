package com.seancoyle.feature.launch.util

import com.seancoyle.database.entities.*
import com.seancoyle.feature.launch.domain.model.*
import com.seancoyle.feature.launch.data.remote.*
import kotlin.random.Random

internal object TestData {

    fun createStatusDto(
        id: Int = 1,
        name: String = "Go for Launch",
        abbrev: String = "Go",
        description: String = "Current T-0 confirmed by official or reliable sources."
    ) = StatusDto(
        id = id,
        name = name,
        abbrev = abbrev,
        description = description
    )

    fun createNetPrecisionDto(
        id: Int = 1,
        name: String = "Minute",
        abbrev: String = "MIN",
        description: String = "The T-0 is accurate to the minute."
    ) = NetPrecisionDto(
        id = id,
        name = name,
        abbrev = abbrev,
        description = description
    )

    fun createImageDto(
        id: Int = 1296,
        name: String = "Starlink night fairing",
        imageUrl: String = "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/falcon2520925_image_20221009234147.png",
        thumbnailUrl: String = "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305192320.png",
        credit: String = "SpaceX"
    ) = ImageDto(
        id = id,
        name = name,
        imageUrl = imageUrl,
        thumbnailUrl = thumbnailUrl,
        credit = credit
    )

    fun createTypeDto(
        id: Int = 1,
        name: String = "Government"
    ) = TypeDto(
        id = id,
        name = name
    )

    fun createCountryDto(
        id: Int = 1,
        name: String = "United States",
        alpha2Code: String = "US",
        alpha3Code: String = "USA",
        nationalityName: String = "American",
        nationalityNameComposed: String = "American"
    ) = CountryDto(
        id = id,
        name = name,
        alpha2Code = alpha2Code,
        alpha3Code = alpha3Code,
        nationalityName = nationalityName,
        nationalityNameComposed = nationalityNameComposed
    )

    fun createAgencyDto(
        responseMode: String = "normal",
        id: Int? = 121,
        url: String? = "https://lldev.thespacedevs.com/2.3.0/agencies/121/",
        name: String? = "SpaceX",
        abbrev: String = "SpX",
        type: TypeDto? = createTypeDto(),
        featured: Boolean = true,
        country: List<CountryDto>? = listOf(createCountryDto()),
        description: String = "Space Exploration Technologies Corp., known as SpaceX, is an American aerospace manufacturer and space transport services company headquartered in Hawthorne, California.",
        administrator: String = "Elon Musk",
        foundingYear: Int = 2002,
        launchers: String = "Falcon 9, Falcon Heavy, Starship",
        spacecraft: String = "Dragon, Crew Dragon, Starship",
        image: ImageDto? = createImageDto(),
        totalLaunchCount: Int = 234,
        consecutiveSuccessfulLaunches: Int = 89,
        successfulLaunches: Int = 220,
        failedLaunches: Int = 2,
        pendingLaunches: Int = 12,
        consecutiveSuccessfulLandings: Int = 45,
        successfulLandings: Int = 180,
        failedLandings: Int = 8,
        attemptedLandings: Int = 188,
        successfulLandingsSpacecraft: Int = 50,
        failedLandingsSpacecraft: Int = 2,
        attemptedLandingsSpacecraft: Int = 52,
        successfulLandingsPayload: Int = 175,
        failedLandingsPayload: Int = 5,
        attemptedLandingsPayload: Int = 180,
        infoUrl: String = "https://www.spacex.com",
        wikiUrl: String = "https://en.wikipedia.org/wiki/SpaceX"
    ) = AgencyDto(
        responseMode = responseMode,
        id = id,
        url = url,
        name = name,
        abbrev = abbrev,
        type = type,
        featured = featured,
        country = country,
        description = description,
        administrator = administrator,
        foundingYear = foundingYear,
        launchers = launchers,
        spacecraft = spacecraft,
        image = image,
        totalLaunchCount = totalLaunchCount,
        consecutiveSuccessfulLaunches = consecutiveSuccessfulLaunches,
        successfulLaunches = successfulLaunches,
        failedLaunches = failedLaunches,
        pendingLaunches = pendingLaunches,
        consecutiveSuccessfulLandings = consecutiveSuccessfulLandings,
        successfulLandings = successfulLandings,
        failedLandings = failedLandings,
        attemptedLandings = attemptedLandings,
        successfulLandingsSpacecraft = successfulLandingsSpacecraft,
        failedLandingsSpacecraft = failedLandingsSpacecraft,
        attemptedLandingsSpacecraft = attemptedLandingsSpacecraft,
        successfulLandingsPayload = successfulLandingsPayload,
        failedLandingsPayload = failedLandingsPayload,
        attemptedLandingsPayload = attemptedLandingsPayload,
        infoUrl = infoUrl,
        wikiUrl = wikiUrl
    )

    fun createFamilyDto(
        responseMode: String = "normal",
        id: Int = 1,
        name: String = "Falcon",
        manufacturer: List<AgencyDto> = listOf(createAgencyDto()),
        description: String? = "Falcon rocket family",
        active: Boolean = true,
        maidenFlight: String = "2010-06-04T18:45:00Z",
        totalLaunchCount: Int = 300,
        consecutiveSuccessfulLaunches: Int = 250,
        successfulLaunches: Int = 290,
        failedLaunches: Int = 10,
        pendingLaunches: Int = 50,
        attemptedLandings: Int = 200,
        successfulLandings: Int = 190,
        failedLandings: Int = 10,
        consecutiveSuccessfulLandings: Int = 150
    ) = FamilyDto(
        responseMode = responseMode,
        id = id,
        name = name,
        manufacturer = manufacturer,
        description = description,
        active = active,
        maidenFlight = maidenFlight,
        totalLaunchCount = totalLaunchCount,
        consecutiveSuccessfulLaunches = consecutiveSuccessfulLaunches,
        successfulLaunches = successfulLaunches,
        failedLaunches = failedLaunches,
        pendingLaunches = pendingLaunches,
        attemptedLandings = attemptedLandings,
        successfulLandings = successfulLandings,
        failedLandings = failedLandings,
        consecutiveSuccessfulLandings = consecutiveSuccessfulLandings
    )

    fun createConfigurationDto(
        id: Int = 164,
        url: String = "https://lldev.thespacedevs.com/2.3.0/config/launcher/164/",
        name: String = "Falcon 9",
        families: List<FamilyDto> = listOf(createFamilyDto()),
        fullName: String = "Falcon 9 Block 5",
        variant: String = "Block 5",
        manufacturer: AgencyDto? = createAgencyDto(),
        image: ImageDto? = createImageDto(),
        wikiUrl: String? = "https://en.wikipedia.org/wiki/Falcon_9",
        description: String? = "Falcon 9 is a reusable, two-stage rocket designed and manufactured by SpaceX.",
        alias: String? = "F9",
        totalLaunchCount: Int? = 300,
        successfulLaunches: Int? = 290,
        failedLaunches: Int? = 10
    ) = ConfigurationDto(
        id = id,
        url = url,
        name = name,
        families = families,
        fullName = fullName,
        variant = variant,
        manufacturer = manufacturer,
        image = image,
        wikiUrl = wikiUrl,
        description = description,
        alias = alias,
        totalLaunchCount = totalLaunchCount,
        successfulLaunches = successfulLaunches,
        failedLaunches = failedLaunches
    )

    fun createOrbitDto(
        id: Int = 8,
        name: String = "Low Earth Orbit",
        abbrev: String = "LEO"
    ) = OrbitDto(
        id = id,
        name = name,
        abbrev = abbrev
    )

    fun createMissionDto(
        id: Int = 6319,
        name: String = "Starlink Group 15-12",
        type: String = "Communications",
        description: String = "A batch of satellites for the Starlink mega-constellation - SpaceX's project for space-based Internet communication system.",
        orbit: OrbitDto? = createOrbitDto(),
        agencies: List<AgencyDto>? = listOf(createAgencyDto()),
        infoUrls: List<InfoUrlDto> = listOf(createInfoUrlDto()),
        vidUrls: List<VidUrlDto> = listOf(createVidUrlDto())
    ) = MissionDto(
        id = id,
        name = name,
        type = type,
        description = description,
        orbit = orbit,
        agencies = agencies,
        infoUrls = infoUrls,
        vidUrls = vidUrls
    )

    fun createLocationDto(
        id: Int = 27,
        url: String = "https://lldev.thespacedevs.com/2.3.0/location/27/",
        name: String = "Kennedy Space Center, FL, USA",
        active: Boolean = true,
        country: CountryDto = createCountryDto(),
        description: String = "Kennedy Space Center is one of the most historic launch sites in the US.",
        image: ImageDto = createImageDto(),
        mapImage: String = "https://example.com/map.png",
        longitude: Double = -80.6039558,
        latitude: Double = 28.5721194,
        timezoneName: String = "America/New_York",
        totalLaunchCount: Int = 500,
        totalLandingCount: Int = 120
    ) = LocationDto(
        id = id,
        url = url,
        name = name,
        active = active,
        country = country,
        description = description,
        image = image,
        mapImage = mapImage,
        longitude = longitude,
        latitude = latitude,
        timezoneName = timezoneName,
        totalLaunchCount = totalLaunchCount,
        totalLandingCount = totalLandingCount
    )

    fun createPadDto(
        id: Int = 87,
        url: String = "https://lldev.thespacedevs.com/2.3.0/pad/87/",
        active: Boolean = true,
        agencies: List<AgencyDto> = listOf(createAgencyDto()),
        name: String = "Launch Complex 39A",
        image: ImageDto = createImageDto(),
        description: String = "Launch Complex 39A is a launch pad at Kennedy Space Center.",
        infoUrl: String = "https://www.nasa.gov",
        wikiUrl: String = "https://en.wikipedia.org/wiki/Kennedy_Space_Center_Launch_Complex_39A",
        mapUrl: String = "https://maps.google.com",
        latitude: Double = 28.6080585,
        longitude: Double = -80.6039558,
        country: CountryDto = createCountryDto(),
        mapImage: String = "https://example.com/map.png",
        totalLaunchCount: Int = 150,
        orbitalLaunchAttemptCount: Int = 140,
        fastestTurnaround: String = "PT51H",
        location: LocationDto = createLocationDto()
    ) = PadDto(
        id = id,
        url = url,
        active = active,
        agencies = agencies,
        name = name,
        image = image,
        description = description,
        infoUrl = infoUrl,
        wikiUrl = wikiUrl,
        mapUrl = mapUrl,
        latitude = latitude,
        longitude = longitude,
        country = country,
        mapImage = mapImage,
        totalLaunchCount = totalLaunchCount,
        orbitalLaunchAttemptCount = orbitalLaunchAttemptCount,
        fastestTurnaround = fastestTurnaround,
        location = location
    )

    fun createProgramDto(
        id: Int = 25,
        url: String = "https://lldev.thespacedevs.com/2.3.0/program/25/",
        name: String = "Starlink",
        image: ImageDto = createImageDto(),
        infoUrl: String = "https://www.starlink.com",
        wikiUrl: String = "https://en.wikipedia.org/wiki/Starlink",
        description: String = "Starlink is a satellite internet constellation being constructed by SpaceX.",
        agencies: List<AgencyDto> = listOf(createAgencyDto()),
        startDate: String = "2019-05-24T02:30:00Z",
        endDate: String = "2030-12-31T23:59:59Z",
        missionPatches: List<MissionPatchDto> = listOf(createMissionPatchDto()),
        type: TypeDto = createTypeDto()
    ) = ProgramDto(
        id = id,
        url = url,
        name = name,
        image = image,
        infoUrl = infoUrl,
        wikiUrl = wikiUrl,
        description = description,
        agencies = agencies,
        startDate = startDate,
        endDate = endDate,
        missionPatches = missionPatches,
        type = type
    )

    fun createInfoUrlDto(
        priority: Int = 10,
        source: String = "spacex.com",
        title: String = "SpaceX",
        description: String = "SpaceX designs, manufactures and launches advanced rockets and spacecraft.",
        featureImage: String = "https://www.spacex.com/static/images/share.jpg",
        url: String = "https://www.spacex.com/launches/cosmo-skymedfm3"
    ) = InfoUrlDto(
        priority = priority,
        source = source,
        title = title,
        description = description,
        featureImage = featureImage,
        url = url
    )

    fun createLaunchUpdateDto(
        id: Int = 1,
        profileImage: String = "https://example.com/profile.png",
        comment: String = "Launch is proceeding nominally. All systems are go.",
        infoUrl: String = "https://example.com/info",
        createdBy: String = "SpaceX Official",
        createdOn: String = "2025-12-31T12:00:00Z"
    ) = LaunchUpdateDto(
        id = id,
        profileImage = profileImage,
        comment = comment,
        infoUrl = infoUrl,
        createdBy = createdBy,
        createdOn = createdOn
    )

    fun createVidUrlDto(
        priority: Int = 10,
        source: String = "youtube.com",
        publisher: String = "SpaceX",
        title: String = "Starlink Mission",
        description: String = "Live coverage of the Starlink launch.",
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

    fun createMissionPatchDto(
        id: Int = 1,
        name: String = "Starlink Patch",
        priority: Int = 10,
        imageUrl: String = "https://example.com/patch.png",
        agency: AgencyDto = createAgencyDto()
    ) = MissionPatchDto(
        id = id,
        name = name,
        priority = priority,
        imageUrl = imageUrl,
        agency = agency
    )

    fun createLandingLocationDto(
        id: Int = 1,
        name: String = "Of Course I Still Love You",
        abbrev: String = "OCISLY",
        description: String = "Autonomous spaceport drone ship"
    ) = LandingLocationDto(
        id = id,
        name = name,
        abbrev = abbrev,
        description = description
    )

    fun createLandingTypeDto(
        id: Int = 1,
        name: String = "Autonomous Spaceport Drone Ship",
        abbrev: String = "ASDS",
        description: String = "Autonomous drone ship for rocket recovery at sea"
    ) = LandingTypeDto(
        id = id,
        name = name,
        abbrev = abbrev,
        description = description
    )

    fun createLandingDto(
        id: Int = 1,
        attempt: Boolean = true,
        success: Boolean = true,
        description: String = "The first stage successfully landed on the droneship.",
        location: LandingLocationDto = createLandingLocationDto(),
        type: LandingTypeDto = createLandingTypeDto()
    ) = LandingDto(
        id = id,
        attempt = attempt,
        success = success,
        description = description,
        location = location,
        type = type
    )

    fun createPreviousFlightDto(
        id: String = "prev-flight-123",
        name: String = "Starlink Group 14-8"
    ) = PreviousFlightDto(
        id = id,
        name = name
    )

    fun createLauncherDto(
        id: Int = 123,
        url: String = "https://lldev.thespacedevs.com/2.3.0/launcher/123/",
        flightProven: Boolean = true,
        serialNumber: String = "B1062",
        status: StatusDto = createStatusDto(),
        details: String = "Falcon 9 first stage booster",
        image: ImageDto = createImageDto(),
        successfulLandings: Int = 8,
        attemptedLandings: Int = 8,
        flights: Int = 8,
        lastLaunchDate: String = "2025-11-15T10:30:00Z",
        firstLaunchDate: String = "2020-06-13T09:21:00Z"
    ) = LauncherDto(
        id = id,
        url = url,
        flightProven = flightProven,
        serialNumber = serialNumber,
        status = status,
        details = details,
        image = image,
        successfulLandings = successfulLandings,
        attemptedLandings = attemptedLandings,
        flights = flights,
        lastLaunchDate = lastLaunchDate,
        firstLaunchDate = firstLaunchDate
    )

    fun createLauncherStageDto(
        id: Int = 1,
        type: String = "Core",
        reused: Boolean = true,
        launcherFlightNumber: Int = 8,
        launcher: LauncherDto = createLauncherDto(),
        landing: LandingDto = createLandingDto(),
        previousFlightDate: String = "2025-11-15T10:30:00Z",
        turnAroundTime: String = "PT720H",
        previousFlight: PreviousFlightDto = createPreviousFlightDto()
    ) = LauncherStageDto(
        id = id,
        type = type,
        reused = reused,
        launcherFlightNumber = launcherFlightNumber,
        launcher = launcher,
        landing = landing,
        previousFlightDate = previousFlightDate,
        turnAroundTime = turnAroundTime,
        previousFlight = previousFlight
    )

    fun createSpacecraftStatusDto(
        id: Int = 1,
        name: String = "Active"
    ) = SpacecraftStatusDto(
        id = id,
        name = name
    )

    fun createSpacecraftTypeDto(
        id: Int = 1,
        name: String = "Cargo"
    ) = SpacecraftTypeDto(
        id = id,
        name = name
    )

    fun createSpacecraftConfigDto(
        id: Int = 3,
        url: String = "https://lldev.thespacedevs.com/2.3.0/config/spacecraft/3/",
        name: String = "Dragon 2",
        type: SpacecraftTypeDto = createSpacecraftTypeDto(),
        agency: AgencyDto = createAgencyDto(),
        inUse: Boolean = true,
        capability: String = "Cargo and Crew Transport",
        history: String = "Dragon 2 is a class of reusable spacecraft developed and manufactured by American aerospace manufacturer SpaceX.",
        details: String = "Dragon 2 can carry up to 7 passengers to and from Earth orbit, and beyond.",
        maidenFlight: String = "2019-03-02T07:49:00Z",
        height: Double = 8.1,
        diameter: Double = 4.0,
        humanRated: Boolean = true,
        crewCapacity: Int = 7
    ) = SpacecraftConfigDto(
        id = id,
        url = url,
        name = name,
        type = type,
        agency = agency,
        inUse = inUse,
        capability = capability,
        history = history,
        details = details,
        maidenFlight = maidenFlight,
        height = height,
        diameter = diameter,
        humanRated = humanRated,
        crewCapacity = crewCapacity
    )

    fun createSpacecraftDto(
        id: Int = 45,
        url: String = "https://lldev.thespacedevs.com/2.3.0/spacecraft/45/",
        name: String = "Dragon C208",
        serialNumber: String = "C208",
        status: SpacecraftStatusDto = createSpacecraftStatusDto(),
        description: String = "Dragon spacecraft for cargo missions",
        spacecraftConfig: SpacecraftConfigDto = createSpacecraftConfigDto()
    ) = SpacecraftDto(
        id = id,
        url = url,
        name = name,
        serialNumber = serialNumber,
        status = status,
        description = description,
        spacecraftConfig = spacecraftConfig
    )

    fun createSpacecraftStageDto(
        id: Int = 1,
        url: String = "https://lldev.thespacedevs.com/2.3.0/spacecraft_stage/1/",
        destination: String = "International Space Station",
        missionEnd: String = "2026-01-15T12:00:00Z",
        spacecraft: SpacecraftDto = createSpacecraftDto(),
        landing: LandingDto = createLandingDto()
    ) = SpacecraftStageDto(
        id = id,
        url = url,
        destination = destination,
        missionEnd = missionEnd,
        spacecraft = spacecraft,
        landing = landing
    )

    fun createRocketDto(
        id: Int = 7815,
        configuration: ConfigurationDto = createConfigurationDto(),
        launcherStage: List<LauncherStageDto> = listOf(createLauncherStageDto()),
        spacecraftStage: List<SpacecraftStageDto> = listOf(createSpacecraftStageDto())
    ) = RocketDto(
        id = id,
        configuration = configuration,
        launcherStage = launcherStage,
        spacecraftStage = spacecraftStage
    )

    fun createLaunchDto(
        id: String? = "faf4a0bc-7dad-4842-b74c-73a9f648b5cc",
        url: String = "https://lldev.thespacedevs.com/2.3.0/launches/faf4a0bc-7dad-4842-b74c-73a9f648b5cc/",
        name: String? = "Falcon 9 Block 5 | Starlink Group 15-12",
        status: StatusDto? = createStatusDto(),
        lastUpdated: String = "2025-12-05T18:39:36Z",
        net: String? = "2025-12-13T05:34:00Z",
        netPrecision: NetPrecisionDto? = createNetPrecisionDto(),
        windowEnd: String = "2025-12-13T09:34:00Z",
        windowStart: String = "2025-12-13T05:34:00Z",
        image: ImageDto? = createImageDto(),
        infographic: String = "https://example.com/infographic.jpg",
        probability: Int = 90,
        weatherConcerns: String = "10% chance of unfavorable winds",
        failReason: String = "Failed",
        launchServiceProvider: AgencyDto? = createAgencyDto(),
        rocket: RocketDto? = createRocketDto(),
        mission: MissionDto? = createMissionDto(),
        pad: PadDto? = createPadDto(),
        webcastLive: Boolean = false,
        program: List<ProgramDto>? = listOf(createProgramDto()),
        orbitalLaunchAttemptCount: Int = 6789,
        locationLaunchAttemptCount: Int = 456,
        padLaunchAttemptCount: Int = 234,
        agencyLaunchAttemptCount: Int = 234,
        orbitalLaunchAttemptCountYear: Int = 123,
        locationLaunchAttemptCountYear: Int = 45,
        padLaunchAttemptCountYear: Int = 23,
        agencyLaunchAttemptCountYear: Int = 67,
        updates: List<LaunchUpdateDto>? = listOf(createLaunchUpdateDto()),
        infoUrls: List<InfoUrlDto>? = listOf(createInfoUrlDto()),
        vidUrls: List<VidUrlDto>? = listOf(createVidUrlDto()),
        padTurnaround: String = "PT240H",
        missionPatches: List<MissionPatchDto>? = listOf(createMissionPatchDto()),
        launcherStage: List<LauncherStageDto>? = listOf(createLauncherStageDto()),
        spacecraftStage: List<SpacecraftStageDto>? = listOf(createSpacecraftStageDto())
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
        missionPatches = missionPatches,
        launcherStage = launcherStage,
        spacecraftStage = spacecraftStage
    )

    fun createLaunchesDto(
        count: Int = 109,
        next: String = "https://lldev.thespacedevs.com/2.3.0/launches/upcoming/?limit=10&mode=list&offset=10&ordering=net",
        previous: String = "",
        results: List<LaunchDto> = listOf(createLaunchDto())
    ) = LaunchesDto(
        count = count,
        next = next,
        previous = previous,
        results = results
    )


    // Entity Factory Functions

    fun createNetPrecisionEntity(
        id: Int = 1,
        name: String = "Minute",
        abbrev: String = "MIN",
        description: String = "The T-0 is accurate to the minute."
    ) = NetPrecisionEntity(
        id = id,
        name = name,
        abbrev = abbrev,
        description = description
    )

    fun createLaunchStatusEntity(
        id: Int = 1,
        name: String = "Success",
        abbrev: String = "Success",
        description: String = "Current T-0 confirmed by official or reliable sources."
    ) = LaunchStatusEntity(
        id = id,
        name = name,
        abbrev = abbrev,
        description = description
    )

    fun createImageEntity(
        id: Int = 1296,
        name: String = "Starlink night fairing",
        imageUrl: String = "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/falcon2520925_image_20221009234147.png",
        thumbnailUrl: String = "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305192320.png",
        credit: String = "SpaceX"
    ) = ImageEntity(
        id = id,
        name = name,
        imageUrl = imageUrl,
        thumbnailUrl = thumbnailUrl,
        credit = credit
    )

    fun createCountryEntity(
        id: Int = 1,
        name: String = "United States",
        alpha2Code: String = "US",
        alpha3Code: String = "USA",
        nationalityName: String = "American"
    ) = CountryEntity(
        id = id,
        name = name,
        alpha2Code = alpha2Code,
        alpha3Code = alpha3Code,
        nationalityName = nationalityName
    )

    fun createAgencyEntity(
        id: Int = 121,
        url: String = "https://lldev.thespacedevs.com/2.3.0/agencies/121/",
        name: String = "SpaceX",
        abbrev: String = "SpX",
        type: String = "Government",
        featured: Boolean = true,
        country: List<CountryEntity> = listOf(createCountryEntity()),
        description: String = "Space Exploration Technologies Corp., known as SpaceX, is an American aerospace manufacturer and space transport services company headquartered in Hawthorne, California.",
        administrator: String = "Elon Musk",
        foundingYear: Int = 2002,
        launchers: String = "Falcon 9, Falcon Heavy, Starship",
        spacecraft: String = "Dragon, Crew Dragon, Starship",
        image: ImageEntity = createImageEntity(),
        totalLaunchCount: Int = 234,
        consecutiveSuccessfulLaunches: Int = 89,
        successfulLaunches: Int = 220,
        failedLaunches: Int = 2,
        pendingLaunches: Int = 12,
        consecutiveSuccessfulLandings: Int = 45,
        successfulLandings: Int = 180,
        failedLandings: Int = 8,
        attemptedLandings: Int = 188,
        successfulLandingsSpacecraft: Int = 50,
        failedLandingsSpacecraft: Int = 2,
        attemptedLandingsSpacecraft: Int = 52,
        successfulLandingsPayload: Int = 175,
        failedLandingsPayload: Int = 5,
        attemptedLandingsPayload: Int = 180,
        infoUrl: String = "https://www.spacex.com",
        wikiUrl: String = "https://en.wikipedia.org/wiki/SpaceX"
    ) = AgencyEntity(
        id = id,
        url = url,
        name = name,
        abbrev = abbrev,
        type = type,
        featured = featured,
        country = country,
        description = description,
        administrator = administrator,
        foundingYear = foundingYear,
        launchers = launchers,
        spacecraft = spacecraft,
        image = image,
        totalLaunchCount = totalLaunchCount,
        consecutiveSuccessfulLaunches = consecutiveSuccessfulLaunches,
        successfulLaunches = successfulLaunches,
        failedLaunches = failedLaunches,
        pendingLaunches = pendingLaunches,
        consecutiveSuccessfulLandings = consecutiveSuccessfulLandings,
        successfulLandings = successfulLandings,
        failedLandings = failedLandings,
        attemptedLandings = attemptedLandings,
        successfulLandingsSpacecraft = successfulLandingsSpacecraft,
        failedLandingsSpacecraft = failedLandingsSpacecraft,
        attemptedLandingsSpacecraft = attemptedLandingsSpacecraft,
        successfulLandingsPayload = successfulLandingsPayload,
        failedLandingsPayload = failedLandingsPayload,
        attemptedLandingsPayload = attemptedLandingsPayload,
        infoUrl = infoUrl,
        wikiUrl = wikiUrl
    )

    fun createOrbitEntity(
        id: Int = 8,
        name: String = "Low Earth Orbit",
        abbrev: String = "LEO"
    ) = OrbitEntity(
        id = id,
        name = name,
        abbrev = abbrev
    )

    fun createMissionEntity(
        id: Int = 6319,
        name: String = "Starlink Group 15-12",
        type: String = "Communications",
        description: String = "A batch of satellites for the Starlink mega-constellation - SpaceX's project for space-based Internet communication system.",
        orbit: OrbitEntity = createOrbitEntity(),
        agencies: List<AgencyEntity> = listOf(createAgencyEntity()),
        infoUrls: List<InfoUrlEntity> = listOf(createInfoUrlEntity()),
        vidUrls: List<VidUrlEntity> = listOf(createVidUrlEntity())
    ) = MissionEntity(
        id = id,
        name = name,
        type = type,
        description = description,
        orbit = orbit,
        agencies = agencies,
        infoUrls = infoUrls,
        vidUrls = vidUrls
    )

    fun createPadEntity(
        id: Int = 87,
        url: String = "https://lldev.thespacedevs.com/2.3.0/pad/87/",
        name: String = "Launch Complex 39A",
        description: String = "Historic launch pad at Kennedy Space Center",
        infoUrl: String = "https://www.nasa.gov/pad-39a",
        wikiUrl: String = "https://en.wikipedia.org/wiki/Kennedy_Space_Center_Launch_Complex_39",
        mapUrl: String = "https://maps.google.com",
        mapImage: String = "https://example.com/pad-map.jpg",
        latitude: Double = 28.6080585,
        longitude: Double = -80.6039558,
        totalLaunchCount: Int = 150,
        orbitalLaunchAttemptCount: Int = 140,
        fastestTurnaround: String = "P51D",
        locationId: Int = 27,
        locationUrl: String = "https://lldev.thespacedevs.com/2.3.0/location/27/",
        locationName: String = "Kennedy Space Center, FL, USA",
        locationDescription: String = "Kennedy Space Center is one of the most historic launch sites in the US.",
        locationTimezone: String = "America/New_York",
        locationTotalLaunchCount: Int = 500,
        locationTotalLandingCount: Int = 50,
        locationLongitude: Double = -80.6039558,
        locationLatitude: Double = 28.6080585,
        locationMapImage: String = "https://example.com/location-map.jpg",
        agencies: List<AgencyEntity> = listOf(createAgencyEntity()),
        image: ImageEntity = createImageEntity(),
        country: CountryEntity = createCountryEntity()
    ) = PadEntity(
        id = id,
        url = url,
        name = name,
        description = description,
        infoUrl = infoUrl,
        wikiUrl = wikiUrl,
        mapUrl = mapUrl,
        mapImage = mapImage,
        latitude = latitude,
        longitude = longitude,
        totalLaunchCount = totalLaunchCount,
        orbitalLaunchAttemptCount = orbitalLaunchAttemptCount,
        fastestTurnaround = fastestTurnaround,
        locationId = locationId,
        locationUrl = locationUrl,
        locationName = locationName,
        locationDescription = locationDescription,
        locationTimezone = locationTimezone,
        locationTotalLaunchCount = locationTotalLaunchCount,
        locationTotalLandingCount = locationTotalLandingCount,
        locationLongitude = locationLongitude,
        locationLatitude = locationLatitude,
        locationMapImage = locationMapImage,
        agencies = agencies,
        image = image,
        country = country
    )

    fun createProgramEntity(
        id: Int = 25,
        url: String = "https://lldev.thespacedevs.com/2.3.0/program/25/",
        name: String = "Starlink",
        description: String = "Starlink is a satellite internet constellation being constructed by SpaceX.",
        image: ImageEntity = createImageEntity(),
        startDate: String = "2019-05-24T02:30:00Z",
        endDate: String = "2030-12-31T23:59:59Z",
        agencies: List<AgencyEntity> = listOf(createAgencyEntity())
    ) = ProgramEntity(
        id = id,
        url = url,
        name = name,
        description = description,
        image = image,
        startDate = startDate,
        endDate = endDate,
        agencies = agencies
    )

    fun createInfoUrlEntity(
        priority: Int = 10,
        source: String = "spacex.com",
        title: String = "SpaceX",
        description: String = "SpaceX designs, manufactures and launches advanced rockets and spacecraft.",
        featureImage: String = "https://www.spacex.com/static/images/share.jpg",
        url: String = "https://www.spacex.com/launches/cosmo-skymedfm3"
    ) = InfoUrlEntity(
        priority = priority,
        source = source,
        title = title,
        description = description,
        featureImage = featureImage,
        url = url
    )

    fun createLaunchUpdateEntity(
        id: Int = 1,
        profileImage: String = "https://example.com/profile.png",
        comment: String = "Launch is proceeding nominally. All systems are go.",
        infoUrl: String = "https://example.com/info",
        createdBy: String = "SpaceX Official",
        createdOn: String = "2025-12-31T12:00:00Z"
    ) = LaunchUpdateEntity(
        id = id,
        profileImage = profileImage,
        comment = comment,
        infoUrl = infoUrl,
        createdBy = createdBy,
        createdOn = createdOn
    )

    fun createVidUrlEntity(
        priority: Int = 10,
        source: String = "youtube.com",
        publisher: String = "SpaceX",
        title: String = "Starlink Mission",
        description: String = "Live coverage of the Starlink launch.",
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
        name: String = "Starlink Patch",
        priority: Int = 10,
        imageUrl: String = "https://example.com/patch.png",
        agency: AgencyEntity = createAgencyEntity()
    ) = MissionPatchEntity(
        id = id,
        name = name,
        priority = priority,
        imageUrl = imageUrl,
        agency = agency
    )

    fun createLandingLocationEntity(
        id: Int = 1,
        name: String = "Of Course I Still Love You",
        abbrev: String = "OCISLY",
        description: String = "Autonomous spaceport drone ship"
    ) = LandingLocationEntity(
        id = id,
        name = name,
        abbrev = abbrev,
        description = description
    )

    fun createLandingTypeEntity(
        id: Int = 1,
        name: String = "Autonomous Spaceport Drone Ship",
        abbrev: String = "ASDS",
        description: String = "Autonomous drone ship for rocket recovery at sea"
    ) = LandingTypeEntity(
        id = id,
        name = name,
        abbrev = abbrev,
        description = description
    )

    fun createLandingEntity(
        id: Int = 1,
        attempt: Boolean = true,
        success: Boolean = true,
        description: String = "The first stage successfully landed on the droneship.",
        location: LandingLocationEntity = createLandingLocationEntity(),
        type: LandingTypeEntity = createLandingTypeEntity()
    ) = LandingEntity(
        id = id,
        attempt = attempt,
        success = success,
        description = description,
        location = location,
        type = type
    )

    fun createPreviousFlightEntity(
        id: String = "prev-flight-123",
        name: String = "Starlink Group 14-8"
    ) = PreviousFlightEntity(
        id = id,
        name = name
    )

    fun createLauncherEntity(
        id: Int = 123,
        url: String = "https://lldev.thespacedevs.com/2.3.0/launcher/123/",
        flightProven: Boolean = true,
        serialNumber: String = "B1062",
        status: LaunchStatusEntity = createLaunchStatusEntity(),
        details: String = "Falcon 9 first stage booster",
        image: ImageEntity = createImageEntity(),
        successfulLandings: Int = 8,
        attemptedLandings: Int = 8,
        flights: Int = 8,
        lastLaunchDate: String = "2025-11-15T10:30:00Z",
        firstLaunchDate: String = "2020-06-13T09:21:00Z"
    ) = LauncherEntity(
        id = id,
        url = url,
        flightProven = flightProven,
        serialNumber = serialNumber,
        status = status,
        details = details,
        image = image,
        successfulLandings = successfulLandings,
        attemptedLandings = attemptedLandings,
        flights = flights,
        lastLaunchDate = lastLaunchDate,
        firstLaunchDate = firstLaunchDate
    )

    fun createLauncherStageEntity(
        id: Int = 1,
        type: String = "Core",
        reused: Boolean = true,
        launcherFlightNumber: Int = 8,
        launcher: LauncherEntity = createLauncherEntity(),
        landing: LandingEntity = createLandingEntity(),
        previousFlightDate: String = "2025-11-15T10:30:00Z",
        turnAroundTime: String = "PT720H",
        previousFlight: PreviousFlightEntity = createPreviousFlightEntity()
    ) = LauncherStageEntity(
        id = id,
        type = type,
        reused = reused,
        launcherFlightNumber = launcherFlightNumber,
        launcher = launcher,
        landing = landing,
        previousFlightDate = previousFlightDate,
        turnAroundTime = turnAroundTime,
        previousFlight = previousFlight
    )

    fun createSpacecraftStatusEntity(
        id: Int = 1,
        name: String = "Active"
    ) = SpacecraftStatusEntity(
        id = id,
        name = name
    )

    fun createSpacecraftTypeEntity(
        id: Int = 1,
        name: String = "Cargo"
    ) = SpacecraftTypeEntity(
        id = id,
        name = name
    )

    fun createSpacecraftConfigEntity(
        id: Int = 3,
        url: String = "https://lldev.thespacedevs.com/2.3.0/config/spacecraft/3/",
        name: String = "Dragon 2",
        type: SpacecraftTypeEntity = createSpacecraftTypeEntity(),
        agency: AgencyEntity = createAgencyEntity(),
        inUse: Boolean = true,
        capability: String = "Cargo and Crew Transport",
        history: String = "Dragon 2 is a class of reusable spacecraft developed and manufactured by American aerospace manufacturer SpaceX.",
        details: String = "Dragon 2 can carry up to 7 passengers to and from Earth orbit, and beyond.",
        maidenFlight: String = "2019-03-02T07:49:00Z",
        height: Double = 8.1,
        diameter: Double = 4.0,
        humanRated: Boolean = true,
        crewCapacity: Int = 7
    ) = SpacecraftConfigEntity(
        id = id,
        url = url,
        name = name,
        type = type,
        agency = agency,
        inUse = inUse,
        capability = capability,
        history = history,
        details = details,
        maidenFlight = maidenFlight,
        height = height,
        diameter = diameter,
        humanRated = humanRated,
        crewCapacity = crewCapacity
    )

    fun createSpacecraftEntity(
        id: Int = 45,
        url: String = "https://lldev.thespacedevs.com/2.3.0/spacecraft/45/",
        name: String = "Dragon C208",
        serialNumber: String = "C208",
        status: SpacecraftStatusEntity = createSpacecraftStatusEntity(),
        description: String = "Dragon spacecraft for cargo missions",
        spacecraftConfig: SpacecraftConfigEntity = createSpacecraftConfigEntity()
    ) = SpacecraftEntity(
        id = id,
        url = url,
        name = name,
        serialNumber = serialNumber,
        status = status,
        description = description,
        spacecraftConfig = spacecraftConfig
    )

    fun createSpacecraftStageEntity(
        id: Int = 1,
        url: String = "https://lldev.thespacedevs.com/2.3.0/spacecraft_stage/1/",
        destination: String = "International Space Station",
        missionEnd: String = "2026-01-15T12:00:00Z",
        spacecraft: SpacecraftEntity = createSpacecraftEntity(),
        landing: LandingEntity = createLandingEntity()
    ) = SpacecraftStageEntity(
        id = id,
        url = url,
        destination = destination,
        missionEnd = missionEnd,
        spacecraft = spacecraft,
        landing = landing
    )

    fun createFamilyEntity(
        id: Int = 1,
        name: String = "Falcon",
        manufacturer: List<AgencyEntity> = listOf(createAgencyEntity()),
        description: String? = "Falcon rocket family",
        active: Boolean = true,
        maidenFlight: String = "2010-06-04T18:45:00Z",
        totalLaunchCount: Int = 300,
        consecutiveSuccessfulLaunches: Int = 250,
        successfulLaunches: Int = 290,
        failedLaunches: Int = 10,
        pendingLaunches: Int = 50,
        attemptedLandings: Int = 200,
        successfulLandings: Int = 190,
        failedLandings: Int = 10,
        consecutiveSuccessfulLandings: Int = 150
    ) = FamilyEntity(
        id = id,
        name = name,
        manufacturer = manufacturer,
        description = description,
        active = active,
        maidenFlight = maidenFlight,
        totalLaunchCount = totalLaunchCount,
        consecutiveSuccessfulLaunches = consecutiveSuccessfulLaunches,
        successfulLaunches = successfulLaunches,
        failedLaunches = failedLaunches,
        pendingLaunches = pendingLaunches,
        attemptedLandings = attemptedLandings,
        successfulLandings = successfulLandings,
        failedLandings = failedLandings,
        consecutiveSuccessfulLandings = consecutiveSuccessfulLandings
    )

    fun createConfigurationEntity(
        id: Int = 164,
        url: String = "https://lldev.thespacedevs.com/2.3.0/config/launcher/164/",
        name: String = "Falcon 9",
        fullName: String = "Falcon 9 Block 5",
        variant: String = "Block 5",
        families: List<FamilyEntity> = listOf(createFamilyEntity()),
        manufacturer: AgencyEntity? = createAgencyEntity(),
        image: ImageEntity? = createImageEntity(),
        wikiUrl: String? = "https://en.wikipedia.org/wiki/Falcon_9",
        description: String? = "Falcon 9 is a reusable, two-stage rocket designed and manufactured by SpaceX.",
        alias: String? = "F9",
        totalLaunchCount: Int? = 300,
        successfulLaunches: Int? = 290,
        failedLaunches: Int? = 10
    ) = ConfigurationEntity(
        id = id,
        url = url,
        name = name,
        fullName = fullName,
        variant = variant,
        families = families,
        manufacturer = manufacturer,
        image = image,
        wikiUrl = wikiUrl,
        description = description,
        alias = alias,
        totalLaunchCount = totalLaunchCount,
        successfulLaunches = successfulLaunches,
        failedLaunches = failedLaunches
    )

    fun createRocketEntity(
        id: Int = 7815,
        configuration: ConfigurationEntity = createConfigurationEntity(),
        launcherStage: List<LauncherStageEntity> = listOf(createLauncherStageEntity()),
        spacecraftStage: List<SpacecraftStageEntity> = listOf(createSpacecraftStageEntity())
    ) = RocketEntity(
        id = id,
        configuration = configuration,
        launcherStage = launcherStage,
        spacecraftStage = spacecraftStage
    )

    fun createLaunchEntity(
        id: String = "faf4a0bc-7dad-4842-b74c-73a9f648b5cc",
        url: String = "https://lldev.thespacedevs.com/2.3.0/launches/faf4a0bc-7dad-4842-b74c-73a9f648b5cc/",
        name: String = "Falcon 9 Block 5 | Starlink Group 15-12",
        lastUpdated: String = "2025-12-05T18:39:36Z",
        net: String = "2025-12-13T05:34:00Z",
        netPrecision: NetPrecisionEntity? = createNetPrecisionEntity(),
        windowEnd: String = "2025-12-13T09:34:00Z",
        windowStart: String = "2025-12-13T05:34:00Z",
        image: ImageEntity = createImageEntity(),
        infographic: String = "https://example.com/infographic.jpg",
        probability: Int = 90,
        weatherConcerns: String = "10% chance of unfavorable winds",
        failReason: String = "Failed",
        launchServiceProvider: AgencyEntity? = createAgencyEntity(),
        rocket: RocketEntity? = createRocketEntity(),
        mission: MissionEntity? = createMissionEntity(),
        pad: PadEntity? = createPadEntity(),
        webcastLive: Boolean = false,
        program: List<ProgramEntity>? = listOf(createProgramEntity()),
        orbitalLaunchAttemptCount: Int = 6789,
        locationLaunchAttemptCount: Int = 456,
        padLaunchAttemptCount: Int = 234,
        agencyLaunchAttemptCount: Int = 234,
        orbitalLaunchAttemptCountYear: Int = 123,
        locationLaunchAttemptCountYear: Int = 45,
        padLaunchAttemptCountYear: Int = 23,
        agencyLaunchAttemptCountYear: Int = 67,
        status: LaunchStatusEntity? = createLaunchStatusEntity(),
        updates: List<LaunchUpdateEntity>? = listOf(createLaunchUpdateEntity()),
        infoUrls: List<InfoUrlEntity>? = listOf(createInfoUrlEntity()),
        vidUrls: List<VidUrlEntity>? = listOf(createVidUrlEntity()),
        padTurnaround: String = "PT240H",
        missionPatches: List<MissionPatchEntity>? = listOf(createMissionPatchEntity()),
        configuration: ConfigurationEntity? = createConfigurationEntity(),
        families: List<FamilyEntity>? = listOf(createFamilyEntity())
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
        updates = updates,
        infoUrls = infoUrls,
        vidUrls = vidUrls,
        padTurnaround = padTurnaround,
        missionPatches = missionPatches,
        status = status,
        configuration = configuration,
        families = families
    )

    fun createNetPrecision(
        id: Int = 1,
        name: String = "Minute",
        abbrev: String = "MIN",
        description: String = "The T-0 is accurate to the minute."
    ) = NetPrecision(
        id = id,
        name = name,
        abbrev = abbrev,
        description = description
    )

    fun createStatus(
        id: Int = 1,
        name: String = "Success",
        abbrev: String = "Success",
        description: String = "Current T-0 confirmed by official or reliable sources."
    ) = Status(
        id = id,
        name = name,
        abbrev = abbrev,
        description = description
    )

    fun createImage(
        id: Int = 1296,
        name: String = "Starlink night fairing",
        imageUrl: String = "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/falcon2520925_image_20221009234147.png",
        thumbnailUrl: String = "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305192320.png",
        credit: String = "SpaceX"
    ) = Image(
        id = id,
        name = name,
        imageUrl = imageUrl,
        thumbnailUrl = thumbnailUrl,
        credit = credit
    )

    fun createCountry(
        id: Int = 1,
        name: String = "United States",
        alpha2Code: String = "US",
        alpha3Code: String = "USA",
        nationalityName: String = "American"
    ) = Country(
        id = id,
        name = name,
        alpha2Code = alpha2Code,
        alpha3Code = alpha3Code,
        nationalityName = nationalityName
    )

    fun createAgency(
        id: Int = 121,
        url: String = "https://lldev.thespacedevs.com/2.3.0/agencies/121/",
        name: String = "SpaceX",
        abbrev: String = "SpX",
        type: String = "Government",
        featured: Boolean = true,
        country: List<Country> = listOf(createCountry()),
        description: String = "Space Exploration Technologies Corp., known as SpaceX, is an American aerospace manufacturer and space transport services company headquartered in Hawthorne, California.",
        administrator: String = "Elon Musk",
        foundingYear: Int = 2002,
        launchers: String = "Falcon 9, Falcon Heavy, Starship",
        spacecraft: String = "Dragon, Crew Dragon, Starship",
        image: Image = createImage(),
        totalLaunchCount: Int = 234,
        consecutiveSuccessfulLaunches: Int = 89,
        successfulLaunches: Int = 220,
        failedLaunches: Int = 2,
        pendingLaunches: Int = 12,
        consecutiveSuccessfulLandings: Int = 45,
        successfulLandings: Int = 180,
        failedLandings: Int = 8,
        attemptedLandings: Int = 188,
        successfulLandingsSpacecraft: Int = 50,
        failedLandingsSpacecraft: Int = 2,
        attemptedLandingsSpacecraft: Int = 52,
        successfulLandingsPayload: Int = 175,
        failedLandingsPayload: Int = 5,
        attemptedLandingsPayload: Int = 180,
        infoUrl: String = "https://www.spacex.com",
        wikiUrl: String = "https://en.wikipedia.org/wiki/SpaceX"
    ) = Agency(
        id = id,
        url = url,
        name = name,
        abbrev = abbrev,
        type = type,
        featured = featured,
        country = country,
        description = description,
        administrator = administrator,
        foundingYear = foundingYear,
        launchers = launchers,
        spacecraft = spacecraft,
        image = image,
        totalLaunchCount = totalLaunchCount,
        consecutiveSuccessfulLaunches = consecutiveSuccessfulLaunches,
        successfulLaunches = successfulLaunches,
        failedLaunches = failedLaunches,
        pendingLaunches = pendingLaunches,
        consecutiveSuccessfulLandings = consecutiveSuccessfulLandings,
        successfulLandings = successfulLandings,
        failedLandings = failedLandings,
        attemptedLandings = attemptedLandings,
        successfulLandingsSpacecraft = successfulLandingsSpacecraft,
        failedLandingsSpacecraft = failedLandingsSpacecraft,
        attemptedLandingsSpacecraft = attemptedLandingsSpacecraft,
        successfulLandingsPayload = successfulLandingsPayload,
        failedLandingsPayload = failedLandingsPayload,
        attemptedLandingsPayload = attemptedLandingsPayload,
        infoUrl = infoUrl,
        wikiUrl = wikiUrl
    )

    fun createFamily(
        id: Int = 1,
        name: String = "Falcon",
        manufacturer: List<Agency> = listOf(createAgency()),
        description: String? = "Falcon rocket family",
        active: Boolean = true,
        maidenFlight: String = "2010-06-04T18:45:00Z",
        totalLaunchCount: Int = 300,
        consecutiveSuccessfulLaunches: Int = 250,
        successfulLaunches: Int = 290,
        failedLaunches: Int = 10,
        pendingLaunches: Int = 50,
        attemptedLandings: Int = 200,
        successfulLandings: Int = 190,
        failedLandings: Int = 10,
        consecutiveSuccessfulLandings: Int = 150
    ) = Family(
        id = id,
        name = name,
        manufacturer = manufacturer,
        description = description,
        active = active,
        maidenFlight = maidenFlight,
        totalLaunchCount = totalLaunchCount,
        consecutiveSuccessfulLaunches = consecutiveSuccessfulLaunches,
        successfulLaunches = successfulLaunches,
        failedLaunches = failedLaunches,
        pendingLaunches = pendingLaunches,
        attemptedLandings = attemptedLandings,
        successfulLandings = successfulLandings,
        failedLandings = failedLandings,
        consecutiveSuccessfulLandings = consecutiveSuccessfulLandings
    )

    fun createConfiguration(
        id: Int = 164,
        url: String = "https://lldev.thespacedevs.com/2.3.0/config/launcher/164/",
        name: String = "Falcon 9",
        fullName: String = "Falcon 9 Block 5",
        variant: String = "Block 5",
        families: List<Family> = listOf(createFamily()),
        manufacturer: Agency? = createAgency(),
        image: Image? = createImage(),
        wikiUrl: String? = "https://en.wikipedia.org/wiki/Falcon_9",
        description: String? = "Falcon 9 is a reusable, two-stage rocket designed and manufactured by SpaceX.",
        alias: String? = "F9",
        totalLaunchCount: Int? = 300,
        successfulLaunches: Int? = 290,
        failedLaunches: Int? = 10
    ) = Configuration(
        id = id,
        url = url,
        name = name,
        fullName = fullName,
        variant = variant,
        families = families,
        manufacturer = manufacturer,
        image = image,
        wikiUrl = wikiUrl,
        description = description,
        alias = alias,
        totalLaunchCount = totalLaunchCount,
        successfulLaunches = successfulLaunches,
        failedLaunches = failedLaunches
    )

    fun createOrbit(
        id: Int = 8,
        name: String = "Low Earth Orbit",
        abbrev: String = "LEO"
    ) = Orbit(
        id = id,
        name = name,
        abbrev = abbrev
    )

    fun createMission(
        id: Int = 6319,
        name: String = "Starlink Group 15-12",
        type: String = "Communications",
        description: String = "A batch of satellites for the Starlink mega-constellation - SpaceX's project for space-based Internet communication system.",
        orbit: Orbit = createOrbit(),
        agencies: List<Agency> = listOf(createAgency()),
        infoUrls: List<InfoUrl> = listOf(createInfoUrl()),
        vidUrls: List<VidUrl> = listOf(createVidUrl())
    ) = Mission(
        id = id,
        name = name,
        type = type,
        description = description,
        orbit = orbit,
        agencies = agencies,
        infoUrls = infoUrls,
        vidUrls = vidUrls
    )

    fun createLocation(
        id: Int = 27,
        url: String = "https://lldev.thespacedevs.com/2.3.0/location/27/",
        name: String = "Kennedy Space Center, FL, USA",
        country: Country = createCountry(),
        description: String = "Kennedy Space Center is one of the most historic launch sites in the US.",
        image: Image? = null,
        mapImage: String? = "https://example.com/location-map.jpg",
        longitude: Double? = -80.6039558,
        latitude: Double? = 28.6080585,
        timezoneName: String = "America/New_York",
        totalLaunchCount: Int = 500,
        totalLandingCount: Int? = 50
    ) = Location(
        id = id,
        url = url,
        name = name,
        country = country,
        description = description,
        image = image,
        mapImage = mapImage,
        longitude = longitude,
        latitude = latitude,
        timezoneName = timezoneName,
        totalLaunchCount = totalLaunchCount,
        totalLandingCount = totalLandingCount
    )

    fun createPad(
        id: Int = 87,
        url: String = "https://lldev.thespacedevs.com/2.3.0/pad/87/",
        agencies: List<Agency>? = listOf(createAgency()),
        name: String = "Launch Complex 39A",
        image: Image = createImage(),
        description: String? = "Historic launch pad at Kennedy Space Center",
        country: Country? = createCountry(),
        latitude: Double = 28.6080585,
        longitude: Double = -80.6039558,
        mapUrl: String = "https://maps.google.com",
        mapImage: String? = "https://example.com/pad-map.jpg",
        wikiUrl: String? = "https://en.wikipedia.org/wiki/Kennedy_Space_Center_Launch_Complex_39",
        infoUrl: String? = "https://www.nasa.gov/pad-39a",
        totalLaunchCount: Int = 150,
        orbitalLaunchAttemptCount: Int? = 140,
        fastestTurnaround: String? = "P51D",
        location: Location = createLocation(),
    ) = Pad(
        id = id,
        url = url,
        agencies = agencies,
        name = name,
        image = image,
        description = description,
        country = country,
        latitude = latitude,
        longitude = longitude,
        mapUrl = mapUrl,
        mapImage = mapImage,
        wikiUrl = wikiUrl,
        infoUrl = infoUrl,
        totalLaunchCount = totalLaunchCount,
        orbitalLaunchAttemptCount = orbitalLaunchAttemptCount,
        fastestTurnaround = fastestTurnaround,
        location = location,
    )

    fun createProgram(
        id: Int = 25,
        url: String = "https://lldev.thespacedevs.com/2.3.0/program/25/",
        name: String = "Starlink",
        description: String = "Starlink is a satellite internet constellation being constructed by SpaceX.",
        image: Image = createImage(),
        startDate: String = "2019-05-24T02:30:00Z",
        endDate: String = "2030-12-31T23:59:59Z",
        agencies: List<Agency> = listOf(createAgency())
    ) = Program(
        id = id,
        url = url,
        name = name,
        description = description,
        image = image,
        startDate = startDate,
        endDate = endDate,
        agencies = agencies
    )

    fun createInfoUrl(
        priority: Int = 10,
        source: String = "spacex.com",
        title: String = "SpaceX",
        description: String = "SpaceX designs, manufactures and launches advanced rockets and spacecraft.",
        featureImage: String = "https://www.spacex.com/static/images/share.jpg",
        url: String = "https://www.spacex.com/launches/cosmo-skymedfm3"
    ) = InfoUrl(
        priority = priority,
        source = source,
        title = title,
        description = description,
        featureImage = featureImage,
        url = url
    )

    fun createLaunchUpdate(
        id: Int = 1,
        profileImage: String = "https://example.com/profile.png",
        comment: String = "Launch is proceeding nominally. All systems are go.",
        infoUrl: String = "https://example.com/info",
        createdBy: String = "SpaceX Official",
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
        publisher: String = "SpaceX",
        title: String = "Starlink Mission",
        description: String = "Live coverage of the Starlink launch.",
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
        name: String = "Starlink Patch",
        priority: Int = 10,
        imageUrl: String = "https://example.com/patch.png",
        agency: Agency = createAgency(),
    ) = MissionPatch(
        id = id,
        name = name,
        priority = priority,
        imageUrl = imageUrl,
        agency = agency
    )

    fun createLandingLocation(
        id: Int = 1,
        name: String = "Of Course I Still Love You",
        abbrev: String = "OCISLY",
        description: String = "Autonomous spaceport drone ship"
    ) = LandingLocation(
        id = id,
        name = name,
        abbrev = abbrev,
        description = description
    )

    fun createLandingType(
        id: Int = 1,
        name: String = "Autonomous Spaceport Drone Ship",
        abbrev: String = "ASDS",
        description: String = "Autonomous drone ship for rocket recovery at sea"
    ) = LandingType(
        id = id,
        name = name,
        abbrev = abbrev,
        description = description
    )

    fun createLanding(
        id: Int = 1,
        attempt: Boolean = true,
        success: Boolean = true,
        description: String = "The first stage successfully landed on the droneship.",
        location: LandingLocation = createLandingLocation(),
        type: LandingType = createLandingType()
    ) = Landing(
        id = id,
        attempt = attempt,
        success = success,
        description = description,
        location = location,
        type = type
    )

    fun createPreviousFlight(
        id: String = "prev-flight-123",
        name: String = "Starlink Group 14-8"
    ) = PreviousFlight(
        id = id,
        name = name
    )

    fun createLauncher(
        id: Int = 123,
        url: String = "https://lldev.thespacedevs.com/2.3.0/launcher/123/",
        flightProven: Boolean = true,
        serialNumber: String = "B1062",
        status: Status = createStatus(),
        details: String = "Falcon 9 first stage booster",
        image: Image = createImage(),
        successfulLandings: Int = 8,
        attemptedLandings: Int = 8,
        flights: Int = 8,
        lastLaunchDate: String = "2025-11-15T10:30:00Z",
        firstLaunchDate: String = "2020-06-13T09:21:00Z"
    ) = Launcher(
        id = id,
        url = url,
        flightProven = flightProven,
        serialNumber = serialNumber,
        status = status,
        details = details,
        image = image,
        successfulLandings = successfulLandings,
        attemptedLandings = attemptedLandings,
        flights = flights,
        lastLaunchDate = lastLaunchDate,
        firstLaunchDate = firstLaunchDate
    )

    fun createLauncherStage(
        id: Int = 1,
        type: String = "Core",
        reused: Boolean = true,
        launcherFlightNumber: Int = 8,
        launcher: Launcher = createLauncher(),
        landing: Landing = createLanding(),
        previousFlightDate: String = "2025-11-15T10:30:00Z",
        turnAroundTime: String = "PT720H",
        previousFlight: PreviousFlight = createPreviousFlight()
    ) = LauncherStage(
        id = id,
        type = type,
        reused = reused,
        launcherFlightNumber = launcherFlightNumber,
        launcher = launcher,
        landing = landing,
        previousFlightDate = previousFlightDate,
        turnAroundTime = turnAroundTime,
        previousFlight = previousFlight
    )

    fun createSpacecraftStatus(
        id: Int = 1,
        name: String = "Active"
    ) = SpacecraftStatus(
        id = id,
        name = name
    )

    fun createSpacecraftType(
        id: Int = 1,
        name: String = "Cargo"
    ) = SpacecraftType(
        id = id,
        name = name
    )

    fun createSpacecraftConfig(
        id: Int = 3,
        url: String = "https://lldev.thespacedevs.com/2.3.0/config/spacecraft/3/",
        name: String = "Dragon 2",
        type: SpacecraftType = createSpacecraftType(),
        agency: Agency = createAgency(),
        inUse: Boolean = true,
        capability: String = "Cargo and Crew Transport",
        history: String = "Dragon 2 is a class of reusable spacecraft developed and manufactured by American aerospace manufacturer SpaceX.",
        details: String = "Dragon 2 can carry up to 7 passengers to and from Earth orbit, and beyond.",
        maidenFlight: String = "2019-03-02T07:49:00Z",
        height: Double = 8.1,
        diameter: Double = 4.0,
        humanRated: Boolean = true,
        crewCapacity: Int = 7
    ) = SpacecraftConfig(
        id = id,
        url = url,
        name = name,
        type = type,
        agency = agency,
        inUse = inUse,
        capability = capability,
        history = history,
        details = details,
        maidenFlight = maidenFlight,
        height = height,
        diameter = diameter,
        humanRated = humanRated,
        crewCapacity = crewCapacity
    )

    fun createSpacecraft(
        id: Int = 45,
        url: String = "https://lldev.thespacedevs.com/2.3.0/spacecraft/45/",
        name: String = "Dragon C208",
        serialNumber: String = "C208",
        status: SpacecraftStatus = createSpacecraftStatus(),
        description: String = "Dragon spacecraft for cargo missions",
        spacecraftConfig: SpacecraftConfig = createSpacecraftConfig()
    ) = Spacecraft(
        id = id,
        url = url,
        name = name,
        serialNumber = serialNumber,
        status = status,
        description = description,
        spacecraftConfig = spacecraftConfig
    )

    fun createSpacecraftStage(
        id: Int = 1,
        url: String = "https://lldev.thespacedevs.com/2.3.0/spacecraft_stage/1/",
        destination: String = "International Space Station",
        missionEnd: String = "2026-01-15T12:00:00Z",
        spacecraft: Spacecraft = createSpacecraft(),
        landing: Landing = createLanding()
    ) = SpacecraftStage(
        id = id,
        url = url,
        destination = destination,
        missionEnd = missionEnd,
        spacecraft = spacecraft,
        landing = landing
    )

    fun createRocket(
        id: Int = 7815,
        configuration: Configuration = createConfiguration(),
        launcherStage: List<LauncherStage> = listOf(createLauncherStage()),
        spacecraftStage: List<SpacecraftStage> = listOf(createSpacecraftStage())
    ) = Rocket(
        id = id,
        configuration = configuration,
        launcherStage = launcherStage,
        spacecraftStage = spacecraftStage
    )

    fun createLaunch(
        id: String = "faf4a0bc-7dad-4842-b74c-73a9f648b5cc",
        url: String = "https://lldev.thespacedevs.com/2.3.0/launches/faf4a0bc-7dad-4842-b74c-73a9f648b5cc/",
        name: String = "Falcon 9 Block 5 | Starlink Group 15-12",
        lastUpdated: String = "2025-12-05T18:39:36Z",
        net: String = "2025-12-13T05:34:00Z",
        netPrecision: NetPrecision = createNetPrecision(),
        windowEnd: String = "2025-12-13T09:34:00Z",
        windowStart: String = "2025-12-13T05:34:00Z",
        image: Image = createImage(),
        infographic: String = "https://example.com/infographic.jpg",
        probability: Int = 90,
        weatherConcerns: String = "10% chance of unfavorable winds",
        failReason: String = "Failed",
        launchServiceProvider: Agency = createAgency(),
        rocket: Rocket = createRocket(),
        mission: Mission = createMission(),
        pad: Pad = createPad(),
        webcastLive: Boolean = false,
        program: List<Program> = listOf(createProgram()),
        orbitalLaunchAttemptCount: Int = 6789,
        locationLaunchAttemptCount: Int = 456,
        padLaunchAttemptCount: Int = 234,
        agencyLaunchAttemptCount: Int = 234,
        orbitalLaunchAttemptCountYear: Int = 123,
        locationLaunchAttemptCountYear: Int = 45,
        padLaunchAttemptCountYear: Int = 23,
        agencyLaunchAttemptCountYear: Int = 67,
        status: Status = createStatus(),
        updates: List<LaunchUpdate> = listOf(createLaunchUpdate()),
        infoUrls: List<InfoUrl> = listOf(createInfoUrl()),
        vidUrls: List<VidUrl> = listOf(createVidUrl()),
        padTurnaround: String = "PT240H",
        missionPatches: List<MissionPatch> = listOf(createMissionPatch())
    ) = Launch(
        id = id,
        url = url,
        missionName = name,
        lastUpdated = lastUpdated,
        net = net,
        netPrecision = netPrecision,
        status = status,
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

    fun createLaunchDtoList(count: Int = 10): List<LaunchDto> {
        return (1..count).map { index ->
            createLaunchDto(
                id = "launch-id-$index",
                name = "Falcon 9 Block 5 | Mission $index"
            )
        }
    }

    fun createLaunchEntityList(count: Int = 10): List<LaunchEntity> {
        return (1..count).map { index ->
            createLaunchEntity(
                id = "launch-id-$index",
                name = "Falcon 9 Block 5 | Mission $index"
            )
        }
    }

    fun createLaunchList(count: Int = 10): List<Launch> {
        return (1..count).map { index ->
            createLaunch(
                id = "launch-id-$index",
                name = "Falcon 9 Block 5 | Mission $index"
            )
        }
    }

    fun generateRandomLaunchId(): String {
        return "launch-${Random.nextInt(1000, 9999)}"
    }

    fun generateRandomDate(): String {
        val year = Random.nextInt(2025, 2027)
        val month = Random.nextInt(1, 13).toString().padStart(2, '0')
        val day = Random.nextInt(1, 29).toString().padStart(2, '0')
        val hour = Random.nextInt(0, 24).toString().padStart(2, '0')
        val minute = Random.nextInt(0, 60).toString().padStart(2, '0')
        val second = Random.nextInt(0, 60).toString().padStart(2, '0')
        return "$year-$month-${day}T$hour:$minute:${second}Z"
    }
}
