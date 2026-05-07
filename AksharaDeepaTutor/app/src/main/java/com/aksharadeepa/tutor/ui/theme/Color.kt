package com.aksharadeepa.tutor.ui.theme

import androidx.compose.ui.graphics.Color

// Core palette
val DeepOlive = Color(0xFF1A2517)
val PrimaryDark = Color(0xFF1A2517)
val SoftSage = Color(0xFFACC8A2)
val SageMist = Color(0xFFF2F6F1)
val SurfaceCream = Color(0xFFF8FAF6)
val InkDark = Color(0xFF1C251C)
val InkLight = Color(0xFF2B3A2B)
val BorderSoft = Color(0xFFE1E9DD)

// Accents
val AccentTeal = Color(0xFF2BAE9A)
val AccentBlue = Color(0xFF5A8DEE)
val AccentAmber = Color(0xFFF2B94B)
val AccentCoral = Color(0xFFEF7B6C)
val AccentLavender = Color(0xFFB59BF3)
val AccentMint = Color(0xFF6DD6A0)

// Semantic colors
val BackgroundLight = SageMist
val SurfaceWhite = SurfaceCream
val TextDark = InkDark
val TextGray = Color(0xFF5F6C5F)
val SuccessGreen = Color(0xFF41B97A)
val ErrorRed = Color(0xFFE05A5A)
val WarningYellow = Color(0xFFF0B429)
val GrayBadge = Color(0xFF9CA89C)

fun subjectAccent(subject: String): Color = when (subject.uppercase()) {
	"SCIENCE" -> AccentTeal
	"MATH" -> AccentBlue
	"SOCIAL" -> AccentAmber
	else -> AccentCoral
}
