package com.seancoyle.launch.api.domain.usecase

import com.seancoyle.launch.api.domain.model.CompanyInfo
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.api.domain.model.ViewType
import kotlinx.coroutines.flow.Flow

interface CreateMergedLaunchesUseCase {
   operator fun invoke(
       companyInfo: CompanyInfo?,
       launches: List<Launch>
    ): Flow<List<ViewType>>
}