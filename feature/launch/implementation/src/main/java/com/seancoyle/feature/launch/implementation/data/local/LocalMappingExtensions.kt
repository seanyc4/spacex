package com.seancoyle.feature.launch.implementation.data.local

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import com.seancoyle.core.common.result.DataError.LocalError
import com.seancoyle.database.entities.AgencyEntity
import com.seancoyle.database.entities.CountryEntity
import com.seancoyle.database.entities.ImageEntity
import com.seancoyle.database.entities.LaunchDateStatusEntity
import com.seancoyle.database.entities.LaunchEntity
import com.seancoyle.database.entities.LaunchStatusEntity
import com.seancoyle.database.entities.MissionEntity
import com.seancoyle.database.entities.NetPrecisionEntity
import com.seancoyle.database.entities.OrbitEntity
import com.seancoyle.database.entities.PadEntity
import com.seancoyle.database.entities.ProgramEntity
import com.seancoyle.database.entities.RocketEntity
import com.seancoyle.feature.launch.implementation.domain.model.Agency
import com.seancoyle.feature.launch.implementation.domain.model.Configuration
import com.seancoyle.feature.launch.implementation.domain.model.Country
import com.seancoyle.feature.launch.implementation.domain.model.Image
import com.seancoyle.feature.launch.implementation.domain.model.LaunchDateStatus
import com.seancoyle.feature.launch.implementation.domain.model.LaunchStatus
import com.seancoyle.feature.launch.implementation.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.domain.model.Location
import com.seancoyle.feature.launch.implementation.domain.model.Mission
import com.seancoyle.feature.launch.implementation.domain.model.NetPrecision
import com.seancoyle.feature.launch.implementation.domain.model.Orbit
import com.seancoyle.feature.launch.implementation.domain.model.Pad
import com.seancoyle.feature.launch.implementation.domain.model.Program
import com.seancoyle.feature.launch.implementation.domain.model.Rocket
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

// Entity to Domain mappings
internal fun List<LaunchEntity>.toDomain(): List<LaunchTypes.Launch> =
    map { it.toDomain() }

internal fun LaunchEntity.toDomain(): LaunchTypes.Launch =
    LaunchTypes.Launch(
        id = id,
        count = count,
        url = url,
        name = name,
        responseMode = responseMode,
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
        launchDateLocalDateTime = launchDateLocalDateTime,
        launchYear = launchYear,
        launchDateStatus = launchDateStatus?.toDomain(),
        launchStatus = launchStatus.toDomain(),
        launchDays = launchDays
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
        parent = parent,
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

private fun RocketEntity.toDomain(): Rocket =
    Rocket(
        id = id,
        configuration = Configuration(
            id = configurationId,
            url = configurationUrl,
            name = configurationName,
            fullName = configurationFullName,
            variant = variant,
            families = null
        )
    )

private fun MissionEntity.toDomain(): Mission =
    Mission(
        id = id,
        name = name,
        type = type,
        description = description,
        orbit = orbit?.toDomain(),
        agencies = agencies?.map { it.toDomain() }
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
        name = name,
        location = toLocation(),
        latitude = latitude,
        longitude = longitude,
        mapUrl = mapUrl,
        totalLaunchCount = totalLaunchCount
    )

private fun PadEntity.toLocation(): Location? =
    locationId?.let { locId ->
        locationName?.let { locName ->
            Location(
                id = locId,
                url = locationUrl,
                name = locName,
                country = null,
                description = locationDescription,
                timezoneName = locationTimezone,
                totalLaunchCount = locationTotalLaunchCount
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

private fun LaunchDateStatusEntity.toDomain(): LaunchDateStatus =
    when (this) {
        LaunchDateStatusEntity.PAST -> LaunchDateStatus.PAST
        LaunchDateStatusEntity.FUTURE -> LaunchDateStatus.FUTURE
    }

private fun LaunchStatusEntity.toDomain(): LaunchStatus =
    when (this) {
        LaunchStatusEntity.SUCCESS -> LaunchStatus.SUCCESS
        LaunchStatusEntity.FAILED -> LaunchStatus.FAILED
        LaunchStatusEntity.UNKNOWN -> LaunchStatus.UNKNOWN
        LaunchStatusEntity.ALL -> LaunchStatus.ALL
    }

// Domain to Entity mappings
internal fun List<LaunchTypes.Launch>.toEntity(): List<LaunchEntity> =
    map { it.toEntity() }

internal fun LaunchTypes.Launch.toEntity(): LaunchEntity =
    LaunchEntity(
        id = id,
        count = count,
        url = url,
        name = name,
        responseMode = responseMode,
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
        rocket = rocket?.toEntity(),
        mission = mission?.toEntity(),
        pad = pad?.toEntity(),
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
        launchDate = launchDate,
        launchDateLocalDateTime = launchDateLocalDateTime,
        launchYear = launchYear,
        launchDateStatus = launchDateStatus?.toEntity(),
        launchStatus = launchStatus.toEntity(),
        launchDays = launchDays
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
        parent = parent,
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

private fun Rocket.toEntity(): RocketEntity =
    RocketEntity(
        id = id,
        configurationId = configuration?.id,
        configurationUrl = configuration?.url,
        configurationName = configuration?.name,
        configurationFullName = configuration?.fullName,
        variant = configuration?.variant
    )

private fun Mission.toEntity(): MissionEntity =
    MissionEntity(
        id = id,
        name = name,
        type = type,
        description = description,
        orbit = orbit?.toEntity(),
        agencies = agencies?.mapNotNull { it?.toEntity() }
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
        locationId = location?.id,
        locationUrl = location?.url,
        locationName = location?.name,
        locationDescription = location?.description,
        locationTimezone = location?.timezoneName,
        locationTotalLaunchCount = location?.totalLaunchCount,
        latitude = latitude,
        longitude = longitude,
        mapUrl = mapUrl,
        totalLaunchCount = totalLaunchCount
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

private fun LaunchDateStatus.toEntity(): LaunchDateStatusEntity =
    when (this) {
        LaunchDateStatus.PAST -> LaunchDateStatusEntity.PAST
        LaunchDateStatus.FUTURE -> LaunchDateStatusEntity.FUTURE
    }

internal fun LaunchStatus.toEntity(): LaunchStatusEntity =
    when (this) {
        LaunchStatus.SUCCESS -> LaunchStatusEntity.SUCCESS
        LaunchStatus.FAILED -> LaunchStatusEntity.FAILED
        LaunchStatus.UNKNOWN -> LaunchStatusEntity.UNKNOWN
        LaunchStatus.ALL -> LaunchStatusEntity.ALL
    }
