package com.seancoyle.core.common.dataformatter

import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

internal class DateTransformerImpl @Inject constructor() : DateTransformer {

    override fun formatDate(dateString: String?): LocalDateTime {
        return try {
            ZonedDateTime.parse(dateString, DateTimeFormatter.ISO_DATE_TIME).toLocalDateTime()
        } catch (e: Exception) {
            throw Exception(e)
        }
    }

    private fun Int.addZeroToSingleDateValue(): String {
        return if (this < 10) {
            "0$this"
        } else {
            this.toString()
        }
    }

    override fun isPastLaunch(launchDate: LocalDateTime): Boolean {
        return launchDate.isBefore(LocalDateTime.now())
    }

    override fun formatDateTimeToString(dateTime: LocalDateTime?, format: String?): String {
        if (dateTime == null) return ""
        val formatter = DateTimeFormatter.ofPattern(format, java.util.Locale.ENGLISH)
        return dateTime.format(formatter)
    }

    override fun getLaunchDaysDifference(dateTime: LocalDateTime): String {
        return buildString {
            append("+/- ")
            append(
                ChronoUnit.DAYS.between(
                    LocalDateTime.now(),
                    dateTime
                ).days.absoluteValue.toString()
            )
        }
    }

    override fun returnYearOfLaunch(launchDate: LocalDateTime): String {
        return buildString {
            append(launchDate.year.addZeroToSingleDateValue())
        }
    }
}
