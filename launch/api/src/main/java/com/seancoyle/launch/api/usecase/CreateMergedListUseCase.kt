package com.seancoyle.launch.api.usecase

import com.seancoyle.launch.api.model.CompanyInfoModel
import com.seancoyle.launch.api.model.LaunchModel
import com.seancoyle.launch.api.model.LaunchType

interface CreateMergedListUseCase {
    fun createLaunchData(
        companyInfo: CompanyInfoModel?,
        launchList: List<LaunchModel>
    ): List<LaunchType>
}