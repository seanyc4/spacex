package com.seancoyle.launch.api.domain.usecase

import com.seancoyle.launch.api.domain.model.CompanyInfo
import com.seancoyle.launch.api.domain.model.ViewModel
import com.seancoyle.launch.api.domain.model.ViewType

interface CreateMergedLaunchesUseCase {
   operator fun invoke(
       companyInfo: CompanyInfo?,
       launches: List<ViewModel>
    ): List<ViewType>
}