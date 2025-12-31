package com.seancoyle.feature.launch.data.local

import com.seancoyle.core.datastore_proto.LaunchPreferencesProto
import com.seancoyle.core.datastore_proto.OrderProto
import com.seancoyle.core.domain.Order

import com.seancoyle.feature.launch.domain.model.LaunchPrefs
import com.seancoyle.feature.launch.domain.model.LaunchStatus
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class LaunchesPreferencesMappingExtensionsKtTest {

    @Test
    fun `toModel converts proto with ASC order to domain model`() {
        val proto = LaunchPreferencesProto.newBuilder()
            .setOrder(OrderProto.ASC)
            .build()

        val result = proto.toModel()

        assertNotNull(result)
        assertEquals(Order.ASC, result.order)
        assertEquals(LaunchStatus.ALL, result.launchStatus)
        assertEquals("", result.query)
    }

    @Test
    fun `toModel converts proto with DESC order to domain model`() {
        val proto = LaunchPreferencesProto.newBuilder()
            .setOrder(OrderProto.DESC)
            .build()

        val result = proto.toModel()

        assertNotNull(result)
        assertEquals(Order.DESC, result.order)
        assertEquals(LaunchStatus.ALL, result.launchStatus)
        assertEquals("", result.query)
    }

    @Test
    fun `toModel creates LaunchPrefs with default values for launchStatus and launchYear`() {
        val proto = LaunchPreferencesProto.newBuilder()
            .setOrder(OrderProto.ASC)
            .build()

        val result = proto.toModel()

        // Verify that the domain model has the expected default values
        assertEquals(LaunchStatus.ALL, result.launchStatus)
        assertEquals("", result.query)
    }

    @Test
    fun `toProto converts ASC order to proto`() {
        val proto = LaunchPreferencesProto.newBuilder()
            .setOrder(OrderProto.DESC) // Initial state
            .build()

        val result = proto.toProto(Order.ASC)

        assertNotNull(result)
        assertEquals(OrderProto.ASC, result.order)
    }

    @Test
    fun `toProto converts DESC order to proto`() {
        val proto = LaunchPreferencesProto.newBuilder()
            .setOrder(OrderProto.ASC) // Initial state
            .build()

        val result = proto.toProto(Order.DESC)

        assertNotNull(result)
        assertEquals(OrderProto.DESC, result.order)
    }

    @Test
    fun `toProto creates new proto instance`() {
        val originalProto = LaunchPreferencesProto.newBuilder()
            .setOrder(OrderProto.ASC)
            .build()

        val newProto = originalProto.toProto(Order.DESC)

        assertNotNull(newProto)
        assertEquals(OrderProto.DESC, newProto.order)
        // Original proto should remain unchanged (proto builders are immutable)
        assertEquals(OrderProto.ASC, originalProto.order)
    }

    @Test
    fun `round trip conversion ASC order maintains consistency`() {
        val originalOrder = Order.ASC
        val proto = LaunchPreferencesProto.newBuilder()
            .setOrder(OrderProto.ASC)
            .build()

        // Proto -> Model -> Proto
        val model = proto.toModel()
        val newProto = proto.toProto(model.order)

        assertEquals(originalOrder, model.order)
        assertEquals(OrderProto.ASC, newProto.order)
    }

    @Test
    fun `round trip conversion DESC order maintains consistency`() {
        val originalOrder = Order.DESC
        val proto = LaunchPreferencesProto.newBuilder()
            .setOrder(OrderProto.DESC)
            .build()

        // Proto -> Model -> Proto
        val model = proto.toModel()
        val newProto = proto.toProto(model.order)

        assertEquals(originalOrder, model.order)
        assertEquals(OrderProto.DESC, newProto.order)
    }

    @Test
    fun `toModel handles multiple conversions correctly`() {
        val protoAsc = LaunchPreferencesProto.newBuilder()
            .setOrder(OrderProto.ASC)
            .build()

        val protoDesc = LaunchPreferencesProto.newBuilder()
            .setOrder(OrderProto.DESC)
            .build()

        val resultAsc = protoAsc.toModel()
        val resultDesc = protoDesc.toModel()

        assertEquals(Order.ASC, resultAsc.order)
        assertEquals(Order.DESC, resultDesc.order)
    }

    @Test
    fun `toProto handles multiple conversions correctly`() {
        val proto = LaunchPreferencesProto.newBuilder()
            .setOrder(OrderProto.ASC)
            .build()

        val protoAsc = proto.toProto(Order.ASC)
        val protoDesc = proto.toProto(Order.DESC)

        assertEquals(OrderProto.ASC, protoAsc.order)
        assertEquals(OrderProto.DESC, protoDesc.order)
    }

    @Test
    fun `LaunchPrefs and proto maintain type safety`() {
        val proto = LaunchPreferencesProto.newBuilder()
            .setOrder(OrderProto.ASC)
            .build()

        val model = proto.toModel()

        assertEquals(LaunchPrefs::class, model::class)
        assertEquals(Order.ASC::class, model.order::class)
    }
}
