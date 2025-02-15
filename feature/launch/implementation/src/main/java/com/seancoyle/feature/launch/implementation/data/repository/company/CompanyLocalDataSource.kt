package com.seancoyle.feature.launch.implementation.data.repository.company

import com.seancoyle.database.entities.CompanyEntity

internal interface CompanyLocalDataSource {
    suspend fun insert(company: CompanyEntity): Result<Long>
    suspend fun get(): Result<CompanyEntity?>
    suspend fun deleteAll(): Result<Unit>
}