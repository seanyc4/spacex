package com.seancoyle.feature.launch.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.seancoyle.database.entities.LaunchEntity
import com.seancoyle.feature.launch.domain.model.LaunchesQuery
import com.seancoyle.feature.launch.domain.repository.LaunchesRepository
import com.seancoyle.feature.launch.util.TestData
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotNull

class LaunchesRepositoryImplTest {

    @MockK
    private lateinit var pagerFactory: LaunchesPagerFactory

    private lateinit var underTest: LaunchesRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = LaunchesRepositoryImpl(
            pagerFactory = pagerFactory
        )
    }

    @Test
    fun `pager returns flow of paging data with query`() = runTest {
        val launchesQuery = LaunchesQuery(query = "Falcon")
        val launchEntity = TestData.createLaunchEntity()
        val mockPager = mockk<Pager<Int, LaunchEntity>>()
        val pagingData = PagingData.from(listOf(launchEntity))

        every { pagerFactory.create(launchesQuery) } returns mockPager
        every { mockPager.flow } returns flowOf(pagingData)

        val result = underTest.pager(launchesQuery)

        assertNotNull(result)
        assertNotNull(result.first())

        verify { pagerFactory.create(launchesQuery) }
    }

    @Test
    fun `pager returns empty flow when no data available`() = runTest {
        val launchesQuery = LaunchesQuery(query = "")
        val mockPager = mockk<Pager<Int, LaunchEntity>>()
        val pagingData = PagingData.empty<LaunchEntity>()

        every { pagerFactory.create(launchesQuery) } returns mockPager
        every { mockPager.flow } returns flowOf(pagingData)

        val result = underTest.pager(launchesQuery)

        assertNotNull(result)
        assertNotNull(result.first())

        verify { pagerFactory.create(launchesQuery) }
    }

    @Test
    fun `pager creates pager with correct launch query`() = runTest {
        val launchesQuery = LaunchesQuery(query = "SpaceX")
        val mockPager = mockk<Pager<Int, LaunchEntity>>()
        val pagingData = PagingData.empty<LaunchEntity>()

        every { pagerFactory.create(launchesQuery) } returns mockPager
        every { mockPager.flow } returns flowOf(pagingData)

        underTest.pager(launchesQuery)

        verify(exactly = 1) { pagerFactory.create(launchesQuery) }
    }

}
