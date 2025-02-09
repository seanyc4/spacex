package com.seancoyle.feature.launch.implementation.data.repository

import com.seancoyle.core.common.result.Result
import com.seancoyle.core.domain.Order
import com.seancoyle.database.entities.LaunchStatusEntity
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.data.cache.LaunchCacheDataSource
import com.seancoyle.feature.launch.implementation.data.cache.mapper.LaunchEntityMapper
import com.seancoyle.feature.launch.implementation.data.network.LaunchNetworkDataSource
import com.seancoyle.feature.launch.implementation.data.network.dto.LaunchesDto
import com.seancoyle.feature.launch.implementation.data.network.mapper.LaunchDtoDomainMapper
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
    private lateinit var launchCacheDataSource: LaunchCacheDataSource

    @MockK
    private lateinit var launchNetworkDataSource: LaunchNetworkDataSource

    @MockK
    private lateinit var launchDtoDomainMapper: LaunchDtoDomainMapper

    @MockK
    private lateinit var launchCacheMapper: LaunchEntityMapper

    private lateinit var underTest: LaunchRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = LaunchRepositoryImpl(
            launchNetworkDataSource = launchNetworkDataSource,
            launchCacheDataSource = launchCacheDataSource,
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

        val result = underTest.getLaunchesAndCache(launchOptions)

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
        coEvery { launchCacheDataSource.paginateLaunches(launchYear, order, launchStatusEntity, page) } returns Result.Success(
            launchesEntity
        )
        every { launchCacheMapper.entityToDomainList(launchesEntity) } returns launches

        val result = underTest.paginateLaunches(launchYear, order, launchStatus, page)

        assertTrue(result is Result.Success)
        assertEquals(launches, (result).data)

        coVerify { launchCacheDataSource.paginateLaunches(launchYear, order, launchStatusEntity, page) }
        verify { launchCacheMapper.toLaunchStatusEntity(launchStatus) }
        verify { launchCacheMapper.entityToDomainList(launchesEntity) }
    }

    @Test
    fun `insertLaunch returns result on success`() = runTest {
        val id = 1L
        every { launchCacheMapper.domainToEntity(launchModel) } returns launchEntity
        coEvery { launchCacheDataSource.insert(launchEntity) } returns Result.Success(id)

        val result = underTest.insertLaunch(launchModel)

        assertTrue(result is Result.Success)
        assertEquals(id, (result).data)

        verify { launchCacheMapper.domainToEntity(launchModel) }
        coVerify { launchCacheDataSource.insert(launchEntity) }
    }

    @Test
    fun `insertLaunches returns result on success`() = runTest {
        val ids = longArrayOf(1L)
        every { launchCacheMapper.domainToEntity(launchModel) } returns launchEntity
        coEvery { launchCacheDataSource.insertList(launchesEntity) } returns Result.Success(ids)

        val result = underTest.insertLaunches(launchesModel)

        assertTrue(result is Result.Success)
        assertEquals(ids, (result).data)

        verify { launchCacheMapper.domainToEntity(launchModel) }
        coVerify { launchCacheDataSource.insertList(launchesEntity) }
    }

    @Test
    fun `deleteList returns result on success`() = runTest {
        val count = 1
        every { launchCacheMapper.domainToEntity(launchModel) } returns launchEntity
        coEvery { launchCacheDataSource.deleteList(launchesEntity) } returns Result.Success(count)

        val result = underTest.deleteList(launchesModel)

        assertTrue(result is Result.Success)
        assertEquals(count, (result).data)

        verify { launchCacheMapper.domainToEntity(launchModel) }
        coVerify { launchCacheDataSource.deleteList(launchesEntity) }
    }

    @Test
    fun `deleteAll returns result on success`() = runTest {
        coEvery { launchCacheDataSource.deleteAll() } returns Result.Success(Unit)

        val result = underTest.deleteAll()

        assertTrue(result is Result.Success)

        coVerify { launchCacheDataSource.deleteAll() }
    }

    @Test
    fun `deleteById returns result on success`() = runTest {
        val id = "1"
        val count = 1
        coEvery { launchCacheDataSource.deleteById(id) } returns Result.Success(count)

        val result = underTest.deleteById(id)

        assertTrue(result is Result.Success)
        assertEquals(count, (result).data)

        coVerify { launchCacheDataSource.deleteById(id) }
    }

    @Test
    fun `getById returns mapped launch on success`() = runTest {
        val id = "1"
        val launch = mockk<LaunchTypes.Launch>()
        coEvery { launchCacheDataSource.getById(id) } returns Result.Success(launchEntity)
        every { launchCacheMapper.entityToDomain(launchEntity) } returns launch

        val result = underTest.getById(id)

        assertTrue(result is Result.Success)
        assertEquals(launch, (result).data)

        coVerify { launchCacheDataSource.getById(id) }
        verify { launchCacheMapper.entityToDomain(launchEntity) }
    }

    @Test
    fun `getAll returns mapped launches on success`() = runTest {
        coEvery { launchCacheDataSource.getAll() } returns Result.Success(launchesEntity)
        every { launchCacheMapper.entityToDomainList(launchesEntity) } returns launchesModel

        val result = underTest.getAll()

        assertTrue(result is Result.Success)
        assertEquals(launchesModel, (result).data)

        coVerify { launchCacheDataSource.getAll() }
        verify { launchCacheMapper.entityToDomainList(launchesEntity) }
    }

    @Test
    fun `getTotalEntries returns result on success`() = runTest {
        val count = 10
        coEvery { launchCacheDataSource.getTotalEntries() } returns Result.Success(count)

        val result = underTest.getTotalEntries()

        assertTrue(result is Result.Success)
        assertEquals(count, (result).data)

        coVerify { launchCacheDataSource.getTotalEntries() }
    }

}