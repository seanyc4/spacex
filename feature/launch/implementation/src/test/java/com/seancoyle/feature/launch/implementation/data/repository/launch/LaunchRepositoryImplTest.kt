package com.seancoyle.feature.launch.implementation.data.repository.launch

import com.seancoyle.core.common.result.DataError.LocalError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.implementation.domain.repository.LaunchRepository
import com.seancoyle.feature.launch.implementation.util.TestData.launchModel
import com.seancoyle.feature.launch.implementation.util.TestData.launchOptions
import com.seancoyle.feature.launch.implementation.util.TestData.launchesModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LaunchRepositoryImplTest {

    @MockK
    private lateinit var launchLocalDataSource: LaunchLocalDataSource

    @MockK
    private lateinit var launchRemoteDataSource: LaunchRemoteDataSource

    private lateinit var underTest: LaunchRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = LaunchRepositoryImpl(
            launchRemoteDataSource = launchRemoteDataSource,
            launchLocalDataSource = launchLocalDataSource
        )
    }

    @Test
    fun `getLaunches returns mapped launches on success`() = runTest {
        coEvery { launchRemoteDataSource.getLaunches(launchOptions) } returns LaunchResult.Success(launchesModel)

        val result = underTest.getLaunchesApi(launchOptions)

        assertTrue(result is LaunchResult.Success)
        assertEquals(launchesModel, result.data)

        coVerify { launchRemoteDataSource.getLaunches(launchOptions) }
    }

    @Test
    fun `paginateLaunches returns mapped launches on success`() = runTest {
        val launchYear = "2022"
        val order = Order.ASC
        val launchStatus = LaunchStatus.SUCCESS
        val page = 1
        coEvery { launchLocalDataSource.paginate(launchYear, order, launchStatus, page) } returns LaunchResult.Success(launchesModel)

        val result = underTest.paginateCache(launchYear, order, launchStatus, page)

        coVerify { launchLocalDataSource.paginate(launchYear, order, launchStatus, page) }

        assertTrue(result is LaunchResult.Success)
        assertEquals(launchesModel, result.data)
    }

    @Test
    fun `insertLaunches returns result on success`() = runTest {
        coEvery { launchLocalDataSource.insertList(launchesModel) } returns LaunchResult.Success(Unit)

        val result = underTest.insertLaunchesCache(launchesModel)

        coVerify { launchLocalDataSource.insertList(launchesModel) }

        assertTrue(result is LaunchResult.Success)
        assertEquals(Unit, result.data)
    }

    @Test
    fun `insertLaunches returns error`() = runTest {
        val expected = LocalError.CACHE_ERROR
        coEvery { launchLocalDataSource.insertList(launchesModel) } returns LaunchResult.Error(expected)

        val result = underTest.insertLaunchesCache(launchesModel)

        coVerify { launchLocalDataSource.insertList(launchesModel) }

        assertTrue(result is LaunchResult.Error)
        assertEquals(expected, (result).error)
    }

    @Test
    fun `deleteList returns result on success`() = runTest {
        val count = 1
        coEvery { launchLocalDataSource.deleteList(launchesModel) } returns LaunchResult.Success(count)

        val result = underTest.deleteLaunhesCache(launchesModel)

        coVerify { launchLocalDataSource.deleteList(launchesModel) }

        assertTrue(result is LaunchResult.Success)
        assertEquals(count, result.data)
    }

    @Test
    fun `deleteAll returns result on success`() = runTest {
        coEvery { launchLocalDataSource.deleteAll() } returns LaunchResult.Success(Unit)

        val result = underTest.deleteAllCache()

        coVerify { launchLocalDataSource.deleteAll() }

        assertTrue(result is LaunchResult.Success)
    }

    @Test
    fun `deleteById returns result on success`() = runTest {
        val id = "1"
        val count = 1
        coEvery { launchLocalDataSource.deleteById(id) } returns LaunchResult.Success(count)

        val result = underTest.deleteByIdCache(id)

        coVerify { launchLocalDataSource.deleteById(id) }

        assertTrue(result is LaunchResult.Success)
        assertEquals(count, result.data)
    }

    @Test
    fun `getById returns mapped launch on success`() = runTest {
        val id = "1"
        coEvery { launchLocalDataSource.getById(id) } returns LaunchResult.Success(launchModel)

        val result = underTest.getByIdCache(id)

        coVerify { launchLocalDataSource.getById(id) }

        assertTrue(result is LaunchResult.Success)
        assertEquals(launchModel, result.data)
    }

    @Test
    fun `getAll returns mapped launches on success`() = runTest {
        coEvery { launchLocalDataSource.getAll() } returns LaunchResult.Success(launchesModel)

        val result = underTest.getAllCache()

        coVerify { launchLocalDataSource.getAll() }

        assertTrue(result is LaunchResult.Success)
        assertEquals(launchesModel, result.data)
    }

    @Test
    fun `getTotalEntries returns result on success`() = runTest {
        val count = 10
        coEvery { launchLocalDataSource.getTotalEntries() } returns LaunchResult.Success(count)

        val result = underTest.getTotalEntriesCache()

        coVerify { launchLocalDataSource.getTotalEntries() }

        assertTrue(result is LaunchResult.Success)
        assertEquals(count, result.data)
    }

}