package com.seancoyle.core.ui.components

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.DataError.*
import com.seancoyle.core.ui.R
import com.seancoyle.core.ui.StringResource

fun DataError.asStringResource(): StringResource {
    return when (this) {
        RemoteError.NETWORK_TIMEOUT -> StringResource.ResId(R.string.network_error_timeout)
        RemoteError.NETWORK_CONNECTION_FAILED -> StringResource.ResId(R.string.network_connection_failed)
        RemoteError.NETWORK_UNAUTHORIZED -> StringResource.ResId(R.string.network_unauthorized)
        RemoteError.NETWORK_FORBIDDEN -> StringResource.ResId(R.string.network_forbidden)
        RemoteError.NETWORK_NOT_FOUND -> StringResource.ResId(R.string.network_not_found)
        RemoteError.NETWORK_INTERNAL_SERVER_ERROR -> StringResource.ResId(R.string.network_internal_server_error)
        RemoteError.NETWORK_UNKNOWN_ERROR -> StringResource.ResId(R.string.network_unknown_error)
        RemoteError.NETWORK_DATA_NULL -> StringResource.ResId(R.string.network_data_null)
        RemoteError.NETWORK_PAYLOAD_TOO_LARGE -> StringResource.ResId(R.string.network_payload_too_large)
        LocalError.CACHE_ERROR -> StringResource.ResId(R.string.cache_error)
        LocalError.CACHE_ERROR_TIMEOUT -> StringResource.ResId(R.string.cache_error_timeout)
        LocalError.CACHE_DATA_NULL -> StringResource.ResId(R.string.cache_data_null)
        LocalError.CACHE_CONSTRAINT_VIOLATION -> StringResource.ResId(R.string.constraint_violation)
        LocalError.CACHE_UNKNOWN_DATABASE_ERROR -> StringResource.ResId(R.string.unknown_database_error)
        LocalError.CACHE_INSERT_FAILED -> StringResource.ResId(R.string.cache_insert_failed)
    }
}

fun Int.asStringResource(args: List<Any> = emptyList()): StringResource =
    StringResource.ResIdWithParams(stringId = this, args = args)