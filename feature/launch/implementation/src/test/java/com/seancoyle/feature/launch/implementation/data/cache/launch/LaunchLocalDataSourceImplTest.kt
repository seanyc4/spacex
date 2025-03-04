package com.seancoyle.feature.launch.implementation.data.cache.launch

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.result.DataError.LocalError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.Order
import com.seancoyle.database.dao.LaunchDao
import com.seancoyle.database.dao.paginateLaunches
import com.seancoyle.database.entities.LaunchStatusEntity
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.implementation.data.mapper.LaunchMapper
import com.seancoyle.feature.launch.implementation.data.mapper.LocalErrorMapper
import com.seancoyle.feature.launch.implementation.data.repository.launch.LaunchLocalDataSource
import com.seancoyle.feature.launch.implementation.util.TestData.launchEntity
import com.seancoyle.feature.launch.implementation.util.TestData.launchModel
import com.seancoyle.feature.launch.implementation.util.TestData.launchesEntity
import com.seancoyle.feature.launch.implementation.util.TestData.launchesModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
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

    @MockK
    private lateinit var launchMapper: LaunchMapper

    @RelaxedMockK
    private lateinit var localErrorMapper: LocalErrorMapper

    private lateinit var underTest: LaunchLocalDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = LaunchLocalDataSourceImpl(
            dao = dao,
            crashlytics = crashlytics,
            launchMapper = launchMapper,
            localErrorMapper = localErrorMapper
        )
    }

    @Test
    fun `paginateLaunches returns mapped launches when DAO provides data`() = runTest {
        val launchYear = "2022"
        val order = Order.ASC
        val page = 1
        val pageSize = 30
        val launchStatus = LaunchStatus.SUCCESS
        val launchStatusEntity = LaunchStatusEntity.ALL

        every { launchMapper.mapToLaunchStatusEntity(launchStatus) } returns launchStatusEntity
        every { launchMapper.entityToDomainList(launchesEntity) } returns launchesModel

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
            launchStatus = launchStatus,
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

        assertTrue(result is LaunchResult.Success)
        assertEquals(launchesModel, result.data)
    }

    @Test
    fun `getById returns a launchEntity when DAO provides an entity`() = runTest {
        val launchId = "1"
        coEvery { dao.getById(launchId) } returns launchEntity
        every { launchMapper.entityToDomain(launchEntity) } returns launchModel

        val result = underTest.getById(launchId)

        coVerify { dao.getById(launchId) }

        assertTrue(result is LaunchResult.Success)
        assertEquals(launchModel, result.data)
    }

    @Test
    fun `getAll returns launchesEntity when DAO provides data`() = runTest {
        coEvery { dao.getAll() } returns launchesEntity
        every { launchMapper.entityToDomainList(launchesEntity) } returns launchesModel

        val result = underTest.getAll()

        coVerify { dao.getAll() }

        assertTrue(result is LaunchResult.Success)
        assertEquals(launchesModel, result.data)
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
        every { launchMapper.domainToEntity(launchModel) } returns launchEntity
        coEvery { dao.insert(launchEntity) } returns 1L

        val result = underTest.insert(launchModel)

        coVerify { dao.insert(launchEntity) }
        assertTrue(result is LaunchResult.Success)
    }

    @Test
    fun `insert returns error when DAO operation fails`() = runTest {
        val exception = RuntimeException("Insert failed")
        every { launchMapper.domainToEntity(launchModel) } returns launchEntity
        coEvery { dao.insert(any()) } throws exception
        every { localErrorMapper.map(exception) } returns LocalError.CACHE_ERROR

        val result = underTest.insert(launchModel)

        assertTrue(result is LaunchResult.Error)
        assertEquals(LocalError.CACHE_ERROR, result.error)
    }

    @Test
    fun `insertList invokes DAO and returns success`() = runTest {
        every { launchMapper.domainToEntityList(launchesModel) } returns launchesEntity
        coEvery { dao.insertList(launchesEntity) } returns longArrayOf(2)

        val result = underTest.insertList(launchesModel)

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