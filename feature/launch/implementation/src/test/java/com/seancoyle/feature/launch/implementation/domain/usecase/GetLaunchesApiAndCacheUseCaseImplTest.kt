package com.seancoyle.feature.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.api.domain.usecase.GetLaunchesApiAndCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.model.LaunchOptions
import com.seancoyle.feature.launch.implementation.domain.network.LaunchNetworkDataSource
import com.seancoyle.feature.launch.implementation.util.TestData.INSERT_SUCCESS
import com.seancoyle.feature.launch.implementation.util.TestData.launchesModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetLaunchesApiAndCacheUseCaseImplTest {

    @MockK
    private lateinit var insertLaunchesToCacheUseCase: InsertLaunchesToCacheUseCase

    @MockK
    private lateinit var launchNetworkDataSource: LaunchNetworkDataSource

    @MockK
    private lateinit var launchOptions: LaunchOptions

    private lateinit var underTest: GetLaunchesApiAndCacheUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = GetLaunchesApiAndCacheUseCaseImpl(
            insertLaunchesToCacheUseCase = insertLaunchesToCacheUseCase,
            launchNetworkDataSource = launchNetworkDataSource,
            launchOptions = launchOptions
        )
    }

    @Test
    fun `invoke should emit success result when network and cache operations are successful`() = runTest {
        coEvery { launchNetworkDataSource.getLaunches(launchOptions) } returns Result.Success(launchesModel)
        coEvery { insertLaunchesToCacheUseCase(launchesModel) } returns Result.Success(INSERT_SUCCESS)

        val results = mutableListOf<Result<List<LaunchTypes.Launch>, DataError>>()
        underTest().collect { results.add(it) }

        assertTrue(results.first() is Result.Success)
        assertEquals(launchesModel, (results.first() as Result.Success).data)
    }

    @Test
    fun `invoke should emit error when network fetch fails`() = runTest {
        val networkError = DataError.NETWORK_UNKNOWN_ERROR
        coEvery { launchNetworkDataSource.getLaunches(launchOptions) } returns Result.Error(networkError)

        val results = mutableListOf<Result<List<LaunchTypes.Launch>, DataError>>()
        underTest().collect { results.add(it) }

        assertTrue(results.first() is Result.Error)
        assertEquals(networkError, (results.first() as Result.Error).error)
    }

    @Test
    fun `invoke should emit error when cache operation fails`() = runTest {
        val cacheError = DataError.CACHE_ERROR
        coEvery { launchNetworkDataSource.getLaunches(launchOptions) } returns Result.Success(launchesModel)
        coEvery { insertLaunchesToCacheUseCase(launchesModel) } returns Result.Error(cacheError)

        val results = mutableListOf<Result<List<LaunchTypes.Launch>, DataError>>()
        underTest().collect { results.add(it) }

        assertTrue(results.first() is Result.Error)
        assertEquals(cacheError, (results.first() as Result.Error).error)
    }
}