package com.seancoyle.core.data

import com.seancoyle.core.domain.Crashlytics
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CrashlyticsImpl @Inject constructor() : Crashlytics {

    /**
     * Enable this implementation for a production app with a valid google-services.json file
     */
    override fun init(isEnabled: Boolean) {
       // FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(isEnabled)
    }

    override fun setCustomKey(key: String, value: String) {
       // FirebaseCrashlytics.getInstance().setCustomKey(key, value)
    }

    override fun logException(throwable: Throwable) {
       // FirebaseCrashlytics.getInstance().recordException(throwable)
    }

    // Doesn't create a crash report, will be transmitted with the next crash report if any crash occurs
    override fun log(msg: String) {
       // FirebaseCrashlytics.getInstance().log(msg)
    }
}