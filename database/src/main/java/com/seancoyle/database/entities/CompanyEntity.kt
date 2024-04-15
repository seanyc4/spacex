package com.seancoyle.database.entities

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "company_info")
data class CompanyEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name="id")
    val id: String,

    @ColumnInfo(name="employees")
    val employees: String,

    @ColumnInfo(name="founded")
    val founded: Int,

    @ColumnInfo(name="founder")
    val founder: String,

    @ColumnInfo(name="launch_sites")
    val launchSites: Int,

    @ColumnInfo(name="name")
    val name: String,

    @ColumnInfo(name="valuation")
    val valuation: String
)