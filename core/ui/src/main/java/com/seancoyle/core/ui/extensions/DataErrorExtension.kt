package com.seancoyle.core.ui.extensions

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.ui.R
import com.seancoyle.core.ui.StringResource

fun DataError.asStringResource(): StringResource {
    return when (this) {
        DataError.NETWORK_TIMEOUT -> StringResource.ResId(R.string.network_error_timeout)
        DataError.NETWORK_CONNECTION_FAILED -> StringResource.ResId(R.string.network_connection_failed)
        DataError.NETWORK_UNAUTHORIZED -> StringResource.ResId(R.string.network_unauthorized)
        DataError.NETWORK_FORBIDDEN -> StringResource.ResId(R.string.network_forbidden)
        DataError.NETWORK_NOT_FOUND -> StringResource.ResId(R.string.network_not_found)
        DataError.NETWORK_INTERNAL_SERVER_ERROR -> StringResource.ResId(R.string.network_internal_server_error)
        DataError.NETWORK_UNKNOWN_ERROR -> StringResource.ResId(R.string.network_unknown_error)
        DataError.NETWORK_DATA_NULL -> StringResource.ResId(R.string.network_data_null)
        DataError.NETWORK_PAYLOAD_TOO_LARGE -> StringResource.ResId(R.string.network_payload_too_large)

        DataError.CACHE_ERROR -> StringResource.ResId(R.string.cache_error)
        DataError.CACHE_ERROR_TIMEOUT -> StringResource.ResId(R.string.cache_error_timeout)
        DataError.CACHE_ERROR_NO_RESULTS -> StringResource.ResId(R.string.cache_error_no_results)
        DataError.CACHE_DATA_NULL -> StringResource.ResId(R.string.cache_data_null)
        DataError.CACHE_CONSTRAINT_VIOLATION -> StringResource.ResId(R.string.constraint_violation)
        DataError.CACHE_UNKNOWN_DATABASE_ERROR -> StringResource.ResId(R.string.unknown_database_error)
        DataError.CACHE_INSERT_FAILED -> StringResource.ResId(R.string.cache_insert_failed)
    }
}

fun Int.asStringResource(args: List<Any> = emptyList()): StringResource =
    StringResource.ResIdWithParams(stringId = this, args = args)