package com.aksharadeepa.tutor.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val GlassBackgroundTop = Color(0xFF0F0C29)
val GlassBackgroundMid = Color(0xFF302B63)
val GlassBackgroundBottom = Color(0xFF24243E)

val GlassBackgroundGradient = Brush.verticalGradient(
	listOf(GlassBackgroundTop, GlassBackgroundMid, GlassBackgroundBottom)
)

val GlassSurface = Color.White.copy(alpha = 0.10f)
val GlassSurfaceStrong = Color.White.copy(alpha = 0.15f)
val GlassBorder = Color.White.copy(alpha = 0.18f)
val GlassBorderStrong = Color.White.copy(alpha = 0.30f)
val GlassDivider = Color.White.copy(alpha = 0.12f)
val GlassTrack = Color.White.copy(alpha = 0.15f)
val GlassNavBar = Color(0xFF1A1A2E).copy(alpha = 0.85f)
val GlassNavBorder = Color.White.copy(alpha = 0.15f)
val GlassSelectedPill = Color.White.copy(alpha = 0.18f)

val TextPrimary = Color.White
val TextSecondary = Color.White.copy(alpha = 0.80f)
val TextMuted = Color.White.copy(alpha = 0.55f)
val TextFaint = Color.White.copy(alpha = 0.45f)

val GradientStart = Color(0xFF7F7FD5)
val GradientEnd = Color(0xFF91EAE4)

val CorrectGreen = Color(0xFF00C9A7)
val WrongRed = Color(0xFFFF6B6B)
val WarningYellow = Color(0xFFF2C94C)
val BadgeGray = Color.White.copy(alpha = 0.25f)

val SurfaceWhite = GlassSurface
val BorderSoft = GlassBorder
val TextGray = TextMuted
val SuccessGreen = CorrectGreen
val ErrorRed = WrongRed

fun subjectAccent(subject: String): Color = when (subject.uppercase()) {
	"SCIENCE" -> GradientStart
	"MATH" -> GradientEnd
	"SOCIAL" -> Color(0xFFFFB347)
	else -> Color(0xFFB06AB3)
}
