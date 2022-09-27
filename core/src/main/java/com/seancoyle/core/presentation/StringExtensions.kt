package com.seancoyle.core.presentation

class StringExtensions {

    inline fun <T> String?.letIfNotNullNorEmpty(block: (String) -> T): T? = if (!this.isNullOrEmpty()) block(this) else null
}