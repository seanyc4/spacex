package com.seancoyle.feature.launch.implementation.data.network.launch

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.result.DataError.RemoteError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.test.TestCoroutineRule
import com.seancoyle.feature.launch.implementation.data.repository.launch.LaunchRemoteDataSource
import com.seancoyle.feature.launch.implementation.util.TestData.launchOptions
import com.seancoyle.feature.launch.implementation.util.TestData.launchesDto
import com.seancoyle.feature.launch.implementation.util.TestData.launchesModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
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
class LaunchRemoteDataSourceImplTest {

    @get:Rule
    val testDispatcher = TestCoroutineRule()

    @MockK
    private lateinit var api: LaunchApi

    @MockK
    private lateinit var launchDtoDomainMapper: LaunchDtoDomainMapper

    @RelaxedMockK
    private lateinit var crashlytics: Crashlytics

    @RelaxedMockK
    private lateinit var remoteErrorMapper: RemoteErrorMapper

    private lateinit var underTest: LaunchRemoteDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = LaunchRemoteDataSourceImpl(
            api = api,
            crashlytics = crashlytics,
            remoteErrorMapper = remoteErrorMapper,
            launchDtoToDomainMapper = launchDtoDomainMapper
        )
    }

    @Test
    fun `getLaunches returns launches DTO when API call is successful`() = runTest {
        coEvery { api.getLaunches(launchOptions) } returns launchesDto
        every { launchDtoDomainMapper.dtoToDomainList(launchesDto) } returns launchesModel

        val result = underTest.getLaunches(launchOptions)

        assertTrue(result is LaunchResult.Success)
        assertEquals(launchesModel, result.data)
    }

    @Test
    fun `getLaunches returns DataError when API call fails`() = runTest {
        val exception = RuntimeException("Network Failure")
        val expected = RemoteError.NETWORK_UNKNOWN_ERROR
        coEvery { api.getLaunches(launchOptions) } throws exception
        every { remoteErrorMapper.map(exception) } returns expected

        val result = underTest.getLaunches(launchOptions)

        assertTrue(result is LaunchResult.Error)
        assertEquals(expected, result.error)
    }
}
