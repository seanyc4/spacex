package com.seancoyle.feature.launch.implementation.data.network

import com.seancoyle.core.common.dataformatter.DateFormatter
import com.seancoyle.core.common.dataformatter.DateTransformer
import com.seancoyle.feature.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.implementation.data.network.mapper.LaunchDtoDomainMapper
import com.seancoyle.feature.launch.implementation.util.TestData.launchModel
import com.seancoyle.feature.launch.implementation.util.TestData.launchesDto
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class LaunchDtoDomainMapperTest {

    @MockK
    private lateinit var dateFormatter: DateFormatter

    @MockK
    private lateinit var dateTransformer: DateTransformer

    private lateinit var underTest: LaunchDtoDomainMapper

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = LaunchDtoDomainMapper(
            dateFormatter = dateFormatter,
            dateTransformer = dateTransformer
        )
    }

    @Test
    fun `mapEntityToList correctly maps DTO to LaunchTypes Launch list`() {
        val expectedLaunchDate = "2024-01-01T00:00:00Z"
        val flightNumber = 136
        val localDateTime = LocalDateTime.of(2024, 1, 1, 0, 0)

        every { dateFormatter.formatDate(expectedLaunchDate) } returns launchModel.launchDateLocalDateTime
        every { dateTransformer.formatDateTimeToString(launchModel.launchDateLocalDateTime) } returns "2024-01-01T00:00:00Z"
        every { dateTransformer.returnYearOfLaunch(launchModel.launchDateLocalDateTime) } returns "2024"
        every { dateTransformer.getLaunchDaysDifference(launchModel.launchDateLocalDateTime) } returns "5 days"
        every { dateTransformer.isPastLaunch(launchModel.launchDateLocalDateTime) } returns false

        val result = underTest.dtoToDomainList(launchesDto)

        assertNotNull(result)
        assertEquals(1, result.size)
        with(result.first()) {
            assertEquals(flightNumber.toString() + launchDateLocalDateTime, id)
            assertEquals("2024-01-01T00:00:00Z", launchDate)
            assertEquals(localDateTime, launchDateLocalDateTime)
            assertEquals(LaunchStatus.SUCCESS, launchStatus)
            assertEquals("2024", launchYear)
            assertNotNull(links)
            assertEquals("https://example.com/images/missions/patch_small_136.png", links.missionImage)
            assertEquals(null, links.articleLink)
            assertEquals("https://youtube.com/watch?v=xyz9876", links.webcastLink)
            assertEquals("https://en.wikipedia.org/wiki/SpaceX_Starlink_Mission", links.wikiLink)
            assertEquals(missionName, missionName)
            assertEquals("Falcon Heavy/Block 5", rocket.rocketNameAndType)
            assertEquals(LaunchDateStatus.FUTURE, launchDateStatus)
            assertEquals("5 days", launchDays)
        }
    }
}
