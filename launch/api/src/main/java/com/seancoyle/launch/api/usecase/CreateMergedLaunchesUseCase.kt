package com.seancoyle.launch.api.usecase

import com.seancoyle.launch.api.model.CompanyInfoModel
import com.seancoyle.launch.api.model.LaunchModel
import com.seancoyle.launch.api.model.LaunchType

interface CreateMergedLaunchesUseCase {
   operator fun invoke(
        companyInfo: CompanyInfoModel?,
        launches: List<LaunchModel>
    ): List<LaunchType>
}