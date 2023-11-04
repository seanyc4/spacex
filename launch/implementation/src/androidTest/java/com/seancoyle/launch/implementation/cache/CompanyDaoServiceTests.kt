package com.seancoyle.launch.implementation.cache

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.core_database.api.CompanyDao
import com.seancoyle.launch.api.data.CompanyCacheDataSource
import com.seancoyle.launch.implementation.CompanyFactory
import com.seancoyle.launch.implementation.data.cache.CompanyCacheDataSourceImpl
import com.seancoyle.launch.implementation.data.cache.CompanyEntityMapper
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
class CompanyDaoServiceTests {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var dao: CompanyDao

    @Inject
    lateinit var entityMapper: CompanyEntityMapper

    @Inject
    lateinit var companyFactory: CompanyFactory

    @Inject
    lateinit var underTest: CompanyCacheDataSource

    @Before
    fun setup() {
        hiltRule.inject()
        underTest = CompanyCacheDataSourceImpl(
            dao = dao,
            entityMapper = entityMapper
        )
    }

    @Test
    fun insertCompany_getCompany_deleteCompany_confirmSuccess() = runBlocking {


        val newCompany = companyFactory.createCompany(
            id = "1",
            employees = UUID.randomUUID().toString(),
            founded = UUID.randomUUID().hashCode(),
            founder = UUID.randomUUID().toString(),
            launchSites = UUID.randomUUID().hashCode(),
            name = UUID.randomUUID().toString(),
            valuation = UUID.randomUUID().toString(),
        )

        underTest.insert(newCompany)

        val companyFromCache = underTest.getCompany()

        // Confirm what was inserted matches what was retrieved
        assertEquals(companyFromCache, newCompany)

        underTest.deleteAll()

        // Confirm table is empty
        val emptyTable = underTest.getCompany()
        assertTrue(emptyTable == null)
    }

}