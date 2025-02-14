package com.seancoyle.feature.launch.implementation.data.cache.company

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.result.LaunchResult
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

    @RelaxedMockK
    private lateinit var localDataSourceErrorMapper: LocalDataSourceErrorMapper

    private lateinit var underTest: CompanyLocalDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = CompanyLocalDataSourceImpl(
            dao = dao,
            localDataSourceErrorMapper = localDataSourceErrorMapper,
            crashlytics = crashlytics,
            ioDispatcher = testDispatcher
        )
    }

    @Test
    fun `get returns mapped company when DAO provides an entity`() = runTest {
        coEvery { dao.getCompany() } returns companyEntity

        val result = underTest.get()

        coVerify { dao.getCompany() }

        assertTrue(result is LaunchResult.Success)
        assertEquals(companyEntity, result.data)
    }

    @Test
    fun `get returns null when DAO provides no entity`() = runTest {
        coEvery { dao.getCompany() } returns null

        val result = underTest.get()

        coVerify { dao.getCompany() }

        assertTrue(result is LaunchResult.Success && result.data == null)
    }

    @Test
    fun `insert company calls DAO and returns a success result`() = runTest {
        val entityId = 1L
        coEvery { dao.insert(companyEntity) } returns entityId

        val result = underTest.insert(companyEntity)

        coVerify { dao.insert(companyEntity) }

        assertTrue(result is LaunchResult.Success && result.data == entityId)
    }

    @Test
    fun `deleteAll invokes DAO and returns success`() = runTest {
        coEvery { dao.deleteAll() } returns Unit

        val result = underTest.deleteAll()

        coVerify { dao.deleteAll() }

        assertTrue(result is LaunchResult.Success)
    }
}
