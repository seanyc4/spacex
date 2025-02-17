package com.seancoyle.feature.launch.implementation.data.cache

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.database.entities.CompanyEntity
import com.seancoyle.feature.launch.implementation.data.repository.company.CompanyLocalDataSource
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
internal class CompanyLocalDataSourceTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    private lateinit var givenCompany: CompanyEntity

    @Inject
    lateinit var underTest: CompanyLocalDataSource

    @Before
    fun setup() {
        hiltRule.inject()
        givenCompany = CompanyEntity(
            id = UUID.randomUUID().toString(),
            employees = UUID.randomUUID().hashCode(),
            founded = UUID.randomUUID().hashCode(),
            founder = UUID.randomUUID().toString(),
            launchSites = UUID.randomUUID().hashCode(),
            name = UUID.randomUUID().toString(),
            valuation = UUID.randomUUID().hashCode().toLong(),
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

        assertTrue(insertResult.isSuccess)

        val result = underTest.get()

        assertTrue(result.isSuccess)
        assertEquals(givenCompany, result.getOrNull())
    }

    @Test
    fun getCompany_confirmRetrieved() = runTest {
        underTest.insert(givenCompany)

        val result = underTest.get()

        assertTrue(result.isSuccess)
        assertEquals(givenCompany, result.getOrNull())
    }

    @Test
    fun deleteCompany_confirmDeleted() = runTest {
        underTest.insert(givenCompany)

        underTest.deleteAll()

        val result = underTest.get()

        assertTrue(result.isSuccess)
        assertNull(result.getOrNull())
    }
}