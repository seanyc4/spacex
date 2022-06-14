package com.seancoyle.launch_datasource.cache.dao.company

import androidx.room.*
import com.seancoyle.launch_datasource.cache.entities.company.CompanyInfoEntity

@Dao
interface CompanyInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(companyInfo: CompanyInfoEntity): Long

    @Query("SELECT * FROM company_info")
    suspend fun getCompanyInfo(): CompanyInfoEntity?

    @Query("DELETE FROM company_info")
    suspend fun deleteAll()

}












