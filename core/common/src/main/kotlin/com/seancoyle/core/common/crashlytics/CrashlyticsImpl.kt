package com.seancoyle.core.common.crashlytics

import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CrashlyticsImpl @Inject constructor() : CrashLogger {

    override fun init(isEnabled: Boolean) {
        try {
            FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = isEnabled
        } catch (e: Throwable) {
            Timber.tag(TAG).e(e)
        }
    }

    override fun setCustomKey(key: String, value: String) {
        try {
            FirebaseCrashlytics.getInstance().setCustomKey(key, value)
        } catch (e: Throwable) {
            Timber.tag(TAG).e(e)
        }
    }

    override fun logException(throwable: Throwable) {
        try {
            FirebaseCrashlytics.getInstance().recordException(throwable)
        } catch (e: Throwable) {
            Timber.tag(TAG).e(e)
        }
    }

    // Doesn't create a crash report, will be transmitted with the next crash report if any crash occurs
    override fun log(msg: String) {
        try {
            FirebaseCrashlytics.getInstance().log(msg)
        } catch (e: Throwable) {
            Timber.tag(TAG).e(e)
        }
    }

    private companion object {
        private const val TAG = "CrashlyticsImpl"
    }
}
