package com.seancoyle.launch.api.domain.usecase

import com.seancoyle.launch.api.domain.model.CompanyInfo
import com.seancoyle.launch.api.domain.model.LaunchModel
import com.seancoyle.launch.api.domain.model.LaunchType

interface CreateMergedLaunchesUseCase {
   operator fun invoke(
       companyInfo: CompanyInfo?,
       launches: List<LaunchModel>
    ): List<LaunchType>
}