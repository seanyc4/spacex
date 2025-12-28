package com.seancoyle.feature.launch.data.local

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import com.seancoyle.core.common.result.DataError.LocalError
import com.seancoyle.database.entities.LaunchStatusEntity
import com.seancoyle.feature.launch.domain.model.Launch
import com.seancoyle.feature.launch.domain.model.LaunchStatus
import com.seancoyle.feature.launch.util.TestData
import io.mockk.mockk
import kotlinx.coroutines.TimeoutCancellationException
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class LocalMappingExtensionsTest {

    @Test
    fun `map returns CACHE_ERROR_TIMEOUT for TimeoutCancellationException`() {
        val throwable = mockk<TimeoutCancellationException>()

        val result = map(throwable)

        assertEquals(LocalError.CACHE_ERROR_TIMEOUT, result)
    }

    @Test
    fun `map returns CACHE_CONSTRAINT_VIOLATION for SQLiteConstraintException`() {
        val throwable = SQLiteConstraintException("Constraint violation")

        val result = map(throwable)

        assertEquals(LocalError.CACHE_CONSTRAINT_VIOLATION, result)
    }

    @Test
    fun `map returns CACHE_ERROR for SQLiteException`() {
        val throwable = SQLiteException("SQLite error")

        val result = map(throwable)

        assertEquals(LocalError.CACHE_ERROR, result)
    }

    @Test
    fun `map returns CACHE_DATA_NULL for NullPointerException`() {
        val throwable = NullPointerException("Null pointer")

        val result = map(throwable)

        assertEquals(LocalError.CACHE_DATA_NULL, result)
    }

    @Test
    fun `map returns CACHE_DATA_NULL for IllegalStateException`() {
        val throwable = IllegalStateException("Illegal state")

        val result = map(throwable)

        assertEquals(LocalError.CACHE_DATA_NULL, result)
    }

    @Test
    fun `map returns CACHE_UNKNOWN_DATABASE_ERROR for generic exception`() {
        val throwable = RuntimeException("Generic error")

        val result = map(throwable)

        assertEquals(LocalError.CACHE_UNKNOWN_DATABASE_ERROR, result)
    }

    @Test
    fun `map returns CACHE_UNKNOWN_DATABASE_ERROR for IOException`() {
        val throwable = java.io.IOException("IO error")

        val result = map(throwable)

        assertEquals(LocalError.CACHE_UNKNOWN_DATABASE_ERROR, result)
    }

    @Test
    fun `LaunchEntity toDomain converts to LaunchTypes Launch correctly`() {
        val launchEntity = TestData.createLaunchEntity()

        val result = launchEntity.toDomain()

        assertNotNull(result)
        assertEquals(launchEntity.id, result.id)
        assertEquals(launchEntity.count, result.count)
        assertEquals(launchEntity.url, result.url)
        assertEquals(launchEntity.name, result.name)
        assertEquals(launchEntity.responseMode, result.responseMode)
        assertEquals(launchEntity.lastUpdated, result.lastUpdated)
        assertEquals(launchEntity.net, result.net)
        assertEquals(launchEntity.windowEnd, result.windowEnd)
        assertEquals(launchEntity.windowStart, result.windowStart)
        assertEquals(launchEntity.infographic, result.infographic)
        assertEquals(launchEntity.probability, result.probability)
        assertEquals(launchEntity.weatherConcerns, result.weatherConcerns)
        assertEquals(launchEntity.failReason, result.failReason)
        assertEquals(launchEntity.webcastLive, result.webcastLive)
    }

    @Test
    fun `LaunchEntity toDomain converts image correctly`() {
        val launchEntity = TestData.createLaunchEntity()

        val result = launchEntity.toDomain()

        assertNotNull(result.image)
        assertEquals(launchEntity.image.id, result.image.id)
        assertEquals(launchEntity.image.name, result.image.name)
        assertEquals(launchEntity.image.imageUrl, result.image.imageUrl)
        assertEquals(launchEntity.image.thumbnailUrl, result.image.thumbnailUrl)
        assertEquals(launchEntity.image.credit, result.image.credit)
    }

    @Test
    fun `LaunchEntity toDomain converts netPrecision correctly`() {
        val launchEntity = TestData.createLaunchEntity()

        val result = launchEntity.toDomain()

        assertNotNull(result.netPrecision)
        assertEquals(launchEntity.netPrecision?.id, result.netPrecision.id)
        assertEquals(launchEntity.netPrecision?.name, result.netPrecision.name)
        assertEquals(launchEntity.netPrecision?.abbrev, result.netPrecision.abbrev)
        assertEquals(launchEntity.netPrecision?.description, result.netPrecision.description)
    }

    @Test
    fun `LaunchEntity toDomain converts launchStatus correctly`() {
        val launchEntity = TestData.createLaunchEntity()

        val result = launchEntity.toDomain()

        assertEquals(LaunchStatus.SUCCESS, result.launchStatus)
    }

    @Test
    fun `List of LaunchEntity toDomain converts all items`() {
        val entity1 = TestData.createLaunchEntity(id = "1")
        val entity2 = TestData.createLaunchEntity(id = "2")
        val entities = listOf(entity1, entity2)

        val result = entities.toDomain()

        assertEquals(2, result.size)
        assertEquals("1", result[0].id)
        assertEquals("2", result[1].id)
    }

    @Test
    fun `LaunchTypes Launch toEntity converts to LaunchEntity correctly`() {
        val launch = TestData.createLaunch()

        val result = launch.toEntity()

        assertNotNull(result)
        assertEquals(launch.id, result.id)
        assertEquals(launch.count, result.count)
        assertEquals(launch.url, result.url)
        assertEquals(launch.name, result.name)
        assertEquals(launch.responseMode, result.responseMode)
        assertEquals(launch.lastUpdated, result.lastUpdated)
        assertEquals(launch.net, result.net)
        assertEquals(launch.windowEnd, result.windowEnd)
        assertEquals(launch.windowStart, result.windowStart)
        assertEquals(launch.infographic, result.infographic)
        assertEquals(launch.probability, result.probability)
        assertEquals(launch.weatherConcerns, result.weatherConcerns)
        assertEquals(launch.failReason, result.failReason)
        assertEquals(launch.webcastLive, result.webcastLive)
    }

    @Test
    fun `LaunchTypes Launch toEntity converts image correctly`() {
        val launch = TestData.createLaunch()

        val result = launch.toEntity()

        assertNotNull(result.image)
        assertEquals(launch.image.id, result.image.id)
        assertEquals(launch.image.name, result.image.name)
        assertEquals(launch.image.imageUrl, result.image.imageUrl)
        assertEquals(launch.image.thumbnailUrl, result.image.thumbnailUrl)
        assertEquals(launch.image.credit, result.image.credit)
    }

    @Test
    fun `LaunchTypes Launch toEntity converts netPrecision correctly`() {
        val launch = TestData.createLaunch()

        val result = launch.toEntity()

        assertNotNull(result.netPrecision)
        assertEquals(launch.netPrecision?.id, result.netPrecision?.id)
        assertEquals(launch.netPrecision?.name, result.netPrecision?.name)
        assertEquals(launch.netPrecision?.abbrev, result.netPrecision?.abbrev)
        assertEquals(launch.netPrecision?.description, result.netPrecision?.description)
    }

    @Test
    fun `LaunchTypes Launch toEntity converts launchStatus correctly`() {
        val launch = TestData.createLaunch()

        val result = launch.toEntity()

        assertEquals(LaunchStatusEntity.UNKNOWN, result.launchStatus)
    }

    @Test
    fun `List of LaunchTypes Launch toEntity converts all items`() {
        val launch1 = TestData.createLaunch(id = "1")
        val launch2 = TestData.createLaunch(id = "2")
        val launches = listOf(launch1, launch2)

        val result = launches.toEntity()

        assertEquals(2, result.size)
        assertEquals("1", result[0].id)
        assertEquals("2", result[1].id)
    }

    @Test
    fun `round trip entity to domain to entity maintains data integrity`() {
        val originalEntity = TestData.createLaunchEntity()

        val domain = originalEntity.toDomain()
        val entityAgain = domain.toEntity()

        assertEquals(originalEntity.id, entityAgain.id)
        assertEquals(originalEntity.count, entityAgain.count)
        assertEquals(originalEntity.name, entityAgain.name)
        assertEquals(originalEntity.url, entityAgain.url)
        assertEquals(originalEntity.image.imageUrl, entityAgain.image.imageUrl)
        assertEquals(originalEntity.launchStatus, entityAgain.launchStatus)
    }

    @Test
    fun `round trip domain to entity to domain maintains data integrity`() {
        val originalDomain = TestData.createLaunch()

        val entity = originalDomain.toEntity()
        val domainAgain = entity.toDomain()

        assertEquals(originalDomain.id, domainAgain.id)
        assertEquals(originalDomain.count, domainAgain.count)
        assertEquals(originalDomain.name, domainAgain.name)
        assertEquals(originalDomain.url, domainAgain.url)
        assertEquals(originalDomain.image.imageUrl, domainAgain.image.imageUrl)
        assertEquals(originalDomain.launchStatus, domainAgain.launchStatus)
    }

    @Test
    fun `LaunchStatus SUCCESS maps to LaunchStatusEntity SUCCESS`() {
        val status = LaunchStatus.SUCCESS

        val result = status.toEntity()

        assertEquals(LaunchStatusEntity.SUCCESS, result)
    }

    @Test
    fun `LaunchStatus FAILED maps to LaunchStatusEntity FAILED`() {
        val status = LaunchStatus.FAILED

        val result = status.toEntity()

        assertEquals(LaunchStatusEntity.FAILED, result)
    }

    @Test
    fun `LaunchStatus UNKNOWN maps to LaunchStatusEntity UNKNOWN`() {
        val status = LaunchStatus.UNKNOWN

        val result = status.toEntity()

        assertEquals(LaunchStatusEntity.UNKNOWN, result)
    }

    @Test
    fun `LaunchStatus ALL maps to LaunchStatusEntity ALL`() {
        val status = LaunchStatus.ALL

        val result = status.toEntity()

        assertEquals(LaunchStatusEntity.ALL, result)
    }

    @Test
    fun `empty list of LaunchEntity toDomain returns empty list`() {
        val entities = emptyList<com.seancoyle.database.entities.LaunchEntity>()

        val result = entities.toDomain()

        assertEquals(0, result.size)
    }

    @Test
    fun `empty list of LaunchTypes Launch toEntity returns empty list`() {
        val launches = emptyList<Launch>()

        val result = launches.toEntity()

        assertEquals(0, result.size)
    }

    @Test
    fun `LaunchEntity with null optional fields converts correctly`() {
        val launchEntity = TestData.createLaunchEntity(
            netPrecision = null,
            launchServiceProvider = null,
            rocket = null,
            mission = null,
            pad = null,
            program = null
        )

        val result = launchEntity.toDomain()

        assertNotNull(result)
        assertEquals(null, result.netPrecision)
        assertEquals(null, result.launchServiceProvider)
        assertEquals(null, result.rocket)
        assertEquals(null, result.mission)
        assertEquals(null, result.pad)
        assertEquals(null, result.program)
    }
}
