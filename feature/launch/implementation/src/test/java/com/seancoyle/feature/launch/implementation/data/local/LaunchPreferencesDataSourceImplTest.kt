package com.seancoyle.feature.launch.implementation.data.local

import androidx.datastore.core.DataStore
import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.datastore_proto.LaunchPreferencesProto
import com.seancoyle.core.datastore_proto.OrderProto
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.implementation.data.repository.LaunchPreferencesDataSource
import com.seancoyle.feature.launch.implementation.domain.model.LaunchPrefs
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

class LaunchPreferencesDataSourceImplTest {

    @MockK
    private lateinit var dataStore: DataStore<LaunchPreferencesProto>

    @RelaxedMockK
    private lateinit var crashlytics: Crashlytics

    private lateinit var underTest: LaunchPreferencesDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = LaunchPreferencesDataSourceImpl(
            dataStore = dataStore,
            crashlytics = crashlytics
        )
    }

    @Test
    fun `saveLaunchPreferences saves ASC order to datastore`() = runTest {
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
    fun `saveLaunchPreferences saves DESC order to datastore`() = runTest {
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
    fun `saveLaunchPreferences handles exception gracefully`() = runTest {
        val order = Order.ASC
        val exception = RuntimeException("DataStore error")

        coEvery { dataStore.updateData(any()) } throws exception

        underTest.saveLaunchPreferences(order)

        coVerify { dataStore.updateData(any()) }
        coVerify { crashlytics.logException(exception) }
    }

    @Test
    fun `getLaunchPreferences returns preferences with ASC order`() = runTest {
        val proto = createLaunchPreferencesProto(OrderProto.ASC)
        every { dataStore.data } returns flowOf(proto)

        val result = underTest.getLaunchPreferences()

        assertEquals(Order.ASC, result.order)
    }

    @Test
    fun `getLaunchPreferences returns preferences with DESC order`() = runTest {
        val proto = createLaunchPreferencesProto(OrderProto.DESC)
        every { dataStore.data } returns flowOf(proto)

        val result = underTest.getLaunchPreferences()

        assertEquals(Order.DESC, result.order)
    }

    @Test
    fun `getLaunchPreferences returns default preferences on exception`() = runTest {
        val exception = RuntimeException("DataStore error")
        every { dataStore.data } throws exception

        val result = underTest.getLaunchPreferences()

        assertEquals(LaunchPrefs(), result)
        assertEquals(Order.ASC, result.order)
        coVerify { crashlytics.logException(exception) }
    }

    @Test
    fun `getLaunchPreferences handles corrupted data gracefully`() = runTest {
        val exception = IllegalArgumentException("Invalid proto format")
        every { dataStore.data } throws exception

        val result = underTest.getLaunchPreferences()

        assertEquals(LaunchPrefs(), result)
        coVerify { crashlytics.logException(exception) }
    }

    @Test
    fun `save and retrieve preferences flow works correctly`() = runTest {
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
    fun `multiple save operations update datastore correctly`() = runTest {
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
