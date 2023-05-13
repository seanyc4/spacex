package com.seancoyle.launch.api.domain.model


abstract class ViewType {
    abstract val type: Int

    companion object {
        const val TYPE_SECTION_TITLE = 0
        const val TYPE_HEADER = 1
        const val TYPE_LIST = 2
        const val TYPE_CAROUSEL = 3
        const val TYPE_GRID = 4
    }
}
