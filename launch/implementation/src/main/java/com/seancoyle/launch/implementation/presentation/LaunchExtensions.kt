package com.seancoyle.launch.implementation.presentation

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.seancoyle.launch.implementation.R

fun ImageView.glideLoadLaunchImage(url: String?, withCrossFade: Boolean? = true) {
    if (withCrossFade == true) {
        Glide.with(this)
            .load(url)
            .error(R.drawable.default_mission_image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
    } else {
        Glide.with(this)
            .load(url)
            .error(R.drawable.default_mission_image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(this)
    }
}