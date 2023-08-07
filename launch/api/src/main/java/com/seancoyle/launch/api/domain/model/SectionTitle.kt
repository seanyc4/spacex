package com.seancoyle.launch.api.domain.model

import androidx.annotation.Keep

@Keep
data class SectionTitle(
    val title: String,
    override val type: Int
) : ViewType()
