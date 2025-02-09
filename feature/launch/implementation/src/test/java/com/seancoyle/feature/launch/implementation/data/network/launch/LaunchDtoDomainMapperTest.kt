package com.seancoyle.feature.launch.implementation.data.network.launch

import com.seancoyle.feature.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.implementation.util.TestData.launchesDto
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class LaunchDtoDomainMapperTest {

    private val underTest = LaunchDtoDomainMapper()

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
