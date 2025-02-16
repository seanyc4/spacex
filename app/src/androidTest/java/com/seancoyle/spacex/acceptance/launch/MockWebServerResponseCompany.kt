package com.seancoyle.spacex.acceptance.launch

object MockWebServerResponseCompany {

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