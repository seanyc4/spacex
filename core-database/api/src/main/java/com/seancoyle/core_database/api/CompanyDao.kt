package com.seancoyle.core_database.api

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CompanyDao {

    @Query("SELECT * FROM company_info")
    suspend fun getCompanyInfo(): CompanyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(company: CompanyEntity): Long

    @Query("DELETE FROM company_info")
    suspend fun deleteAll()

}