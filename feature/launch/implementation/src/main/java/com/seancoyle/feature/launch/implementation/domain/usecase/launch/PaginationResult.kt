package com.seancoyle.feature.launch.implementation.domain.usecase.launch

sealed interface PaginationResult {
    data class Success(val nextPage: Int) : PaginationResult
    data object EndReached : PaginationResult
}
