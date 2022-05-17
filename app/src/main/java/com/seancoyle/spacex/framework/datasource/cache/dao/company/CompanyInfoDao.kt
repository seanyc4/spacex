package com.seancoyle.spacex.framework.datasource.cache.dao.company

import androidx.room.*
import com.seancoyle.spacex.framework.datasource.cache.model.company.CompanyInfoEntity

@Dao
interface CompanyInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(companyInfo: CompanyInfoEntity): Long

    @Query("SELECT * FROM company_info")
    suspend fun getCompanyInfo(): CompanyInfoEntity?

    @Query("DELETE FROM company_info")
    suspend fun deleteAll()

}












