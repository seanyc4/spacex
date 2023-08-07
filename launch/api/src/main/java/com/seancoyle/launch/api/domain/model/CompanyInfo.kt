package com.seancoyle.launch.api.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class CompanyInfo(
    val id: String,
    val employees: String,
    val founded: Int,
    val founder: String,
    val launchSites: Int,
    val name: String,
    val valuation: String
) : Parcelable