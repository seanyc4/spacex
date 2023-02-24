package com.seancoyle.ui_launch.ui.composables

import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun LaunchBottomSheetTitles(name: String) {
    Text(
     text = name,
        style = MaterialTheme.typography.h5,
        modifier = Modifier
            .wrapContentWidth(align = Alignment.CenterHorizontally)
    )

}

@Preview
@Composable
fun LaunchBottomSheetTitlesPreview(){
    MaterialTheme {
        LaunchBottomSheetTitles("Links")
    }
}
