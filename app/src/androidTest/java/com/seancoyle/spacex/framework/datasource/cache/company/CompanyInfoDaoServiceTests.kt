package com.seancoyle.spacex.framework.datasource.cache.company

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.spacex.BaseTest
import com.seancoyle.spacex.business.domain.model.company.CompanyInfoFactory
import com.seancoyle.spacex.di.AppModule
import com.seancoyle.spacex.di.CompanyInfoModule
import com.seancoyle.spacex.di.ProductionModule
import com.seancoyle.spacex.framework.datasource.cache.abstraction.company.CompanyInfoDaoService
import com.seancoyle.spacex.framework.datasource.cache.dao.company.CompanyInfoDao
import com.seancoyle.spacex.framework.datasource.cache.implementation.company.CompanyInfoDaoServiceImpl
import com.seancoyle.spacex.framework.datasource.cache.mappers.company.CompanyInfoEntityMapper
import com.seancoyle.spacex.framework.datasource.data.company.CompanyInfoDataFactory
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

/*
    LEGEND:
    1. CBS = "Confirm by searching"

    Test cases:
    1. confirm company_info database table empty to start (should be test data inserted from CacheTest.kt)
    2. insert new company info, CBS

 */
@ExperimentalCoroutinesApi
@FlowPreview
@RunWith(AndroidJUnit4ClassRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@HiltAndroidTest
@UninstallModules(
    CompanyInfoModule::class,
    AppModule::class,
    ProductionModule::class
)
class CompanyInfoDaoServiceTests : BaseTest() {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    // system in test
    private lateinit var daoService: CompanyInfoDaoService

    // dependencies
    @Inject
    lateinit var dao: CompanyInfoDao

    @Inject
    lateinit var dataFactory: CompanyInfoDataFactory

    @Inject
    lateinit var companyInfoFactory: CompanyInfoFactory

    @Inject
    lateinit var entityMapper: CompanyInfoEntityMapper

    @Before
    fun init() {
        hiltRule.inject()
        insertTestData()
        daoService = CompanyInfoDaoServiceImpl(
            dao = dao,
            cacheMapper = entityMapper
        )
    }

    private fun insertTestData() = runBlocking {
        val entity = entityMapper.mapToEntity(
            dataFactory.produceCompanyInfo()
        )
        dao.insert(entity)
    }


    @Test
    fun insertCompanyInfo_CBS() = runBlocking {

        val newCompanyInfo = companyInfoFactory.createCompanyInfo(
            id = "1",
            employees = UUID.randomUUID().toString(),
            founded = UUID.randomUUID().hashCode(),
            founder = UUID.randomUUID().toString(),
            launchSites = UUID.randomUUID().hashCode(),
            name = UUID.randomUUID().toString(),
            valuation = UUID.randomUUID().toString(),
        )

        daoService.insert(newCompanyInfo)

        val companyInfoFromCache = daoService.getCompanyInfo()
        assert(companyInfoFromCache == newCompanyInfo)
    }

}














