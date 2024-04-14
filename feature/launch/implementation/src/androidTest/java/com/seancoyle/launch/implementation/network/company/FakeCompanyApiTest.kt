package com.seancoyle.launch.implementation.network.company

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seancoyle.core.test.JsonFileReader
import com.seancoyle.feature.launch.implementation.data.network.dto.CompanyDto
import com.seancoyle.launch.implementation.TestConstants.COMPANY_200_RESPONSE
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4ClassRunner::class)
@HiltAndroidTest
internal class FakeCompanyApiTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var jsonFileReader: JsonFileReader

    @Inject
    lateinit var underTest: FakeCompanyApi

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun getCompanyReadsCorrectDataFromJSON() = runTest {
        val jsonContent = jsonFileReader.readJSONFromAsset(COMPANY_200_RESPONSE)
        val expectedDto: CompanyDto = Gson().fromJson(
            jsonContent,
            object : TypeToken<CompanyDto>() {}.type
        )

        val actualDto = underTest.getCompany()

        assertEquals(expected = expectedDto, actual = actualDto)
    }
}