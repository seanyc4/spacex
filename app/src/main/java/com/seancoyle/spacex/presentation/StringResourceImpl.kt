package com.seancoyle.spacex.presentation

import android.content.Context
import com.seancoyle.core.util.StringResource
import javax.inject.Inject

class StringResourceImpl @Inject constructor(private val context: Context) : StringResource {
    override fun getString(resId: Int): String {
        return context.getString(resId)
    }
}