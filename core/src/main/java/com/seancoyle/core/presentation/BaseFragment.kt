package com.seancoyle.core.presentation

import android.content.Context
import androidx.fragment.app.Fragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
abstract class BaseFragment : Fragment() {

    lateinit var uiController: UIController

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setUIController(null, context) // null in production
    }

    private fun setUIController(mockController: UIController?, context: Context) {

        // TEST: Set interface from mock
        if (mockController != null) {
            this.uiController = mockController
        } else { // PRODUCTION: if no mock, get from context
            activity?.let {
                try {
                    uiController = context as UIController
                } catch (e: ClassCastException) {
                    e.printStackTrace()
                }
            }
        }
    }
}

















