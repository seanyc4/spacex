package com.seancoyle.core.presentation.util

import java.time.LocalDateTime

interface DateFormatter {
    fun formatDate(dateString: String): LocalDateTime
}