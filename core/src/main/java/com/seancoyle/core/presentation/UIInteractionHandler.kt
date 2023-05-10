package com.seancoyle.core.presentation

import com.seancoyle.core.state.Response
import com.seancoyle.core.state.StateMessageCallback


interface UIInteractionHandler {

    fun displayProgressBar(isDisplayed: Boolean)

    fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    )

}


















