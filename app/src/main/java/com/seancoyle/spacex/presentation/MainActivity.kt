package com.seancoyle.spacex.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.spacex.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var crashlytics: Crashlytics

    private lateinit var binding :ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        // add google-services.json before init in prod app
        //crashlytics.init(true)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}