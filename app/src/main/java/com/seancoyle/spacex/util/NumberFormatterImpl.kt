package com.seancoyle.spacex.util

import com.seancoyle.core.util.NumberFormatter
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

class NumberFormatterImpl
@Inject
constructor() : NumberFormatter {

    override fun formatNumber(number: Long?): String {
        return NumberFormat.getNumberInstance(Locale.US)
            .format(number)
            .replace(",", "،")
    }

}