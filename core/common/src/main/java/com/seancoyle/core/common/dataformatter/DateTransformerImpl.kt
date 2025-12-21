package com.seancoyle.core.common.dataformatter

import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

internal class DateTransformerImpl @Inject constructor(
    private val dateFormat: DateTimeFormatter
) : DateTransformer {

    override fun formatDate(dateString: String?): LocalDateTime {
        return try {
            val normalizedDate = dateString?.replace("Z", "+0000")
            ZonedDateTime.parse(normalizedDate, dateFormat).toLocalDateTime()
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

    override fun formatDateTimeToString(dateTime: LocalDateTime): String {
        return buildString {
            append(dateTime.dayOfMonth.addZeroToSingleDateValue())
            append("-")
            append(dateTime.monthValue.addZeroToSingleDateValue())
            append("-")
            append(dateTime.year.addZeroToSingleDateValue())
            append(" at ")
            append(dateTime.hour.addZeroToSingleDateValue())
            append(":")
            append(dateTime.minute.addZeroToSingleDateValue())
        }
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