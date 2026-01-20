package com.seancoyle.core.common.dataformatter

import com.seancoyle.core.common.crashlytics.CrashLogger
import timber.log.Timber
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

internal class DateTransformerImpl @Inject constructor(
    private val crashLogger: CrashLogger
) : DateTransformer {

    override fun formatDate(dateString: String?): LocalDateTime {
        // Return current time if input is null or blank
        if (dateString.isNullOrBlank()) {
            return LocalDateTime.now()
        }

        val formatters = listOf(
            DateTimeFormatter.ISO_DATE_TIME,           // 2026-01-20T14:30:00Z
            DateTimeFormatter.ISO_DATE,                // 2026-01-20
            DateTimeFormatter.ISO_OFFSET_DATE_TIME,    // 2026-01-20T14:30:00+01:00
            DateTimeFormatter.ISO_ZONED_DATE_TIME,     // 2026-01-20T14:30:00+01:00[Europe/Paris]
            DateTimeFormatter.ISO_INSTANT,             // 2026-01-20T14:30:00Z
            DateTimeFormatter.ISO_LOCAL_DATE_TIME,     // 2026-01-20T14:30:00
            DateTimeFormatter.RFC_1123_DATE_TIME,      // Mon, 20 Jan 2026 14:30:00 GMT
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"),
        )

        for (formatter in formatters) {
            try {
                return ZonedDateTime.parse(dateString, formatter).toLocalDateTime()
            } catch (e: Exception) {
                crashLogger.logException(e)
            }
        }

        // If all parsers fail, try as LocalDateTime directly
        try {
            return LocalDateTime.parse(dateString)
        } catch (e: Exception) {
            crashLogger.logException(e)
            Timber.tag("DateTransformerImpl").e("Failed to parse date: '$dateString'. Using current time as fallback.")
            return LocalDateTime.now()
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
