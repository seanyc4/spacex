package com.seancoyle.feature.launch.data.local

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

class LaunchesPreferencesSerializerTest {

    private lateinit var underTest: LaunchesPreferencesSerializer

    @Before
    fun setup() {
        underTest = LaunchesPreferencesSerializer()
    }

    @Test
    fun `GIVEN serializer WHEN defaultValue accessed THEN returns default LaunchPreferencesProto`() {
        val result = underTest.defaultValue

        assertNotNull(result)
        assertEquals(LaunchPreferencesProto.getDefaultInstance(), result)
    }

    @Test
    fun `GIVEN serializer WHEN defaultValue accessed THEN has default order value`() {
        val result = underTest.defaultValue

        assertNotNull(result.order)
    }

    @Test
    fun `GIVEN valid proto with ASC order WHEN readFrom THEN deserializes correctly`() = runTest {
        val proto = LaunchPreferencesProto.newBuilder()
            .setOrder(OrderProto.ASC)
            .build()
        val inputStream = ByteArrayInputStream(proto.toByteArray())

        val result = underTest.readFrom(inputStream)

        assertNotNull(result)
        assertEquals(OrderProto.ASC, result.order)
    }

    @Test
    fun `GIVEN valid proto with DESC order WHEN readFrom THEN deserializes correctly`() = runTest {
        val proto = LaunchPreferencesProto.newBuilder()
            .setOrder(OrderProto.DESC)
            .build()
        val inputStream = ByteArrayInputStream(proto.toByteArray())

        val result = underTest.readFrom(inputStream)

        assertNotNull(result)
        assertEquals(OrderProto.DESC, result.order)
    }

    @Test
    fun `GIVEN default instance WHEN readFrom THEN deserializes correctly`() = runTest {
        val proto = LaunchPreferencesProto.getDefaultInstance()
        val inputStream = ByteArrayInputStream(proto.toByteArray())

        val result = underTest.readFrom(inputStream)

        assertNotNull(result)
        assertEquals(LaunchPreferencesProto.getDefaultInstance(), result)
    }

    @Test
    fun `GIVEN invalid proto data WHEN readFrom THEN throws CorruptionException`() = runTest {
        val invalidData = "invalid proto data".toByteArray()
        val inputStream = ByteArrayInputStream(invalidData)

        assertFailsWith<CorruptionException> {
            underTest.readFrom(inputStream)
        }
    }

    @Test
    fun `GIVEN empty invalid data WHEN readFrom THEN throws CorruptionException`() = runTest {
        val invalidData = byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte())
        val inputStream = ByteArrayInputStream(invalidData)

        assertFailsWith<CorruptionException> {
            underTest.readFrom(inputStream)
        }
    }

    @Test
    fun `GIVEN empty stream WHEN readFrom THEN handles gracefully`() = runTest {
        val emptyData = byteArrayOf()
        val inputStream = ByteArrayInputStream(emptyData)

        val result = underTest.readFrom(inputStream)

        assertNotNull(result)
    }

    @Test
    fun `GIVEN proto with ASC order WHEN writeTo THEN serializes correctly`() = runTest {
        val proto = LaunchPreferencesProto.newBuilder()
            .setOrder(OrderProto.ASC)
            .build()
        val outputStream = ByteArrayOutputStream()

        underTest.writeTo(proto, outputStream)

        val bytes = outputStream.toByteArray()
        assertNotNull(bytes)
        val inputStream = ByteArrayInputStream(bytes)
        val deserialized = underTest.readFrom(inputStream)
        assertEquals(proto, deserialized)
    }

    @Test
    fun `GIVEN proto with DESC order WHEN writeTo THEN serializes correctly`() = runTest {
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
    fun `GIVEN default instance WHEN writeTo THEN serializes correctly`() = runTest {
        val proto = LaunchPreferencesProto.getDefaultInstance()
        val outputStream = ByteArrayOutputStream()

        underTest.writeTo(proto, outputStream)

        val bytes = outputStream.toByteArray()
        assertNotNull(bytes)
    }

    @Test
    fun `GIVEN ASC order proto WHEN round trip serialization THEN maintains data integrity`() = runTest {
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
    fun `GIVEN DESC order proto WHEN round trip serialization THEN maintains data integrity`() = runTest {
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
    fun `GIVEN default instance WHEN round trip serialization THEN maintains data integrity`() = runTest {
        val originalProto = LaunchPreferencesProto.getDefaultInstance()
        val outputStream = ByteArrayOutputStream()

        underTest.writeTo(originalProto, outputStream)
        val inputStream = ByteArrayInputStream(outputStream.toByteArray())
        val deserializedProto = underTest.readFrom(inputStream)

        assertEquals(originalProto, deserializedProto)
    }

    @Test
    fun `GIVEN proto WHEN multiple round trips THEN maintains consistency`() = runTest {
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
    fun `GIVEN different protos WHEN serialized consecutively THEN handles correctly`() = runTest {
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
