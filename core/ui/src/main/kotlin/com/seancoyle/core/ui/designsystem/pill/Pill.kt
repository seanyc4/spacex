package com.seancoyle.core.ui.designsystem.pill

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppColors
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode

@Composable
fun Pill(
    modifier: Modifier = Modifier,
    text: String,
    color: Color,
    onClick: () -> Unit = {}
) {
    AssistChip(
        label = {
            AppText.bodySmall(
                text = text,
                color = AppColors.White,
                isUppercase = true,
                fontWeight = FontWeight.ExtraBold,
                isSelectable = false
            )
        },
        colors = AssistChipDefaults.assistChipColors(containerColor = color),
        border = AssistChipDefaults.assistChipBorder(
            enabled = true,
            borderColor = AppColors.Black,
            borderWidth = 0.5.dp
        ),
        onClick = onClick,
        shape = RoundedCornerShape(50),
        modifier = modifier
            .height(24.dp)
    )
}

@PreviewDarkLightMode
@Composable
fun PillPreview() {
    AppTheme {
        Pill(
            text = "Go",
            color = AppColors.Green
        )
    }
}