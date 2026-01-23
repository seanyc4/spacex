package com.seancoyle.feature.launch.data.repository

import androidx.paging.Pager
import com.seancoyle.database.entities.PastLaunchEntity
import com.seancoyle.database.entities.UpcomingLaunchEntity
import com.seancoyle.feature.launch.domain.model.LaunchesQuery
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotNull
import kotlin.test.assertSame

class LaunchesPagerFactoryTest {

    @MockK
    private lateinit var upcoming: PagerFactory<UpcomingLaunchEntity>

    @MockK
    private lateinit var past: PagerFactory<PastLaunchEntity>

    private lateinit var underTest: LaunchesPagerFactory

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = LaunchesPagerFactory(
            upcoming = upcoming,
            past = past
        )
    }

    @Test
    fun `createUpcoming delegates to upcoming pager factory`() {
        val launchesQuery = LaunchesQuery(query = "Falcon")
        val pager = mockk<Pager<Int, UpcomingLaunchEntity>>(relaxed = true)
        every { upcoming.create(launchesQuery) } returns pager

        val result = underTest.createUpcoming(launchesQuery)

        assertNotNull(result)
        assertNotNull(result.flow)
        assertSame(pager, result)
    }

    @Test
    fun `createPast returns Pager with flow`() {
        val launchesQuery = LaunchesQuery(query = "Falcon")
        val pager = mockk<Pager<Int, PastLaunchEntity>>(relaxed = true)
        every { past.create(launchesQuery) } returns pager

        val result = underTest.createPast(launchesQuery)

        assertNotNull(result)
        assertNotNull(result.flow)
        assertSame(pager, result)
    }
}
