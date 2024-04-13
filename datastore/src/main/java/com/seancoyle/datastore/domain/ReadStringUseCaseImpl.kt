package com.seancoyle.datastore.domain

import javax.inject.Inject

internal class ReadStringUseCaseImpl @Inject constructor(
    private val appPreferencesDataSource: AppPreferencesDataSource
) : ReadStringUseCase {
    override suspend fun invoke(key: String): String? {
        return appPreferencesDataSource.readStringValue(key)
    }
}