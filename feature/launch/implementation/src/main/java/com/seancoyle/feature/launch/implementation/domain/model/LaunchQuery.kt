package com.seancoyle.feature.launch.implementation.domain.model

import com.seancoyle.core.domain.Order

internal data class LaunchQuery(
    val query: String? = null,
    val order: Order = Order.DESC
)
