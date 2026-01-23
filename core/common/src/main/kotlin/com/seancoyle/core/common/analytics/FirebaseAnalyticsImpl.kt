package com.seancoyle.core.common.analytics

import androidx.core.os.bundleOf
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class FirebaseAnalyticsImpl @Inject constructor() : AnalyticsLogger {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun init() {
        firebaseAnalytics = Firebase.analytics
    }

    override fun logEvent(eventName: String, params: Map<String, String>?) {
        val bundle = params?.let {
            bundleOf(*it.map { (key, value) -> key to value }.toTypedArray())
        }
        firebaseAnalytics.logEvent(eventName, bundle)
    }
}