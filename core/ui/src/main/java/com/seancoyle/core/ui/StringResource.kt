package com.seancoyle.core.ui

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

sealed interface StringResource {
    data class DynamicString(val value: String) : StringResource
    class AndroidStringResource(
        @StringRes val id: Int,
        val args: Array<Any> = arrayOf()
    ) : StringResource

    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is AndroidStringResource -> LocalContext.current.getString(id, *args)
        }
    }

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is AndroidStringResource -> context.getString(id, *args)
        }
    }
}