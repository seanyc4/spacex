package com.seancoyle.spacex.framework.presentation

import com.seancoyle.core.domain.state.Response
import com.seancoyle.core.domain.state.StateMessageCallback


interface UIController {

    fun displayProgressBar(isDisplayed: Boolean)

    fun hideSoftKeyboard()

    fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    )

}


















