package com.seancoyle.launch.implementation.domain

import com.seancoyle.launch.api.usecase.GetCompanyInfoFromCacheUseCase
import com.seancoyle.launch.api.usecase.GetCompanyInfoFromNetworkAndInsertToCacheUseCase

class CompanyInfoUseCases
constructor(
    val getCompanyInfoFromCacheUseCase: GetCompanyInfoFromCacheUseCase,
    val getCompanyInfoFromNetworkAndInsertToCacheUseCase: GetCompanyInfoFromNetworkAndInsertToCacheUseCase,
)














