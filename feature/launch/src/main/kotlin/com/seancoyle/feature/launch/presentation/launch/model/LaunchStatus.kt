package com.seancoyle.feature.launch.presentation.launch.model

enum class LaunchStatus(
    val id: Int,
    val label: String,
    val abbrev: String
) {
    GO(
        id = 1,
        label = "Go for Launch",
        abbrev = "Go"
    ),
    TBD(
        id = 2,
        label = "To be Determined",
        abbrev = "TBD"
    ),
    SUCCESS(
        id = 3,
        label = "Success",
        abbrev = "Success"
    ),
    FAILED(
        id = 4,
        label = "Failed",
        abbrev = "Failed"
    ),
    TBC(
        id = 8,
        label = "To Be Confirmed",
        abbrev = "TBC"
    ),
    ALL(
        id = 0,
        label = "Unknown",
        abbrev = "All"
    )
}
