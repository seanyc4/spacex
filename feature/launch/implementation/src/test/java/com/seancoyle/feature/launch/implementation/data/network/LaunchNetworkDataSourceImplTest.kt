package com.seancoyle.feature.launch.implementation.data.network

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.core.test.TestCoroutineRule
import com.seancoyle.feature.launch.implementation.util.TestData.launchOptions
import com.seancoyle.feature.launch.implementation.util.TestData.launchesDto
import com.seancoyle.feature.launch.implementation.util.TestData.launchesModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class LaunchNetworkDataSourceImplTest {

    @get:Rule
    val testDispatcher = TestCoroutineRule()

    @MockK
    private lateinit var api: LaunchApi

    @MockK
    private lateinit var networkMapper: LaunchNetworkMapper

    @MockK(relaxed = true)
    private lateinit var crashlytics: Crashlytics

    private lateinit var underTest: LaunchNetworkDataSourceImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = LaunchNetworkDataSourceImpl(
            api = api,
            networkMapper = networkMapper,
            crashlytics = crashlytics,
            ioDispatcher = testDispatcher.testCoroutineDispatcher
        )
    }

    @Test
    fun `getLaunches returns mapped launches when API call is successful`() = runTest {
        coEvery { api.getLaunches(launchOptions) } returns launchesDto
        coEvery { networkMapper.mapEntityToList(launchesDto) } returns launchesModel

        val result = underTest.getLaunches(launchOptions)

        assertTrue(result is Result.Success)
        assertEquals(launchesModel, result.data)
    }

    @Test
    fun `getLaunches returns DataError when API call fails`() = runTest {
        coEvery { api.getLaunches(launchOptions) } throws RuntimeException("Network failure")

        val result = underTest.getLaunches(launchOptions)

        assertTrue(result is Result.Error)
        assertEquals(DataError.NETWORK_UNKNOWN_ERROR, result.error)
    }
}
