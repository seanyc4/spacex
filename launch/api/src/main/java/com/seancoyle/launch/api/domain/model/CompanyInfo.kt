package com.seancoyle.launch.api.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CompanyInfo(
    val id: String,
    val employees: String,
    val founded: Int,
    val founder: String,
    val launchSites: Int,
    val name: String,
    val valuation: String
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CompanyInfo

        if (id != other.id) return false
        if (employees != other.employees) return false
        if (founded != other.founded) return false
        if (founder != other.founder) return false
        if (launchSites != other.launchSites) return false
        if (name != other.name) return false
        if (valuation != other.valuation) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + employees.hashCode()
        result = 31 * result + founded
        result = 31 * result + founder.hashCode()
        result = 31 * result + launchSites
        result = 31 * result + name.hashCode()
        result = 31 * result + valuation.hashCode()
        return result
    }
}