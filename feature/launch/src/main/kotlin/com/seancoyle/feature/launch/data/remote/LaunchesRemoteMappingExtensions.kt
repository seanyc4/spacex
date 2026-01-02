package com.seancoyle.feature.launch.data.remote

import com.seancoyle.core.common.result.DataError.RemoteError
import com.seancoyle.feature.launch.presentation.LaunchesConstants
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
import com.seancoyle.feature.launch.domain.model.Launcher
import com.seancoyle.feature.launch.domain.model.LauncherStage
import com.seancoyle.feature.launch.domain.model.LaunchUpdate
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
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

internal fun map(throwable: Throwable): RemoteError {
    return when (throwable) {
        is TimeoutCancellationException -> RemoteError.NETWORK_TIMEOUT
        is IOException -> RemoteError.NETWORK_CONNECTION_FAILED
        is HttpException -> {
            when (throwable.code()) {
                401 -> RemoteError.NETWORK_UNAUTHORIZED
                403 -> RemoteError.NETWORK_FORBIDDEN
                404 -> RemoteError.NETWORK_NOT_FOUND
                408 -> RemoteError.NETWORK_TIMEOUT
                413 -> RemoteError.NETWORK_PAYLOAD_TOO_LARGE
                500 -> RemoteError.NETWORK_INTERNAL_SERVER_ERROR
                else -> RemoteError.NETWORK_UNKNOWN_ERROR
            }
        }

        is CancellationException -> throw throwable
        else -> RemoteError.NETWORK_UNKNOWN_ERROR
    }
}

internal fun LaunchesDto.toDomain(): List<Launch> {
    return results?.mapNotNull { launchDto ->
        launchDto.toDomain()
    } ?: emptyList()
}

internal fun LaunchDto.toDomain(): Launch? {
    val launchId = id ?: return null
    val launchName = name ?: return null

    return Launch(
        id = launchId,
        url = url,
        missionName = launchName,
        lastUpdated = lastUpdated,
        net = net,
        netPrecision = netPrecision?.toDomain(),
        windowEnd = windowEnd,
        windowStart = windowStart,
        image = image?.toDomain() ?: defaultImage(),
        infographic = infographic,
        probability = probability,
        weatherConcerns = weatherConcerns,
        failReason = failReason,
        launchServiceProvider = launchServiceProvider?.toDomain(),
        rocket = rocket?.toDomain(),
        mission = mission?.toDomain(),
        pad = pad?.toDomain(),
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
        status = status?.toDomain(),
    )
}

private fun StatusDto?.toDomain() = Status(
    id = this?.id,
    name = this?.name,
    abbrev = this?.abbrev,
    description = this?.description
)

private fun NetPrecisionDto.toDomain() =
    NetPrecision(
        id = id,
        name = name,
        abbrev = abbrev,
        description = description
    )

private fun ImageDto.toDomain() =
    Image(
        id = id,
        name = name,
        imageUrl = imageUrl ?: LaunchesConstants.DEFAULT_LAUNCH_IMAGE,
        thumbnailUrl = thumbnailUrl ?: LaunchesConstants.DEFAULT_LAUNCH_THUMBNAIL,
        credit = credit
    )

private fun AgencyDto.toDomain() =
    Agency(
        id = id,
        url = url,
        name = name,
        abbrev = abbrev,
        type = type?.name,
        featured = featured,
        country = country?.map { it.toDomain() },
        description = description,
        administrator = administrator,
        foundingYear = foundingYear,
        launchers = launchers,
        spacecraft = spacecraft,
        image = image?.toDomain() ?: defaultImage(),
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

private fun RocketDto.toDomain() =
    Rocket(
        id = id,
        configuration = configuration?.toDomain(),
        launcherStage = launcherStage?.map { it.toDomain() },
        spacecraftStage = spacecraftStage?.map { it.toDomain() }
    )

private fun ConfigurationDto.toDomain() =
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
        failedLaunches = failedLaunches
    )

private fun FamilyDto.toDomain() =
    Family(
        id = id,
        name = name,
        manufacturer = manufacturer?.map { it.toDomain() },
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

private fun MissionDto.toDomain() =
    Mission(
        id = id,
        name = name,
        type = type,
        description = description,
        orbit = orbit?.toDomain(),
        agencies = agencies?.map { it.toDomain() },
        infoUrls = infoUrls?.map { it.toDomain() },
        vidUrls = vidUrls?.map { it.toDomain() }
    )

private fun OrbitDto.toDomain() =
    Orbit(
        id = id,
        name = name,
        abbrev = abbrev
    )

private fun PadDto.toDomain() =
    Pad(
        id = id,
        url = url,
        agencies = agencies?.map { it.toDomain() },
        name = name,
        image = image?.toDomain() ?: defaultImage(),
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
        location = location?.toDomain()
    )

private fun LocationDto.toDomain() =
    Location(
        id = id,
        url = url,
        name = name,
        country = country?.toDomain(),
        description = description,
        image = image?.toDomain(),
        mapImage = mapImage,
        longitude = longitude,
        latitude = latitude,
        timezoneName = timezoneName,
        totalLaunchCount = totalLaunchCount,
        totalLandingCount = totalLandingCount
    )

private fun CountryDto.toDomain() =
    Country(
        id = id,
        name = name,
        alpha2Code = alpha2Code,
        alpha3Code = alpha3Code,
        nationalityName = nationalityName
    )

private fun ProgramDto.toDomain() =
    Program(
        id = id,
        url = url,
        name = name,
        description = description,
        image = image?.toDomain() ?: defaultImage(),
        startDate = startDate,
        endDate = endDate,
        agencies = agencies?.map { it.toDomain() }
    )

private fun defaultImage() =
    Image(
        id = null,
        name = null,
        imageUrl = LaunchesConstants.DEFAULT_LAUNCH_IMAGE,
        thumbnailUrl = LaunchesConstants.DEFAULT_LAUNCH_THUMBNAIL,
        credit = null
    )

private fun LaunchUpdateDto.toDomain() =
    LaunchUpdate(
        id = id,
        profileImage = profileImage,
        comment = comment,
        infoUrl = infoUrl,
        createdBy = createdBy,
        createdOn = createdOn
    )

private fun VidUrlDto.toDomain() =
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

private fun MissionPatchDto.toDomain() =
    MissionPatch(
        id = id,
        name = name,
        priority = priority,
        imageUrl = imageUrl,
        agency = agency?.toDomain()
    )

private fun InfoUrlDto.toDomain() = InfoUrl(
    priority = priority,
    source = source,
    title = title,
    description = description,
    featureImage = featureImage,
    url = url,
)

private fun LauncherStageDto.toDomain() = LauncherStage(
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

private fun LauncherDto.toDomain() = Launcher(
    id = id,
    url = url,
    flightProven = flightProven,
    serialNumber = serialNumber,
    status = status?.toDomainStatus(),
    details = details,
    image = image?.toDomain() ?: defaultImage(),
    successfulLandings = successfulLandings,
    attemptedLandings = attemptedLandings,
    flights = flights,
    lastLaunchDate = lastLaunchDate,
    firstLaunchDate = firstLaunchDate
)

private fun LandingDto.toDomain() = Landing(
    id = id,
    attempt = attempt,
    success = success,
    description = description,
    location = location?.toDomain(),
    type = type?.toDomain()
)

private fun LandingLocationDto.toDomain() = LandingLocation(
    id = id,
    name = name,
    abbrev = abbrev,
    description = description
)

private fun LandingTypeDto.toDomain() = LandingType(
    id = id,
    name = name,
    abbrev = abbrev,
    description = description
)

private fun PreviousFlightDto.toDomain() = PreviousFlight(
    id = id,
    name = name
)

private fun SpacecraftStageDto.toDomain() = SpacecraftStage(
    id = id,
    url = url,
    destination = destination,
    missionEnd = missionEnd,
    spacecraft = spacecraft?.toDomain(),
    landing = landing?.toDomain()
)

private fun SpacecraftDto.toDomain() = Spacecraft(
    id = id,
    url = url,
    name = name,
    serialNumber = serialNumber,
    status = status?.toDomain(),
    description = description,
    spacecraftConfig = spacecraftConfig?.toDomain()
)

private fun SpacecraftStatusDto.toDomain() = SpacecraftStatus(
    id = id,
    name = name
)

private fun SpacecraftConfigDto.toDomain() = SpacecraftConfig(
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

private fun SpacecraftTypeDto.toDomain() = SpacecraftType(
    id = id,
    name = name
)

private fun StatusDto.toDomainStatus() = Status(
    id = id,
    name = name,
    abbrev = abbrev,
    description = description
)
