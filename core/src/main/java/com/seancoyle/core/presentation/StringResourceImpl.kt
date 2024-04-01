package com.seancoyle.core.presentation

import android.content.Context
import com.seancoyle.core.domain.StringResource
import javax.inject.Inject

class StringResourceImpl @Inject constructor(private val context: Context) : StringResource {
    override fun getString(resId: Int): String {
        return context.getString(resId)
    }

    override fun getString(id: Int, args: Array<Any>): String {
        return context.getString(id, *args)
    }
}