package com.seancoyle.feature.launch.implementation.data.cache

import com.seancoyle.feature.launch.implementation.util.TestData.launchEntity
import com.seancoyle.feature.launch.implementation.util.TestData.launchModel
import com.seancoyle.feature.launch.implementation.util.TestData.launchesEntity
import com.seancoyle.feature.launch.implementation.util.TestData.launchesModel
import org.junit.Test
import kotlin.test.assertEquals

class LaunchEntityMapperTest {

    private val mapper = LaunchEntityMapper()

    @Test
    fun `mapFromEntity correctly maps from LaunchEntity to LaunchTypes Launch`() {
        val model = mapper.mapFromEntity(launchEntity)

        with(model) {
            assertEquals(launchEntity.id, id)
            assertEquals(launchEntity.launchDate, launchDate)
            assertEquals(launchEntity.launchDateLocalDateTime, launchDateLocalDateTime)
            assertEquals(launchEntity.launchStatus, launchStatus)
            assertEquals(launchEntity.launchYear, launchYear)
            assertEquals(launchEntity.links.missionImage, links.missionImage)
            assertEquals(launchEntity.missionName, missionName)
            assertEquals(launchEntity.rocket.rocketNameAndType, rocket.rocketNameAndType)
            assertEquals(launchEntity.launchDateStatus, launchDateStatus)
            assertEquals(launchEntity.launchDays, launchDays)
        }
    }

    @Test
    fun `mapToEntity correctly maps from LaunchTypes Launch to LaunchEntity`() {
        val entity = mapper.mapToEntity(launchModel)

        with(entity) {
            assertEquals(launchModel.id, id)
            assertEquals(launchModel.launchDate, launchDate)
            assertEquals(launchModel.launchDateLocalDateTime, launchDateLocalDateTime)
            assertEquals(launchModel.launchStatus, launchStatus)
            assertEquals(launchModel.launchYear, launchYear)
            assertEquals(launchModel.links.missionImage, links.missionImage)
            assertEquals(launchModel.missionName, missionName)
            assertEquals(launchModel.rocket.rocketNameAndType, rocket.rocketNameAndType)
            assertEquals(launchModel.launchDateStatus, launchDateStatus)
            assertEquals(launchModel.launchDays, launchDays)
        }
    }

    @Test
    fun `mapEntityListToDomainList correctly maps list of entities to list of domain models`() {
        val models = mapper.entityToDomainList(launchesEntity)

        assertEquals(launchesModel.size, models.size)
        models.forEachIndexed { index, model ->
            assertEquals(launchesModel[index], model)
        }
    }

    @Test
    fun `mapDomainListToEntityList correctly maps list of domain models to list of entities`() {
        val entities = mapper.domaintoEntityList(launchesModel)

        assertEquals(launchesEntity.size, entities.size)
        entities.forEachIndexed { index, entity ->
            assertEquals(launchesEntity[index], entity)
        }
    }
}