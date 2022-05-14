package com.seancoyle.spacex.framework.datasource.cache.model.company

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "company_info")
data class CompanyInfoCacheEntity(

    @PrimaryKey(autoGenerate = false)
    val id: String,
    val employees: String,
    val founded: Int,
    val founder: String,
    val launch_sites: Int,
    val name: String,
    val valuation: String
)