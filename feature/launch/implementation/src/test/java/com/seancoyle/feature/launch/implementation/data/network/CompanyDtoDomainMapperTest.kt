package com.seancoyle.feature.launch.implementation.data.network

import com.seancoyle.core.common.numberformatter.NumberFormatter
import com.seancoyle.feature.launch.implementation.data.network.company.CompanyDto
import com.seancoyle.feature.launch.implementation.util.TestData.companyDto
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CompanyDtoDomainMapperTest {

    @MockK
    private lateinit var numberFormatter: NumberFormatter

    private lateinit var mapper: CompanyDtoDomainMapper

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mapper = CompanyDtoDomainMapper(numberFormatter)
    }

    @Test
    fun `mapFromEntity correctly maps DTO to Company model`() = runTest {
        every { numberFormatter.formatNumber(100) } returns "100"
        every { numberFormatter.formatNumber(74000000000L) } returns "74,000,000,000"

        val result = mapper.dtoToDomain(companyDto)

        assertNotNull(result)
        with(result) {
            assertEquals("", id)
            assertEquals("100", employees)
            assertEquals(2000, founded)
            assertEquals("Elon Musk", founder)
            assertEquals(4, launchSites)
            assertEquals("SpaceX", name)
            assertEquals("74,000,000,000", valuation)
        }
    }

    @Test
    fun `mapFromEntity handles null values gracefully`() = runTest {
        val companyDto = CompanyDto(
            employees = null,
            founded = null,
            founder = null,
            launchSites = null,
            name = null,
            valuation = null
        )
        every { numberFormatter.formatNumber(null) } returns "0"

        val result = mapper.dtoToDomain(companyDto)

        assertNotNull(result)
        with(result) {
            assertEquals("", id)
            assertEquals("0", employees)
            assertEquals(0, founded)
            assertEquals("", founder)
            assertEquals(0, launchSites)
            assertEquals("", name)
            assertEquals("0", valuation)
        }
    }
}