package com.seancoyle.datastore.domain

import javax.inject.Inject

internal class ClearDataUseCaseImpl @Inject constructor(
    private val appDataStore: AppDataStore
) : ClearDataUseCase {
    override suspend fun invoke() {
        appDataStore.clearData()
    }
}