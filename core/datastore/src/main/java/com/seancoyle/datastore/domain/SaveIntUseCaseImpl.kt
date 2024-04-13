package com.seancoyle.datastore.domain

import javax.inject.Inject

internal class SaveIntUseCaseImpl @Inject constructor(
    private val appPreferencesDataSource: AppPreferencesDataSource
) : SaveIntUseCase {
    override suspend fun invoke(key: String, value: Int) {
        appPreferencesDataSource.saveIntValue(key, value)
    }
}