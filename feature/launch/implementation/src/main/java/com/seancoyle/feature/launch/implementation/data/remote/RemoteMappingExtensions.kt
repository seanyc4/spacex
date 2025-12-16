package com.seancoyle.feature.launch.implementation.data.remote

import com.seancoyle.core.common.result.DataError.RemoteError
import com.seancoyle.feature.launch.api.domain.model.Agency
import com.seancoyle.feature.launch.api.domain.model.Configuration
import com.seancoyle.feature.launch.api.domain.model.Country
import com.seancoyle.feature.launch.api.domain.model.Family
import com.seancoyle.feature.launch.api.domain.model.Image
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.api.domain.model.Location
import com.seancoyle.feature.launch.api.domain.model.Mission
import com.seancoyle.feature.launch.api.domain.model.NetPrecision
import com.seancoyle.feature.launch.api.domain.model.Orbit
import com.seancoyle.feature.launch.api.domain.model.Pad
import com.seancoyle.feature.launch.api.domain.model.Program
import com.seancoyle.feature.launch.api.domain.model.Rocket
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

internal fun LaunchesDto.toDomain(): List<LaunchTypes.Launch> {
    return results?.mapNotNull { launchDto ->
        launchDto.toDomain(this.count ?: 0)
    } ?: emptyList()
}

private fun LaunchDto.toDomain(count: Int): LaunchTypes.Launch? {
    val launchId = id ?: return null
    val launchName = name ?: return null
    val launchDate = net ?: return null

    return LaunchTypes.Launch(
        id = launchId,
        count = count,
        url = url,
        name = launchName,
        responseMode = responseMode,
        lastUpdated = lastUpdated,
        net = net,
        netPrecision = netPrecision?.toDomain(),
        windowEnd = windowEnd,
        windowStart = windowStart,
        image = image?.toDomain(),
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
        launchDate = launchDate,
        launchDateLocalDateTime = null,
        launchYear = null,
        launchDateStatus = null,
        launchStatus = status.toDomain(),
        launchDays = null
    )
}

private fun StatusDto?.toDomain(): LaunchStatus =
    when {
        this?.abbrev?.contains("Success", ignoreCase = true) == true -> LaunchStatus.SUCCESS
        this?.abbrev?.contains("Go", ignoreCase = true) == true -> LaunchStatus.SUCCESS
        this?.abbrev?.contains("Fail", ignoreCase = true) == true -> LaunchStatus.FAILED
        this?.abbrev?.contains("To Be Confirmed", ignoreCase = true) == true -> LaunchStatus.UNKNOWN
        this?.abbrev?.contains(
            "To Be Determined",
            ignoreCase = true
        ) == true -> LaunchStatus.UNKNOWN

        else -> LaunchStatus.UNKNOWN
    }

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
        imageUrl = imageUrl,
        thumbnailUrl = thumbnailUrl,
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
        parent = parent,
        image = image?.toDomain(),
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
        configuration = configuration?.toDomain()
    )

private fun ConfigurationDto.toDomain() =
    Configuration(
        id = id,
        url = url,
        name = name,
        fullName = fullName,
        variant = variant,
        families = families?.map { it.toDomain() }
    )

private fun FamilyDto.toDomain() =
    Family(
        id = id,
        name = name
    )

private fun MissionDto.toDomain() =
    Mission(
        id = id,
        name = name,
        type = type,
        description = description,
        orbit = orbit?.toDomain(),
        agencies = agencies?.map { it.toDomain() }
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
        name = name,
        location = location?.toDomain(),
        latitude = latitude,
        longitude = longitude,
        mapUrl = mapUrl,
        totalLaunchCount = totalLaunchCount
    )

private fun LocationDto.toDomain() =
    Location(
        id = id,
        url = url,
        name = name,
        country = country?.toDomain(),
        description = description,
        timezoneName = timezoneName,
        totalLaunchCount = totalLaunchCount
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
        image = image?.toDomain(),
        startDate = startDate,
        endDate = endDate,
        agencies = agencies?.map { it.toDomain() }
    )
