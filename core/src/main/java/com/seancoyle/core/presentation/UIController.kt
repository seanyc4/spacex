package com.seancoyle.core.presentation

import com.seancoyle.core.state.Response
import com.seancoyle.core.state.StateMessageCallback


interface UIController {

    fun displayProgressBar(isDisplayed: Boolean)

    fun hideSoftKeyboard()

    fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    )

}


















