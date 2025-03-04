package com.seancoyle.feature.launch.implementation.data.mapper

import com.seancoyle.core.common.result.DataError.RemoteError
import kotlinx.coroutines.TimeoutCancellationException
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

internal class RemoteErrorMapper @Inject constructor() {
    fun map(throwable: Throwable): RemoteError {
        return when (throwable) {
            is TimeoutCancellationException -> RemoteError.NETWORK_TIMEOUT
            is IOException -> RemoteError.NETWORK_CONNECTION_FAILED
            is HttpException -> {
                when (throwable.code()) {
                    401 -> RemoteError.NETWORK_UNAUTHORIZED
                    403 -> RemoteError.NETWORK_FORBIDDEN
                    404 -> RemoteError.NETWORK_NOT_FOUND
                    408 -> RemoteError.NETWORK_TIMEOUT
                    413 -> RemoteError.NETWORK_PAYLOAD_TOO_LARGE
                    500 -> RemoteError.NETWORK_INTERNAL_SERVER_ERROR
                    else -> RemoteError.NETWORK_UNKNOWN_ERROR
                }
            }
            is CancellationException -> throw throwable
            else -> RemoteError.NETWORK_UNKNOWN_ERROR
        }
    }
}