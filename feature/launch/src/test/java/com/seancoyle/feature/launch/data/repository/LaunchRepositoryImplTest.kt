package com.seancoyle.feature.launch.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.seancoyle.core.domain.Order
import com.seancoyle.database.entities.LaunchEntity
import com.seancoyle.feature.launch.domain.model.LaunchQuery
import com.seancoyle.feature.launch.domain.repository.LaunchRepository
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

class LaunchRepositoryImplTest {

    @MockK
    private lateinit var pagerFactory: LaunchPagerFactory

    private lateinit var underTest: LaunchRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = LaunchRepositoryImpl(
            pagerFactory = pagerFactory
        )
    }

    @Test
    fun `pager returns flow of paging data with query`() = runTest {
        val launchQuery = LaunchQuery(query = "Falcon", order = Order.DESC)
        val launchEntity = TestData.createLaunchEntity()
        val mockPager = mockk<Pager<Int, LaunchEntity>>()
        val pagingData = PagingData.from(listOf(launchEntity))

        every { pagerFactory.create(launchQuery) } returns mockPager
        every { mockPager.flow } returns flowOf(pagingData)

        val result = underTest.pager(launchQuery)

        assertNotNull(result)
        assertNotNull(result.first())

        verify { pagerFactory.create(launchQuery) }
    }

    @Test
    fun `pager returns empty flow when no data available`() = runTest {
        val launchQuery = LaunchQuery(query = "", order = Order.ASC)
        val mockPager = mockk<Pager<Int, LaunchEntity>>()
        val pagingData = PagingData.empty<LaunchEntity>()

        every { pagerFactory.create(launchQuery) } returns mockPager
        every { mockPager.flow } returns flowOf(pagingData)

        val result = underTest.pager(launchQuery)

        assertNotNull(result)
        assertNotNull(result.first())

        verify { pagerFactory.create(launchQuery) }
    }

    @Test
    fun `pager creates pager with correct launch query`() = runTest {
        val launchQuery = LaunchQuery(query = "SpaceX", order = Order.DESC)
        val mockPager = mockk<Pager<Int, LaunchEntity>>()
        val pagingData = PagingData.empty<LaunchEntity>()

        every { pagerFactory.create(launchQuery) } returns mockPager
        every { mockPager.flow } returns flowOf(pagingData)

        underTest.pager(launchQuery)

        verify(exactly = 1) { pagerFactory.create(launchQuery) }
    }

}
