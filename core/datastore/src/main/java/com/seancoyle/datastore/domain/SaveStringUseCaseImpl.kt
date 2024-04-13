package com.seancoyle.datastore.domain

import javax.inject.Inject

internal class SaveStringUseCaseImpl @Inject constructor(
    private val appPreferencesDataSource: AppPreferencesDataSource
) : SaveStringUseCase {
    override suspend fun invoke(key: String, value: String) {
        appPreferencesDataSource.saveStringValue(key, value)
    }
}