package com.seancoyle.core.ui.designsystem.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import com.seancoyle.core.ui.designsystem.theme.Dimens.cornerRadiusXSmall
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode

@Composable
fun TertiaryButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    Surface(
        modifier = modifier
            .semantics {
                contentDescription = text
                role = Role.Button
            }
            .background(
                color = AppTheme.colors.inversePrimary,
                shape = RoundedCornerShape(
                    topEnd = 0.dp,
                    topStart = cornerRadiusXSmall,
                    bottomEnd = 0.dp,
                    bottomStart = 0.dp
                )
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        color = Color.Transparent,
        shape = RoundedCornerShape(
            topEnd = 0.dp,
            topStart = cornerRadiusXSmall,
            bottomEnd = 0.dp,
            bottomStart = 0.dp
        ),
        shadowElevation = 0.dp
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
            AppText.labelSmall(
                text = text,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@PreviewDarkLightMode
@Composable
private fun Preview() {
    TertiaryButton(
        text = "Open in Maps",
        icon = Icons.Default.Place,
        onClick = { }
    )
}
