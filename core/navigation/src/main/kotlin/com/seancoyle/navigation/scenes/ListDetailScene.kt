package com.seancoyle.navigation.scenes

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.Scene
import com.seancoyle.navigation.transitions.fadeOutTransition

class ListDetailScene<T : Any>(
    val listEntry: NavEntry<T>,
    val detailEntry: NavEntry<T>,
    override val key: Any,
    override val previousEntries: List<NavEntry<T>>
) : Scene<T> {

    override val entries: List<NavEntry<T>>
        get() = listOf(listEntry, detailEntry)

    override val content: @Composable () -> Unit
        get() = {
            Row(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.weight(0.5f)) {
                    listEntry.Content()
                }
                Column(modifier = Modifier.weight(0.5f)) {
                    AnimatedContent(
                        targetState = detailEntry,
                        transitionSpec = {
                            fadeOutTransition()
                        },
                        label = "DetailPaneTransition"
                    ) { entry ->
                        entry.Content()
                    }
                }
            }
        }

    companion object {
        const val LIST_KEY = "ListDetailScene-List"
        const val DETAIL_KEY = "ListDetailScene-Detail"

        fun listPane() = mapOf(LIST_KEY to true)
        fun detailPane() = mapOf(DETAIL_KEY to true)
    }
}
