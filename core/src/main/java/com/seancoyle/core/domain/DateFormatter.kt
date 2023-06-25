package com.seancoyle.core.domain

import java.time.LocalDateTime

interface DateFormatter {
    fun formatDate(dateString: String): LocalDateTime
}