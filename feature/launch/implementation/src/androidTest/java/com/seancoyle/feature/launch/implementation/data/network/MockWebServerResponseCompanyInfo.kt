package com.seancoyle.feature.launch.implementation.data.network

object MockWebServerResponseCompanyInfo {

    val companyResponse = """
{
    "name": "SpaceX",
    "founder": "Elon Musk",
    "founded": 2002,
    "employees": 7000,
    "launch_sites": 3,
    "valuation": 27500000000
}
""".trimIndent()

}