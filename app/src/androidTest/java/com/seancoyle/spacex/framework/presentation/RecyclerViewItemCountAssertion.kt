package com.seancoyle.spacex.framework.presentation

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import kotlin.test.assertTrue

class RecyclerViewItemCountAssertion(
    private val expectedCount: Int
) : ViewAssertion {

    override fun check(
        view: View,
        noViewFoundException: NoMatchingViewException?
    ) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }
        val recyclerView = view as RecyclerView
        val adapter = recyclerView.adapter
        // minus for the 3 header items which are added to the top of the adapter list
        assertTrue { adapter?.itemCount?.minus(3) == expectedCount }
    }

}