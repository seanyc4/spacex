package com.seancoyle.movies.framework.presentation.movielist

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.seancoyle.movies.util.Constants.BASE_IMAGE_URL

fun ImageView.glideLoadMoviePosters(url: String?, withCrossFade: Boolean? = true) {
    if (withCrossFade == true) {
        Glide.with(this)
            .load(BASE_IMAGE_URL+url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
    } else {
        Glide.with(this)
            .load(BASE_IMAGE_URL+url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(this)
    }
}