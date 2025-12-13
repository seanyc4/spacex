package com.seancoyle.feature.launch.implementation.data.local

import com.seancoyle.core.datastore_proto.LaunchPreferencesProto
import com.seancoyle.core.datastore_proto.LaunchStatusProto
import com.seancoyle.core.datastore_proto.OrderProto
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.LaunchPrefs
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus

fun LaunchPreferencesProto.toModel(): LaunchPrefs = LaunchPrefs(
    order = Order.valueOf(this.order.name),
    launchStatus = LaunchStatus.valueOf(this.launchStatus.name),
    launchYear = this.launchDate
)

fun LaunchPreferencesProto.toProto(
    order: Order,
    launchStatus: LaunchStatus,
    launchYear: String
): LaunchPreferencesProto = LaunchPreferencesProto.newBuilder().apply {
    this.order = OrderProto.valueOf(order.name)
    this.launchStatus = LaunchStatusProto.valueOf(launchStatus.name)
    this.launchDate = launchYear
}.build()
