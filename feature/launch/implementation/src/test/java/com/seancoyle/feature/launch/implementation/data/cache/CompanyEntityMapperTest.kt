package com.seancoyle.feature.launch.implementation.data.cache

import com.seancoyle.database.entities.CompanyEntity
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.implementation.data.cache.mapper.CompanyEntityMapper
import org.junit.Test
import kotlin.test.assertEquals

class CompanyEntityMapperTest {

    private val mapper = CompanyEntityMapper()

    @Test
    fun `mapFromEntity correctly maps from CompanyEntity to Company`() {
        val entity = CompanyEntity(
            id = "1",
            employees = "1000",
            founded = 2002,
            founder = "Elon Musk",
            launchSites = 3,
            name = "SpaceX",
            valuation = "100000000000",
            summary = "Summary"
        )

        val model = mapper.entityToDomain(entity)

        with(model) {
            assertEquals(entity.id, id)
            assertEquals(entity.employees, employees)
            assertEquals(entity.founded, founded)
            assertEquals(entity.founder, founder)
            assertEquals(entity.launchSites, launchSites)
            assertEquals(entity.name, name)
            assertEquals(entity.valuation, valuation)
        }
    }

    @Test
    fun `mapToEntity correctly maps from Company to CompanyEntity`() {
        val model = Company(
            id = "1",
            employees = "1000",
            founded = 2002,
            founder = "Elon Musk",
            launchSites = 3,
            name = "SpaceX",
            valuation = "100000000000",
            summary = "Summary"
        )

        val entity = mapper.domainToEntity(model)

        with(entity) {
            assertEquals(model.id, id)
            assertEquals(model.employees, employees)
            assertEquals(model.founded, founded)
            assertEquals(model.founder, founder)
            assertEquals(model.launchSites, launchSites)
            assertEquals(model.name, name)
            assertEquals(model.valuation, valuation)
        }
    }
}