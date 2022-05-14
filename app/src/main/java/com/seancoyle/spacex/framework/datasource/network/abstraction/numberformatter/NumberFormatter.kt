package com.seancoyle.spacex.framework.datasource.network.abstraction.numberformatter

interface NumberFormatter {

    fun formatNumber(number: Long?): String

}