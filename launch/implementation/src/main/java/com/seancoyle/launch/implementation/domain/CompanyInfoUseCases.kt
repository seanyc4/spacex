package com.seancoyle.launch.implementation.domain

import com.seancoyle.launch.api.domain.usecase.GetCompanyInfoFromCacheUseCase
import com.seancoyle.launch.api.domain.usecase.GetCompanyInfoFromNetworkAndInsertToCacheUseCase

class CompanyInfoUseCases
constructor(
    val getCompanyInfoFromCacheUseCase: GetCompanyInfoFromCacheUseCase,
    val getCompanyInfoFromNetworkAndInsertToCacheUseCase: GetCompanyInfoFromNetworkAndInsertToCacheUseCase,
)














