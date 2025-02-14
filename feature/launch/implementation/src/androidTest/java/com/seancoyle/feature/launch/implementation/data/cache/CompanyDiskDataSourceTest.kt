package com.seancoyle.feature.launch.implementation.data.cache

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.implementation.data.repository.company.CompanyDiskDataSource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID
import javax.inject.Inject
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@HiltAndroidTest
@RunWith(AndroidJUnit4ClassRunner::class)
internal class CompanyDiskDataSourceTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    private lateinit var givenCompany: Company

    @Inject
    lateinit var underTest: CompanyDiskDataSource

    @Before
    fun setup() {
        hiltRule.inject()
        givenCompany = Company(
            id = "1",
            employees = UUID.randomUUID().toString(),
            founded = UUID.randomUUID().hashCode(),
            founder = UUID.randomUUID().toString(),
            launchSites = UUID.randomUUID().hashCode(),
            name = UUID.randomUUID().toString(),
            valuation = UUID.randomUUID().toString(),
            summary = UUID.randomUUID().toString()
        )

        // Clear cache
        runTest { underTest.deleteAll() }
    }

    @After
    fun tearDown() = runTest {
        underTest.deleteAll()
    }

    @Test
    fun insertCompany_confirmInserted() = runTest {
        val insertResult = underTest.insert(givenCompany)

        assertTrue(insertResult is LaunchResult.Success)

        val result = underTest.get()

        assertTrue(result is LaunchResult.Success)
        assertEquals(givenCompany, result.data)
    }

    @Test
    fun getCompany_confirmRetrieved() = runTest {
        underTest.insert(givenCompany)

        val result = underTest.get()

        assertTrue(result is LaunchResult.Success)
        assertEquals(givenCompany, result.data)
    }

    @Test
    fun deleteCompany_confirmDeleted() = runTest {
        underTest.insert(givenCompany)

        underTest.deleteAll()

        val result = underTest.get()

        assertTrue(result is LaunchResult.Success)
        assertNull(result.data)
    }
}