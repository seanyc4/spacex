package com.seancoyle.core.util

import java.text.NumberFormat
import java.util.*
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