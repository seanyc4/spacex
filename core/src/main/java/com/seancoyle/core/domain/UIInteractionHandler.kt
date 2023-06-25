package com.seancoyle.core.domain

interface UIInteractionHandler {
    fun displayProgressBar(isDisplayed: Boolean)
    fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    )
}