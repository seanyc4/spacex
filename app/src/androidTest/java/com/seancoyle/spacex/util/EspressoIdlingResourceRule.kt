package com.seancoyle.spacex.util

import androidx.test.espresso.IdlingRegistry
import com.seancoyle.core.domain.util.printLogDebug
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class EspressoIdlingResourceRule : TestWatcher(){

    private val CLASS_NAME = "EspressoIdlingResourceRule"

    private val idlingResource = EspressoIdlingResource.countingIdlingResource

    override fun finished(description: Description?) {
        printLogDebug(CLASS_NAME, "FINISHED")
        IdlingRegistry.getInstance().unregister(idlingResource)
        super.finished(description)
    }

    override fun starting(description: Description?) {
        printLogDebug(CLASS_NAME, "STARTING")
        IdlingRegistry.getInstance().register(idlingResource)
        super.starting(description)
    }


}