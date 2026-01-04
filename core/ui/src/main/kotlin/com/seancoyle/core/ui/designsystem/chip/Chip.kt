package com.seancoyle.core.ui.designsystem.chip

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode

@Composable
fun Chip(
    modifier: Modifier = Modifier,
    text: String,
    contentColor: Color,
    containerColor: Color,
    icon: ImageVector? = null
) {
    AssistChip(
        onClick = {},
        label = {
            AppText.labelMedium(
                text = text,
                fontWeight = FontWeight.SemiBold,
                color = contentColor,
                maxLines = 1
            )
        },
        leadingIcon = {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = containerColor.copy(alpha = 0.4f),
            labelColor = contentColor,
            leadingIconContentColor = contentColor
        ),
        modifier = modifier.semantics {
            contentDescription = "Launch status: $text"
        }
    )
}

@PreviewDarkLightMode
@Composable
fun ChipPreview() {
    AppTheme {
        Chip(
            text = "Successful",
            icon = Icons.Filled.CheckCircle,
            contentColor = Color(0xFF4CAF50),
            containerColor = Color(0xFF4CAF50)
        )
    }
}