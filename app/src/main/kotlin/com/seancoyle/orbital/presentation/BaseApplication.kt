package com.seancoyle.orbital.presentation

import android.app.Application
import com.revenuecat.purchases.LogLevel
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration
import com.seancoyle.core.common.analytics.AnalyticsLogger
import com.seancoyle.core.common.crashlytics.CrashLogger
import com.seancoyle.orbital.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application() {

    @Inject
    lateinit var analyticsLogger: AnalyticsLogger

    @Inject
    lateinit var crashLogger: CrashLogger

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        initRevenueCat()
        initAnalyticsLogging()
        initCrashLogging()

    }

    private fun initRevenueCat() {
        if (BuildConfig.DEBUG) Purchases.logLevel = LogLevel.DEBUG
        Purchases.configure(
            PurchasesConfiguration.Builder(this, BuildConfig.REVENUECAT_API_KEY)
                .build()
        )
    }

    private fun initAnalyticsLogging() {
        analyticsLogger.init()
    }

    private fun initCrashLogging() {
        crashLogger.init(!BuildConfig.DEBUG)
    }
}
