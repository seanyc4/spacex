package com.seancoyle.spacex.framework.datasource.network.abstraction.dateformatter

import java.time.LocalDateTime

interface DateFormatter {

    fun formatDate(dateString: String): LocalDateTime

}