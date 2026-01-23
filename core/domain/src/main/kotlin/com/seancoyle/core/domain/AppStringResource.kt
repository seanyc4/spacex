package com.seancoyle.core.domain

interface AppStringResource {
    fun getString(
        resId: Int,
        args: Array<Any> = emptyArray()
    ): String
}