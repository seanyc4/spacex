package com.seancoyle.core.presentation

import com.seancoye.core.R
import com.seancoyle.core.domain.DataError
import com.seancoyle.core.domain.DataResult
import com.seancoyle.core.domain.StringResource

fun DataResult.Error<DataError>.getErrorMessage(stringResource: StringResource): String {
    return when (this.error) {
        DataError.NETWORK_TIMEOUT -> stringResource.getString(R.string.network_error_timeout)
        DataError.NETWORK_CONNECTION_FAILED -> stringResource.getString(R.string.network_connection_failed)
        DataError.NETWORK_UNAUTHORIZED -> stringResource.getString(R.string.network_unauthorized)
        DataError.NETWORK_FORBIDDEN -> stringResource.getString(R.string.network_forbidden)
        DataError.NETWORK_NOT_FOUND -> stringResource.getString(R.string.network_not_found)
        DataError.NETWORK_INTERNAL_SERVER_ERROR -> stringResource.getString(R.string.network_internal_server_error)
        DataError.UNKNOWN_NETWORK_ERROR -> stringResource.getString(R.string.unknown_network_error)
        DataError.NETWORK_ERROR_UNKNOWN -> stringResource.getString(R.string.network_error_unknown)
        DataError.NETWORK_DATA_NULL -> stringResource.getString(R.string.network_data_null)
        DataError.CACHE_ERROR -> stringResource.getString(R.string.cache_error)
        DataError.CACHE_ERROR_TIMEOUT -> stringResource.getString(R.string.cache_error_timeout)
        DataError.CACHE_ERROR_NO_RESULTS -> stringResource.getString(R.string.cache_error_no_results)
        DataError.CACHE_DATA_NULL -> stringResource.getString(R.string.cache_data_null)
        DataError.CONSTRAINT_VIOLATION -> stringResource.getString(R.string.constraint_violation)
        DataError.UNKNOWN_DATABASE_ERROR -> stringResource.getString(R.string.unknown_database_error)
        DataError.CACHE_INSERT_FAILED -> stringResource.getString(R.string.cache_insert_failed)
        else -> stringResource.getString(R.string.unknown_error)
    }
}