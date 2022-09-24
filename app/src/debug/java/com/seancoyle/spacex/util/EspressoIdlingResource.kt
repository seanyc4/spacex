package com.seancoyle.spacex.util

import androidx.test.espresso.idling.CountingIdlingResource
import com.seancoyle.core.util.printLogDebug


object EspressoIdlingResource {

    private val CLASS_NAME = "EspressoIdlingResource"

    private const val RESOURCE = "GLOBAL"

    @JvmField val countingIdlingResource = CountingIdlingResource(RESOURCE)

    fun increment() {
        printLogDebug(CLASS_NAME, "INCREMENTING.")
        countingIdlingResource.increment()
    }

    fun decrement() {
        if (!countingIdlingResource.isIdleNow) {
            printLogDebug(CLASS_NAME, "DECREMENTING.")
            countingIdlingResource.decrement()
        }
    }

    fun clear() {
        if (!countingIdlingResource.isIdleNow) {
            decrement()
            clear()
        }
    }
}








