package com.seancoyle.database.daos

import androidx.room.*
import com.seancoyle.database.entities.CompanyInfoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CompanyInfoDao {

    @Query("SELECT * FROM company_info")
    fun getCompanyInfo(): Flow<CompanyInfoEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(companyInfo: CompanyInfoEntity): Long

    @Query("DELETE FROM company_info")
    suspend fun deleteAll()

}