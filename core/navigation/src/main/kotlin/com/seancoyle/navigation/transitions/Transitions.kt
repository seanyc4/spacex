package com.seancoyle.navigation.transitions

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.navigation3.ui.NavDisplay

fun verticalSlideTransition() =
    NavDisplay.transitionSpec {
        slideInVertically(
            initialOffsetY = { -it },
            animationSpec = tween(500)
        ) togetherWith ExitTransition.KeepUntilTransitionsFinished
    } + NavDisplay.popTransitionSpec {
        EnterTransition.None togetherWith
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(500)
                )
    } + NavDisplay.predictivePopTransitionSpec {
        EnterTransition.None togetherWith
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(500)
                )
    }

fun slideInFromRightTransition() =
    slideInHorizontally(initialOffsetX = { it }) togetherWith
            slideOutHorizontally(targetOffsetX = { -it })

fun slideInFromLeftTransition() =
    slideInHorizontally(initialOffsetX = { -it }) togetherWith
            slideOutHorizontally(targetOffsetX = { it })

fun fadeOutTransition() =
    fadeIn(animationSpec = tween(500)) togetherWith
        fadeOut(animationSpec = tween(500))