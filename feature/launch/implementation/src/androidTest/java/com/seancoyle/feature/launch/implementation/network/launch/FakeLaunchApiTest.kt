package com.seancoyle.feature.launch.implementation.network.launch

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seancoyle.core.test.JsonFileReader
import com.seancoyle.feature.launch.implementation.TestConstants.LAUNCHES_200_RESPONSE
import com.seancoyle.feature.launch.implementation.data.network.dto.LaunchDto
import com.seancoyle.feature.launch.implementation.domain.model.LaunchOptions
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
internal class FakeLaunchApiTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var jsonFileReader: JsonFileReader

    @Inject
    lateinit var launchOptions: LaunchOptions

    @Inject
    lateinit var underTest: FakeLaunchApi

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun getLaunchesReadsCorrectDataFromJSON() = runTest {
        val jsonContent = jsonFileReader.readJSONFromAsset(LAUNCHES_200_RESPONSE)
        val expectedDto: LaunchDto = Gson().fromJson(
            jsonContent,
            object : TypeToken<LaunchDto>() {}.type
        )

        val actualDto = underTest.getLaunches(launchOptions)

        assertEquals(expected = expectedDto, actual = actualDto)
    }
}