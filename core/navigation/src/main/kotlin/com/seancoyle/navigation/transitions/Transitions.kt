package com.seancoyle.navigation.transitions

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.navigation3.ui.NavDisplay

fun verticalSlideTransition() =
    NavDisplay.transitionSpec {
        slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(1000)
        ) togetherWith ExitTransition.KeepUntilTransitionsFinished
    } + NavDisplay.popTransitionSpec {
        EnterTransition.None togetherWith
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(1000)
                )
    } + NavDisplay.predictivePopTransitionSpec {
        EnterTransition.None togetherWith
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(1000)
                )
    }

fun NavDisplay.slideInFromRightTransition() =
    slideInHorizontally(initialOffsetX = { it }) togetherWith
            slideOutHorizontally(targetOffsetX = { -it })

fun NavDisplay.slideInFromLeftTransition() =
    slideInHorizontally(initialOffsetX = { -it }) togetherWith
            slideOutHorizontally(targetOffsetX = { it })