package com.seancoyle.spacex.framework.datasource.cache

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.database.daos.CompanyInfoDao
import com.seancoyle.launch.api.CompanyInfoFactory
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
@HiltAndroidTest
@RunWith(AndroidJUnit4ClassRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class CompanyInfoDaoServiceTests {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    // system in test
    private lateinit var daoService: com.seancoyle.launch.api.CompanyInfoCacheDataSource

    @Inject
    lateinit var dao: CompanyInfoDao

    @Inject
    lateinit var companyInfoFactory: com.seancoyle.launch.api.CompanyInfoFactory

    @Inject
    lateinit var entityMapper: com.seancoyle.launch.implementation.data.cache.CompanyInfoEntityMapper

    @Before
    fun setup() {
        hiltRule.inject()
        daoService = com.seancoyle.launch.implementation.data.cache.CompanyInfoCacheDataSourceImpl(
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














