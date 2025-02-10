package com.seancoyle.feature.launch.implementation.data.repository.launch

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.core.domain.Order
import com.seancoyle.database.entities.LaunchStatusEntity
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.data.cache.launch.LaunchDomainEntityMapper
import com.seancoyle.feature.launch.implementation.data.network.launch.LaunchesDto
import com.seancoyle.feature.launch.implementation.data.network.launch.LaunchDtoDomainMapper
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
    private lateinit var launchDomainEntityMapper: LaunchDomainEntityMapper

    private lateinit var underTest: LaunchRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = LaunchRepositoryImpl(
            launchNetworkDataSource = launchNetworkDataSource,
            launchDiskDataSource = launchDiskDataSource,
            launchDtoToDomainMapper = launchDtoDomainMapper,
            launchDomainEntityMapper = launchDomainEntityMapper
        )
    }

    @Test
    fun `getLaunches returns mapped launches on success`() = runTest {
        coEvery { launchNetworkDataSource.getLaunches(launchOptions) } returns Result.Success(launchesDto)
        every { launchDtoDomainMapper.dtoToDomainList(LaunchesDto(listOf(launchDto))) } returns launchesModel

        val result = underTest.getLaunchesApi(launchOptions)

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
        every { launchDomainEntityMapper.mapToLaunchStatusEntity(launchStatus) } returns launchStatusEntity
        coEvery { launchDiskDataSource.paginate(launchYear, order, launchStatusEntity, page) } returns Result.Success(
            launchesEntity
        )
        every { launchDomainEntityMapper.entityToDomainList(launchesEntity) } returns launches

        val result = underTest.paginateCache(launchYear, order, launchStatus, page)

        coVerify { launchDiskDataSource.paginate(launchYear, order, launchStatusEntity, page) }
        verify { launchDomainEntityMapper.mapToLaunchStatusEntity(launchStatus) }
        verify { launchDomainEntityMapper.entityToDomainList(launchesEntity) }

        assertTrue(result is Result.Success)
        assertEquals(launches, (result).data)
    }

    @Test
    fun `insertLaunches returns result on success`() = runTest {
        every { launchDomainEntityMapper.domainToEntityList(launchesModel) } returns launchesEntity
        coEvery { launchDiskDataSource.insertList(launchesEntity) } returns Result.Success(Unit)

        val result = underTest.insertLaunchesCache(launchesModel)

        verify { launchDomainEntityMapper.domainToEntityList(launchesModel) }
        coVerify { launchDiskDataSource.insertList(launchesEntity) }

        assertTrue(result is Result.Success)
        assertEquals(Unit, (result).data)
    }

    @Test
    fun `insertLaunches returns error`() = runTest {
        val expected = DataError.CACHE_ERROR
        every { launchDomainEntityMapper.domainToEntityList(launchesModel) } returns launchesEntity
        coEvery { launchDiskDataSource.insertList(launchesEntity) } returns Result.Error(expected)

        val result = underTest.insertLaunchesCache(launchesModel)

        verify { launchDomainEntityMapper.domainToEntityList(launchesModel) }
        coVerify { launchDiskDataSource.insertList(launchesEntity) }

        assertTrue(result is Result.Error)
        assertEquals(expected, (result).error)
    }

    @Test
    fun `deleteList returns result on success`() = runTest {
        val count = 1
        every { launchDomainEntityMapper.domainToEntity(launchModel) } returns launchEntity
        coEvery { launchDiskDataSource.deleteList(launchesEntity) } returns Result.Success(count)

        val result = underTest.deleteLaunhesCache(launchesModel)

        verify { launchDomainEntityMapper.domainToEntity(launchModel) }
        coVerify { launchDiskDataSource.deleteList(launchesEntity) }

        assertTrue(result is Result.Success)
        assertEquals(count, (result).data)
    }

    @Test
    fun `deleteAll returns result on success`() = runTest {
        coEvery { launchDiskDataSource.deleteAll() } returns Result.Success(Unit)

        val result = underTest.deleteAllCache()

        coVerify { launchDiskDataSource.deleteAll() }

        assertTrue(result is Result.Success)
    }

    @Test
    fun `deleteById returns result on success`() = runTest {
        val id = "1"
        val count = 1
        coEvery { launchDiskDataSource.deleteById(id) } returns Result.Success(count)

        val result = underTest.deleteByIdCache(id)

        coVerify { launchDiskDataSource.deleteById(id) }

        assertTrue(result is Result.Success)
        assertEquals(count, (result).data)
    }

    @Test
    fun `getById returns mapped launch on success`() = runTest {
        val id = "1"
        val launch = mockk<LaunchTypes.Launch>()
        coEvery { launchDiskDataSource.getById(id) } returns Result.Success(launchEntity)
        every { launchDomainEntityMapper.entityToDomain(launchEntity) } returns launch

        val result = underTest.getByIdCache(id)

        coVerify { launchDiskDataSource.getById(id) }
        verify { launchDomainEntityMapper.entityToDomain(launchEntity) }

        assertTrue(result is Result.Success)
        assertEquals(launch, (result).data)
    }

    @Test
    fun `getAll returns mapped launches on success`() = runTest {
        coEvery { launchDiskDataSource.getAll() } returns Result.Success(launchesEntity)
        every { launchDomainEntityMapper.entityToDomainList(launchesEntity) } returns launchesModel

        val result = underTest.getAllCache()

        coVerify { launchDiskDataSource.getAll() }
        verify { launchDomainEntityMapper.entityToDomainList(launchesEntity) }

        assertTrue(result is Result.Success)
        assertEquals(launchesModel, (result).data)
    }

    @Test
    fun `getTotalEntries returns result on success`() = runTest {
        val count = 10
        coEvery { launchDiskDataSource.getTotalEntries() } returns Result.Success(count)

        val result = underTest.getTotalEntriesCache()

        coVerify { launchDiskDataSource.getTotalEntries() }

        assertTrue(result is Result.Success)
        assertEquals(count, (result).data)
    }

}