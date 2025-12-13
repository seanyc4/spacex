package com.seancoyle.core.common.dataformatter

import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

internal class DateFormatterImpl @Inject constructor(
    private val dateFormat: DateTimeFormatter
) : DateFormatter {
    override fun formatDate(dateString: String?): LocalDateTime {
        return try {
            val normalizedDate = dateString?.replace("Z", "+0000")
            ZonedDateTime.parse(normalizedDate, dateFormat).toLocalDateTime()
        } catch (e: Exception) {
            throw Exception(e)
        }
    }
}
