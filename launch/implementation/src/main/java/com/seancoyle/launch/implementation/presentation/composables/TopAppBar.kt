package com.seancoyle.launch.implementation.presentation.composables

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import com.seancoyle.launch.implementation.R

@Composable
fun HomeAppBar(
    title: @Composable() () -> Unit,
    onClick: () -> Unit
) {
    TopAppBar(
        title = title,
        backgroundColor = colorResource(id = R.color.colorPrimary),
        navigationIcon = {
            IconButton(onClick = { onClick() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_filter_list),
                    contentDescription = "Filter List Button"
                )
            }
        }
    )
}