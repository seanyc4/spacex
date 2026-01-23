package com.seancoyle.orbital.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.seancoyle.core.common.crashlytics.CrashLogger
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var crashLogger: CrashLogger

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        // add google-services.json before init in prod app
        //crashlytics.init(true)

        setContent {
            val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
            App(windowSizeClass)
        }
    }
}
