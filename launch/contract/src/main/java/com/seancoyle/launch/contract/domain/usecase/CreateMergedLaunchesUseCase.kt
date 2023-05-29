package com.seancoyle.launch.contract.domain.usecase

import com.seancoyle.launch.contract.domain.model.CompanyInfo
import com.seancoyle.launch.contract.domain.model.Launch
import com.seancoyle.launch.contract.domain.model.ViewType

interface CreateMergedLaunchesUseCase {
   operator fun invoke(
       companyInfo: CompanyInfo?,
       launches: List<Launch>
    ): List<ViewType>
}