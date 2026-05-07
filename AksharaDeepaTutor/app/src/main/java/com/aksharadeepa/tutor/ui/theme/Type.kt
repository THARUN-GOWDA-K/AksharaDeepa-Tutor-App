package com.aksharadeepa.tutor.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val DisplayFont = FontFamily.Serif
private val BodyFont = FontFamily.Serif

val Typography = Typography(
	displayLarge = TextStyle(
		fontFamily = DisplayFont,
		fontWeight = FontWeight.Bold,
		fontSize = 34.sp,
		lineHeight = 40.sp
	),
	headlineMedium = TextStyle(
		fontFamily = DisplayFont,
		fontWeight = FontWeight.SemiBold,
		fontSize = 24.sp,
		lineHeight = 30.sp
	),
	titleLarge = TextStyle(
		fontFamily = DisplayFont,
		fontWeight = FontWeight.SemiBold,
		fontSize = 20.sp,
		lineHeight = 26.sp
	),
	titleMedium = TextStyle(
		fontFamily = BodyFont,
		fontWeight = FontWeight.Medium,
		fontSize = 16.sp,
		lineHeight = 22.sp
	),
	bodyLarge = TextStyle(
		fontFamily = BodyFont,
		fontWeight = FontWeight.Normal,
		fontSize = 16.sp,
		lineHeight = 24.sp
	),
	bodyMedium = TextStyle(
		fontFamily = BodyFont,
		fontWeight = FontWeight.Normal,
		fontSize = 14.sp,
		lineHeight = 20.sp
	),
	labelLarge = TextStyle(
		fontFamily = BodyFont,
		fontWeight = FontWeight.SemiBold,
		fontSize = 12.sp,
		letterSpacing = 0.5.sp
	)
)
