package com.seancoyle.feature.launch.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.RemoteMediator
import com.seancoyle.feature.launch.domain.model.LaunchesQuery
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertNotNull

class PagerFactoryTest {

    data class FakeEntity(val id: String)

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun `create returns Pager with flow`() {
        val pagingSource = mockk<PagingSource<Int, FakeEntity>>(relaxed = true)
        val mediator = mockk<RemoteMediator<Int, FakeEntity>>(relaxed = true)

        val underTest = PagerFactory(
            pagingSourceFactory = { pagingSource },
            remoteMediatorFactory = { _: LaunchesQuery -> mediator }
        )

        val pager = underTest.create(LaunchesQuery(query = "test"))

        assertNotNull(pager)
        assertNotNull(pager.flow)
    }
}
