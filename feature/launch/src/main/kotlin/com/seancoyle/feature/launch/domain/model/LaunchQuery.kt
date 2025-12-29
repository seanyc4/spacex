package com.seancoyle.feature.launch.domain.model

import com.seancoyle.core.domain.Order

data class LaunchQuery(
    val query: String = "",
    val order: Order = Order.ASC,
    val status: LaunchStatus? = null
)
