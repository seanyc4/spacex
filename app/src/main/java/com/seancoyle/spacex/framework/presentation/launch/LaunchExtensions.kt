package com.seancoyle.spacex.framework.presentation.launch

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

fun ImageView.glideLoadLaunchImage(url: String?, withCrossFade: Boolean? = true) {
    if (withCrossFade == true) {
        Glide.with(this)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
    } else {
        Glide.with(this)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(this)
    }
}