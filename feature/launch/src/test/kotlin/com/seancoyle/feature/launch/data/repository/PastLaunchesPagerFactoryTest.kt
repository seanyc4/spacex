package com.seancoyle.feature.launch.data.repository

import androidx.paging.ExperimentalPagingApi
import com.seancoyle.feature.launch.domain.model.LaunchesQuery
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotNull

@OptIn(ExperimentalPagingApi::class)
class PastLaunchesPagerFactoryTest {

    @MockK
    private lateinit var launchesRemoteDataSource: LaunchesRemoteDataSource

    @MockK
    private lateinit var pastLaunchesLocalDataSource: PastLaunchesLocalDataSource

    @MockK
    private lateinit var launchDetailLocalDataSource: LaunchDetailLocalDataSource

    private lateinit var pastLaunchDao: com.seancoyle.database.dao.PastLaunchDao
    private lateinit var underTest: PastLaunchesPagerFactory

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        pastLaunchDao = mockk(relaxed = true)
        underTest = PastLaunchesPagerFactory(
            pastLaunchDao = pastLaunchDao,
            remoteDataSource = launchesRemoteDataSource,
            localDataSource = pastLaunchesLocalDataSource,
            launchDetailLocalDataSource = launchDetailLocalDataSource
        )
    }

    @Test
    fun `create returns Pager with valid flow`() {
        val launchesQuery = LaunchesQuery(query = "Falcon")

        val result = underTest.create(launchesQuery)

        assertNotNull(result)
        assertNotNull(result.flow)
    }

    @Test
    fun `create returns new Pager instance on each invocation`() {
        val launchesQuery = LaunchesQuery(query = "Falcon")

        val pager1 = underTest.create(launchesQuery)
        val pager2 = underTest.create(launchesQuery)

        // Should be different instances even with same query
        assert(pager1 !== pager2) { "Expected different Pager instances" }
    }

    @Test
    fun `created Pager flow can be accessed multiple times`() {
        val launchesQuery = LaunchesQuery(query = "Dragon")

        val pager = underTest.create(launchesQuery)

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
        val launchesQuery = LaunchesQuery(query = "")

        val result = underTest.create(launchesQuery)

        assertNotNull(result)
        assertNotNull(result.flow)
    }

    @Test
    fun `create handles very long query string`() {
        val longQuery = "A".repeat(1000)
        val launchesQuery = LaunchesQuery(query = longQuery)

        val result = underTest.create(launchesQuery)

        assertNotNull(result)
        assertNotNull(result.flow)
    }

    @Test
    fun `create handles special characters in query`() {
        val launchesQuery = LaunchesQuery(query = "Test @#\$% & * ()")

        val result = underTest.create(launchesQuery)

        assertNotNull(result)
        assertNotNull(result.flow)
    }
}
