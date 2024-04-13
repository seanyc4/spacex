package com.seancoyle.datastore.domain

import javax.inject.Inject

internal class ReadIntUseCaseImpl @Inject constructor(
    private val appPreferencesDataSource: AppPreferencesDataSource
) : ReadIntUseCase {
    override suspend fun invoke(key: String): Int? {
        return appPreferencesDataSource.readIntValue(key)
    }
}