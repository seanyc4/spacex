package com.seancoyle.core.domain

import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

internal class NumberFormatterImpl @Inject constructor() : NumberFormatter {

    override fun formatNumber(number: Long?): String {
        return NumberFormat.getNumberInstance(Locale.US)
            .format(number)
            .replace(",", "،")
    }
}