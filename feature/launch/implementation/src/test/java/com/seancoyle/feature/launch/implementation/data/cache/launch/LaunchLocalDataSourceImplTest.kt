package com.seancoyle.feature.launch.implementation.data.cache.launch

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.domain.Order
import com.seancoyle.core.test.TestCoroutineRule
import com.seancoyle.database.dao.LaunchDao
import com.seancoyle.database.dao.paginateLaunches
import com.seancoyle.database.entities.LaunchStatusEntity
import com.seancoyle.feature.launch.implementation.data.repository.launch.LaunchLocalDataSource
import com.seancoyle.feature.launch.implementation.util.TestData.launchEntity
import com.seancoyle.feature.launch.implementation.util.TestData.launchesEntity
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class LaunchLocalDataSourceImplTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

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
            crashlytics = crashlytics,
            ioDispatcher = testCoroutineRule.testCoroutineDispatcher
        )
    }

    @Test
    fun `paginateLaunches returns mapped launches when DAO provides data`() = runTest {
        val launchYear = "2022"
        val order = Order.ASC
        val launchStatusEntity = LaunchStatusEntity.ALL
        val page = 1
        val pageSize = 30

        coEvery {
            dao.paginateLaunches(
                launchYear = launchYear,
                order = order,
                launchStatus = launchStatusEntity,
                page = page,
                pageSize = pageSize
            )
        } returns launchesEntity

        val result = underTest.paginate(
            launchYear = launchYear,
            order = order,
            launchStatus = launchStatusEntity,
            page = page
        )

        coVerify {
            dao.paginateLaunches(
                launchYear = launchYear,
                order = order,
                launchStatus = launchStatusEntity,
                page = page,
                pageSize = pageSize
            )
        }

        assertTrue(result.isSuccess)
        assertEquals(launchesEntity, result.getOrNull())
    }

    @Test
    fun `getById returns a launchEntity when DAO provides an entity`() = runTest {
        val launchId = "1"
        coEvery { dao.getById(launchId) } returns launchEntity

        val result = underTest.getById(launchId)

        coVerify { dao.getById(launchId) }

        assertTrue(result.isSuccess)
        assertEquals(launchEntity, result.getOrNull())
    }

    @Test
    fun `getAll returns launchesEntity when DAO provides data`() = runTest {
        coEvery { dao.getAll() } returns launchesEntity

        val result = underTest.getAll()

        coVerify { dao.getAll() }

        assertTrue(result.isSuccess)
        assertEquals(launchesEntity, result.getOrNull())
    }

    @Test
    fun `deleteById invokes DAO and returns success when DAO operation is successful`() = runTest {
        val launchId = "1"
        coEvery { dao.deleteById(launchId) } returns 1

        val result = underTest.deleteById(launchId)

        coVerify { dao.deleteById(launchId) }

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull())
    }

    @Test
    fun `deleteAll invokes DAO and returns success when DAO operation is successful`() = runTest {
        coEvery { dao.deleteAll() } returns Unit

        val result = underTest.deleteAll()

        coVerify { dao.deleteAll() }

        assertTrue(result.isSuccess)
    }
}