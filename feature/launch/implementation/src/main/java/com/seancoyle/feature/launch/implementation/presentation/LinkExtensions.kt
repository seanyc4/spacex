package com.seancoyle.feature.launch.implementation.presentation

import androidx.annotation.StringRes
import com.seancoyle.feature.launch.api.domain.model.LinkType

internal fun createLinkType(
    @StringRes titleRes: Int,
    link: String?,
    onClick: (String?) -> Unit
): LinkType? {
    return link?.let {
        LinkType(
            titleRes, it,
            onClick = { onClick(link) }
        )
    }
}
