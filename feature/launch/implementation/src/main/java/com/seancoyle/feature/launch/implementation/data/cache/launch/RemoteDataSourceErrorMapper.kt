package com.seancoyle.feature.launch.implementation.data.cache.launch

import com.seancoyle.core.common.result.DataSourceError
import kotlinx.coroutines.TimeoutCancellationException
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

internal class RemoteDataSourceErrorMapper @Inject constructor() {
    fun map(throwable: Throwable?): DataSourceError {
        return when (throwable) {
            is TimeoutCancellationException -> DataSourceError.NETWORK_TIMEOUT
            is IOException -> DataSourceError.NETWORK_CONNECTION_FAILED
            is HttpException -> {
                when (throwable.code()) {
                    401 -> DataSourceError.NETWORK_UNAUTHORIZED
                    403 -> DataSourceError.NETWORK_FORBIDDEN
                    404 -> DataSourceError.NETWORK_NOT_FOUND
                    408 -> DataSourceError.NETWORK_TIMEOUT
                    413 -> DataSourceError.NETWORK_PAYLOAD_TOO_LARGE
                    500 -> DataSourceError.NETWORK_INTERNAL_SERVER_ERROR
                    else -> DataSourceError.NETWORK_UNKNOWN_ERROR
                }
            }
            is CancellationException -> throw throwable
            else -> DataSourceError.NETWORK_UNKNOWN_ERROR
        }
    }
}