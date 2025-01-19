package com.seancoyle.core.datastore.implementation.domain

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DataStorePreferencesComponentImplTest {

    @MockK(relaxed = true)
    private lateinit var dataStorePreferences: DataStorePreferences

    private lateinit var underTest: DataStorePreferencesComponentImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        underTest = DataStorePreferencesComponentImpl(dataStorePreferences)
    }

    @Test
    fun `getString should return value from appDataStorePreferences`() = runBlocking {
        val givenKey = "test_key"
        val expectedValue = "test_value"
        val givenDefault = "default_value"

        coEvery { dataStorePreferences.getString(givenKey, givenDefault) } returns expectedValue

        val result = underTest.getString(givenKey, givenDefault)

        assertEquals(expectedValue, result)
        coVerify { dataStorePreferences.getString(givenKey, givenDefault) }
    }

    @Test
    fun `observeString should return flow from appDataStorePreferences`() {
        val givenKey = "test_key"
        val expectedFlow: Flow<String> = flowOf("test_value")
        val givenDefault = "default_value"

        coEvery { dataStorePreferences.observeString(givenKey, givenDefault) } returns expectedFlow

        val result = underTest.observeString(givenKey, givenDefault)

        assertEquals(expectedFlow, result)
        verify { dataStorePreferences.observeString(givenKey, givenDefault) }
    }

    @Test
    fun `getInt should return value from appDataStorePreferences`() = runBlocking {
        val givenKey = "test_key"
        val expectedValue = 123
        val givenDefault = 0

        coEvery { dataStorePreferences.getInt(givenKey, givenDefault) } returns expectedValue

        val result = underTest.getInt(givenKey, givenDefault)

        assertEquals(expectedValue, result)
        coVerify { dataStorePreferences.getInt(givenKey, givenDefault) }
    }

    @Test
    fun `observeInt should return flow from appDataStorePreferences`() {
        val givenKey = "test_key"
        val expectedFlow: Flow<Int> = flowOf(123)
        val givenDefault = 0

        coEvery { dataStorePreferences.observeInt(givenKey, givenDefault) } returns expectedFlow

        val result = underTest.observeInt(givenKey, givenDefault)

        assertEquals(expectedFlow, result)
        verify { dataStorePreferences.observeInt(givenKey, givenDefault) }
    }

    @Test
    fun `getLong should return value from appDataStorePreferences`() = runBlocking {
        val givenKey = "test_key"
        val expectedValue = 123L
        val givenDefault = 0L

        coEvery { dataStorePreferences.getLong(givenKey, givenDefault) } returns expectedValue

        val result = underTest.getLong(givenKey, givenDefault)

        assertEquals(expectedValue, result)
        coVerify { dataStorePreferences.getLong(givenKey, givenDefault) }
    }

    @Test
    fun `observeLong should return flow from appDataStorePreferences`() {
        val givenKey = "test_key"
        val expectedFlow: Flow<Long> = flowOf(123L)
        val givenDefault = 0L

        coEvery { dataStorePreferences.observeLong(givenKey, givenDefault) } returns expectedFlow

        val result = underTest.observeLong(givenKey, givenDefault)

        assertEquals(expectedFlow, result)
        verify { dataStorePreferences.observeLong(givenKey, givenDefault) }
    }

    @Test
    fun `getFloat should return value from appDataStorePreferences`() = runBlocking {
        val givenKey = "test_key"
        val expectedValue = 123.45f
        val givenDefault = 0f

        coEvery { dataStorePreferences.getFloat(givenKey, givenDefault) } returns expectedValue

        val result = underTest.getFloat(givenKey, givenDefault)

        assertEquals(expectedValue, result)
        coVerify { dataStorePreferences.getFloat(givenKey, givenDefault) }
    }

    @Test
    fun `observeFloat should return flow from appDataStorePreferences`() {
        val givenKey = "test_key"
        val expectedFlow: Flow<Float> = flowOf(123.45f)
        val givenDefault = 0f

        coEvery { dataStorePreferences.observeFloat(givenKey, givenDefault) } returns expectedFlow

        val result = underTest.observeFloat(givenKey, givenDefault)

        assertEquals(expectedFlow, result)
        verify { dataStorePreferences.observeFloat(givenKey, givenDefault) }
    }

    @Test
    fun `getBoolean should return value from appDataStorePreferences`() = runBlocking {
        val givenKey = "test_key"
        val expectedValue = true
        val givenDefault = false

        coEvery { dataStorePreferences.getBoolean(givenKey, givenDefault) } returns expectedValue

        val result = underTest.getBoolean(givenKey, givenDefault)

        assertEquals(expectedValue, result)
        coVerify { dataStorePreferences.getBoolean(givenKey, givenDefault) }
    }

    @Test
    fun `observeBoolean should return flow from appDataStorePreferences`() {
        val givenKey = "test_key"
        val expectedFlow: Flow<Boolean> = flowOf(true)
        val givenDefault = false

        coEvery { dataStorePreferences.observeBoolean(givenKey, givenDefault) } returns expectedFlow

        val result = underTest.observeBoolean(givenKey, givenDefault)

        assertEquals(expectedFlow, result)
        verify { dataStorePreferences.observeBoolean(givenKey, givenDefault) }
    }

    @Test
    fun `contains should return value from appDataStorePreferences`() = runBlocking {
        val givenKey = "test_key"
        val expectedValue = true

        coEvery { dataStorePreferences.contains(givenKey) } returns expectedValue

        val result = underTest.contains(givenKey)

        assertEquals(expectedValue, result)
        coVerify { dataStorePreferences.contains(givenKey) }
    }

    @Test
    fun `delete should call delete on appDataStorePreferences`() = runBlocking {
        val givenKey = "test_key"

        coEvery { dataStorePreferences.delete(givenKey) } returns Unit

        underTest.delete(givenKey)

        coVerify { dataStorePreferences.delete(givenKey) }
    }

    @Test
    fun `saveString should call saveString on appDataStorePreferences`() = runBlocking {
        val givenKey = "test_key"
        val value = "test_value"

        coEvery { dataStorePreferences.saveString(givenKey, value) } returns Unit

        underTest.saveString(givenKey, value)

        coVerify { dataStorePreferences.saveString(givenKey, value) }
    }

    @Test
    fun `saveInt should call saveInt on appDataStorePreferences`() = runBlocking {
        val givenKey = "test_key"
        val value = 123

        coEvery { dataStorePreferences.saveInt(givenKey, value) } returns Unit

        underTest.saveInt(givenKey, value)

        coVerify { dataStorePreferences.saveInt(givenKey, value) }
    }

    @Test
    fun `saveLong should call saveLong on appDataStorePreferences`() = runBlocking {
        val givenKey = "test_key"
        val value = 123L

        coEvery { dataStorePreferences.saveLong(givenKey, value) } returns Unit

        underTest.saveLong(givenKey, value)

        coVerify { dataStorePreferences.saveLong(givenKey, value) }
    }

    @Test
    fun `saveFloat should call saveFloat on appDataStorePreferences`() = runBlocking {
        val givenKey = "test_key"
        val value = 123.45f

        coEvery { dataStorePreferences.saveFloat(givenKey, value) } returns Unit

        underTest.saveFloat(givenKey, value)

        coVerify { dataStorePreferences.saveFloat(givenKey, value) }
    }

    @Test
    fun `saveBoolean should call saveBoolean on appDataStorePreferences`() = runBlocking {
        val givenKey = "test_key"
        val value = true

        coEvery { dataStorePreferences.saveBoolean(givenKey, value) } returns Unit

        underTest.saveBoolean(givenKey, value)

        coVerify { dataStorePreferences.saveBoolean(givenKey, value) }
    }
}
