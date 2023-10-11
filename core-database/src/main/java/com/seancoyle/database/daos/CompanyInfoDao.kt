package com.seancoyle.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.seancoyle.database.entities.CompanyInfoEntity

@Dao
interface CompanyInfoDao {

    @Query("SELECT * FROM company_info")
    suspend fun getCompanyInfo(): CompanyInfoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(companyInfo: CompanyInfoEntity): Long

    @Query("DELETE FROM company_info")
    suspend fun deleteAll()

}