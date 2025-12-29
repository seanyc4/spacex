package com.seancoyle.core.ui.designsystem.text

import androidx.annotation.StringRes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import com.seancoyle.core.ui.designsystem.theme.AppTextStyles
import com.seancoyle.core.ui.designsystem.theme.AppTheme

enum class AppText {
    displayLarge,
    displayMedium,
    displaySmall,
    headlineLarge,
    headlineMedium,
    headlineSmall,
    titleLarge,
    titleMedium,
    titleSmall,
    bodyLarge,
    bodyMedium,
    bodySmall,
    labelLarge,
    labelMedium,
    labelSmall;

    @Composable
    operator fun invoke(
        text: String,
        textDecoration: TextDecoration = TextDecoration.None,
        textAlign: TextAlign? = null,
        maxLines: Int = Int.MAX_VALUE,
        color: Color = AppTheme.colors.primary,
        overflow: TextOverflow = TextOverflow.Clip,
        fontSize: TextUnit? = null,
        fontWeight: FontWeight? = null,
        lineHeight: TextUnit? = null,
        onTextLayout: (TextLayoutResult) -> Unit = {},
        modifier: Modifier = Modifier,
    ) {
        val style = asStyle()
        Text(
            modifier = modifier,
            text = text,
            style = style,
            textDecoration = textDecoration,
            textAlign = textAlign,
            maxLines = maxLines,
            color = color,
            overflow = overflow,
            onTextLayout = onTextLayout,
            fontWeight = fontWeight ?: style.fontWeight,
            fontSize = fontSize ?: style.fontSize,
            lineHeight = lineHeight ?: style.lineHeight,
        )
    }

    @Composable
    operator fun invoke(
        @StringRes textRes: Int,
        textDecoration: TextDecoration = TextDecoration.None,
        textAlign: TextAlign? = null,
        maxLines: Int = Int.MAX_VALUE,
        color: Color = AppTheme.colors.primary,
        overflow: TextOverflow = TextOverflow.Clip,
        fontSize: TextUnit? = null,
        fontWeight: FontWeight? = null,
        onTextLayout: (TextLayoutResult) -> Unit = {},
        modifier: Modifier = Modifier,
    ) {
        this(
            text = stringResource(textRes),
            modifier = modifier,
            textDecoration = textDecoration,
            textAlign = textAlign,
            maxLines = maxLines,
            color = color,
            overflow = overflow,
            fontSize = fontSize,
            fontWeight = fontWeight,
            onTextLayout = onTextLayout,
        )
    }

    @Composable
    operator fun invoke(
        annotatedString: AnnotatedString,
        textDecoration: TextDecoration = TextDecoration.None,
        textAlign: TextAlign? = null,
        maxLines: Int = Int.MAX_VALUE,
        color: Color = AppTheme.colors.primary,
        overflow: TextOverflow = TextOverflow.Clip,
        fontSize: TextUnit? = null,
        fontWeight: FontWeight? = null,
        onTextLayout: (TextLayoutResult) -> Unit = {},
        modifier: Modifier = Modifier,
    ) {
        val style = asStyle()
        Text(
            text = annotatedString,
            modifier = modifier,
            style = style,
            textDecoration = textDecoration,
            textAlign = textAlign,
            maxLines = maxLines,
            color = color,
            overflow = overflow,
            onTextLayout = onTextLayout,
            fontWeight = fontWeight ?: style.fontWeight,
            fontSize = fontSize ?: style.fontSize,
        )
    }

    @Composable
    @ReadOnlyComposable
    private fun AppText.asStyle(): TextStyle {
        return when (this) {
            displayLarge -> AppTextStyles.displayLarge
            displayMedium -> AppTextStyles.displayMedium
            displaySmall -> AppTextStyles.displaySmall
            headlineLarge -> AppTextStyles.headlineLarge
            headlineMedium -> AppTextStyles.headlineMedium
            headlineSmall -> AppTextStyles.headlineSmall
            titleLarge -> AppTextStyles.titleLarge
            titleMedium -> AppTextStyles.titleMedium
            titleSmall -> AppTextStyles.titleSmall
            bodyLarge -> AppTextStyles.bodyLarge
            bodyMedium -> AppTextStyles.bodyMedium
            bodySmall -> AppTextStyles.bodySmall
            labelLarge -> AppTextStyles.labelLarge
            labelMedium -> AppTextStyles.labelMedium
            labelSmall -> AppTextStyles.labelSmall
        }
    }
}
