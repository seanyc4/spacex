package com.seancoyle.datastore.implementation.domain

import com.seancoyle.datastore.api.AppDataStore
import javax.inject.Inject

internal class ClearDataUseCaseImpl @Inject constructor(
    private val appDataStore: AppDataStore
) : ClearDataUseCase {
    override suspend fun invoke() {
        appDataStore.clearData()
    }
}