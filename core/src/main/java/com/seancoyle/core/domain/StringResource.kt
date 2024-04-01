package com.seancoyle.core.domain

interface StringResource {
    fun getString(resId: Int): String
    fun getString(id: Int, args: Array<Any> = arrayOf()): String
}