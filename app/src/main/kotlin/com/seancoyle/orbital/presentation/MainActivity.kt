package com.seancoyle.orbital.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.seancoyle.core.common.crashlytics.Crashlytics
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
@ExperimentalMaterial3WindowSizeClassApi
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var crashlytics: Crashlytics

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        // add google-services.json before init in prod app
        //crashlytics.init(true)

        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            App(windowSizeClass)
        }
    }
}
