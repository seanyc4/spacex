package com.seancoyle.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.seancoyle.database.entities.CompanyEntity

@Dao
interface CompanyDao {

    @Query("SELECT * FROM company_info")
    suspend fun getCompany(): CompanyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(company: CompanyEntity): Long

    @Query("DELETE FROM company_info")
    suspend fun deleteAll()

}