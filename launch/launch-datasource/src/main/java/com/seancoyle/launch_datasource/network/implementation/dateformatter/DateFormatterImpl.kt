package com.seancoyle.launch_datasource.network.implementation.dateformatter

import com.seancoyle.launch_datasource.network.abstraction.dateformatter.DateFormatter
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Singleton

@Singleton
class DateFormatterImpl
constructor(
    private val dateFormat: DateTimeFormatter
): DateFormatter {

    override fun formatDate(dateString: String): LocalDateTime {
        try {
            return ZonedDateTime.parse(
                dateString.replace("Z", "+0000"),
                dateFormat
            ).toLocalDateTime()
        } catch (e: Exception) {
            throw Exception(e)
        }
    }

}














