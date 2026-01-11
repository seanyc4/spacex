package com.seancoyle.core.ui.util

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

fun Context.openUrl(url: String?) {
    if (!url.isNullOrBlank()) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        this.startActivity(intent)
    }
}
