package com.seancoyle.feature.launch.implementation.data.cache.company

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.result.DataError.LocalError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.database.dao.CompanyDao
import com.seancoyle.feature.launch.implementation.data.local.company.CompanyLocalDataSourceImpl
import com.seancoyle.feature.launch.implementation.data.mapper.CompanyMapper
import com.seancoyle.feature.launch.implementation.data.mapper.LocalErrorMapper
import com.seancoyle.feature.launch.implementation.data.repository.company.CompanyLocalDataSource
import com.seancoyle.feature.launch.implementation.util.TestData.companyEntity
import com.seancoyle.feature.launch.implementation.util.TestData.companyModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

class CompanyLocalDataSourceImplTest {

    @MockK
    private lateinit var dao: CompanyDao

    @MockK
    private lateinit var companyMapper: CompanyMapper

    @RelaxedMockK
    private lateinit var localErrorMapper: LocalErrorMapper

    @RelaxedMockK
    private lateinit var crashlytics: Crashlytics

    private lateinit var underTest: CompanyLocalDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = CompanyLocalDataSourceImpl(
            dao = dao,
            crashlytics = crashlytics,
            errorMapper = localErrorMapper,
            mapper = companyMapper
        )
    }

    @Test
    fun `get returns mapped company when DAO provides an entity`() = runTest {
        coEvery { dao.getCompany() } returns companyEntity
        every { companyMapper.entityToDomain(companyEntity) } returns companyModel

        val result = underTest.get()

        coVerify { dao.getCompany() }
        verify { companyMapper.entityToDomain(companyEntity) }

        assertTrue(result is LaunchResult.Success)
        assertEquals(companyModel, result.data)
    }

    @Test
    fun `insert company calls DAO and returns a success result`() = runTest {
        val entityId = 1L
        coEvery { dao.insert(companyEntity) } returns entityId
        every { companyMapper.domainToEntity(companyModel) } returns companyEntity

        val result = underTest.insert(companyModel)

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
