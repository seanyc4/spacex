package com.seancoyle.launch.contract.domain.usecase

import com.seancoyle.launch.contract.domain.model.CompanyInfo
import com.seancoyle.launch.contract.domain.model.ViewModel
import com.seancoyle.launch.contract.domain.model.ViewType

interface CreateMergedLaunchesUseCase {
   operator fun invoke(
       companyInfo: CompanyInfo?,
       launches: List<ViewModel>
    ): List<ViewType>
}