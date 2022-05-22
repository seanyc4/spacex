package com.seancoyle.spacex.framework.datasource.cache.company

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.spacex.business.domain.model.company.CompanyInfoFactory
import com.seancoyle.spacex.di.ProductionModule
import com.seancoyle.spacex.framework.datasource.cache.abstraction.company.CompanyInfoDaoService
import com.seancoyle.spacex.framework.datasource.cache.dao.company.CompanyInfoDao
import com.seancoyle.spacex.framework.datasource.cache.implementation.company.CompanyInfoDaoServiceImpl
import com.seancoyle.spacex.framework.datasource.cache.mappers.company.CompanyInfoEntityMapper
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
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
@UninstallModules(
    ProductionModule::class
)
class CompanyInfoDaoServiceTests {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    // system in test
    private lateinit var daoService: CompanyInfoDaoService

    @Inject
    lateinit var dao: CompanyInfoDao

    @Inject
    lateinit var companyInfoFactory: CompanyInfoFactory

    @Inject
    lateinit var entityMapper: CompanyInfoEntityMapper

    @Before
    fun init() {
        hiltRule.inject()
        daoService = CompanyInfoDaoServiceImpl(
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














