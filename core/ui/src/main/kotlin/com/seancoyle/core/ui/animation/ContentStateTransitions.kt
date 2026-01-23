package com.seancoyle.core.ui.animation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith

object ContentStateTransitions {

    fun <S> AnimatedContentTransitionScope<S>.defaultFadeTransition(
        durationMillis: Int = 300
    ): ContentTransform {
        return fadeIn(animationSpec = tween(durationMillis = durationMillis)) togetherWith
                fadeOut(animationSpec = tween(durationMillis = durationMillis))
    }

    fun <S> AnimatedContentTransitionScope<S>.fastFadeTransition(): ContentTransform {
        return fadeIn(animationSpec = tween(durationMillis = 150)) togetherWith
                fadeOut(animationSpec = tween(durationMillis = 150))
    }

    fun <S> AnimatedContentTransitionScope<S>.slowFadeTransition(): ContentTransform {
        return fadeIn(animationSpec = tween(durationMillis = 700)) togetherWith
                fadeOut(animationSpec = tween(durationMillis = 700))
    }
}
