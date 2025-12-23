package com.seancoyle.feature.launch.implementation.data.local

import androidx.datastore.core.CorruptionException
import com.seancoyle.core.datastore_proto.LaunchPreferencesProto
import com.seancoyle.core.datastore_proto.OrderProto
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class LaunchPreferencesSerializerTest {

    private lateinit var underTest: LaunchPreferencesSerializer

    @Before
    fun setup() {
        underTest = LaunchPreferencesSerializer()
    }

    @Test
    fun `defaultValue returns default LaunchPreferencesProto instance`() {
        val result = underTest.defaultValue

        assertNotNull(result)
        assertEquals(LaunchPreferencesProto.getDefaultInstance(), result)
    }

    @Test
    fun `defaultValue has default order value`() {
        val result = underTest.defaultValue

        // Default proto should have UNRECOGNIZED or first enum value
        assertNotNull(result.order)
    }

    @Test
    fun `readFrom deserializes valid proto with ASC order`() = runTest {
        val proto = LaunchPreferencesProto.newBuilder()
            .setOrder(OrderProto.ASC)
            .build()
        val inputStream = ByteArrayInputStream(proto.toByteArray())

        val result = underTest.readFrom(inputStream)

        assertNotNull(result)
        assertEquals(OrderProto.ASC, result.order)
    }

    @Test
    fun `readFrom deserializes valid proto with DESC order`() = runTest {
        val proto = LaunchPreferencesProto.newBuilder()
            .setOrder(OrderProto.DESC)
            .build()
        val inputStream = ByteArrayInputStream(proto.toByteArray())

        val result = underTest.readFrom(inputStream)

        assertNotNull(result)
        assertEquals(OrderProto.DESC, result.order)
    }

    @Test
    fun `readFrom deserializes default instance`() = runTest {
        val proto = LaunchPreferencesProto.getDefaultInstance()
        val inputStream = ByteArrayInputStream(proto.toByteArray())

        val result = underTest.readFrom(inputStream)

        assertNotNull(result)
        assertEquals(LaunchPreferencesProto.getDefaultInstance(), result)
    }

    @Test
    fun `readFrom throws CorruptionException on invalid proto data`() = runTest {
        val invalidData = "invalid proto data".toByteArray()
        val inputStream = ByteArrayInputStream(invalidData)

        assertFailsWith<CorruptionException> {
            underTest.readFrom(inputStream)
        }
    }

    @Test
    fun `readFrom throws CorruptionException on empty invalid data`() = runTest {
        val invalidData = byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte())
        val inputStream = ByteArrayInputStream(invalidData)

        assertFailsWith<CorruptionException> {
            underTest.readFrom(inputStream)
        }
    }

    @Test
    fun `readFrom handles empty stream`() = runTest {
        val emptyData = byteArrayOf()
        val inputStream = ByteArrayInputStream(emptyData)

        val result = underTest.readFrom(inputStream)

        // Empty byte array should deserialize to default proto
        assertNotNull(result)
    }

    @Test
    fun `writeTo serializes proto with ASC order`() = runTest {
        val proto = LaunchPreferencesProto.newBuilder()
            .setOrder(OrderProto.ASC)
            .build()
        val outputStream = ByteArrayOutputStream()

        underTest.writeTo(proto, outputStream)

        val bytes = outputStream.toByteArray()
        assertNotNull(bytes)
        // Proto3 omits default values, so the byte array may be empty for ASC (default)
        // Instead, check round-trip integrity
        val inputStream = ByteArrayInputStream(bytes)
        val deserialized = underTest.readFrom(inputStream)
        assertEquals(proto, deserialized)
    }

    @Test
    fun `writeTo serializes proto with DESC order`() = runTest {
        val proto = LaunchPreferencesProto.newBuilder()
            .setOrder(OrderProto.DESC)
            .build()
        val outputStream = ByteArrayOutputStream()

        underTest.writeTo(proto, outputStream)

        val bytes = outputStream.toByteArray()
        assertNotNull(bytes)
        assert(bytes.isNotEmpty())
    }

    @Test
    fun `writeTo serializes default instance`() = runTest {
        val proto = LaunchPreferencesProto.getDefaultInstance()
        val outputStream = ByteArrayOutputStream()

        underTest.writeTo(proto, outputStream)

        val bytes = outputStream.toByteArray()
        assertNotNull(bytes)
    }

    @Test
    fun `round trip serialization with ASC order maintains data integrity`() = runTest {
        val originalProto = LaunchPreferencesProto.newBuilder()
            .setOrder(OrderProto.ASC)
            .build()
        val outputStream = ByteArrayOutputStream()

        underTest.writeTo(originalProto, outputStream)

        val inputStream = ByteArrayInputStream(outputStream.toByteArray())
        val deserializedProto = underTest.readFrom(inputStream)

        assertEquals(OrderProto.ASC, deserializedProto.order)
        assertEquals(originalProto.order, deserializedProto.order)
    }

    @Test
    fun `round trip serialization with DESC order maintains data integrity`() = runTest {
        val originalProto = LaunchPreferencesProto.newBuilder()
            .setOrder(OrderProto.DESC)
            .build()
        val outputStream = ByteArrayOutputStream()

        underTest.writeTo(originalProto, outputStream)

        val inputStream = ByteArrayInputStream(outputStream.toByteArray())
        val deserializedProto = underTest.readFrom(inputStream)

        assertEquals(OrderProto.DESC, deserializedProto.order)
        assertEquals(originalProto.order, deserializedProto.order)
    }

    @Test
    fun `round trip with default instance maintains data integrity`() = runTest {
        val originalProto = LaunchPreferencesProto.getDefaultInstance()
        val outputStream = ByteArrayOutputStream()

        underTest.writeTo(originalProto, outputStream)

        val inputStream = ByteArrayInputStream(outputStream.toByteArray())
        val deserializedProto = underTest.readFrom(inputStream)

        assertEquals(originalProto, deserializedProto)
    }

    @Test
    fun `multiple round trips maintain consistency`() = runTest {
        val originalProto = LaunchPreferencesProto.newBuilder()
            .setOrder(OrderProto.ASC)
            .build()

        val outputStream1 = ByteArrayOutputStream()
        underTest.writeTo(originalProto, outputStream1)
        val inputStream1 = ByteArrayInputStream(outputStream1.toByteArray())
        val firstResult = underTest.readFrom(inputStream1)

        val outputStream2 = ByteArrayOutputStream()
        underTest.writeTo(firstResult, outputStream2)
        val inputStream2 = ByteArrayInputStream(outputStream2.toByteArray())
        val secondResult = underTest.readFrom(inputStream2)

        assertEquals(OrderProto.ASC, secondResult.order)
        assertEquals(originalProto.order, secondResult.order)
    }

    @Test
    fun `serializer handles multiple different values consecutively`() = runTest {
        val protoAsc = LaunchPreferencesProto.newBuilder()
            .setOrder(OrderProto.ASC)
            .build()
        val protoDesc = LaunchPreferencesProto.newBuilder()
            .setOrder(OrderProto.DESC)
            .build()

        val outputStream1 = ByteArrayOutputStream()
        underTest.writeTo(protoAsc, outputStream1)
        val inputStream1 = ByteArrayInputStream(outputStream1.toByteArray())
        val result1 = underTest.readFrom(inputStream1)

        val outputStream2 = ByteArrayOutputStream()
        underTest.writeTo(protoDesc, outputStream2)
        val inputStream2 = ByteArrayInputStream(outputStream2.toByteArray())
        val result2 = underTest.readFrom(inputStream2)

        assertEquals(OrderProto.ASC, result1.order)
        assertEquals(OrderProto.DESC, result2.order)
    }
}
