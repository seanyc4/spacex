package com.seancoyle.spacex.framework.presentation.common

import android.app.Activity
import android.view.View
import android.widget.Toast
import com.seancoyle.core.domain.state.StateMessageCallback

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun Activity.displayToast(
    message:String,
    stateMessageCallback: StateMessageCallback
){
    Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    stateMessageCallback.removeMessageFromStack()
}












