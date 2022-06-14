package com.seancoyle.launch_datasource.cache.entities.company

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "company_info")
data class CompanyInfoEntity(

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
    val launch_sites: Int,

    @ColumnInfo(name="name")
    val name: String,

    @ColumnInfo(name="valuation")
    val valuation: String
)