package com.seancoyle.feature.launch.implementation.data.repository

import com.seancoyle.database.entities.LaunchStatusEntity
import com.seancoyle.feature.launch.implementation.data.cache.CompanyCacheDataSource
import com.seancoyle.feature.launch.implementation.data.cache.LaunchCacheDataSource
import com.seancoyle.feature.launch.implementation.data.cache.LaunchPreferencesDataSource
import com.seancoyle.feature.launch.implementation.data.network.CompanyNetworkDataSource
import com.seancoyle.feature.launch.implementation.data.network.LaunchNetworkDataSource
import com.seancoyle.feature.launch.implementation.data.network.mapper.CompanyNetworkMapper
import com.seancoyle.feature.launch.implementation.data.network.mapper.LaunchNetworkMapper
import com.seancoyle.feature.launch.implementation.util.TestData.companyEntity
import com.seancoyle.feature.launch.implementation.util.TestData.companyModel
import com.seancoyle.feature.launch.implementation.util.TestData.launchEntity
import com.seancoyle.feature.launch.implementation.util.TestData.launchModel
import com.seancoyle.feature.launch.implementation.util.TestData.launchOptions
import com.seancoyle.feature.launch.implementation.util.TestData.launchesEntity
import com.seancoyle.feature.launch.implementation.util.TestData.launchesModel
import com.seancoyle.core.common.result.Result
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.*
import com.seancoyle.feature.launch.implementation.data.cache.mapper.*
import com.seancoyle.feature.launch.implementation.data.network.dto.LaunchesDto
import com.seancoyle.feature.launch.implementation.util.TestData.companyDto
import com.seancoyle.feature.launch.implementation.util.TestData.launchDto
import com.seancoyle.feature.launch.implementation.util.TestData.launchesDto
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import io.mockk.impl.annotations.MockK

class LaunchPreferencesRepositoryImplTest {

    private lateinit var repository: LaunchPreferencesRepositoryImpl

    @MockK
    private lateinit var companyNetworkDataSource: CompanyNetworkDataSource

    @MockK
    private lateinit var launchNetworkDataSource: LaunchNetworkDataSource

    @MockK
    private lateinit var launchCacheDataSource: LaunchCacheDataSource

    @MockK
    private lateinit var companyCacheDataSource: CompanyCacheDataSource

    @MockK
    private lateinit var launchPreferencesDataSource: LaunchPreferencesDataSource

    @MockK
    private lateinit var companyNetworkMapper: CompanyNetworkMapper

    @MockK
    private lateinit var launchNetworkMapper: LaunchNetworkMapper

    @MockK
    private lateinit var companyCacheMapper: CompanyEntityMapper

    @MockK
    private lateinit var launchCacheMapper: LaunchEntityMapper

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repository = LaunchPreferencesRepositoryImpl(
            companyNetworkDataSource = companyNetworkDataSource,
            launchNetworkDataSource = launchNetworkDataSource,
            launchCacheDataSource = launchCacheDataSource,
            companyCacheDataSource = companyCacheDataSource,
            launchPreferencesDataSource = launchPreferencesDataSource,
            companyNetworkMapper = companyNetworkMapper,
            launchNetworkMapper = launchNetworkMapper,
            companyCacheMapper = companyCacheMapper,
            launchCacheMapper = launchCacheMapper
        )
    }

    @Test
    fun `getCompany returns mapped company on success`() = runTest {
        coEvery { companyNetworkDataSource.getCompany() } returns Result.Success(companyDto)
        every { companyNetworkMapper.dtoToDomain(companyDto) } returns companyModel

        val result = repository.getCompany()

        assertTrue(result is Result.Success)
        assertEquals(companyModel, (result).data)

        coVerify { companyNetworkDataSource.getCompany() }
        verify { companyNetworkMapper.dtoToDomain(companyDto) }
    }

    @Test
    fun `getLaunches returns mapped launches on success`() = runTest {
        coEvery { launchNetworkDataSource.getLaunches(launchOptions) } returns Result.Success(launchesDto)
        every { launchNetworkMapper.dtoToDomainList(LaunchesDto(listOf(launchDto))) } returns launchesModel

        val result = repository.getLaunches(launchOptions)

        assertTrue(result is Result.Success)
        assertEquals(launchesModel, (result).data)

        coVerify { launchNetworkDataSource.getLaunches(launchOptions) }
        verify { launchNetworkMapper.dtoToDomainList(LaunchesDto(listOf(launchDto))) }
    }

    @Test
    fun `saveLaunchPreferences saves preferences`() = runTest {
        val order = Order.ASC
        val launchStatus = LaunchStatus.SUCCESS
        val launchYear = "2022"
        coEvery { launchPreferencesDataSource.saveLaunchPreferences(order, launchStatus, launchYear) } just Runs

        repository.saveLaunchPreferences(order, launchStatus, launchYear)

        coVerify { launchPreferencesDataSource.saveLaunchPreferences(order, launchStatus, launchYear) }
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
        coEvery { launchCacheDataSource.paginateLaunches(launchYear, order, launchStatusEntity, page) } returns Result.Success(launchesEntity)
        every { launchCacheMapper.entityToDomainList(launchesEntity) } returns launches

        val result = repository.paginateLaunches(launchYear, order, launchStatus, page)

        assertTrue(result is Result.Success)
        assertEquals(launches, (result).data)

        coVerify { launchCacheDataSource.paginateLaunches(launchYear, order, launchStatusEntity, page) }
        verify { launchCacheMapper.toLaunchStatusEntity(launchStatus) }
        verify { launchCacheMapper.entityToDomainList(launchesEntity) }
    }

    @Test
    fun `getLaunchPreferences returns preferences`() = runTest {
        val launchPrefs = mockk<LaunchPrefs>()
        coEvery { launchPreferencesDataSource.getLaunchPreferences() } returns launchPrefs

        val result = repository.getLaunchPreferences()

        assertEquals(launchPrefs, result)

        coVerify { launchPreferencesDataSource.getLaunchPreferences() }
    }

    @Test
    fun `insertLaunch returns result on success`() = runTest {
        val id = 1L
        every { launchCacheMapper.domainToEntity(launchModel) } returns launchEntity
        coEvery { launchCacheDataSource.insert(launchEntity) } returns Result.Success(id)

        val result = repository.insertLaunch(launchModel)

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

        val result = repository.insertLaunches(launchesModel)

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

        val result = repository.deleteList(launchesModel)

        assertTrue(result is Result.Success)
        assertEquals(count, (result).data)

        verify { launchCacheMapper.domainToEntity(launchModel) }
        coVerify { launchCacheDataSource.deleteList(launchesEntity) }
    }

    @Test
    fun `deleteAll returns result on success`() = runTest {
        coEvery { launchCacheDataSource.deleteAll() } returns Result.Success(Unit)

        val result = repository.deleteAll()

        assertTrue(result is Result.Success)

        coVerify { launchCacheDataSource.deleteAll() }
    }

    @Test
    fun `deleteById returns result on success`() = runTest {
        val id = "1"
        val count = 1
        coEvery { launchCacheDataSource.deleteById(id) } returns Result.Success(count)

        val result = repository.deleteById(id)

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

        val result = repository.getById(id)

        assertTrue(result is Result.Success)
        assertEquals(launch, (result).data)

        coVerify { launchCacheDataSource.getById(id) }
        verify { launchCacheMapper.entityToDomain(launchEntity) }
    }

    @Test
    fun `getAll returns mapped launches on success`() = runTest {
        coEvery { launchCacheDataSource.getAll() } returns Result.Success(launchesEntity)
        every { launchCacheMapper.entityToDomainList(launchesEntity) } returns launchesModel

        val result = repository.getAll()

        assertTrue(result is Result.Success)
        assertEquals(launchesModel, (result).data)

        coVerify { launchCacheDataSource.getAll() }
        verify { launchCacheMapper.entityToDomainList(launchesEntity) }
    }

    @Test
    fun `getTotalEntries returns result on success`() = runTest {
        val count = 10
        coEvery { launchCacheDataSource.getTotalEntries() } returns Result.Success(count)

        val result = repository.getTotalEntries()

        assertTrue(result is Result.Success)
        assertEquals(count, (result).data)

        coVerify { launchCacheDataSource.getTotalEntries() }
    }

    @Test
    fun `insertCompany returns result on success`() = runTest {
        val id = 1L
        every { companyCacheMapper.domainToEntity(companyModel) } returns companyEntity
        coEvery { companyCacheDataSource.insert(companyEntity) } returns Result.Success(id)

        val result = repository.insertCompany(companyModel)

        assertTrue(result is Result.Success)
        assertEquals(id, (result).data)

        verify { companyCacheMapper.domainToEntity(companyModel) }
        coVerify { companyCacheDataSource.insert(companyEntity) }
    }

    @Test
    fun `getCompanyFromCache returns mapped company on success`() = runTest {
        coEvery { companyCacheDataSource.getCompany() } returns Result.Success(companyEntity)
        every { companyCacheMapper.entityToDomain(companyEntity) } returns companyModel

        val result = repository.getCompanyFromCache()

        assertTrue(result is Result.Success)
        assertEquals(companyModel, (result).data)

        coVerify { companyCacheDataSource.getCompany() }
        verify { companyCacheMapper.entityToDomain(companyEntity) }
    }

    @Test
    fun `deleteAllCompanyCache returns result on success`() = runTest {
        coEvery { companyCacheDataSource.deleteAll() } returns Result.Success(Unit)

        val result = repository.deleteAllCompanyCache()

        assertTrue(result is Result.Success)

        coVerify { companyCacheDataSource.deleteAll() }
    }
}