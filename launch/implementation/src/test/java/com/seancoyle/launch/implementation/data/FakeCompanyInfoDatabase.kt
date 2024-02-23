package com.seancoyle.launch.implementation.data

import com.seancoyle.launch.implementation.domain.model.Company
import javax.inject.Inject

class FakeCompanyInfoDatabase @Inject constructor() {
    // fake for company_info table in local db
    var company : Company? = null
}