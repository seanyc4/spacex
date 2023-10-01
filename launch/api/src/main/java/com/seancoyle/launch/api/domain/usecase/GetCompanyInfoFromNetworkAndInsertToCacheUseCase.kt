package com.seancoyle.launch.api.domain.usecase

interface GetCompanyInfoFromNetworkAndInsertToCacheUseCase {
    suspend operator fun invoke()
}