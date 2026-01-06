package com.seancoyle.feature.launch.presentation.launch.model

enum class LaunchStatus(
    val label: String,
    val abbrev: String
) {
    SUCCESS(
        label = "Success",
        abbrev = "Success"
    ),
    FAILED(
        label = "Failed",
        abbrev = "Failed"
    ),
    TBD(
        label = "To be Determined",
        abbrev = "TBD"
    ),
    GO(
        label = "Go for Launch",
        abbrev = "Go"
    ),
    TBC(
        label = "To Be Confirmed",
        abbrev = "TBC"
    ),
    ALL(
        label = "Unknown",
        abbrev = "All"
    )
}
