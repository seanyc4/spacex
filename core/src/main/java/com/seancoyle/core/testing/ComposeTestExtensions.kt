package com.seancoyle.core.testing

import androidx.annotation.StringRes
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import kotlin.properties.ReadOnlyProperty

fun AndroidComposeTestRule<*, *>.stringResource(@StringRes resId: Int) =
    ReadOnlyProperty<Any?, String> { _, _ -> activity.getString(resId) }