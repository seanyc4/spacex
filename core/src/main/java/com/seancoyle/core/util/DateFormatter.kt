package com.seancoyle.core.util

import java.time.LocalDateTime

interface DateFormatter {

    fun formatDate(dateString: String): LocalDateTime

}