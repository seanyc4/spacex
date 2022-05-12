package com.seancoyle.spacex.framework.presentation

import com.seancoyle.spacex.business.domain.state.Response
import com.seancoyle.spacex.business.domain.state.StateMessageCallback


interface UIController {

    fun displayProgressBar(isDisplayed: Boolean)

    fun hideSoftKeyboard()

    fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    )

}


















