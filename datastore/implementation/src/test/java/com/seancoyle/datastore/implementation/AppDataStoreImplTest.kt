package com.seancoyle.datastore.implementation

import kotlinx.coroutines.runBlocking
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AppDataStoreImplTest {
    private lateinit var underTest: AppDataStoreManagerFake

    @Before
    fun setUp() {
        underTest = AppDataStoreManagerFake()
    }

    @Test
    fun `test set string value`() = runBlocking {
        underTest.saveStringValue(STRING_KEY, STRING_VALUE)
        val result = underTest.readStringValue(STRING_KEY)
        assertEquals(STRING_VALUE, result)
    }

    @Test
    fun `test set int value`() = runBlocking {
        underTest.saveIntValue(INT_KEY, INT_VALUE)
        val result = underTest.readIntValue(INT_KEY)
        assertEquals(INT_VALUE, result)
    }

    @Test
    fun `test clear data`() = runBlocking {
        underTest.saveStringValue(STRING_KEY, STRING_VALUE)
        underTest.saveIntValue(INT_KEY, INT_VALUE)
        underTest.clearData()

        val stringResult = underTest.readStringValue(STRING_KEY)
        val intResult = underTest.readIntValue(INT_KEY)

        assertTrue(stringResult?.isEmpty() == true)
        assertEquals(0, intResult)
    }

    companion object{
        private const val STRING_KEY = "stringKey"
        private const val STRING_VALUE = "testValue"
        private const val INT_KEY = "intKey"
        private const val INT_VALUE = 100
    }
}