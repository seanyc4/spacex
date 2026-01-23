package com.seancoyle.feature.launch.data.local

import com.seancoyle.core.datastore_proto.LaunchPreferencesProto
import com.seancoyle.core.datastore_proto.OrderProto
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.domain.model.LaunchPrefs

fun LaunchPreferencesProto.toModel(): LaunchPrefs = LaunchPrefs(
    order = Order.valueOf(this.order.name)
)

fun LaunchPreferencesProto.toProto(order: Order): LaunchPreferencesProto = LaunchPreferencesProto.newBuilder().apply {
    this.order = OrderProto.valueOf(order.name)
}.build()
