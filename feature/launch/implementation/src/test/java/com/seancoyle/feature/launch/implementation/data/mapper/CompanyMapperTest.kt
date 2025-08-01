package com.seancoyle.feature.launch.implementation.data.mapper

import com.seancoyle.database.entities.CompanyEntity
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.implementation.data.remote.company.CompanyDto
import org.junit.Assert.*
import org.junit.Test

class CompanyMapperTest {

    private val underTest = CompanyMapper()

    @Test
    fun `entityToDomain correctly maps from CompanyEntity to Company`() {
        val entity = CompanyEntity(
            id = "1",
            employees = 1000,
            founded = 2002,
            founder = "Elon Musk",
            launchSites = 3,
            name = "SpaceX",
            valuation = 100000000000L
        )

        val model = underTest.entityToDomain(entity)

        with(model) {
            assertEquals(entity.id, "1")
            assertEquals(entity.employees, employees)
            assertEquals(entity.founded, founded)
            assertEquals(entity.founder, founder)
            assertEquals(entity.launchSites, launchSites)
            assertEquals(entity.name, name)
            assertEquals(entity.valuation, valuation)
        }
    }

    @Test
    fun `domainToEntity correctly maps from Company to CompanyEntity`() {
        val model = Company(
            employees = 1000,
            founded = 2002,
            founder = "Elon Musk",
            launchSites = 3,
            name = "SpaceX",
            valuation = 100000000000,
        )

        val entity = underTest.domainToEntity(model)

        with(entity) {
            assertEquals(model.employees, employees)
            assertEquals(model.founded, founded)
            assertEquals(model.founder, founder)
            assertEquals(model.launchSites, launchSites)
            assertEquals(model.name, name)
            assertEquals(model.valuation, valuation)
        }
    }

    @Test
    fun `dtoToDomain correctly maps from CompanyDto to Company`() {
        val dto = CompanyDto(
            employees = 1000,
            founded = 2002,
            founder = "Elon Musk",
            launchSites = 3,
            name = "SpaceX",
            valuation = 100000000000
        )

        val model = underTest.dtoToDomain(dto)

        with(model) {
            assertEquals(dto.employees, employees)
            assertEquals(dto.founded, founded)
            assertEquals(dto.founder, founder)
            assertEquals(dto.launchSites, launchSites)
            assertEquals(dto.name, name)
            assertEquals(dto.valuation, valuation)
        }
    }

    @Test
    fun `dtoToEntity correctly maps from CompanyDto to CompanyEntity`() {
        val dto = CompanyDto(
            employees = 1000,
            founded = 2002,
            founder = "Elon Musk",
            launchSites = 3,
            name = "SpaceX",
            valuation = 100000000000
        )

        val entity = underTest.dtoToEntity(dto)

        with(entity) {
            assertEquals(dto.employees, employees)
            assertEquals(dto.founded, founded)
            assertEquals(dto.founder, founder)
            assertEquals(dto.launchSites, launchSites)
            assertEquals(dto.name, name)
            assertEquals(dto.valuation, valuation)
        }
    }

}