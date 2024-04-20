package com.seancoyle.core.ui

import android.content.Context
import com.seancoyle.core.domain.AppStringResource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class AppStringResourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AppStringResource {
    override fun getString(resId: Int, args: Array<Any>): String {
        return context.getString(resId, *args)
    }
}