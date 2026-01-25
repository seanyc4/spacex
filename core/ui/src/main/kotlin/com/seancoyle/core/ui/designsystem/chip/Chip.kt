package com.seancoyle.core.ui.designsystem.chip

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
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
    icon: ImageVector? = null,
    onClick: (() -> Unit)? = null,
    accessibilityLabel: String? = null
) {
    val isClickable = onClick != null
    val chipDescription = accessibilityLabel ?: text

    AssistChip(
        onClick = onClick ?: {},
        label = {
            AppText.labelMedium(
                text = text,
                fontWeight = FontWeight.SemiBold,
                color = contentColor,
                maxLines = 1,
                
            )
        },
        leadingIcon = {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null, // Decorative - part of the chip's meaning
                    modifier = Modifier.size(16.dp)
                )
            }
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = containerColor.copy(alpha = 0.5f),
            labelColor = contentColor,
            leadingIconContentColor = contentColor
        ),
        modifier = modifier
            .clearAndSetSemantics {
                contentDescription = chipDescription
                if (isClickable) {
                    role = Role.Button
                }
            },
        border = BorderStroke(0.25.dp, AppTheme.colors.onSurfaceVariant)
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
            containerColor = Color(0xFF4CAF50),
            accessibilityLabel = "Launch status: Successful"
        )
    }
}
