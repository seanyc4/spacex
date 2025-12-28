package com.seancoyle.feature.launch.domain.usecase.launch

import androidx.paging.PagingData
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.domain.model.Launch
import com.seancoyle.feature.launch.domain.model.LaunchQuery
import com.seancoyle.feature.launch.domain.repository.LaunchRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertSame

class ObserveLaunchesUseCaseTest {

    @MockK
    private lateinit var launchRepository: LaunchRepository

    private lateinit var underTest: ObserveLaunchesUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = ObserveLaunchesUseCase(
            launchRepository = launchRepository
        )
    }

    @Test
    fun `invoke delegates to repository and returns flow`() = runTest {
        val launchQuery = LaunchQuery(query = "Falcon", order = Order.DESC)
        val expectedFlow = flowOf(PagingData.empty<Launch>())
        every { launchRepository.pager(launchQuery) } returns expectedFlow

        val result = underTest.invoke(launchQuery)

        assertSame(expectedFlow, result)
        verify(exactly = 1) { launchRepository.pager(launchQuery) }
    }
}
