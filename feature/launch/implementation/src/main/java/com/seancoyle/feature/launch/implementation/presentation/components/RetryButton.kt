package com.seancoyle.feature.launch.implementation.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A generic, reusable retry button for Compose screens.
 *
 * @param onClick Lambda to execute when the button is clicked.
 * @param modifier Modifier for styling and layout.
 * @param text The text to display on the button. Default is "Retry".
 * @param enabled Whether the button is enabled. Default is true.
 * @param textStyle Optional text style for the button label.
 * @param contentPadding Optional padding inside the button.
 * @param buttonColor Optional button color.
 */
@Composable
fun RetryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = "Retry",
    enabled: Boolean = true,
    textStyle: TextStyle = TextStyle(fontSize = 16.sp),
    contentPadding: PaddingValues = PaddingValues(vertical = 8.dp, horizontal = 24.dp),
    buttonColor: Color = Color.Unspecified
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .width(120.dp),
        contentPadding = contentPadding,
        colors = if (buttonColor != Color.Unspecified) ButtonDefaults.buttonColors(containerColor = buttonColor) else ButtonDefaults.buttonColors()
    ) {
        Text(
            text = text,
            style = textStyle
        )
    }
}

