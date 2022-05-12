package com.seancoyle.spacex.framework.datasource.cache.abstraction.datetransformer

import java.time.LocalDateTime

interface DateTransformer {

    fun isPastLaunch(launchDate: LocalDateTime): Boolean

    fun formatDateTimeToString(dateTime: LocalDateTime): String

    fun getLaunchDaysDifference(dateTime: LocalDateTime): String

}