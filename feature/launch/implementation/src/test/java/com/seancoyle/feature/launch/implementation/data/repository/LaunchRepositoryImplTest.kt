package com.seancoyle.feature.launch.implementation.data.repository

import com.seancoyle.core.common.result.Result
import com.seancoyle.core.domain.Order
import com.seancoyle.database.entities.LaunchStatusEntity
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.data.cache.launch.LaunchDomainEntityMapper
import com.seancoyle.feature.launch.implementation.data.network.launch.LaunchesDto
import com.seancoyle.feature.launch.implementation.data.network.launch.LaunchDtoDomainMapper
import com.seancoyle.feature.launch.implementation.data.repository.launch.LaunchDiskDataSource
import com.seancoyle.feature.launch.implementation.data.repository.launch.LaunchNetworkDataSource
import com.seancoyle.feature.launch.implementation.data.repository.launch.LaunchRepositoryImpl
import com.seancoyle.feature.launch.implementation.domain.repository.LaunchRepository
import com.seancoyle.feature.launch.implementation.util.TestData.launchDto
import com.seancoyle.feature.launch.implementation.util.TestData.launchEntity
import com.seancoyle.feature.launch.implementation.util.TestData.launchModel
import com.seancoyle.feature.launch.implementation.util.TestData.launchOptions
import com.seancoyle.feature.launch.implementation.util.TestData.launchesDto
import com.seancoyle.feature.launch.implementation.util.TestData.launchesEntity
import com.seancoyle.feature.launch.implementation.util.TestData.launchesModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LaunchRepositoryImplTest {

    @MockK
    private lateinit var launchDiskDataSource: LaunchDiskDataSource

    @MockK
    private lateinit var launchNetworkDataSource: LaunchNetworkDataSource

    @MockK
    private lateinit var launchDtoDomainMapper: LaunchDtoDomainMapper

    @MockK
    private lateinit var launchCacheMapper: LaunchDomainEntityMapper

    private lateinit var underTest: LaunchRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = LaunchRepositoryImpl(
            launchNetworkDataSource = launchNetworkDataSource,
            launchDiskDataSource = launchDiskDataSource,
            launchDtoDomainMapper = launchDtoDomainMapper,
            launchCacheMapper = launchCacheMapper
        )
    }

    @Test
    fun `getLaunches returns mapped launches on success`() = runTest {
        coEvery { launchNetworkDataSource.getLaunches(launchOptions) } returns Result.Success(
            launchesDto
        )
        every { launchDtoDomainMapper.dtoToDomainList(LaunchesDto(listOf(launchDto))) } returns launchesModel

        val result = underTest.getLaunches(launchOptions)

        assertTrue(result is Result.Success)
        assertEquals(launchesModel, (result).data)

        coVerify { launchNetworkDataSource.getLaunches(launchOptions) }
        verify { launchDtoDomainMapper.dtoToDomainList(LaunchesDto(listOf(launchDto))) }
    }

    @Test
    fun `paginateLaunches returns mapped launches on success`() = runTest {
        val launchYear = "2022"
        val order = Order.ASC
        val launchStatus = LaunchStatus.SUCCESS
        val launchStatusEntity = LaunchStatusEntity.SUCCESS
        val page = 1
        val launches = listOf(mockk<LaunchTypes.Launch>())
        every { launchCacheMapper.toLaunchStatusEntity(launchStatus) } returns launchStatusEntity
        coEvery { launchDiskDataSource.paginate(launchYear, order, launchStatusEntity, page) } returns Result.Success(
            launchesEntity
        )
        every { launchCacheMapper.entityToDomainList(launchesEntity) } returns launches

        val result = underTest.paginateLaunches(launchYear, order, launchStatus, page)

        assertTrue(result is Result.Success)
        assertEquals(launches, (result).data)

        coVerify { launchDiskDataSource.paginate(launchYear, order, launchStatusEntity, page) }
        verify { launchCacheMapper.toLaunchStatusEntity(launchStatus) }
        verify { launchCacheMapper.entityToDomainList(launchesEntity) }
    }

    @Test
    fun `insertLaunch returns result on success`() = runTest {
        val id = 1L
        every { launchCacheMapper.domainToEntity(launchModel) } returns launchEntity
        coEvery { launchDiskDataSource.insert(launchEntity) } returns Result.Success(id)

        val result = underTest.insertLaunch(launchModel)

        assertTrue(result is Result.Success)
        assertEquals(id, (result).data)

        verify { launchCacheMapper.domainToEntity(launchModel) }
        coVerify { launchDiskDataSource.insert(launchEntity) }
    }

    @Test
    fun `insertLaunches returns result on success`() = runTest {
        val ids = longArrayOf(1L)
        every { launchCacheMapper.domainToEntity(launchModel) } returns launchEntity
        coEvery { launchDiskDataSource.insertList(launchesEntity) } returns Result.Success(ids)

        val result = underTest.insertLaunches(launchesModel)

        assertTrue(result is Result.Success)
        assertEquals(ids, (result).data)

        verify { launchCacheMapper.domainToEntity(launchModel) }
        coVerify { launchDiskDataSource.insertList(launchesEntity) }
    }

    @Test
    fun `deleteList returns result on success`() = runTest {
        val count = 1
        every { launchCacheMapper.domainToEntity(launchModel) } returns launchEntity
        coEvery { launchDiskDataSource.deleteList(launchesEntity) } returns Result.Success(count)

        val result = underTest.deleteList(launchesModel)

        assertTrue(result is Result.Success)
        assertEquals(count, (result).data)

        verify { launchCacheMapper.domainToEntity(launchModel) }
        coVerify { launchDiskDataSource.deleteList(launchesEntity) }
    }

    @Test
    fun `deleteAll returns result on success`() = runTest {
        coEvery { launchDiskDataSource.deleteAll() } returns Result.Success(Unit)

        val result = underTest.deleteAll()

        assertTrue(result is Result.Success)

        coVerify { launchDiskDataSource.deleteAll() }
    }

    @Test
    fun `deleteById returns result on success`() = runTest {
        val id = "1"
        val count = 1
        coEvery { launchDiskDataSource.deleteById(id) } returns Result.Success(count)

        val result = underTest.deleteById(id)

        assertTrue(result is Result.Success)
        assertEquals(count, (result).data)

        coVerify { launchDiskDataSource.deleteById(id) }
    }

    @Test
    fun `getById returns mapped launch on success`() = runTest {
        val id = "1"
        val launch = mockk<LaunchTypes.Launch>()
        coEvery { launchDiskDataSource.getById(id) } returns Result.Success(launchEntity)
        every { launchCacheMapper.entityToDomain(launchEntity) } returns launch

        val result = underTest.getById(id)

        assertTrue(result is Result.Success)
        assertEquals(launch, (result).data)

        coVerify { launchDiskDataSource.getById(id) }
        verify { launchCacheMapper.entityToDomain(launchEntity) }
    }

    @Test
    fun `getAll returns mapped launches on success`() = runTest {
        coEvery { launchDiskDataSource.getAll() } returns Result.Success(launchesEntity)
        every { launchCacheMapper.entityToDomainList(launchesEntity) } returns launchesModel

        val result = underTest.getAll()

        assertTrue(result is Result.Success)
        assertEquals(launchesModel, (result).data)

        coVerify { launchDiskDataSource.getAll() }
        verify { launchCacheMapper.entityToDomainList(launchesEntity) }
    }

    @Test
    fun `getTotalEntries returns result on success`() = runTest {
        val count = 10
        coEvery { launchDiskDataSource.getTotalEntries() } returns Result.Success(count)

        val result = underTest.getTotalEntries()

        assertTrue(result is Result.Success)
        assertEquals(count, (result).data)

        coVerify { launchDiskDataSource.getTotalEntries() }
    }

}