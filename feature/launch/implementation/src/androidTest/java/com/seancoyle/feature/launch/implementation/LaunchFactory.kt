package com.seancoyle.feature.launch.implementation

import javax.inject.Inject

internal class LaunchFactory @Inject constructor() {

    fun provideValidFilterYearDates() = listOf(
        "2022", "2021", "2020", "2019", "2018", "2017", "2016", "2015", "2014", "2013",
        "2012", "2010", "2009", "2008", "2007", "2006"
    ).shuffled()
}