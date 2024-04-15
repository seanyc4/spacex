package com.seancoyle.feature.launch.implementation.data.cache

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.core.common.result.Result
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.implementation.domain.cache.CompanyCacheDataSource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
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
    lateinit var underTest: CompanyCacheDataSource

    private lateinit var givenCompany: Company

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

        assert(insertResult is Result.Success)

        val result = underTest.getCompany()

        assert(result is Result.Success)
        result as Result.Success
        assertThat(result.data, equalTo(givenCompany))
    }

    @Test
    fun getCompany_confirmRetrieved() = runTest {
        underTest.insert(givenCompany)

        val result = underTest.getCompany()
        assert(result is Result.Success)
        result as Result.Success
        assertThat(result.data, equalTo(givenCompany))
    }

    @Test
    fun deleteCompany_confirmDeleted() = runTest {
        underTest.insert(givenCompany)

        underTest.deleteAll()

        val result = underTest.getCompany()
        assert(result is Result.Success)
        result as Result.Success
        assertNull(result.data)
    }
}