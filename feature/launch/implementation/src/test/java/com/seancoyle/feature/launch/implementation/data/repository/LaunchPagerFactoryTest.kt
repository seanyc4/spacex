package com.seancoyle.feature.launch.implementation.data.repository

import androidx.paging.ExperimentalPagingApi
import com.seancoyle.core.domain.Order
import com.seancoyle.database.dao.LaunchDao
import com.seancoyle.feature.launch.implementation.domain.model.LaunchQuery
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotNull

@OptIn(ExperimentalPagingApi::class)
class LaunchPagerFactoryTest {

    @MockK
    private lateinit var launchRemoteDataSource: LaunchRemoteDataSource

    @MockK
    private lateinit var launchLocalDataSource: LaunchLocalDataSource

    private lateinit var launchDao: LaunchDao
    private lateinit var underTest: LaunchPagerFactory

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        launchDao = mockk(relaxed = true)
        underTest = LaunchPagerFactory(
            launchDao = launchDao,
            launchRemoteDataSource = launchRemoteDataSource,
            launchLocalDataSource = launchLocalDataSource
        )
    }

    @Test
    fun `create returns Pager with valid flow`() {
        val launchQuery = LaunchQuery(query = "Falcon", order = Order.DESC)

        val result = underTest.create(launchQuery)

        assertNotNull(result)
        assertNotNull(result.flow)
    }

    @Test
    fun `create returns new Pager instance on each invocation`() {
        val launchQuery = LaunchQuery(query = "Falcon", order = Order.DESC)

        val pager1 = underTest.create(launchQuery)
        val pager2 = underTest.create(launchQuery)

        // Should be different instances even with same query
        assert(pager1 !== pager2) { "Expected different Pager instances" }
    }

    @Test
    fun `created Pager flow can be accessed multiple times`() {
        val launchQuery = LaunchQuery(query = "Dragon", order = Order.DESC)

        val pager = underTest.create(launchQuery)

        // Flow should be stable and accessible multiple times
        val flow1 = pager.flow
        val flow2 = pager.flow
        val flow3 = pager.flow

        assertNotNull(flow1)
        assertNotNull(flow2)
        assertNotNull(flow3)
    }

    @Test
    fun `create handles empty query string`() {
        val launchQuery = LaunchQuery(query = "", order = Order.DESC)

        val result = underTest.create(launchQuery)

        assertNotNull(result)
        assertNotNull(result.flow)
    }

    @Test
    fun `create handles very long query string`() {
        val longQuery = "A".repeat(1000)
        val launchQuery = LaunchQuery(query = longQuery, order = Order.ASC)

        val result = underTest.create(launchQuery)

        assertNotNull(result)
        assertNotNull(result.flow)
    }

    @Test
    fun `create handles special characters in query`() {
        val launchQuery = LaunchQuery(query = "Test @#$% & * ()", order = Order.DESC)

        val result = underTest.create(launchQuery)

        assertNotNull(result)
        assertNotNull(result.flow)
    }

}
