package com.seancoyle.feature.launch.data.local

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import com.seancoyle.core.common.result.DataError.LocalError
import com.seancoyle.database.entities.AgencyEntity
import com.seancoyle.database.entities.ConfigurationEntity
import com.seancoyle.database.entities.CountryEntity
import com.seancoyle.database.entities.FamilyEntity
import com.seancoyle.database.entities.ImageEntity
import com.seancoyle.database.entities.InfoUrlEntity
import com.seancoyle.database.entities.LandingEntity
import com.seancoyle.database.entities.LandingLocationEntity
import com.seancoyle.database.entities.LandingTypeEntity
import com.seancoyle.database.entities.LaunchEntity
import com.seancoyle.database.entities.LaunchStatusEntity
import com.seancoyle.database.entities.LaunchSummaryEntity
import com.seancoyle.database.entities.LaunchUpdateEntity
import com.seancoyle.database.entities.LauncherEntity
import com.seancoyle.database.entities.LauncherStageEntity
import com.seancoyle.database.entities.MissionEntity
import com.seancoyle.database.entities.MissionPatchEntity
import com.seancoyle.database.entities.NetPrecisionEntity
import com.seancoyle.database.entities.OrbitEntity
import com.seancoyle.database.entities.PadEntity
import com.seancoyle.database.entities.PreviousFlightEntity
import com.seancoyle.database.entities.ProgramEntity
import com.seancoyle.database.entities.RocketEntity
import com.seancoyle.database.entities.SpacecraftConfigEntity
import com.seancoyle.database.entities.SpacecraftEntity
import com.seancoyle.database.entities.SpacecraftStageEntity
import com.seancoyle.database.entities.SpacecraftStatusEntity
import com.seancoyle.database.entities.SpacecraftTypeEntity
import com.seancoyle.database.entities.VidUrlEntity
import com.seancoyle.feature.launch.domain.model.Agency
import com.seancoyle.feature.launch.domain.model.Configuration
import com.seancoyle.feature.launch.domain.model.Country
import com.seancoyle.feature.launch.domain.model.Family
import com.seancoyle.feature.launch.domain.model.Image
import com.seancoyle.feature.launch.domain.model.InfoUrl
import com.seancoyle.feature.launch.domain.model.Landing
import com.seancoyle.feature.launch.domain.model.LandingLocation
import com.seancoyle.feature.launch.domain.model.LandingType
import com.seancoyle.feature.launch.domain.model.Launch
import com.seancoyle.feature.launch.domain.model.LaunchSummary
import com.seancoyle.feature.launch.domain.model.LaunchUpdate
import com.seancoyle.feature.launch.domain.model.Launcher
import com.seancoyle.feature.launch.domain.model.LauncherStage
import com.seancoyle.feature.launch.domain.model.Location
import com.seancoyle.feature.launch.domain.model.Mission
import com.seancoyle.feature.launch.domain.model.MissionPatch
import com.seancoyle.feature.launch.domain.model.NetPrecision
import com.seancoyle.feature.launch.domain.model.Orbit
import com.seancoyle.feature.launch.domain.model.Pad
import com.seancoyle.feature.launch.domain.model.PreviousFlight
import com.seancoyle.feature.launch.domain.model.Program
import com.seancoyle.feature.launch.domain.model.Rocket
import com.seancoyle.feature.launch.domain.model.Spacecraft
import com.seancoyle.feature.launch.domain.model.SpacecraftConfig
import com.seancoyle.feature.launch.domain.model.SpacecraftStage
import com.seancoyle.feature.launch.domain.model.SpacecraftStatus
import com.seancoyle.feature.launch.domain.model.SpacecraftType
import com.seancoyle.feature.launch.domain.model.Status
import com.seancoyle.feature.launch.domain.model.VidUrl
import kotlinx.coroutines.TimeoutCancellationException

internal fun map(throwable: Throwable): LocalError {
    return when (throwable) {
        is TimeoutCancellationException -> LocalError.CACHE_ERROR_TIMEOUT
        is SQLiteConstraintException -> LocalError.CACHE_CONSTRAINT_VIOLATION
        is SQLiteException -> LocalError.CACHE_ERROR
        is NullPointerException -> LocalError.CACHE_DATA_NULL
        is IllegalStateException -> LocalError.CACHE_DATA_NULL
        else -> LocalError.CACHE_UNKNOWN_DATABASE_ERROR
    }
}

internal fun LaunchSummaryEntity.toDomain(): LaunchSummary =
    LaunchSummary(
        id = id,
        missionName = missionName,
        net = net,
        imageUrl = imageUrl,
        status = status.toDomain()
    )

internal fun List<LaunchSummary>.toEntity(): List<LaunchSummaryEntity> =
    map { it.toEntity() }

internal fun LaunchSummary.toEntity(): LaunchSummaryEntity =
    LaunchSummaryEntity(
        id = id,
        missionName = missionName,
        net = net,
        imageUrl = imageUrl,
        status = status.toEntity()
    )

internal fun List<LaunchEntity>.toDomain(): List<Launch> =
    map { it.toDomain() }

internal fun LaunchEntity.toDomain(): Launch =
    Launch(
        id = id,
        url = url,
        missionName = name,
        lastUpdated = lastUpdated,
        net = net,
        netPrecision = netPrecision?.toDomain(),
        windowEnd = windowEnd,
        windowStart = windowStart,
        image = image.toDomain(),
        infographic = infographic,
        probability = probability,
        weatherConcerns = weatherConcerns,
        failReason = failReason,
        launchServiceProvider = launchServiceProvider?.toDomain(),
        rocket = rocket.toDomain(),
        mission = mission.toDomain(),
        pad = pad.toDomain(),
        webcastLive = webcastLive,
        program = program?.map { it.toDomain() },
        orbitalLaunchAttemptCount = orbitalLaunchAttemptCount,
        locationLaunchAttemptCount = locationLaunchAttemptCount,
        padLaunchAttemptCount = padLaunchAttemptCount,
        agencyLaunchAttemptCount = agencyLaunchAttemptCount,
        orbitalLaunchAttemptCountYear = orbitalLaunchAttemptCountYear,
        locationLaunchAttemptCountYear = locationLaunchAttemptCountYear,
        padLaunchAttemptCountYear = padLaunchAttemptCountYear,
        agencyLaunchAttemptCountYear = agencyLaunchAttemptCountYear,
        updates = updates?.map { it.toDomain() },
        infoUrls = infoUrls?.map { it.toDomain() },
        vidUrls = vidUrls?.map { it.toDomain() },
        padTurnaround = padTurnaround,
        missionPatches = missionPatches?.map { it.toDomain() },
        status = status.toDomain(),
    )

fun LaunchStatusEntity.toDomain(): Status =
    Status(
        id = id,
        name = name,
        abbrev = abbrev,
        description = description
    )

private fun NetPrecisionEntity.toDomain(): NetPrecision =
    NetPrecision(
        id = id,
        name = name,
        abbrev = abbrev,
        description = description
    )

private fun ImageEntity.toDomain(): Image =
    Image(
        id = id,
        name = name,
        imageUrl = imageUrl,
        thumbnailUrl = thumbnailUrl,
        credit = credit
    )

private fun AgencyEntity.toDomain(): Agency =
    Agency(
        id = id,
        url = url,
        name = name,
        abbrev = abbrev,
        type = type,
        featured = featured,
        country = country?.map { it.toDomain() },
        description = description,
        administrator = administrator,
        foundingYear = foundingYear,
        launchers = launchers,
        spacecraft = spacecraft,
        image = image.toDomain(),
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
        wikiUrl = wikiUrl,
    )

private fun CountryEntity.toDomain(): Country =
    Country(
        id = id,
        name = name,
        alpha2Code = alpha2Code,
        alpha3Code = alpha3Code,
        nationalityName = nationalityName
    )

private fun ConfigurationEntity.toDomain(): Configuration =
    Configuration(
        id = id,
        url = url,
        name = name,
        fullName = fullName,
        variant = variant,
        families = families?.map { it.toDomain() },
        manufacturer = manufacturer?.toDomain(),
        image = image?.toDomain(),
        wikiUrl = wikiUrl,
        description = description,
        alias = alias,
        totalLaunchCount = totalLaunchCount,
        successfulLaunches = successfulLaunches,
        failedLaunches = failedLaunches,
        length = length,
        diameter = diameter,
        maidenFlight = maidenFlight,
        launchMass = launchMass
    )

private fun FamilyEntity.toDomain(): Family =
    Family(
        id = id,
        name = name,
        manufacturer = manufacturer?.map { it?.toDomain() },
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

private fun RocketEntity.toDomain(): Rocket =
    Rocket(
        id = id,
        configuration = configuration.toDomain(),
        launcherStage = launcherStage?.map { it.toDomain() },
        spacecraftStage = spacecraftStage?.map { it.toDomain() }
    )

private fun MissionEntity.toDomain(): Mission =
    Mission(
        id = id,
        name = name,
        type = type,
        description = description,
        orbit = orbit?.toDomain(),
        agencies = agencies?.map { it?.toDomain() },
        infoUrls = infoUrls?.map { it.toDomain() },
        vidUrls = vidUrls?.map { it.toDomain() }
    )

private fun OrbitEntity.toDomain(): Orbit =
    Orbit(
        id = id,
        name = name,
        abbrev = abbrev
    )

private fun PadEntity.toDomain(): Pad =
    Pad(
        id = id,
        url = url,
        agencies = agencies?.map { it?.toDomain() },
        name = name,
        image = image.toDomain(),
        description = description,
        country = country?.toDomain(),
        latitude = latitude,
        longitude = longitude,
        mapUrl = mapUrl,
        mapImage = mapImage,
        wikiUrl = wikiUrl,
        infoUrl = infoUrl,
        totalLaunchCount = totalLaunchCount,
        orbitalLaunchAttemptCount = orbitalLaunchAttemptCount,
        fastestTurnaround = fastestTurnaround,
        location = toLocation()
    )

private fun PadEntity.toLocation(): Location? =
    locationId?.let { locId ->
        locationName?.let { locName ->
            Location(
                id = locId,
                url = locationUrl,
                name = locName,
                country = country?.toDomain(),
                description = locationDescription,
                image = image.toDomain(),
                mapImage = locationMapImage,
                longitude = locationLongitude,
                latitude = locationLatitude,
                timezoneName = locationTimezone,
                totalLaunchCount = locationTotalLaunchCount,
                totalLandingCount = locationTotalLandingCount
            )
        }
    }

private fun ProgramEntity.toDomain(): Program =
    Program(
        id = id,
        url = url,
        name = name,
        description = description,
        image = image.toDomain(),
        startDate = startDate,
        endDate = endDate,
        agencies = agencies?.map { it.toDomain() }
    )

@JvmName("launchListToEntity")
internal fun List<Launch>.toEntity(): List<LaunchEntity> =
    map { it.toEntity() }

internal fun Launch.toEntity(): LaunchEntity =
    LaunchEntity(
        id = id,
        url = url,
        name = missionName,
        lastUpdated = lastUpdated,
        net = net,
        netPrecision = netPrecision?.toEntity(),
        windowEnd = windowEnd,
        windowStart = windowStart,
        image = image.toEntity(),
        infographic = infographic,
        probability = probability,
        weatherConcerns = weatherConcerns,
        failReason = failReason,
        launchServiceProvider = launchServiceProvider?.toEntity(),
        rocket = rocket.toEntity(),
        mission = mission.toEntity(),
        pad = pad.toEntity(),
        program = program?.map { it.toEntity() },
        webcastLive = webcastLive,
        orbitalLaunchAttemptCount = orbitalLaunchAttemptCount,
        locationLaunchAttemptCount = locationLaunchAttemptCount,
        padLaunchAttemptCount = padLaunchAttemptCount,
        agencyLaunchAttemptCount = agencyLaunchAttemptCount,
        orbitalLaunchAttemptCountYear = orbitalLaunchAttemptCountYear,
        locationLaunchAttemptCountYear = locationLaunchAttemptCountYear,
        padLaunchAttemptCountYear = padLaunchAttemptCountYear,
        agencyLaunchAttemptCountYear = agencyLaunchAttemptCountYear,
        updates = updates?.map { it.toEntity() },
        infoUrls = infoUrls?.map { it.toEntity() },
        vidUrls = vidUrls?.map { it.toEntity() },
        padTurnaround = padTurnaround,
        missionPatches = missionPatches?.map { it.toEntity() },
        status = status.toEntity(),
        configuration = rocket.configuration.toEntity(),
        families = rocket.configuration.families?.map { it.toEntity() }
    )

private fun Status.toEntity(): LaunchStatusEntity =
    LaunchStatusEntity(
        id = id,
        name = name,
        abbrev = abbrev,
        description = description
    )

private fun NetPrecision.toEntity(): NetPrecisionEntity =
    NetPrecisionEntity(
        id = id,
        name = name,
        abbrev = abbrev,
        description = description
    )

private fun Image.toEntity(): ImageEntity =
    ImageEntity(
        id = id,
        name = name,
        imageUrl = imageUrl,
        thumbnailUrl = thumbnailUrl,
        credit = credit
    )

private fun Agency.toEntity(): AgencyEntity =
    AgencyEntity(
        id = id,
        url = url,
        name = name,
        abbrev = abbrev,
        type = type,
        featured = featured,
        country = country?.map { it.toEntity() },
        description = description,
        administrator = administrator,
        foundingYear = foundingYear,
        launchers = launchers,
        spacecraft = spacecraft,
        image = image.toEntity(),
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
        wikiUrl = wikiUrl,
    )

private fun Country.toEntity(): CountryEntity =
    CountryEntity(
        id = id,
        name = name,
        alpha2Code = alpha2Code,
        alpha3Code = alpha3Code,
        nationalityName = nationalityName
    )

private fun Configuration.toEntity(): ConfigurationEntity =
    ConfigurationEntity(
        id = id,
        url = url,
        name = name,
        fullName = fullName,
        variant = variant,
        families = families?.map { it.toEntity() },
        manufacturer = manufacturer?.toEntity(),
        image = image?.toEntity(),
        wikiUrl = wikiUrl,
        description = description,
        alias = alias,
        totalLaunchCount = totalLaunchCount,
        successfulLaunches = successfulLaunches,
        failedLaunches = failedLaunches,
        length = length,
        diameter = diameter,
        maidenFlight = maidenFlight,
        launchMass = launchMass
    )

private fun Family.toEntity(): FamilyEntity =
    FamilyEntity(
        id = id,
        name = name,
        manufacturer = manufacturer?.map { it?.toEntity() },
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

private fun Rocket.toEntity(): RocketEntity =
    RocketEntity(
        id = id,
        configuration = configuration.toEntity(),
        launcherStage = launcherStage?.map { it.toEntity() },
        spacecraftStage = spacecraftStage?.map { it.toEntity() }
    )

private fun Mission.toEntity(): MissionEntity =
    MissionEntity(
        id = id,
        name = name,
        type = type,
        description = description,
        orbit = orbit?.toEntity(),
        agencies = agencies?.mapNotNull { it?.toEntity() },
        infoUrls = infoUrls?.map { it.toEntity() },
        vidUrls = vidUrls?.map { it.toEntity() }
    )

private fun Orbit.toEntity(): OrbitEntity =
    OrbitEntity(
        id = id,
        name = name,
        abbrev = abbrev
    )

private fun Pad.toEntity(): PadEntity =
    PadEntity(
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
        locationId = location?.id,
        locationUrl = location?.url,
        locationName = location?.name,
        locationDescription = location?.description,
        locationTimezone = location?.timezoneName,
        locationTotalLaunchCount = location?.totalLaunchCount,
        locationTotalLandingCount = location?.totalLandingCount,
        locationLongitude = location?.longitude,
        locationLatitude = location?.latitude,
        locationMapImage = location?.mapImage,
        agencies = agencies?.map { it?.toEntity() },
        image = image.toEntity(),
        country = country?.toEntity()
    )

private fun Program.toEntity(): ProgramEntity =
    ProgramEntity(
        id = id,
        url = url,
        name = name,
        description = description,
        image = image.toEntity(),
        startDate = startDate,
        endDate = endDate,
        agencies = agencies?.mapNotNull { it?.toEntity() }
    )

private fun LaunchUpdateEntity.toDomain(): LaunchUpdate =
    LaunchUpdate(
        id = id,
        profileImage = profileImage,
        comment = comment,
        infoUrl = infoUrl,
        createdBy = createdBy,
        createdOn = createdOn
    )

private fun VidUrlEntity.toDomain(): VidUrl =
    VidUrl(
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

private fun MissionPatchEntity.toDomain(): MissionPatch =
    MissionPatch(
        id = id,
        name = name,
        priority = priority,
        imageUrl = imageUrl,
        agency = agency?.toDomain()
    )

private fun InfoUrlEntity.toDomain() = InfoUrl(
    priority = priority,
    source = source,
    title = title,
    description = description,
    featureImage = featureImage,
    url = url
)

// Domain to Entity for new fields
private fun LaunchUpdate.toEntity(): LaunchUpdateEntity =
    LaunchUpdateEntity(
        id = id,
        profileImage = profileImage,
        comment = comment,
        infoUrl = infoUrl,
        createdBy = createdBy,
        createdOn = createdOn
    )

private fun VidUrl.toEntity(): VidUrlEntity =
    VidUrlEntity(
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

private fun MissionPatch.toEntity(): MissionPatchEntity =
    MissionPatchEntity(
        id = id,
        name = name,
        priority = priority,
        imageUrl = imageUrl,
        agency = agency?.toEntity()
    )

private fun InfoUrl.toEntity() = InfoUrlEntity(
    priority = priority,
    source = source,
    title = title,
    description = description,
    featureImage = featureImage,
    url = url
)

private fun LauncherStageEntity.toDomain(): LauncherStage =
    LauncherStage(
        id = id,
        type = type,
        reused = reused,
        launcherFlightNumber = launcherFlightNumber,
        launcher = launcher?.toDomain(),
        landing = landing?.toDomain(),
        previousFlightDate = previousFlightDate,
        turnAroundTime = turnAroundTime,
        previousFlight = previousFlight?.toDomain()
    )

private fun LauncherStage.toEntity(): LauncherStageEntity =
    LauncherStageEntity(
        id = id,
        type = type,
        reused = reused,
        launcherFlightNumber = launcherFlightNumber,
        launcher = launcher?.toEntity(),
        landing = landing?.toEntity(),
        previousFlightDate = previousFlightDate,
        turnAroundTime = turnAroundTime,
        previousFlight = previousFlight?.toEntity()
    )

private fun LauncherEntity.toDomain(): Launcher =
    Launcher(
        id = id,
        url = url,
        flightProven = flightProven,
        serialNumber = serialNumber,
        status = status?.toDomainStatus(),
        details = details,
        image = image.toDomain(),
        successfulLandings = successfulLandings,
        attemptedLandings = attemptedLandings,
        flights = flights,
        lastLaunchDate = lastLaunchDate,
        firstLaunchDate = firstLaunchDate
    )

private fun Launcher.toEntity(): LauncherEntity =
    LauncherEntity(
        id = id,
        url = url,
        flightProven = flightProven,
        serialNumber = serialNumber,
        status = status?.toEntityStatus(),
        details = details,
        image = image.toEntity(),
        successfulLandings = successfulLandings,
        attemptedLandings = attemptedLandings,
        flights = flights,
        lastLaunchDate = lastLaunchDate,
        firstLaunchDate = firstLaunchDate
    )

private fun LandingEntity.toDomain(): Landing =
    Landing(
        id = id,
        attempt = attempt,
        success = success,
        description = description,
        location = location?.toDomain(),
        type = type?.toDomain()
    )

private fun Landing.toEntity(): LandingEntity =
    LandingEntity(
        id = id,
        attempt = attempt,
        success = success,
        description = description,
        location = location?.toEntity(),
        type = type?.toEntity()
    )

// LandingLocation mappings
private fun LandingLocationEntity.toDomain(): LandingLocation =
    LandingLocation(
        id = id,
        name = name,
        abbrev = abbrev,
        description = description
    )

private fun LandingLocation.toEntity(): LandingLocationEntity =
    LandingLocationEntity(
        id = id,
        name = name,
        abbrev = abbrev,
        description = description
    )

private fun LandingTypeEntity.toDomain(): LandingType =
    LandingType(
        id = id,
        name = name,
        abbrev = abbrev,
        description = description
    )

private fun LandingType.toEntity(): LandingTypeEntity =
    LandingTypeEntity(
        id = id,
        name = name,
        abbrev = abbrev,
        description = description
    )

private fun PreviousFlightEntity.toDomain(): PreviousFlight =
    PreviousFlight(
        id = id,
        name = name
    )

private fun PreviousFlight.toEntity(): PreviousFlightEntity =
    PreviousFlightEntity(
        id = id,
        name = name
    )

private fun SpacecraftStageEntity.toDomain(): SpacecraftStage =
    SpacecraftStage(
        id = id,
        url = url,
        destination = destination,
        missionEnd = missionEnd,
        spacecraft = spacecraft?.toDomain(),
        landing = landing?.toDomain()
    )

private fun SpacecraftStage.toEntity(): SpacecraftStageEntity =
    SpacecraftStageEntity(
        id = id,
        url = url,
        destination = destination,
        missionEnd = missionEnd,
        spacecraft = spacecraft?.toEntity(),
        landing = landing?.toEntity()
    )

private fun SpacecraftEntity.toDomain(): Spacecraft =
    Spacecraft(
        id = id,
        url = url,
        name = name,
        serialNumber = serialNumber,
        status = status?.toDomain(),
        description = description,
        spacecraftConfig = spacecraftConfig?.toDomain()
    )

private fun Spacecraft.toEntity(): SpacecraftEntity =
    SpacecraftEntity(
        id = id,
        url = url,
        name = name,
        serialNumber = serialNumber,
        status = status?.toEntity(),
        description = description,
        spacecraftConfig = spacecraftConfig?.toEntity()
    )

private fun SpacecraftStatusEntity.toDomain(): SpacecraftStatus =
    SpacecraftStatus(
        id = id,
        name = name
    )

private fun SpacecraftStatus.toEntity(): SpacecraftStatusEntity =
    SpacecraftStatusEntity(
        id = id,
        name = name
    )

private fun SpacecraftConfigEntity.toDomain(): SpacecraftConfig =
    SpacecraftConfig(
        id = id,
        url = url,
        name = name,
        type = type?.toDomain(),
        agency = agency?.toDomain(),
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

private fun SpacecraftConfig.toEntity(): SpacecraftConfigEntity =
    SpacecraftConfigEntity(
        id = id,
        url = url,
        name = name,
        type = type?.toEntity(),
        agency = agency?.toEntity(),
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

private fun SpacecraftTypeEntity.toDomain(): SpacecraftType =
    SpacecraftType(
        id = id,
        name = name
    )

private fun SpacecraftType.toEntity(): SpacecraftTypeEntity =
    SpacecraftTypeEntity(
        id = id,
        name = name
    )

private fun LaunchStatusEntity.toDomainStatus(): Status =
    Status(
        id = id,
        name = name,
        abbrev = abbrev,
        description = description
    )

private fun Status.toEntityStatus(): LaunchStatusEntity =
    LaunchStatusEntity(
        id = id,
        name = name,
        abbrev = abbrev,
        description = description
    )
