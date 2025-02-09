package com.seancoyle.feature.launch.implementation.data.cache.launch

import com.seancoyle.database.entities.LaunchDateStatusEntity
import com.seancoyle.database.entities.LaunchStatusEntity
import com.seancoyle.feature.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.implementation.util.TestData.launchEntity
import com.seancoyle.feature.launch.implementation.util.TestData.launchModel
import com.seancoyle.feature.launch.implementation.util.TestData.launchesEntity
import com.seancoyle.feature.launch.implementation.util.TestData.launchesModel
import org.junit.Test
import kotlin.test.assertEquals

class LaunchDomainEntityMapperTest {

    private val underTest = LaunchDomainEntityMapper()

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
}