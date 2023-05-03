package com.seancoyle.launch.implementation.domain

import com.seancoyle.core.util.StringResource
import com.seancoyle.launch.api.model.CompanyInfoModel
import com.seancoyle.launch.api.model.CompanySummary
import com.seancoyle.launch.api.model.LaunchModel
import com.seancoyle.launch.api.model.LaunchType
import com.seancoyle.launch.api.model.SectionTitle
import com.seancoyle.launch.api.usecase.CreateMergedListUseCase
import com.seancoyle.launch.implementation.R
import javax.inject.Inject

class CreateMergedListUseCaseImpl @Inject constructor(
    private val stringResource: StringResource
): CreateMergedListUseCase {

    override fun createLaunchData(
        companyInfo: CompanyInfoModel?,
        launchList: List<LaunchModel>
    ): List<LaunchType> {

        val consolidatedList = mutableListOf<LaunchType>()

        consolidatedList.add(
            SectionTitle(
                title = "COMPANY",
                type = LaunchType.TYPE_TITLE
            )
        )
        consolidatedList.add(
            CompanySummary(
                summary = buildCompanyInfoString(companyInfo),
                type = LaunchType.TYPE_COMPANY
            )
        )
        consolidatedList.add(
            SectionTitle(
                title = "LAUNCH",
                type = LaunchType.TYPE_TITLE
            )
        )
        launchList.map { launchItems ->
            consolidatedList.add(
                launchItems
            )

        }

        return consolidatedList
    }

    private fun buildCompanyInfoString(companyInfo: CompanyInfoModel?) = String.format(
        stringResource.getString(R.string.company_info),
        companyInfo?.name,
        companyInfo?.founder,
        companyInfo?.founded,
        companyInfo?.employees,
        companyInfo?.launchSites,
        companyInfo?.valuation
    )
}