package com.seancoyle.spacex.framework.datasource.cache.dao.company

import androidx.room.*
import com.seancoyle.spacex.framework.datasource.cache.model.company.CompanyInfoCacheEntity

@Dao
interface CompanyInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(companyInfo: CompanyInfoCacheEntity): Long

    @Query("SELECT * FROM company_info")
    suspend fun getCompanyInfo(): CompanyInfoCacheEntity?

    @Query("DELETE FROM company_info")
    suspend fun deleteAll()

}












