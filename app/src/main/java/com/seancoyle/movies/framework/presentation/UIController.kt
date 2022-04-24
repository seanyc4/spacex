package com.seancoyle.movies.framework.presentation

import com.seancoyle.movies.business.domain.state.Response
import com.seancoyle.movies.business.domain.state.StateMessageCallback


interface UIController {

    fun displayProgressBar(isDisplayed: Boolean)

    fun hideSoftKeyboard()

    fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    )

}


















