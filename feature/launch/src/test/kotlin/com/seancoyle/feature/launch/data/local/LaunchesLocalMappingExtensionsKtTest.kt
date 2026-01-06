package com.seancoyle.feature.launch.data.local

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import com.seancoyle.core.common.result.DataError.LocalError
import com.seancoyle.feature.launch.domain.model.Launch
import com.seancoyle.feature.launch.util.TestData
import io.mockk.mockk
import kotlinx.coroutines.TimeoutCancellationException
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class LaunchesLocalMappingExtensionsKtTest {

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
    fun `LaunchEntity toDomain converts all fields correctly including rocket stages`() {
        val updates = listOf(TestData.createLaunchUpdateEntity())
        val infoUrls = listOf(TestData.createInfoUrlEntity())
        val vidUrls = listOf(TestData.createVidUrlEntity())
        val padTurnaround = "P1DT2H"
        val missionPatches = listOf(TestData.createMissionPatchEntity())
        val rocketEntity = TestData.createRocketEntity(
            launcherStage = listOf(TestData.createLauncherStageEntity()),
            spacecraftStage = listOf(TestData.createSpacecraftStageEntity())
        )
        val launchEntity = TestData.createLaunchEntity(
            updates = updates,
            infoUrls = infoUrls,
            vidUrls = vidUrls,
            padTurnaround = padTurnaround,
            missionPatches = missionPatches,
            rocket = rocketEntity
        )

        val result = launchEntity.toDomain()

        // Assert basic fields
        assertNotNull(result)
        assertEquals(launchEntity.id, result.id)
        assertEquals(launchEntity.url, result.url)
        assertEquals(launchEntity.name, result.missionName)
        assertEquals(launchEntity.lastUpdated, result.lastUpdated)
        assertEquals(launchEntity.net, result.net)
        assertEquals(launchEntity.windowEnd, result.windowEnd)
        assertEquals(launchEntity.windowStart, result.windowStart)
        assertEquals(launchEntity.infographic, result.infographic)
        assertEquals(launchEntity.probability, result.probability)
        assertEquals(launchEntity.weatherConcerns, result.weatherConcerns)
        assertEquals(launchEntity.failReason, result.failReason)
        assertEquals(launchEntity.webcastLive, result.webcastLive)

        // Assert image
        assertNotNull(result.image)
        assertEquals(launchEntity.image.id, result.image.id)
        assertEquals(launchEntity.image.name, result.image.name)
        assertEquals(launchEntity.image.imageUrl, result.image.imageUrl)
        assertEquals(launchEntity.image.thumbnailUrl, result.image.thumbnailUrl)
        assertEquals(launchEntity.image.credit, result.image.credit)

        // Assert netPrecision
        assertNotNull(result.netPrecision)
        assertEquals(launchEntity.netPrecision?.id, result.netPrecision.id)
        assertEquals(launchEntity.netPrecision?.name, result.netPrecision.name)
        assertEquals(launchEntity.netPrecision?.abbrev, result.netPrecision.abbrev)
        assertEquals(launchEntity.netPrecision?.description, result.netPrecision.description)

        // Assert updates, infoUrls, vidUrls
        assertEquals(updates[0].comment, result.updates?.get(0)?.comment)
        assertEquals(infoUrls[0].url, result.infoUrls?.get(0)?.url)
        assertEquals(vidUrls[0].title, result.vidUrls?.get(0)?.title)

        // Assert padTurnaround and missionPatches
        assertEquals(padTurnaround, result.padTurnaround)
        assertEquals(missionPatches[0].name, result.missionPatches?.get(0)?.name)

        // Assert rocket and stages
        assertNotNull(result.rocket)
        assertEquals(1, result.rocket.launcherStage?.size)
        assertEquals(1, result.rocket.spacecraftStage?.size)
    }

    @Test
    fun `Launch toEntity converts all fields correctly including rocket stages`() {
        val updates = listOf(TestData.createLaunchUpdate())
        val infoUrls = listOf(TestData.createInfoUrl())
        val vidUrls = listOf(TestData.createVidUrl())
        val padTurnaround = "P1DT2H"
        val missionPatches = listOf(TestData.createMissionPatch())
        val rocket = TestData.createRocket(
            launcherStage = listOf(TestData.createLauncherStage()),
            spacecraftStage = listOf(TestData.createSpacecraftStage())
        )
        val launch = TestData.createLaunch().copy(
            updates = updates,
            infoUrls = infoUrls,
            vidUrls = vidUrls,
            padTurnaround = padTurnaround,
            missionPatches = missionPatches,
            rocket = rocket
        )

        val result = launch.toEntity()

        // Assert basic fields
        assertNotNull(result)
        assertEquals(launch.id, result.id)
        assertEquals(launch.url, result.url)
        assertEquals(launch.missionName, result.name)
        assertEquals(launch.lastUpdated, result.lastUpdated)
        assertEquals(launch.net, result.net)
        assertEquals(launch.windowEnd, result.windowEnd)
        assertEquals(launch.windowStart, result.windowStart)
        assertEquals(launch.infographic, result.infographic)
        assertEquals(launch.probability, result.probability)
        assertEquals(launch.weatherConcerns, result.weatherConcerns)
        assertEquals(launch.failReason, result.failReason)
        assertEquals(launch.webcastLive, result.webcastLive)

        // Assert image
        assertNotNull(result.image)
        assertEquals(launch.image.id, result.image.id)
        assertEquals(launch.image.name, result.image.name)
        assertEquals(launch.image.imageUrl, result.image.imageUrl)
        assertEquals(launch.image.thumbnailUrl, result.image.thumbnailUrl)
        assertEquals(launch.image.credit, result.image.credit)

        // Assert netPrecision
        assertNotNull(result.netPrecision)
        assertEquals(launch.netPrecision?.id, result.netPrecision?.id)
        assertEquals(launch.netPrecision?.name, result.netPrecision?.name)
        assertEquals(launch.netPrecision?.abbrev, result.netPrecision?.abbrev)
        assertEquals(launch.netPrecision?.description, result.netPrecision?.description)

        // Assert status
        assertNotNull(result.status)
        assertEquals(launch.status?.id, result.status?.id)
        assertEquals(launch.status?.name, result.status?.name)
        assertEquals(launch.status?.abbrev, result.status?.abbrev)
        assertEquals(launch.status?.description, result.status?.description)

        // Assert updates, infoUrls, vidUrls
        assertEquals(updates[0].comment, result.updates?.get(0)?.comment)
        assertEquals(infoUrls[0].url, result.infoUrls?.get(0)?.url)
        assertEquals(vidUrls[0].title, result.vidUrls?.get(0)?.title)

        // Assert padTurnaround and missionPatches
        assertEquals(padTurnaround, result.padTurnaround)
        assertEquals(missionPatches[0].name, result.missionPatches?.get(0)?.name)

        // Assert rocket and stages
        assertNotNull(result.rocket)
        assertEquals(1, result.rocket?.launcherStage?.size)
        assertEquals(1, result.rocket?.spacecraftStage?.size)
    }

    @Test
    fun `LaunchStatusEntity toDomain converts all fields correctly`() {
        val launchEntity = TestData.createLaunchStatusEntity()

        val result = launchEntity.toDomain()

        assertEquals(launchEntity.id, result.id)
        assertEquals(launchEntity.name, result.name)
        assertEquals(launchEntity.abbrev, result.abbrev)
        assertEquals(launchEntity.description, result.description)
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
    fun `List of Launch toEntity converts all items`() {
        val launch1 = TestData.createLaunch(id = "1")
        val launch2 = TestData.createLaunch(id = "2")
        val launches = listOf(launch1, launch2)

        val result = launches.toEntity()

        assertEquals(2, result.size)
        assertEquals("1", result[0].id)
        assertEquals("2", result[1].id)
    }

    @Test
    fun `Round trip entity to domain to entity maintains data integrity`() {
        val rocketEntity = TestData.createRocketEntity(
            launcherStage = listOf(TestData.createLauncherStageEntity()),
            spacecraftStage = listOf(TestData.createSpacecraftStageEntity())
        )
        val originalEntity = TestData.createLaunchEntity(rocket = rocketEntity)
        val domain = originalEntity.toDomain()
        val entityAgain = domain.toEntity()

        assertEquals(originalEntity.id, entityAgain.id)
        assertEquals(originalEntity.name, entityAgain.name)
        assertEquals(originalEntity.url, entityAgain.url)
        assertEquals(originalEntity.image.imageUrl, entityAgain.image.imageUrl)
        assertEquals(originalEntity.status, entityAgain.status)
        assertNotNull(entityAgain.rocket)
        assertEquals(1, entityAgain.rocket?.launcherStage?.size)
        assertEquals(1, entityAgain.rocket?.spacecraftStage?.size)
    }

    @Test
    fun `round trip domain to entity to domain maintains data integrity`() {
        val rocket = TestData.createRocket(
            launcherStage = listOf(TestData.createLauncherStage()),
            spacecraftStage = listOf(TestData.createSpacecraftStage())
        )
        val originalDomain = TestData.createLaunch().copy(rocket = rocket)
        val entity = originalDomain.toEntity()
        val domainAgain = entity.toDomain()

        assertEquals(originalDomain.id, domainAgain.id)
        assertEquals(originalDomain.missionName, domainAgain.missionName)
        assertEquals(originalDomain.url, domainAgain.url)
        assertEquals(originalDomain.image.imageUrl, domainAgain.image.imageUrl)
        assertEquals(originalDomain.status, domainAgain.status)
        assertNotNull(domainAgain.rocket)
        assertEquals(1, domainAgain.rocket.launcherStage?.size)
        assertEquals(1, domainAgain.rocket.spacecraftStage?.size)
    }


    @Test
    fun `empty list of LaunchEntity toDomain returns empty list`() {
        val entities = emptyList<com.seancoyle.database.entities.LaunchEntity>()

        val result = entities.toDomain()

        assertEquals(0, result.size)
    }

    @Test
    fun `empty list of Launch toEntity returns empty list`() {
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
