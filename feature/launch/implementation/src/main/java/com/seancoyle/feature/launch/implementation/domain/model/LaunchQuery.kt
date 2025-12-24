package com.seancoyle.feature.launch.implementation.domain.model

import com.seancoyle.core.domain.Order

internal data class LaunchQuery(
    val query: String = "",
    val order: Order = Order.ASC
)
