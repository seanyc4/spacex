package com.seancoyle.datastore.domain

import javax.inject.Inject

internal class ClearDataUseCaseImpl @Inject constructor(
    private val appPreferencesDataSource: AppPreferencesDataSource
) : ClearDataUseCase {
    override suspend fun invoke() {
        appPreferencesDataSource.clearData()
    }
}