package com.seancoyle.feature.launch.implementation.data.mapper

import com.seancoyle.database.entities.LaunchDateStatusEntity
import com.seancoyle.database.entities.LaunchStatusEntity
import com.seancoyle.feature.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.implementation.util.TestData.launchEntity
import com.seancoyle.feature.launch.implementation.util.TestData.launchModel
import com.seancoyle.feature.launch.implementation.util.TestData.launchesDto
import com.seancoyle.feature.launch.implementation.util.TestData.launchesEntity
import com.seancoyle.feature.launch.implementation.util.TestData.launchesModel
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class LaunchMapperTest {

    private val underTest = LaunchMapper()

    @Test
    fun `entityToDomain correctly maps from LaunchEntity to LaunchTypes Launch`() {
        val model = underTest.entityToDomain(launchEntity)

        with(model) {
            assertEquals(launchEntity.id, id)
            assertEquals(launchEntity.launchDate, launchDate)
            assertEquals(launchEntity.launchDateLocalDateTime, launchDateLocalDateTime)
            assertEquals(underTest.mapToLaunchStatus(launchEntity.launchStatus), launchStatus)
            assertEquals(launchEntity.launchYear, launchYear)
            assertEquals(launchEntity.links.missionImage, links.missionImage)
            assertEquals(launchEntity.missionName, missionName)
            assertEquals(launchEntity.rocket.rocketNameAndType, rocket.rocketNameAndType)
            assertEquals(underTest.mapToLaunchDateStatus(launchEntity.launchDateStatus), launchDateStatus)
            assertEquals(launchEntity.launchDays, launchDays)
        }
    }

    @Test
    fun `domainToEntity correctly maps from LaunchTypes Launch to LaunchEntity`() {
        val entity = underTest.domainToEntity(launchModel)

        with(entity) {
            assertEquals(launchModel.id, id)
            assertEquals(launchModel.launchDate, launchDate)
            assertEquals(launchModel.launchDateLocalDateTime, launchDateLocalDateTime)
            assertEquals(underTest.mapToLaunchStatusEntity(launchModel.launchStatus), launchStatus)
            assertEquals(launchModel.launchYear, launchYear)
            assertEquals(launchModel.links.missionImage, links.missionImage)
            assertEquals(launchModel.missionName, missionName)
            assertEquals(launchModel.rocket.rocketNameAndType, rocket.rocketNameAndType)
            assertEquals(underTest.mapToLaunchDateStatusEntity(launchModel.launchDateStatus), launchDateStatus)
            assertEquals(launchModel.launchDays, launchDays)
        }
    }

    @Test
    fun `entityToDomainList correctly maps list of entities to list of domain models`() {
        val models = underTest.entityToDomainList(launchesEntity)

        assertEquals(launchesModel.size, models.size)
        models.forEachIndexed { index, model ->
            assertEquals(launchesModel[index], model)
        }
    }

    @Test
    fun `domainToEntityList correctly maps list of domain models to list of entities`() {
        val entities = underTest.domainToEntityList(launchesModel)

        assertEquals(launchesEntity.size, entities.size)
        entities.forEachIndexed { index, entity ->
            assertEquals(launchesEntity[index], entity)
        }
    }

    @Test
    fun `mapToLaunchStatus correctly maps LaunchStatusEntity to LaunchStatus`() {
        val entity = LaunchStatusEntity.SUCCESS
        val domain = underTest.mapToLaunchStatus(entity)

        assertEquals(LaunchStatus.SUCCESS, domain)
    }

    @Test
    fun `mapToLaunchStatusEntity correctly maps LaunchStatus to LaunchStatusEntity`() {
        val domain = LaunchStatus.SUCCESS
        val entity = underTest.mapToLaunchStatusEntity(domain)

        assertEquals(LaunchStatusEntity.SUCCESS, entity)
    }

    @Test
    fun `mapToLaunchDateStatus correctly maps LaunchDateStatusEntity to LaunchDateStatus`() {
        val entity = LaunchDateStatusEntity.PAST
        val domain = underTest.mapToLaunchDateStatus(entity)

        assertEquals(LaunchDateStatus.PAST, domain)
    }

    @Test
    fun `mapToLaunchDateStatusEntity correctly maps LaunchDateStatus to LaunchDateStatusEntity`() {
        val domain = LaunchDateStatus.PAST
        val entity = underTest.mapToLaunchDateStatusEntity(domain)

        assertEquals(LaunchDateStatusEntity.PAST, entity)
    }

    @Test
    fun `dtoToDomainList correctly maps DTO to LaunchTypes Launch list`() {
        val flightNumber = 136

        val result = underTest.dtoToDomainList(launchesDto)

        assertNotNull(result)
        assertEquals(1, result.size)
        with(result.first()) {
            assertEquals(flightNumber.toString(), id)
            assertEquals("2024-01-01T00:00:00Z", launchDate)
            assertEquals(LaunchStatus.UNKNOWN, launchStatus)
            assertEquals("", launchYear)
            assertNotNull(links)
            assertEquals(
                "https://example.com/images/missions/patch_small_136.png",
                links.missionImage
            )
            assertEquals(null, links.articleLink)
            assertEquals("https://youtube.com/watch?v=xyz9876", links.webcastLink)
            assertEquals("https://en.wikipedia.org/wiki/SpaceX_Starlink_Mission", links.wikiLink)
            assertEquals(missionName, missionName)
            assertEquals("Falcon Heavy/Block 5", rocket.rocketNameAndType)
            assertEquals(LaunchDateStatus.PAST, launchDateStatus)
            assertEquals("", launchDays)
        }
    }

}