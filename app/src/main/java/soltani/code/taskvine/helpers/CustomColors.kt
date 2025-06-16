package soltani.code.taskvine.helpers

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class CustomColors(
    val primaryText: Color,
    val secondaryText: Color,
    val thirdText: Color,
    val primaryBackground: Color,
    val SecondaryBackground: Color,
    val cardBackground: Color,
    val highlight: Color
)

val LocalCustomColors = staticCompositionLocalOf {
    CustomColors(
        primaryText = Color.Unspecified,
        secondaryText = Color.Unspecified,
        thirdText = Color.Unspecified,
        primaryBackground = Color.Unspecified,
        SecondaryBackground = Color.Unspecified,
        cardBackground = Color.Unspecified,
        highlight = Color.Unspecified
    )
}