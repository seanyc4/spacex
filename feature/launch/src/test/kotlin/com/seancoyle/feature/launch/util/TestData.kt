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
    ) = StatusDto(id, name, abbrev, description)

    fun createNetPrecisionDto(
        id: Int = 1,
        name: String = "Minute",
        abbrev: String = "MIN",
        description: String = "The T-0 is accurate to the minute."
    ) = NetPrecisionDto(id, name, abbrev, description)

    fun createImageDto(
        id: Int = 1296,
        name: String = "Starlink night fairing",
        imageUrl: String = "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/falcon2520925_image_20221009234147.png",
        thumbnailUrl: String = "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305192320.png",
        credit: String = "SpaceX"
    ) = ImageDto(id, name, imageUrl, thumbnailUrl, credit)

    fun createTypeDto(
        id: Int = 1,
        name: String = "Government"
    ) = TypeDto(id, name)

    fun createCountryDto(
        id: Int = 1,
        name: String = "United States",
        alpha2Code: String = "US",
        alpha3Code: String = "USA",
        nationalityName: String = "American",
        nationalityNameComposed: String = "American"
    ) = CountryDto(id, name, alpha2Code, alpha3Code, nationalityName, nationalityNameComposed)

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
        parent: String = "SpaceX Corporation",
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
        responseMode, id, url, name, abbrev, type, featured, country, description,
        administrator, foundingYear, launchers, spacecraft, parent, image,
        totalLaunchCount, consecutiveSuccessfulLaunches, successfulLaunches,
        failedLaunches, pendingLaunches, consecutiveSuccessfulLandings,
        successfulLandings, failedLandings, attemptedLandings,
        successfulLandingsSpacecraft, failedLandingsSpacecraft,
        attemptedLandingsSpacecraft, successfulLandingsPayload,
        failedLandingsPayload, attemptedLandingsPayload, infoUrl, wikiUrl
    )

    fun createFamilyDto(
        responseMode: String = "normal",
        id: Int = 1,
        name: String = "Falcon"
    ) = FamilyDto(responseMode, id, name)

    fun createConfigurationDto(
        responseMode: String = "normal",
        id: Int = 164,
        url: String = "https://lldev.thespacedevs.com/2.3.0/config/launcher/164/",
        name: String = "Falcon 9",
        families: List<FamilyDto> = listOf(createFamilyDto()),
        fullName: String = "Falcon 9 Block 5",
        variant: String = "Block 5"
    ) = ConfigurationDto(responseMode, id, url, name, families, fullName, variant)

    fun createOrbitDto(
        id: Int = 8,
        name: String = "Low Earth Orbit",
        abbrev: String = "LEO"
    ) = OrbitDto(id, name, abbrev)

    fun createMissionDto(
        id: Int = 6319,
        name: String = "Starlink Group 15-12",
        type: String = "Communications",
        description: String = "A batch of satellites for the Starlink mega-constellation - SpaceX's project for space-based Internet communication system.",
        orbit: OrbitDto? = createOrbitDto(),
        agencies: List<AgencyDto>? = listOf(createAgencyDto()),
        infoUrls: List<InfoUrlDto> = listOf(createInfoUrlDto()),
        vidUrls: List<VidUrlDto> = listOf(createVidUrlDto())
    ) = MissionDto(id, name, type, description, orbit, agencies, infoUrls, vidUrls)

    fun createLocationDto(
        responseMode: String = "normal",
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
        responseMode, id, url, name, active, country, description,
        image, mapImage, longitude, latitude, timezoneName,
        totalLaunchCount, totalLandingCount
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
        id, url, active, agencies, name, image, description, infoUrl,
        wikiUrl, mapUrl, latitude, longitude, country, mapImage,
        totalLaunchCount, orbitalLaunchAttemptCount, fastestTurnaround, location
    )

    fun createProgramDto(
        responseMode: String = "normal",
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
        responseMode, id, url, name, image, infoUrl, wikiUrl, description,
        agencies, startDate, endDate, missionPatches, type
    )

    fun createInfoUrlDto(
        priority: Int = 10,
        source: String = "spacex.com",
        title: String = "SpaceX",
        description: String = "SpaceX designs, manufactures and launches advanced rockets and spacecraft.",
        featureImage: String = "https://www.spacex.com/static/images/share.jpg",
        url: String = "https://www.spacex.com/launches/cosmo-skymedfm3"
    ) = InfoUrlDto(priority, source, title, description, featureImage, url)

    fun createLaunchUpdateDto(
        id: Int = 1,
        profileImage: String = "https://example.com/profile.png",
        comment: String = "Launch is proceeding nominally. All systems are go.",
        infoUrl: String = "https://example.com/info",
        createdBy: String = "SpaceX Official",
        createdOn: String = "2025-12-31T12:00:00Z"
    ) = LaunchUpdateDto(id, profileImage, comment, infoUrl, createdBy, createdOn)

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
        priority, source, publisher, title, description, featureImage,
        url, startTime, endTime, live
    )

    fun createMissionPatchDto(
        id: Int = 1,
        name: String = "Starlink Patch",
        priority: Int = 10,
        imageUrl: String = "https://example.com/patch.png",
        agency: AgencyDto = createAgencyDto()
    ) = MissionPatchDto(id, name, priority, imageUrl, agency)

    fun createLandingLocationDto(
        id: Int = 1,
        name: String = "Of Course I Still Love You",
        abbrev: String = "OCISLY",
        description: String = "Autonomous spaceport drone ship"
    ) = LandingLocationDto(id, name, abbrev, description)

    fun createLandingTypeDto(
        id: Int = 1,
        name: String = "Autonomous Spaceport Drone Ship",
        abbrev: String = "ASDS",
        description: String = "Autonomous drone ship for rocket recovery at sea"
    ) = LandingTypeDto(id, name, abbrev, description)

    fun createLandingDto(
        id: Int = 1,
        attempt: Boolean = true,
        success: Boolean = true,
        description: String = "The first stage successfully landed on the droneship.",
        location: LandingLocationDto = createLandingLocationDto(),
        type: LandingTypeDto = createLandingTypeDto()
    ) = LandingDto(id, attempt, success, description, location, type)

    fun createPreviousFlightDto(
        id: String = "prev-flight-123",
        name: String = "Starlink Group 14-8"
    ) = PreviousFlightDto(id, name)

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
        id, url, flightProven, serialNumber, status, details, image,
        successfulLandings, attemptedLandings, flights, lastLaunchDate, firstLaunchDate
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
        id, type, reused, launcherFlightNumber, launcher, landing,
        previousFlightDate, turnAroundTime, previousFlight
    )

    fun createSpacecraftStatusDto(
        id: Int = 1,
        name: String = "Active"
    ) = SpacecraftStatusDto(id, name)

    fun createSpacecraftTypeDto(
        id: Int = 1,
        name: String = "Cargo"
    ) = SpacecraftTypeDto(id, name)

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
        id, url, name, type, agency, inUse, capability, history, details,
        maidenFlight, height, diameter, humanRated, crewCapacity
    )

    fun createSpacecraftDto(
        id: Int = 45,
        url: String = "https://lldev.thespacedevs.com/2.3.0/spacecraft/45/",
        name: String = "Dragon C208",
        serialNumber: String = "C208",
        status: SpacecraftStatusDto = createSpacecraftStatusDto(),
        description: String = "Dragon spacecraft for cargo missions",
        spacecraftConfig: SpacecraftConfigDto = createSpacecraftConfigDto()
    ) = SpacecraftDto(id, url, name, serialNumber, status, description, spacecraftConfig)

    fun createSpacecraftStageDto(
        id: Int = 1,
        url: String = "https://lldev.thespacedevs.com/2.3.0/spacecraft_stage/1/",
        destination: String = "International Space Station",
        missionEnd: String = "2026-01-15T12:00:00Z",
        spacecraft: SpacecraftDto = createSpacecraftDto(),
        landing: LandingDto = createLandingDto()
    ) = SpacecraftStageDto(id, url, destination, missionEnd, spacecraft, landing)

    fun createRocketDto(
        id: Int = 7815,
        configuration: ConfigurationDto = createConfigurationDto(),
        launcherStage: List<LauncherStageDto> = listOf(createLauncherStageDto()),
        spacecraftStage: List<SpacecraftStageDto> = listOf(createSpacecraftStageDto())
    ) = RocketDto(id, configuration, launcherStage, spacecraftStage)

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
        id, url, name, status, lastUpdated, net, netPrecision, windowEnd, windowStart,
        image, infographic, probability, weatherConcerns, failReason,
        launchServiceProvider, rocket, mission, pad, webcastLive, program,
        orbitalLaunchAttemptCount, locationLaunchAttemptCount, padLaunchAttemptCount,
        agencyLaunchAttemptCount, orbitalLaunchAttemptCountYear, locationLaunchAttemptCountYear,
        padLaunchAttemptCountYear, agencyLaunchAttemptCountYear, updates, infoUrls,
        vidUrls, padTurnaround, missionPatches, launcherStage, spacecraftStage
    )

    fun createLaunchesDto(
        count: Int = 109,
        next: String = "https://lldev.thespacedevs.com/2.3.0/launches/upcoming/?limit=10&mode=list&offset=10&ordering=net",
        previous: String = "",
        results: List<LaunchDto> = listOf(createLaunchDto())
    ) = LaunchesDto(count, next, previous, results)


    // Entity Factory Functions

    fun createNetPrecisionEntity(
        id: Int = 1,
        name: String = "Minute",
        abbrev: String = "MIN",
        description: String = "The T-0 is accurate to the minute."
    ) = NetPrecisionEntity(id, name, abbrev, description)

    fun createLaunchStatusEntity(
        id: Int = 1,
        name: String = "Success",
        abbrev: String = "Success",
        description: String = "Current T-0 confirmed by official or reliable sources."
    ) = LaunchStatusEntity(id, name, abbrev, description)

    fun createImageEntity(
        id: Int = 1296,
        name: String = "Starlink night fairing",
        imageUrl: String = "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/falcon2520925_image_20221009234147.png",
        thumbnailUrl: String = "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305192320.png",
        credit: String = "SpaceX"
    ) = ImageEntity(id, name, imageUrl, thumbnailUrl, credit)

    fun createCountryEntity(
        id: Int = 1,
        name: String = "United States",
        alpha2Code: String = "US",
        alpha3Code: String = "USA",
        nationalityName: String = "American"
    ) = CountryEntity(id, name, alpha2Code, alpha3Code, nationalityName)

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
        parent: String = "SpaceX Corporation",
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
        id, url, name, abbrev, type, featured, country, description,
        administrator, foundingYear, launchers, spacecraft, parent, image,
        totalLaunchCount, consecutiveSuccessfulLaunches, successfulLaunches,
        failedLaunches, pendingLaunches, consecutiveSuccessfulLandings,
        successfulLandings, failedLandings, attemptedLandings,
        successfulLandingsSpacecraft, failedLandingsSpacecraft,
        attemptedLandingsSpacecraft, successfulLandingsPayload,
        failedLandingsPayload, attemptedLandingsPayload, infoUrl, wikiUrl
    )

    fun createOrbitEntity(
        id: Int = 8,
        name: String = "Low Earth Orbit",
        abbrev: String = "LEO"
    ) = OrbitEntity(id, name, abbrev)

    fun createMissionEntity(
        id: Int = 6319,
        name: String = "Starlink Group 15-12",
        type: String = "Communications",
        description: String = "A batch of satellites for the Starlink mega-constellation - SpaceX's project for space-based Internet communication system.",
        orbit: OrbitEntity = createOrbitEntity(),
        agencies: List<AgencyEntity> = listOf(createAgencyEntity()),
        infoUrls: List<InfoUrlEntity> = listOf(createInfoUrlEntity()),
        vidUrls: List<VidUrlEntity> = listOf(createVidUrlEntity())
    ) = MissionEntity(id, name, type, description, orbit, agencies, infoUrls, vidUrls)

    fun createPadEntity(
        id: Int = 87,
        url: String = "https://lldev.thespacedevs.com/2.3.0/pad/87/",
        name: String = "Launch Complex 39A",
        locationId: Int = 27,
        locationUrl: String = "https://lldev.thespacedevs.com/2.3.0/location/27/",
        locationName: String = "Kennedy Space Center, FL, USA",
        locationDescription: String = "Kennedy Space Center is one of the most historic launch sites in the US.",
        locationTimezone: String = "America/New_York",
        locationTotalLaunchCount: Int = 500,
        latitude: Double = 28.6080585,
        longitude: Double = -80.6039558,
        mapUrl: String = "https://maps.google.com",
        totalLaunchCount: Int = 150
    ) = PadEntity(
        id, url, name, locationId, locationUrl, locationName, locationDescription,
        locationTimezone, locationTotalLaunchCount, latitude, longitude, mapUrl, totalLaunchCount
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
    ) = ProgramEntity(id, url, name, description, image, startDate, endDate, agencies)

    fun createInfoUrlEntity(
        priority: Int = 10,
        source: String = "spacex.com",
        title: String = "SpaceX",
        description: String = "SpaceX designs, manufactures and launches advanced rockets and spacecraft.",
        featureImage: String = "https://www.spacex.com/static/images/share.jpg",
        url: String = "https://www.spacex.com/launches/cosmo-skymedfm3"
    ) = InfoUrlEntity(priority, source, title, description, featureImage, url)

    fun createLaunchUpdateEntity(
        id: Int = 1,
        profileImage: String = "https://example.com/profile.png",
        comment: String = "Launch is proceeding nominally. All systems are go.",
        infoUrl: String = "https://example.com/info",
        createdBy: String = "SpaceX Official",
        createdOn: String = "2025-12-31T12:00:00Z"
    ) = LaunchUpdateEntity(id, profileImage, comment, infoUrl, createdBy, createdOn)

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
        priority, source, publisher, title, description, featureImage,
        url, startTime, endTime, live
    )

    fun createMissionPatchEntity(
        id: Int = 1,
        name: String = "Starlink Patch",
        priority: Int = 10,
        imageUrl: String = "https://example.com/patch.png",
        agency: AgencyEntity = createAgencyEntity()
    ) = MissionPatchEntity(id, name, priority, imageUrl, agency)

    fun createLandingLocationEntity(
        id: Int = 1,
        name: String = "Of Course I Still Love You",
        abbrev: String = "OCISLY",
        description: String = "Autonomous spaceport drone ship"
    ) = LandingLocationEntity(id, name, abbrev, description)

    fun createLandingTypeEntity(
        id: Int = 1,
        name: String = "Autonomous Spaceport Drone Ship",
        abbrev: String = "ASDS",
        description: String = "Autonomous drone ship for rocket recovery at sea"
    ) = LandingTypeEntity(id, name, abbrev, description)

    fun createLandingEntity(
        id: Int = 1,
        attempt: Boolean = true,
        success: Boolean = true,
        description: String = "The first stage successfully landed on the droneship.",
        location: LandingLocationEntity = createLandingLocationEntity(),
        type: LandingTypeEntity = createLandingTypeEntity()
    ) = LandingEntity(id, attempt, success, description, location, type)

    fun createPreviousFlightEntity(
        id: String = "prev-flight-123",
        name: String = "Starlink Group 14-8"
    ) = PreviousFlightEntity(id, name)

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
        id, url, flightProven, serialNumber, status, details, image,
        successfulLandings, attemptedLandings, flights, lastLaunchDate, firstLaunchDate
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
        id, type, reused, launcherFlightNumber, launcher, landing,
        previousFlightDate, turnAroundTime, previousFlight
    )

    fun createSpacecraftStatusEntity(
        id: Int = 1,
        name: String = "Active"
    ) = SpacecraftStatusEntity(id, name)

    fun createSpacecraftTypeEntity(
        id: Int = 1,
        name: String = "Cargo"
    ) = SpacecraftTypeEntity(id, name)

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
        id, url, name, type, agency, inUse, capability, history, details,
        maidenFlight, height, diameter, humanRated, crewCapacity
    )

    fun createSpacecraftEntity(
        id: Int = 45,
        url: String = "https://lldev.thespacedevs.com/2.3.0/spacecraft/45/",
        name: String = "Dragon C208",
        serialNumber: String = "C208",
        status: SpacecraftStatusEntity = createSpacecraftStatusEntity(),
        description: String = "Dragon spacecraft for cargo missions",
        spacecraftConfig: SpacecraftConfigEntity = createSpacecraftConfigEntity()
    ) = SpacecraftEntity(id, url, name, serialNumber, status, description, spacecraftConfig)

    fun createSpacecraftStageEntity(
        id: Int = 1,
        url: String = "https://lldev.thespacedevs.com/2.3.0/spacecraft_stage/1/",
        destination: String = "International Space Station",
        missionEnd: String = "2026-01-15T12:00:00Z",
        spacecraft: SpacecraftEntity = createSpacecraftEntity(),
        landing: LandingEntity = createLandingEntity()
    ) = SpacecraftStageEntity(id, url, destination, missionEnd, spacecraft, landing)

    fun createRocketEntity(
        id: Int = 7815,
        configurationId: Int = 164,
        configurationUrl: String = "https://lldev.thespacedevs.com/2.3.0/config/launcher/164/",
        configurationName: String = "Falcon 9",
        configurationFullName: String = "Falcon 9 Block 5",
        variant: String = "Block 5",
        launcherStage: List<LauncherStageEntity> = listOf(createLauncherStageEntity()),
        spacecraftStage: List<SpacecraftStageEntity> = listOf(createSpacecraftStageEntity())
    ) = RocketEntity(
        id, configurationId, configurationUrl, configurationName,
        configurationFullName, variant, launcherStage, spacecraftStage
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
        missionPatches: List<MissionPatchEntity>? = listOf(createMissionPatchEntity())
    ) = LaunchEntity(
        id, url, name, lastUpdated, net, netPrecision, windowEnd, windowStart,
        image, infographic, probability, weatherConcerns, failReason,
        launchServiceProvider, rocket, mission, pad, webcastLive, program,
        orbitalLaunchAttemptCount, locationLaunchAttemptCount, padLaunchAttemptCount,
        agencyLaunchAttemptCount, orbitalLaunchAttemptCountYear, locationLaunchAttemptCountYear,
        padLaunchAttemptCountYear, agencyLaunchAttemptCountYear, updates, infoUrls,
        vidUrls, padTurnaround, missionPatches, status
    )

    fun createNetPrecision(
        id: Int = 1,
        name: String = "Minute",
        abbrev: String = "MIN",
        description: String = "The T-0 is accurate to the minute."
    ) = NetPrecision(id, name, abbrev, description)

    fun createStatus(
        id: Int = 1,
        name: String = "Success",
        abbrev: String = "Success",
        description: String = "Current T-0 confirmed by official or reliable sources."
    ) = Status(id, name, abbrev, description)

    fun createImage(
        id: Int = 1296,
        name: String = "Starlink night fairing",
        imageUrl: String = "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/falcon2520925_image_20221009234147.png",
        thumbnailUrl: String = "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305192320.png",
        credit: String = "SpaceX"
    ) = Image(id, name, imageUrl, thumbnailUrl, credit)

    fun createCountry(
        id: Int = 1,
        name: String = "United States",
        alpha2Code: String = "US",
        alpha3Code: String = "USA",
        nationalityName: String = "American"
    ) = Country(id, name, alpha2Code, alpha3Code, nationalityName)

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
        parent: String = "SpaceX Corporation",
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
        id, url, name, abbrev, type, featured, country, description,
        administrator, foundingYear, launchers, spacecraft, parent, image,
        totalLaunchCount, consecutiveSuccessfulLaunches, successfulLaunches,
        failedLaunches, pendingLaunches, consecutiveSuccessfulLandings,
        successfulLandings, failedLandings, attemptedLandings,
        successfulLandingsSpacecraft, failedLandingsSpacecraft,
        attemptedLandingsSpacecraft, successfulLandingsPayload,
        failedLandingsPayload, attemptedLandingsPayload, infoUrl, wikiUrl
    )

    fun createFamily(
        id: Int = 1,
        name: String = "Falcon"
    ) = Family(id, name)

    fun createConfiguration(
        id: Int = 164,
        url: String = "https://lldev.thespacedevs.com/2.3.0/config/launcher/164/",
        name: String = "Falcon 9",
        fullName: String = "Falcon 9 Block 5",
        variant: String = "Block 5",
        families: List<Family> = listOf(createFamily())
    ) = Configuration(id, url, name, fullName, variant, families)

    fun createOrbit(
        id: Int = 8,
        name: String = "Low Earth Orbit",
        abbrev: String = "LEO"
    ) = Orbit(id, name, abbrev)

    fun createMission(
        id: Int = 6319,
        name: String = "Starlink Group 15-12",
        type: String = "Communications",
        description: String = "A batch of satellites for the Starlink mega-constellation - SpaceX's project for space-based Internet communication system.",
        orbit: Orbit = createOrbit(),
        agencies: List<Agency> = listOf(createAgency()),
        infoUrls: List<InfoUrl> = listOf(createInfoUrl()),
        vidUrls: List<VidUrl> = listOf(createVidUrl())
    ) = Mission(id, name, type, description, orbit, agencies, infoUrls, vidUrls)

    fun createLocation(
        id: Int = 27,
        url: String = "https://lldev.thespacedevs.com/2.3.0/location/27/",
        name: String = "Kennedy Space Center, FL, USA",
        country: Country = createCountry(),
        description: String = "Kennedy Space Center is one of the most historic launch sites in the US.",
        timezoneName: String = "America/New_York",
        totalLaunchCount: Int = 500
    ) = Location(id, url, name, country, description, timezoneName, totalLaunchCount)

    fun createPad(
        id: Int = 87,
        url: String = "https://lldev.thespacedevs.com/2.3.0/pad/87/",
        name: String = "Launch Complex 39A",
        location: Location = createLocation(),
        latitude: Double = 28.6080585,
        longitude: Double = -80.6039558,
        mapUrl: String = "https://maps.google.com",
        totalLaunchCount: Int = 150
    ) = Pad(id, url, name, location, latitude, longitude, mapUrl, totalLaunchCount)

    fun createProgram(
        id: Int = 25,
        url: String = "https://lldev.thespacedevs.com/2.3.0/program/25/",
        name: String = "Starlink",
        description: String = "Starlink is a satellite internet constellation being constructed by SpaceX.",
        image: Image = createImage(),
        startDate: String = "2019-05-24T02:30:00Z",
        endDate: String = "2030-12-31T23:59:59Z",
        agencies: List<Agency> = listOf(createAgency())
    ) = Program(id, url, name, description, image, startDate, endDate, agencies)

    fun createInfoUrl(
        priority: Int = 10,
        source: String = "spacex.com",
        title: String = "SpaceX",
        description: String = "SpaceX designs, manufactures and launches advanced rockets and spacecraft.",
        featureImage: String = "https://www.spacex.com/static/images/share.jpg",
        url: String = "https://www.spacex.com/launches/cosmo-skymedfm3"
    ) = InfoUrl(priority, source, title, description, featureImage, url)

    fun createLaunchUpdate(
        id: Int = 1,
        profileImage: String = "https://example.com/profile.png",
        comment: String = "Launch is proceeding nominally. All systems are go.",
        infoUrl: String = "https://example.com/info",
        createdBy: String = "SpaceX Official",
        createdOn: String = "2025-12-31T12:00:00Z"
    ) = LaunchUpdate(id, profileImage, comment, infoUrl, createdBy, createdOn)

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
        priority, source, publisher, title, description, featureImage,
        url, startTime, endTime, live
    )

    fun createMissionPatch(
        id: Int = 1,
        name: String = "Starlink Patch",
        priority: Int = 10,
        imageUrl: String = "https://example.com/patch.png",
        agency: Agency = createAgency(),
    ) = MissionPatch(id, name, priority, imageUrl, agency)

    fun createLandingLocation(
        id: Int = 1,
        name: String = "Of Course I Still Love You",
        abbrev: String = "OCISLY",
        description: String = "Autonomous spaceport drone ship"
    ) = LandingLocation(id, name, abbrev, description)

    fun createLandingType(
        id: Int = 1,
        name: String = "Autonomous Spaceport Drone Ship",
        abbrev: String = "ASDS",
        description: String = "Autonomous drone ship for rocket recovery at sea"
    ) = LandingType(id, name, abbrev, description)

    fun createLanding(
        id: Int = 1,
        attempt: Boolean = true,
        success: Boolean = true,
        description: String = "The first stage successfully landed on the droneship.",
        location: LandingLocation = createLandingLocation(),
        type: LandingType = createLandingType()
    ) = Landing(id, attempt, success, description, location, type)

    fun createPreviousFlight(
        id: String = "prev-flight-123",
        name: String = "Starlink Group 14-8"
    ) = PreviousFlight(id, name)

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
        id, url, flightProven, serialNumber, status, details, image,
        successfulLandings, attemptedLandings, flights, lastLaunchDate, firstLaunchDate
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
        id, type, reused, launcherFlightNumber, launcher, landing,
        previousFlightDate, turnAroundTime, previousFlight
    )

    fun createSpacecraftStatus(
        id: Int = 1,
        name: String = "Active"
    ) = SpacecraftStatus(id, name)

    fun createSpacecraftType(
        id: Int = 1,
        name: String = "Cargo"
    ) = SpacecraftType(id, name)

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
        id, url, name, type, agency, inUse, capability, history, details,
        maidenFlight, height, diameter, humanRated, crewCapacity
    )

    fun createSpacecraft(
        id: Int = 45,
        url: String = "https://lldev.thespacedevs.com/2.3.0/spacecraft/45/",
        name: String = "Dragon C208",
        serialNumber: String = "C208",
        status: SpacecraftStatus = createSpacecraftStatus(),
        description: String = "Dragon spacecraft for cargo missions",
        spacecraftConfig: SpacecraftConfig = createSpacecraftConfig()
    ) = Spacecraft(id, url, name, serialNumber, status, description, spacecraftConfig)

    fun createSpacecraftStage(
        id: Int = 1,
        url: String = "https://lldev.thespacedevs.com/2.3.0/spacecraft_stage/1/",
        destination: String = "International Space Station",
        missionEnd: String = "2026-01-15T12:00:00Z",
        spacecraft: Spacecraft = createSpacecraft(),
        landing: Landing = createLanding()
    ) = SpacecraftStage(id, url, destination, missionEnd, spacecraft, landing)

    fun createRocket(
        id: Int = 7815,
        configuration: Configuration = createConfiguration(),
        launcherStage: List<LauncherStage> = listOf(createLauncherStage()),
        spacecraftStage: List<SpacecraftStage> = listOf(createSpacecraftStage())
    ) = Rocket(id, configuration, launcherStage, spacecraftStage)

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
        id, url, name, lastUpdated, net, netPrecision, status, windowEnd, windowStart,
        image, infographic, probability, weatherConcerns, failReason,
        launchServiceProvider, rocket, mission, pad, webcastLive, program,
        orbitalLaunchAttemptCount, locationLaunchAttemptCount, padLaunchAttemptCount,
        agencyLaunchAttemptCount, orbitalLaunchAttemptCountYear, locationLaunchAttemptCountYear,
        padLaunchAttemptCountYear, agencyLaunchAttemptCountYear, updates, infoUrls,
        vidUrls, padTurnaround, missionPatches
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
