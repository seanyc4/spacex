package com.seancoyle.feature.launch.data.local

import androidx.datastore.core.DataStore
import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.datastore_proto.LaunchPreferencesProto
import com.seancoyle.core.datastore_proto.OrderProto
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.data.repository.LaunchesPreferencesDataSource
import com.seancoyle.feature.launch.domain.model.LaunchPrefs
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.slot
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class LaunchesPreferencesDataSourceImplTest {

    @MockK
    private lateinit var dataStore: DataStore<LaunchPreferencesProto>

    @RelaxedMockK
    private lateinit var crashlytics: Crashlytics

    private lateinit var underTest: LaunchesPreferencesDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = LaunchesPreferencesDataSourceImpl(
            dataStore = dataStore,
            crashlytics = crashlytics
        )
    }

    @Test
    fun `GIVEN ASC order WHEN saveLaunchPreferences THEN saves to datastore`() = runTest {
        val order = Order.ASC
        val updateSlot = slot<suspend (LaunchPreferencesProto) -> LaunchPreferencesProto>()
        val currentProto = createLaunchPreferencesProto(OrderProto.DESC)
        coEvery { dataStore.updateData(capture(updateSlot)) } coAnswers {
            val updateFunction = updateSlot.captured
            updateFunction(currentProto)
        }

        underTest.saveLaunchPreferences(order)

        coVerify { dataStore.updateData(any()) }
    }

    @Test
    fun `GIVEN DESC order WHEN saveLaunchPreferences THEN saves to datastore`() = runTest {
        val order = Order.DESC
        val updateSlot = slot<suspend (LaunchPreferencesProto) -> LaunchPreferencesProto>()
        val currentProto = createLaunchPreferencesProto(OrderProto.ASC)
        coEvery { dataStore.updateData(capture(updateSlot)) } coAnswers {
            val updateFunction = updateSlot.captured
            updateFunction(currentProto)
        }

        underTest.saveLaunchPreferences(order)

        coVerify { dataStore.updateData(any()) }
    }

    @Test
    fun `GIVEN datastore throws exception WHEN saveLaunchPreferences THEN handles gracefully`() = runTest {
        val order = Order.ASC
        val exception = RuntimeException("DataStore error")
        coEvery { dataStore.updateData(any()) } throws exception

        underTest.saveLaunchPreferences(order)

        coVerify { dataStore.updateData(any()) }
        coVerify { crashlytics.logException(exception) }
    }

    @Test
    fun `GIVEN proto has ASC order WHEN getLaunchPreferences THEN returns ASC`() = runTest {
        val proto = createLaunchPreferencesProto(OrderProto.ASC)
        every { dataStore.data } returns flowOf(proto)

        val result = underTest.getLaunchPreferences()

        assertEquals(Order.ASC, result.order)
    }

    @Test
    fun `GIVEN proto has DESC order WHEN getLaunchPreferences THEN returns DESC`() = runTest {
        val proto = createLaunchPreferencesProto(OrderProto.DESC)
        every { dataStore.data } returns flowOf(proto)

        val result = underTest.getLaunchPreferences()

        assertEquals(Order.DESC, result.order)
    }

    @Test
    fun `GIVEN datastore throws exception WHEN getLaunchPreferences THEN returns default`() = runTest {
        val exception = RuntimeException("DataStore error")
        every { dataStore.data } throws exception

        val result = underTest.getLaunchPreferences()

        assertEquals(LaunchPrefs(), result)
        assertEquals(Order.ASC, result.order)
        coVerify { crashlytics.logException(exception) }
    }

    @Test
    fun `GIVEN corrupted data WHEN getLaunchPreferences THEN returns default`() = runTest {
        val exception = IllegalArgumentException("Invalid proto format")
        every { dataStore.data } throws exception

        val result = underTest.getLaunchPreferences()

        assertEquals(LaunchPrefs(), result)
        coVerify { crashlytics.logException(exception) }
    }

    @Test
    fun `GIVEN saved preferences WHEN getLaunchPreferences THEN retrieves correctly`() = runTest {
        val order = Order.DESC
        val updateSlot = slot<suspend (LaunchPreferencesProto) -> LaunchPreferencesProto>()
        val initialProto = createLaunchPreferencesProto(OrderProto.ASC)
        val savedProto = createLaunchPreferencesProto(OrderProto.DESC)
        coEvery { dataStore.updateData(capture(updateSlot)) } coAnswers {
            val updateFunction = updateSlot.captured
            updateFunction(initialProto)
        }
        every { dataStore.data } returns flowOf(savedProto)

        underTest.saveLaunchPreferences(order)
        val result = underTest.getLaunchPreferences()

        assertEquals(Order.DESC, result.order)
        coVerify { dataStore.updateData(any()) }
    }

    @Test
    fun `GIVEN multiple saves WHEN saveLaunchPreferences called twice THEN updates datastore twice`() = runTest {
        val updateSlot = slot<suspend (LaunchPreferencesProto) -> LaunchPreferencesProto>()
        val currentProto = createLaunchPreferencesProto(OrderProto.ASC)
        coEvery { dataStore.updateData(capture(updateSlot)) } coAnswers {
            val updateFunction = updateSlot.captured
            updateFunction(currentProto)
        }

        underTest.saveLaunchPreferences(Order.ASC)
        underTest.saveLaunchPreferences(Order.DESC)

        coVerify(exactly = 2) { dataStore.updateData(any()) }
    }

    private fun createLaunchPreferencesProto(order: OrderProto): LaunchPreferencesProto {
        return LaunchPreferencesProto.newBuilder()
            .setOrder(order)
            .build()
    }
}
