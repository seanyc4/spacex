package com.seancoyle.launch.implementation.domain

import com.seancoyle.launch.contract.domain.usecase.GetCompanyInfoFromCacheUseCase
import com.seancoyle.launch.contract.domain.usecase.GetCompanyInfoFromNetworkAndInsertToCacheUseCase

class CompanyInfoUseCases
constructor(
    val getCompanyInfoFromCacheUseCase: GetCompanyInfoFromCacheUseCase,
    val getCompanyInfoFromNetworkAndInsertToCacheUseCase: GetCompanyInfoFromNetworkAndInsertToCacheUseCase,
)














