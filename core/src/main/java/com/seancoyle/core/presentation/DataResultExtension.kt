package com.seancoyle.core.presentation

import com.seancoyle.core.R
import com.seancoyle.core.domain.DataError
import com.seancoyle.core.domain.DataResult

fun DataError.asStringResource(): StringResource {
    return when (this) {
        DataError.NETWORK_TIMEOUT -> StringResource.AndroidStringResource(R.string.network_error_timeout)
        DataError.NETWORK_CONNECTION_FAILED -> StringResource.AndroidStringResource(R.string.network_connection_failed)
        DataError.NETWORK_UNAUTHORIZED -> StringResource.AndroidStringResource(R.string.network_unauthorized)
        DataError.NETWORK_FORBIDDEN -> StringResource.AndroidStringResource(R.string.network_forbidden)
        DataError.NETWORK_NOT_FOUND -> StringResource.AndroidStringResource(R.string.network_not_found)
        DataError.NETWORK_INTERNAL_SERVER_ERROR -> StringResource.AndroidStringResource(R.string.network_internal_server_error)
        DataError.UNKNOWN_NETWORK_ERROR -> StringResource.AndroidStringResource(R.string.unknown_network_error)
        DataError.NETWORK_ERROR_UNKNOWN -> StringResource.AndroidStringResource(R.string.network_error_unknown)
        DataError.NETWORK_DATA_NULL -> StringResource.AndroidStringResource(R.string.network_data_null)
        DataError.CACHE_ERROR -> StringResource.AndroidStringResource(R.string.cache_error)
        DataError.CACHE_ERROR_TIMEOUT -> StringResource.AndroidStringResource(R.string.cache_error_timeout)
        DataError.CACHE_ERROR_NO_RESULTS -> StringResource.AndroidStringResource(R.string.cache_error_no_results)
        DataError.CACHE_DATA_NULL -> StringResource.AndroidStringResource(R.string.cache_data_null)
        DataError.CONSTRAINT_VIOLATION -> StringResource.AndroidStringResource(R.string.constraint_violation)
        DataError.UNKNOWN_DATABASE_ERROR -> StringResource.AndroidStringResource(R.string.unknown_database_error)
        DataError.CACHE_INSERT_FAILED -> StringResource.AndroidStringResource(R.string.cache_insert_failed)
        DataError.NETWORK_PAYLOAD_TOO_LARGE -> TODO()
    }
}

fun Int.asStringResource(args: Array<Any> = emptyArray()): StringResource =
    StringResource.AndroidStringResource(id = this, args = args)

fun String.asStringResource(): StringResource {
    return this.asStringResource()
}

fun DataResult.Error<DataError>.asStringResource(): StringResource {
    return error.asStringResource()
}