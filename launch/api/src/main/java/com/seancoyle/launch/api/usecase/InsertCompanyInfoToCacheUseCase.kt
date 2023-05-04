package com.seancoyle.launch.api.usecase

import com.seancoyle.core.state.DataState
import com.seancoyle.core.state.Event
import com.seancoyle.launch.api.model.CompanyInfoModel
import com.seancoyle.launch.api.model.LaunchViewState
import kotlinx.coroutines.flow.Flow

interface InsertCompanyInfoToCacheUseCase {
    operator fun invoke(
        companyInfo: CompanyInfoModel,
        event: Event
    ): Flow<DataState<LaunchViewState>?>
}