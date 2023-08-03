package com.fang.taipeitour.ui.component

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.MultiParagraph
import androidx.compose.ui.text.MultiParagraphIntrinsics
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.isUnspecified

@Composable
internal fun AutoSizeText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    targetFontSize: TextUnit = TextUnit.Unspecified,
    minFontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
) {
    BoxWithConstraints {
        var shrunkFontSize = targetFontSize
        val calculateIntrinsics = @Composable {
            MultiParagraph(
                intrinsics = MultiParagraphIntrinsics(
                    annotatedString = AnnotatedString(text),
                    style = TextStyle(
                        color = color,
                        fontSize = shrunkFontSize,
                        fontWeight = fontWeight,
                        textAlign = textAlign,
                        lineHeight = lineHeight,
                        fontFamily = fontFamily,
                        textDecoration = textDecoration,
                        fontStyle = fontStyle,
                        letterSpacing = letterSpacing
                    ),
                    density = LocalDensity.current,
                    fontFamilyResolver = LocalFontFamilyResolver.current,
                    placeholders = listOf()
                ),
                constraints = Constraints(maxWidth = constraints.maxWidth),
                maxLines = Int.MAX_VALUE,
                ellipsis = false
            )
        }

        var paragraph = calculateIntrinsics()
        var canShrink = minFontSize.isUnspecified || shrunkFontSize > minFontSize
        var shouldShrink = paragraph.height > constraints.maxHeight || paragraph.width > constraints.maxWidth || paragraph.lineCount > maxLines
        while (canShrink && shouldShrink) {
            shrunkFontSize *= 0.9
            paragraph = calculateIntrinsics()
            canShrink = minFontSize.isUnspecified || shrunkFontSize > minFontSize
            shouldShrink = paragraph.height > constraints.maxHeight || paragraph.width > constraints.maxWidth || paragraph.lineCount > maxLines
        }
        Text(
            text = text,
            modifier = modifier,
            color = color,
            fontSize = shrunkFontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            letterSpacing = letterSpacing,
            textDecoration = textDecoration,
            textAlign = textAlign,
            lineHeight = lineHeight,
            onTextLayout = onTextLayout,
            style = style,
            maxLines = maxLines,
            overflow = overflow,
        )
    }
}
