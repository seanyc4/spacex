package com.seancoyle.feature.launch.data.local

import com.seancoyle.core.datastore_proto.LaunchPreferencesProto
import com.seancoyle.core.datastore_proto.OrderProto
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.domain.model.LaunchPrefs
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class LaunchesPreferencesMappingExtensionsKtTest {

    @Test
    fun `GIVEN proto with ASC order WHEN toModel THEN returns domain model with ASC order`() {
        val proto = LaunchPreferencesProto.newBuilder()
            .setOrder(OrderProto.ASC)
            .build()

        val result = proto.toModel()

        assertNotNull(result)
        assertEquals(Order.ASC, result.order)
    }

    @Test
    fun `GIVEN proto with DESC order WHEN toModel THEN returns domain model with DESC order`() {
        val proto = LaunchPreferencesProto.newBuilder()
            .setOrder(OrderProto.DESC)
            .build()

        val result = proto.toModel()

        assertNotNull(result)
        assertEquals(Order.DESC, result.order)
    }

    @Test
    fun `GIVEN proto with DESC order WHEN toProto with ASC order THEN returns proto with ASC order`() {
        val proto = LaunchPreferencesProto.newBuilder()
            .setOrder(OrderProto.DESC)
            .build()

        val result = proto.toProto(Order.ASC)

        assertNotNull(result)
        assertEquals(OrderProto.ASC, result.order)
    }

    @Test
    fun `GIVEN proto with ASC order WHEN toProto with DESC order THEN returns proto with DESC order`() {
        val proto = LaunchPreferencesProto.newBuilder()
            .setOrder(OrderProto.ASC)
            .build()

        val result = proto.toProto(Order.DESC)

        assertNotNull(result)
        assertEquals(OrderProto.DESC, result.order)
    }

    @Test
    fun `GIVEN proto with ASC order WHEN toProto with DESC order THEN creates new proto instance`() {
        val originalProto = LaunchPreferencesProto.newBuilder()
            .setOrder(OrderProto.ASC)
            .build()

        val newProto = originalProto.toProto(Order.DESC)

        assertNotNull(newProto)
        assertEquals(OrderProto.DESC, newProto.order)

        assertEquals(OrderProto.ASC, originalProto.order)
    }

    @Test
    fun `GIVEN proto with ASC order WHEN round trip toModel and toProto THEN maintains ASC order consistency`() {
        val originalOrder = Order.ASC
        val proto = LaunchPreferencesProto.newBuilder()
            .setOrder(OrderProto.ASC)
            .build()


        val model = proto.toModel()
        val newProto = proto.toProto(model.order)

        assertEquals(originalOrder, model.order)
        assertEquals(OrderProto.ASC, newProto.order)
    }

    @Test
    fun `GIVEN proto with DESC order WHEN round trip toModel and toProto THEN maintains DESC order consistency`() {
        val originalOrder = Order.DESC
        val proto = LaunchPreferencesProto.newBuilder()
            .setOrder(OrderProto.DESC)
            .build()


        val model = proto.toModel()
        val newProto = proto.toProto(model.order)

        assertEquals(originalOrder, model.order)
        assertEquals(OrderProto.DESC, newProto.order)
    }

    @Test
    fun `GIVEN multiple protos WHEN toModel called THEN returns correct orders`() {
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
    fun `GIVEN proto WHEN toProto called with different orders THEN returns correct proto orders`() {
        val proto = LaunchPreferencesProto.newBuilder()
            .setOrder(OrderProto.ASC)
            .build()

        val protoAsc = proto.toProto(Order.ASC)
        val protoDesc = proto.toProto(Order.DESC)

        assertEquals(OrderProto.ASC, protoAsc.order)
        assertEquals(OrderProto.DESC, protoDesc.order)
    }

    @Test
    fun `GIVEN proto with ASC order WHEN toModel called THEN returns LaunchPrefs with correct type`() {
        val proto = LaunchPreferencesProto.newBuilder()
            .setOrder(OrderProto.ASC)
            .build()

        val model = proto.toModel()

        assertEquals(LaunchPrefs::class, model::class)
        assertEquals(Order.ASC::class, model.order::class)
    }
}
