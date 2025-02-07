package com.seancoyle.feature.launch.implementation.data.cache

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.result.Result
import com.seancoyle.core.test.TestCoroutineRule
import com.seancoyle.database.dao.CompanyDao
import com.seancoyle.feature.launch.implementation.util.TestData.companyEntity
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CompanyCacheDataSourceImplTest {

    @get: Rule
    val testCoroutineRule = TestCoroutineRule()

    private var testDispatcher: TestDispatcher = testCoroutineRule.testCoroutineDispatcher

    @MockK
    private lateinit var dao: CompanyDao

    @MockK(relaxed = true)
    private lateinit var crashlytics: Crashlytics

    private lateinit var underTest: CompanyCacheDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = CompanyCacheDataSourceImpl(
            dao = dao,
            crashlytics = crashlytics,
            ioDispatcher = testDispatcher
        )
    }

    @Test
    fun `getCompany returns mapped company when DAO provides an entity`() = runTest {
        coEvery { dao.getCompanyInfo() } returns companyEntity

        val result = underTest.getCompany()

        assertTrue(result is Result.Success)
        assertEquals(companyEntity, result.data)
        coVerify { dao.getCompanyInfo() }
    }

    @Test
    fun `getCompany returns null when DAO provides no entity`() = runTest {
        coEvery { dao.getCompanyInfo() } returns null

        val result = underTest.getCompany()

        assertTrue(result is Result.Success && result.data == null)
    }

    @Test
    fun `insert company calls DAO and returns a success result`() = runTest {
        val entityId = 1L
        coEvery { dao.insert(companyEntity) } returns entityId

        val result = underTest.insert(companyEntity)

        assertTrue(result is Result.Success && result.data == entityId)
        coVerify { dao.insert(companyEntity) }
    }

    @Test
    fun `deleteAll invokes DAO and returns success`() = runTest {
        coEvery { dao.deleteAll() } returns Unit

        val result = underTest.deleteAll()

        assertTrue(result is Result.Success)
        coVerify { dao.deleteAll() }
    }
}
