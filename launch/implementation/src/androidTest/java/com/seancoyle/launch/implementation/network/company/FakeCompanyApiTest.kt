package com.seancoyle.launch.implementation.network.company

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seancoyle.core.testing.JsonFileReader
import com.seancoyle.launch.implementation.data.network.dto.CompanyInfoDto
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
    lateinit var fakeCompanyApi: FakeCompanyApi

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun getCompanyInfoReadsCorrectDataFromJSON() = runTest {
        val jsonContent = jsonFileReader.readJSONFromAsset(COMPANY_200_RESPONSE)
        val expectedDto: CompanyInfoDto = Gson().fromJson(
            jsonContent,
            object : TypeToken<CompanyInfoDto>() {}.type
        )

        val actualDto = fakeCompanyApi.getCompanyInfo()

        assertEquals(expected = expectedDto, actual = actualDto)
    }
}