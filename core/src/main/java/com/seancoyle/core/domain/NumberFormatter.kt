package com.seancoyle.core.domain

interface NumberFormatter {
    fun formatNumber(number: Long?): String
}