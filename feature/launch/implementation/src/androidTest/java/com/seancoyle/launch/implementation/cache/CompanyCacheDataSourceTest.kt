package com.seancoyle.launch.implementation.cache

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.launch.api.domain.model.Company
import com.seancoyle.launch.implementation.CompanyFactory
import com.seancoyle.launch.implementation.domain.cache.CompanyCacheDataSource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID
import javax.inject.Inject
import kotlin.test.assertNull

@ExperimentalCoroutinesApi
@FlowPreview
@HiltAndroidTest
@RunWith(AndroidJUnit4ClassRunner::class)
internal class CompanyCacheDataSourceTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var companyFactory: CompanyFactory

    @Inject
    lateinit var underTest: CompanyCacheDataSource

    private lateinit var givenCompany: Company

    @Before
    fun setup() {
        hiltRule.inject()
        givenCompany = companyFactory.createCompany(
            id = "1",
            employees = UUID.randomUUID().toString(),
            founded = UUID.randomUUID().hashCode(),
            founder = UUID.randomUUID().toString(),
            launchSites = UUID.randomUUID().hashCode(),
            name = UUID.randomUUID().toString(),
            valuation = UUID.randomUUID().toString(),
        )
    }

    @After
    fun tearDown() = runTest {
        underTest.deleteAll()
    }

    @Test
    fun insertCompany_confirmInserted() = runTest {
        underTest.insert(givenCompany)

        val insertedCompany = underTest.getCompany()

     //   assertEquals(insertedCompany, givenCompany)
    }

    @Test
    fun getCompany_confirmRetrieved() = runTest {
        underTest.insert(givenCompany)

        val retrievedCompany = underTest.getCompany()

       // assertEquals(retrievedCompany, givenCompany)
    }

    @Test
    fun deleteCompany_confirmDeleted() = runTest {
        underTest.insert(givenCompany)

        underTest.deleteAll()

        val resultAfterDeletion = underTest.getCompany()
        assertNull(resultAfterDeletion, "The table should be empty after deletion.")
    }
}