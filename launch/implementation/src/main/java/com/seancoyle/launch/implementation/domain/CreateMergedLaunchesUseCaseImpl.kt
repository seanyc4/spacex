package com.seancoyle.launch.implementation.domain

import com.seancoyle.core.util.StringResource
import com.seancoyle.launch.api.domain.model.CompanyInfoModel
import com.seancoyle.launch.api.domain.model.CompanySummary
import com.seancoyle.launch.api.domain.model.LaunchModel
import com.seancoyle.launch.api.domain.model.LaunchType
import com.seancoyle.launch.api.domain.model.SectionTitle
import com.seancoyle.launch.api.domain.usecase.CreateMergedLaunchesUseCase
import com.seancoyle.launch.implementation.R
import javax.inject.Inject

class CreateMergedLaunchesUseCaseImpl @Inject constructor(
    private val stringResource: StringResource
): CreateMergedLaunchesUseCase {

    override operator fun invoke(
        companyInfo: CompanyInfoModel?,
        launches: List<LaunchModel>
    ): List<LaunchType> {
        val mergedLaunches = mutableListOf<LaunchType>().apply {
            add(SectionTitle(title = "COMPANY", type = LaunchType.TYPE_TITLE))
            add(CompanySummary(summary = buildCompanySummary(companyInfo), type = LaunchType.TYPE_COMPANY))
            add(SectionTitle(title = "LAUNCH", type = LaunchType.TYPE_TITLE))
            addAll(launches)
        }
        return mergedLaunches
    }

    private fun buildCompanySummary(companyInfo: CompanyInfoModel?) = String.format(
        stringResource.getString(R.string.company_info),
        companyInfo?.name,
        companyInfo?.founder,
        companyInfo?.founded,
        companyInfo?.employees,
        companyInfo?.launchSites,
        companyInfo?.valuation
    )
}