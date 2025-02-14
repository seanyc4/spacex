package com.seancoyle.core.ui.extensions

import com.seancoyle.core.common.result.DataSourceError
import com.seancoyle.core.ui.R
import com.seancoyle.core.ui.StringResource

fun DataSourceError.asStringResource(): StringResource {
    return when (this) {
        DataSourceError.NETWORK_TIMEOUT -> StringResource.ResId(R.string.network_error_timeout)
        DataSourceError.NETWORK_CONNECTION_FAILED -> StringResource.ResId(R.string.network_connection_failed)
        DataSourceError.NETWORK_UNAUTHORIZED -> StringResource.ResId(R.string.network_unauthorized)
        DataSourceError.NETWORK_FORBIDDEN -> StringResource.ResId(R.string.network_forbidden)
        DataSourceError.NETWORK_NOT_FOUND -> StringResource.ResId(R.string.network_not_found)
        DataSourceError.NETWORK_INTERNAL_SERVER_ERROR -> StringResource.ResId(R.string.network_internal_server_error)
        DataSourceError.NETWORK_UNKNOWN_ERROR -> StringResource.ResId(R.string.network_unknown_error)
        DataSourceError.NETWORK_DATA_NULL -> StringResource.ResId(R.string.network_data_null)
        DataSourceError.NETWORK_PAYLOAD_TOO_LARGE -> StringResource.ResId(R.string.network_payload_too_large)

        DataSourceError.CACHE_ERROR -> StringResource.ResId(R.string.cache_error)
        DataSourceError.CACHE_ERROR_TIMEOUT -> StringResource.ResId(R.string.cache_error_timeout)
        DataSourceError.CACHE_ERROR_NO_RESULTS -> StringResource.ResId(R.string.cache_error_no_results)
        DataSourceError.CACHE_DATA_NULL -> StringResource.ResId(R.string.cache_data_null)
        DataSourceError.CACHE_CONSTRAINT_VIOLATION -> StringResource.ResId(R.string.constraint_violation)
        DataSourceError.CACHE_UNKNOWN_DATABASE_ERROR -> StringResource.ResId(R.string.unknown_database_error)
        DataSourceError.CACHE_INSERT_FAILED -> StringResource.ResId(R.string.cache_insert_failed)
    }
}

fun Int.asStringResource(args: List<Any> = emptyList()): StringResource =
    StringResource.ResIdWithParams(stringId = this, args = args)