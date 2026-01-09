package com.seancoyle.navigation.scenes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.Scene

class ListDetailScene<T: Any>(
    val listEntry: NavEntry<T>,
    val detailEntry: NavEntry<T>,
    override val key: Any,
    override val previousEntries: List<NavEntry<T>>
): Scene<T> {

    override val entries: List<NavEntry<T>>
        get() = listOf(listEntry, detailEntry)

    override val content: @Composable (() -> Unit) = {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.weight(0.5f)) {
                listEntry.Content()
            }
            Column(modifier = Modifier.weight(0.5f)) {
                detailEntry.Content()
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
