package com.seancoyle.core.common.dataformatter

import com.seancoyle.core.common.dataformatter.DateFormatConstants.DD_MMMM_YYYY
import java.time.LocalDateTime

interface DateTransformer {
    fun formatDate(dateString: String?): LocalDateTime
    fun isPastLaunch(launchDate: LocalDateTime): Boolean
    fun formatDateTimeToString(dateTime: LocalDateTime?, format: String? = DD_MMMM_YYYY): String
    fun getLaunchDaysDifference(dateTime: LocalDateTime): String
    fun returnYearOfLaunch(launchDate: LocalDateTime): String
}