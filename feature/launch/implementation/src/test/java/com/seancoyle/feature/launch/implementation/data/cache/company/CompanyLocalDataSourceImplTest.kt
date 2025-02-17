package com.seancoyle.feature.launch.implementation.data.cache.company

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.test.TestCoroutineRule
import com.seancoyle.database.dao.CompanyDao
import com.seancoyle.feature.launch.implementation.data.repository.company.CompanyLocalDataSource
import com.seancoyle.feature.launch.implementation.util.TestData.companyEntity
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CompanyLocalDataSourceImplTest {

    @get: Rule
    val testCoroutineRule = TestCoroutineRule()

    private var testDispatcher: TestDispatcher = testCoroutineRule.testCoroutineDispatcher

    @MockK
    private lateinit var dao: CompanyDao

    @RelaxedMockK
    private lateinit var crashlytics: Crashlytics

    private lateinit var underTest: CompanyLocalDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = CompanyLocalDataSourceImpl(
            dao = dao,
            crashlytics = crashlytics,
            ioDispatcher = testDispatcher
        )
    }

    @Test
    fun `get returns mapped company when DAO provides an entity`() = runTest {
        coEvery { dao.getCompany() } returns companyEntity

        val result = underTest.get()

        coVerify { dao.getCompany() }

        assertTrue(result.isSuccess)
        assertEquals(companyEntity, result.getOrNull())
    }

    @Test
    fun `get returns null when DAO provides no entity`() = runTest {
        coEvery { dao.getCompany() } returns null

        val result = underTest.get()

        coVerify { dao.getCompany() }

        assertTrue(result.isSuccess && result.getOrNull() == null)
    }

    @Test
    fun `insert company calls DAO and returns a success result`() = runTest {
        val entityId = 1L
        coEvery { dao.insert(companyEntity) } returns entityId

        val result = underTest.insert(companyEntity)

        coVerify { dao.insert(companyEntity) }

        assertTrue(result.isSuccess && result.getOrNull() == entityId)
    }

    @Test
    fun `deleteAll invokes DAO and returns success`() = runTest {
        coEvery { dao.deleteAll() } returns Unit

        val result = underTest.deleteAll()

        coVerify { dao.deleteAll() }

        assertTrue(result.isSuccess)
    }
}
