package com.seancoyle.spacex.framework.datasource.network.implementation.numberformatter

import com.seancoyle.spacex.framework.datasource.network.abstraction.numberformatter.NumberFormatter
import java.text.NumberFormat
import java.util.*

class NumberFormatterImpl : NumberFormatter {

    override fun formatNumber(number: Long?): String {
        return NumberFormat.getNumberInstance(Locale.US)
            .format(number)
            .replace(",", "،")
    }

}