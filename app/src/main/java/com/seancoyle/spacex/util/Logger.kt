package com.seancoyle.spacex.util

import android.util.Log
import com.seancoyle.spacex.util.Constants.DEBUG
import com.seancoyle.spacex.util.Constants.TAG

var isUnitTest = false

fun printLogD(className: String?, message: String ) {
    if (DEBUG && !isUnitTest) {
        Log.d(TAG, "$className: $message")
    }
    else if(DEBUG && isUnitTest){
        println("$className: $message")
    }
}
