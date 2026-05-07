package com.aksharadeepa.tutor.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape

private val LightColorScheme = lightColorScheme(
    primary = DeepOlive,
    onPrimary = SurfaceCream,
    secondary = SoftSage,
    onSecondary = DeepOlive,
    tertiary = AccentTeal,
    onTertiary = SurfaceCream,
    background = SageMist,
    onBackground = InkDark,
    surface = SurfaceCream,
    onSurface = InkDark,
    surfaceVariant = BorderSoft,
    onSurfaceVariant = InkLight,
    error = ErrorRed,
    onError = SurfaceCream
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
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        shapes = AppShapes,
        content = content
    )
}
