package com.seancoyle.launch_domain.model.launch

abstract class LaunchType {
    abstract val type: Int

    companion object {
        const val TYPE_TITLE = 0
        const val TYPE_COMPANY = 1
        const val TYPE_LAUNCH = 2
    }
}