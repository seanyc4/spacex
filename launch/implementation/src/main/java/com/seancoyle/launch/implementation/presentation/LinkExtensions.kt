package com.seancoyle.launch.implementation.presentation

import androidx.annotation.StringRes
import com.seancoyle.launch.implementation.domain.model.LinkType

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
