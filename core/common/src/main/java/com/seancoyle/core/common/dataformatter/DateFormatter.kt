package com.seancoyle.core.common.dataformatter

import java.time.LocalDateTime

interface DateFormatter {
    fun formatDate(dateString: String?): LocalDateTime
}
