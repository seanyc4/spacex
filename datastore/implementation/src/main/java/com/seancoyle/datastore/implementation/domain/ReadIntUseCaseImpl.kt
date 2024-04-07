package com.seancoyle.datastore.implementation.domain

import javax.inject.Inject

internal class ReadIntUseCaseImpl @Inject constructor(
    private val appDataStore: AppDataStore
) : ReadIntUseCase {
    override suspend fun invoke(key: String): Int? {
        return appDataStore.readIntValue(key)
    }
}