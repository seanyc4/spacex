package com.seancoyle.core.ui

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.res.stringResource

@Stable
sealed interface StringResource {
    fun resolve(context: Context): String

    data class Text(val text: String) : StringResource {
        override fun resolve(context: Context): String {
            return text
        }
    }

    data class ResId(@param:StringRes val stringId: Int): StringResource {
        override fun resolve(context: Context): String {
            return context.getString(stringId)
        }
    }

    data class ResIdWithParams(
        @param:StringRes val stringId: Int,
        val args: List<Any>
    ) : StringResource {
        override fun resolve(context: Context): String {
            return context.getString(stringId, *args.toTypedArray())
        }
    }

    @Composable
    fun resolve(): String {
        return when (this) {
            is ResId -> stringResource(id = stringId)
            is ResIdWithParams -> stringResource(id = stringId, *args.toTypedArray())
            is Text -> text
        }
    }
}