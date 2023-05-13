package com.seancoyle.core.presentation.util

import java.time.LocalDateTime

interface DateTransformer {
    fun isPastLaunch(launchDate: LocalDateTime): Boolean
    fun formatDateTimeToString(dateTime: LocalDateTime): String
    fun getLaunchDaysDifference(dateTime: LocalDateTime): String
    fun returnYearOfLaunch(launchDate: LocalDateTime): String
}