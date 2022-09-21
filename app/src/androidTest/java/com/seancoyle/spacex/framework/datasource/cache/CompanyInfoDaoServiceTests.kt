package com.seancoyle.spacex.framework.datasource.cache

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.database.daos.CompanyInfoDao
import com.seancoyle.launch_datasource.cache.abstraction.company.CompanyInfoCacheDataSource
import com.seancoyle.launch_datasource.cache.implementation.company.CompanyInfoCacheDataSourceImpl
import com.seancoyle.launch_models.model.company.CompanyInfoFactory
import com.seancoyle.launch_datasource.cache.mappers.company.CompanyInfoEntityMapper
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import java.util.*
import javax.inject.Inject
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
@FlowPreview
@RunWith(AndroidJUnit4ClassRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@HiltAndroidTest
class CompanyInfoDaoServiceTests {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    // system in test
    private lateinit var daoService: CompanyInfoCacheDataSource

    @Inject
    lateinit var dao: CompanyInfoDao

    @Inject
    lateinit var companyInfoFactory: CompanyInfoFactory

    @Inject
    lateinit var entityMapper: CompanyInfoEntityMapper

    @Before
    fun init() {
        hiltRule.inject()
        daoService = CompanyInfoCacheDataSourceImpl(
            dao = dao,
            entityMapper = entityMapper
        )
    }

    @Test
    fun insertCompanyInfo_getCompanyInfo_deleteCompanyInfo_confirmSuccess() = runBlocking {

        // Create CompanyInfoModel
        val newCompanyInfo = companyInfoFactory.createCompanyInfo(
            id = "1",
            employees = UUID.randomUUID().toString(),
            founded = UUID.randomUUID().hashCode(),
            founder = UUID.randomUUID().toString(),
            launchSites = UUID.randomUUID().hashCode(),
            name = UUID.randomUUID().toString(),
            valuation = UUID.randomUUID().toString(),
        )

        // Insert to fake database
        daoService.insert(newCompanyInfo)

        // Get from fake database
        val companyInfoFromCache = daoService.getCompanyInfo()

        // Confirm what was inserted matches what was retrieved
        assertEquals(companyInfoFromCache, newCompanyInfo)

        // Delete data
        daoService.deleteAll()

        // Confirm table is empty
        val emptyTable = daoService.getCompanyInfo()
        assertTrue(emptyTable == null)
    }

}














