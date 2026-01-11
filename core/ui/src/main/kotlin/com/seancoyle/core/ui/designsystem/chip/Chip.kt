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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
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
    icon: ImageVector? = null,
    onClick: (() -> Unit)? = null
) {
    AssistChip(
        onClick = onClick ?: {},
        label = {
            AppText.labelMedium(
                text = text,
                fontWeight = FontWeight.SemiBold,
                color = contentColor,
                maxLines = 1,
                isSelectable = false
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
            containerColor = containerColor.copy(alpha = 0.5f),
            labelColor = contentColor,
            leadingIconContentColor = contentColor
        ),
        modifier = modifier.semantics {
            contentDescription = "Launch status: $text"
            // Only set button role if the chip is clickable
            if (onClick != null) {
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
            containerColor = Color(0xFF4CAF50)
        )
    }
}
