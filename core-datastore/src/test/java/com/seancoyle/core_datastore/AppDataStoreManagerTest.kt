package com.seancoyle.core_datastore

import com.seancoyle.core_datastore_test.AppDataStoreManagerFake
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AppDataStoreManagerTest {
    private lateinit var underTest: AppDataStoreManagerFake

    @BeforeEach
    fun setUp() {
        underTest = AppDataStoreManagerFake()
    }

    @Test
    fun testSetStringValue() = runBlocking {
        underTest.setStringValue(STRING_KEY, STRING_VALUE)
        val result = underTest.readStringValue(STRING_KEY)
        assertEquals(STRING_VALUE, result)
    }

    @Test
    fun testSetIntValue() = runBlocking {
        underTest.setIntValue(INT_KEY, INT_VALUE)
        val result = underTest.readIntValue(INT_KEY)
        assertEquals(INT_VALUE, result)
    }

    @Test
    fun testClearData() = runBlocking {
        underTest.setStringValue(STRING_KEY, STRING_VALUE)
        underTest.setIntValue(INT_KEY, INT_VALUE)
        underTest.clearData()

        val stringResult = underTest.readStringValue(STRING_KEY)
        val intResult = underTest.readIntValue(INT_KEY)

        assertTrue(stringResult.isEmpty())
        assertEquals(0, intResult)
    }

    companion object{
        private const val STRING_KEY = "stringKey"
        private const val STRING_VALUE = "testValue"
        private const val INT_KEY = "intKey"
        private const val INT_VALUE = 100
    }
}