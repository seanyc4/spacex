package com.seancoyle.core.datastore.implementation.data

import com.seancoyle.core.common.crashlytics.CrashLogger
import com.seancoyle.core.datastore.implementation.domain.DataStorePreferences
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

@OptIn(ExperimentalCoroutinesApi::class)
class DataStorePreferencesImplTest {

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @MockK(relaxed = true)
    private lateinit var crashLoggerWrapper: CrashLogger

    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var underTest: DataStorePreferences

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        underTest = DataStorePreferencesImpl(
            dataStore = fakeDataStore(testScope, tmpFolder),
            crashLoggerWrapper = crashLoggerWrapper
        )
    }

    @Test
    fun `test save and get string value`() = runTest {
        val givenKey = "test_key"
        val givenValue = "test_value"
        val defaultValue = "default_value"

        underTest.saveString(givenKey, givenValue)

        val expectedResult = underTest.getString(givenKey, defaultValue)

        assertEquals(givenValue, expectedResult)
    }

    @Test
    fun `test get string value returns specified default`() = runTest {
        val givenKey = "test_key"
        val givenDefault = "default_value"

        val expectedResult = underTest.getString(givenKey, givenDefault)

        assertEquals(givenDefault, expectedResult)
    }

    @Test
    fun `test observe string value`() = runTest {
        val givenKey = "observe_string_key"
        val givenValue = "observe_string_value"
        val givenDefault = "default_value"
        val flow = underTest.observeString(givenKey, givenDefault)

        val initialValue = flow.first()
        assertEquals(givenDefault, initialValue)

        underTest.saveString(givenKey, givenValue)

        val expectedResult = flow.first()
        assertEquals(givenValue, expectedResult)
    }

    @Test
    fun `test save and get int value`() = runTest {
        val givenKey = "int_key"
        val givenValue = 123
        val givenDefault = 0

        underTest.saveInt(givenKey, givenValue)

        val expectedResult = underTest.getInt(givenKey, givenDefault)

        assertEquals(givenValue, expectedResult)
    }

    @Test
    fun `test get int value returns specified default`() = runTest {
        val givenKey = "int_key"
        val defaultValue = 0

        val expectedResult = underTest.getInt(givenKey, defaultValue)

        assertEquals(defaultValue, expectedResult)
    }

    @Test
    fun `test observe int value`() = runTest {
        val givenKey = "observe_int_key"
        val givenValue = 123
        val givenDefault = 0
        val flow = underTest.observeInt(givenKey, givenDefault)

        val initialValue = flow.first()
        assertEquals(givenDefault, initialValue)

        underTest.saveInt(givenKey, givenValue)

        val expectedResult = flow.first()
        assertEquals(givenValue, expectedResult)
    }

    @Test
    fun `test save and get long value`() = runTest {
        val givenKey = "long_key"
        val givenValue = 123456789L
        val givenDefault = 0L

        underTest.saveLong(givenKey, givenValue)

        val expectedResult = underTest.getLong(givenKey, givenDefault)

        assertEquals(givenValue, expectedResult)
    }

    @Test
    fun `test get long value returns specified default`() = runTest {
        val givenKey = "int_key"
        val defaultValue = 0L

        val expectedResult = underTest.getLong(givenKey, defaultValue)

        assertEquals(defaultValue, expectedResult)
    }

    @Test
    fun `test observe long value`() = runTest {
        val givenKey = "observe_long_key"
        val givenValue = 123456789L
        val givenDefault = 0L
        val flow = underTest.observeLong(givenKey, givenDefault)

        val initialValue = flow.first()
        assertEquals(givenDefault, initialValue)

        underTest.saveLong(givenKey, givenValue)

        val expectedResult = flow.first()
        assertEquals(givenValue, expectedResult)
    }

    @Test
    fun `test save and get float value`() = runTest {
        val givenKey = "float_key"
        val givenValue = 1.23f
        val givenDefault = 0.0f

        underTest.saveFloat(givenKey, givenValue)

        val expectedResult = underTest.getFloat(givenKey, givenDefault)

        assertEquals(givenValue, expectedResult)
    }

    @Test
    fun `test get float value returns specified default`() = runTest {
        val givenKey = "int_key"
        val defaultValue = 0.0f

        val expectedResult = underTest.getFloat(givenKey, defaultValue)

        assertEquals(defaultValue, expectedResult)
    }

    @Test
    fun `test observe float value`() = runTest {
        val givenKey = "observe_float_key"
        val givenValue = 1.23f
        val givenDefault = 0.0f
        val flow = underTest.observeFloat(givenKey, givenDefault)

        val initialValue = flow.first()
        assertEquals(givenDefault, initialValue)

        underTest.saveFloat(givenKey, givenValue)

        val expectedResult = flow.first()
        assertEquals(givenValue, expectedResult)
    }

    @Test
    fun `test save and get boolean value`() = runTest {
        val givenKey = "boolean_key"
        val givenValue = true
        val givenDefault = false

        underTest.saveBoolean(givenKey, givenValue)

        val expectedResult = underTest.getBoolean(givenKey, givenDefault)

        assertEquals(givenValue, expectedResult)
    }

    @Test
    fun `test get boolean value returns specified default`() = runTest {
        val givenKey = "test_key"
        val defaultValue = true

        val expectedResult = underTest.getBoolean(givenKey, defaultValue)

        assertEquals(defaultValue, expectedResult)
    }

    @Test
    fun `test observe boolean value`() = runTest {
        val givenKey = "observe_boolean_key"
        val givenValue = true
        val givenDefault = false
        val flow = underTest.observeBoolean(givenKey, givenDefault)

        val initialValue = flow.first()
        assertEquals(givenDefault, initialValue)

        underTest.saveBoolean(givenKey, givenValue)

        val expectedResult = flow.first()
        assertEquals(givenValue, expectedResult)
    }

    @Test
    fun `test contains key`() = runTest {
        val givenKey = "contains_key"
        val givenValue = "contains_value"
        assertFalse(underTest.contains(givenKey))

        underTest.saveString(givenKey, givenValue)

        val expectedResult = underTest.contains(givenKey)
        assertTrue(expectedResult)
    }

    @Test
    fun `test delete key`() = runTest {
        val givenKey = "delete_key"
        val givenValue = "delete_value"
        underTest.saveString(givenKey, givenValue)
        assertTrue(underTest.contains(givenKey))

        underTest.delete(givenKey)

        val expectedResult = underTest.contains(givenKey)
        assertFalse(expectedResult)
    }

    @Test
    fun `test exception handling during get operations`() = runTest {
        val underTest = DataStorePreferencesImpl(
            dataStore = fakeDataStore(
                scope = testScope,
                tmpFolder = tmpFolder,
                throwException = true
            ),
            crashLoggerWrapper = crashLoggerWrapper
        )

        val result = underTest.getString("some_key", "default")
        assertEquals("default", result)

        verify { crashLoggerWrapper.logException(any()) }
    }

    @Test
    fun `test exception handling during save operations`() = runTest {
        val underTest = DataStorePreferencesImpl(
            dataStore = fakeDataStore(
                scope = testScope,
                tmpFolder = tmpFolder,
                throwException = true
            ),
            crashLoggerWrapper = crashLoggerWrapper
        )

        underTest.saveString("some_key", "some_value")

        verify { crashLoggerWrapper.logException(any()) }
    }

}