package com.aksharadeepa.tutor.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
    primary = GradientStart,
    onPrimary = TextPrimary,
    secondary = GradientEnd,
    onSecondary = TextPrimary,
    tertiary = CorrectGreen,
    onTertiary = TextPrimary,
    background = GlassBackgroundBottom,
    onBackground = TextPrimary,
    surface = GlassBackgroundBottom,
    onSurface = TextPrimary,
    surfaceVariant = GlassSurface,
    onSurfaceVariant = TextSecondary,
    error = WrongRed,
    onError = TextPrimary
)

private val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun AksharaDeepaTutorTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalRippleTheme provides GlassRippleTheme) {
        MaterialTheme(
            colorScheme = DarkColorScheme,
            typography = Typography,
            shapes = AppShapes,
            content = content
        )
    }
}

private object GlassRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor(): Color = TextPrimary

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleAlpha(
        draggedAlpha = 0.12f,
        focusedAlpha = 0.18f,
        hoveredAlpha = 0.10f,
        pressedAlpha = 0.22f
    )
}
