package com.seancoyle.feature.launch.implementation.data.cache

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.database.dao.LaunchDao
import com.seancoyle.feature.launch.implementation.data.local.LaunchLocalDataSourceImpl
import com.seancoyle.feature.launch.implementation.data.repository.LaunchLocalDataSource
import com.seancoyle.feature.launch.implementation.util.TestData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LaunchLocalDataSourceImplTest {

    @MockK
    private lateinit var dao: LaunchDao

    @RelaxedMockK
    private lateinit var crashlytics: Crashlytics

    private lateinit var underTest: LaunchLocalDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = LaunchLocalDataSourceImpl(
            dao = dao,
            crashlytics = crashlytics
        )
    }

    /*@Test
    fun `paginateLaunches returns mapped launches when DAO provides data`() = runTest {
        val launchYear = "2025"
        val order = Order.ASC
        val page = 1
        val launchStatus = LaunchStatus.UNKNOWN
        val launchEntity = TestData.createLaunchEntity()
        val launchesEntity = listOf(launchEntity, launchEntity)

        coEvery {
            dao.paginateLaunches(
                launchYear = launchYear,
                order = order,
                launchStatus = LaunchStatusEntity.ALL,
                page = page,
                pageSize = PAGINATION_LIMIT
            )
        } returns launchesEntity

        val result = underTest.paginate(
            launchYear = launchYear,
            order = order,
            launchStatus = launchStatus,
            page = page
        )

        coVerify {
            dao.paginateLaunches(
                launchYear = launchYear,
                order = order,
                launchStatus = LaunchStatusEntity.ALL,
                page = page,
                pageSize = PAGINATION_LIMIT
            )
        }

        assertTrue(result is LaunchResult.Success)
        assertEquals(2, result.data.size)
        assertEquals("faf4a0bc-7dad-4842-b74c-73a9f648b5cc", result.data[0].id)
    }*/

    @Test
    fun `getById returns a launch when DAO provides an entity`() = runTest {
        val launchId = "faf4a0bc-7dad-4842-b74c-73a9f648b5cc"
        val launchEntity = TestData.createLaunchEntity()
        coEvery { dao.getById(launchId) } returns launchEntity

        val result = underTest.getById(launchId)

        coVerify { dao.getById(launchId) }

        assertTrue(result is LaunchResult.Success)
        assertEquals(launchId, result.data?.id)
        assertEquals("Falcon 9 Block 5 | Starlink Group 15-12", result.data?.name)
    }

    @Test
    fun `getAll returns launches when DAO provides data`() = runTest {
        val launchEntity = TestData.createLaunchEntity()
        val launchesEntity = listOf(launchEntity, launchEntity)
        coEvery { dao.getAll() } returns launchesEntity

        val result = underTest.getAll()

        coVerify { dao.getAll() }

        assertTrue(result is LaunchResult.Success)
        assertEquals(2, result.data.size)
        assertEquals("faf4a0bc-7dad-4842-b74c-73a9f648b5cc", result.data[0].id)
    }

    @Test
    fun `deleteById invokes DAO and returns success when DAO operation is successful`() = runTest {
        val launchId = "1"
        coEvery { dao.deleteById(launchId) } returns 1

        val result = underTest.deleteById(launchId)

        coVerify { dao.deleteById(launchId) }

        assertTrue(result is LaunchResult.Success)
        assertEquals(1, result.data)
    }

    @Test
    fun `deleteAll invokes DAO and returns success when DAO operation is successful`() = runTest {
        coEvery { dao.deleteAll() } returns Unit

        val result = underTest.deleteAll()

        coVerify { dao.deleteAll() }

        assertTrue(result is LaunchResult.Success)
    }

    @Test
    fun `insert invokes DAO and returns success`() = runTest {
        val launchEntity = TestData.createLaunchEntity()
        val launch = TestData.createLaunchTransformed()
        coEvery { dao.insert(launchEntity) } returns 1L

        val result = underTest.insert(launch)

        coVerify { dao.insert(launchEntity) }
        assertTrue(result is LaunchResult.Success)
    }

    @Test
    fun `insert returns error when DAO operation fails`() = runTest {
        val exception = RuntimeException("Insert failed")
        val expected = DataError.LocalError.CACHE_UNKNOWN_DATABASE_ERROR
        val launch = TestData.createLaunchTransformed()
        coEvery { dao.insert(any()) } throws exception

        val result = underTest.insert(launch)

        assertTrue(result is LaunchResult.Error)
        assertEquals(expected, result.error)
    }

    @Test
    fun `insertList invokes DAO and returns success`() = runTest {
        val launchEntity = TestData.createLaunchEntity()
        val launchesEntity = listOf(launchEntity, launchEntity)
        val launch = TestData.createLaunchTransformed()
        val launches = listOf(launch, launch)
        coEvery { dao.insertList(launchesEntity) } returns longArrayOf(2)

        val result = underTest.insertList(launches)

        coVerify { dao.insertList(launchesEntity) }
        assertTrue(result is LaunchResult.Success)
    }

    @Test
    fun `getTotalEntries invokes DAO and returns count`() = runTest {
        coEvery { dao.getTotalEntries() } returns 10

        val result = underTest.getTotalEntries()

        coVerify { dao.getTotalEntries() }
        assertTrue(result is LaunchResult.Success)
        assertEquals(10, result.data)
    }
}
