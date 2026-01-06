package com.seancoyle.feature.launch.domain.usecase.launch

import com.seancoyle.core.common.result.DataError.RemoteError
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.feature.launch.domain.repository.LaunchesRepository
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.util.TestData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetLaunchUseCaseTest {

    @MockK
    private lateinit var launchesRepository: LaunchesRepository

    private lateinit var underTest: GetLaunchUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        underTest = GetLaunchUseCase(launchesRepository)
    }

    @Test
    fun `invoke returns success when repository returns launch`() = runTest {
        val id = "123"
        val launchType = LaunchesType.UPCOMING
        val launch = TestData.createLaunch()
        val expected = LaunchResult.Success(launch)
        coEvery { launchesRepository.getLaunch(id, launchType) } returns expected

        val result = underTest(id, launchType)

        assertTrue(result is LaunchResult.Success)
        assertEquals(launch, (result as LaunchResult.Success).data)
    }

    @Test
    fun `invoke returns error when repository returns error`() = runTest {
        val id = "123"
        val launchType = LaunchesType.PAST
        val error = RemoteError.NETWORK_UNKNOWN_ERROR
        val expected = LaunchResult.Error(error)
        coEvery { launchesRepository.getLaunch(id, launchType) } returns expected

        val result = underTest(id, launchType)

        assertTrue(result is LaunchResult.Error)
        assertEquals(error, (result as LaunchResult.Error).error)
    }
}
