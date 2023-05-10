package com.seancoyle.core.presentation.util

import com.seancoyle.core.domain.Response
import com.seancoyle.core.domain.StateMessageCallback

interface UIInteractionHandler {
    fun displayProgressBar(isDisplayed: Boolean)
    fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    )
}