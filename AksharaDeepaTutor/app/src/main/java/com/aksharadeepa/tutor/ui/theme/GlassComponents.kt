package com.aksharadeepa.tutor.ui.theme

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = GlassSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, GlassBorder)
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlassPrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    val alpha = if (enabled) 1f else 0.35f
    Surface(
        modifier = modifier.alpha(alpha),
        onClick = onClick,
        enabled = enabled,
        color = GlassSurfaceStrong,
        contentColor = TextPrimary,
        shape = RoundedCornerShape(50),
        border = BorderStroke(1.dp, GlassBorderStrong)
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                content = content
            )
        }
    }
}

@Composable
fun GlassTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        singleLine = singleLine,
        placeholder = {
            androidx.compose.material3.Text(text = placeholder, color = TextMuted)
        },
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = GlassSurface,
            unfocusedContainerColor = GlassSurface,
            disabledContainerColor = GlassSurface,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            focusedIndicatorColor = GlassBorderStrong,
            unfocusedIndicatorColor = GlassBorder,
            cursorColor = TextPrimary
        ),
        shape = RoundedCornerShape(18.dp)
    )
}

@Composable
fun GlassLinearProgress(
    progress: Float,
    modifier: Modifier = Modifier
) {
    val clamped = progress.coerceIn(0f, 1f)
    Box(
        modifier = modifier
            .height(8.dp)
            .clip(RoundedCornerShape(50))
            .background(GlassTrack)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(clamped)
                .height(8.dp)
                .background(Brush.horizontalGradient(listOf(GradientStart, GradientEnd)))
        )
    }
}

@Composable
fun GlassShimmerCard(
    modifier: Modifier = Modifier,
    height: Dp = 120.dp
) {
    val transition = rememberInfiniteTransition(label = "")
    val shimmer by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    val brush = Brush.linearGradient(
        colors = listOf(
            GlassSurface,
            TextPrimary.copy(alpha = 0.25f),
            GlassSurface
        ),
        start = Offset(0f, 0f),
        end = Offset(600f * shimmer, 600f * shimmer)
    )

    GlassCard(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .background(brush)
        )
    }
}

@Composable
fun GlassIconPill(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        modifier = modifier,
        color = GlassSelectedPill,
        contentColor = TextPrimary,
        shape = RoundedCornerShape(50),
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            content = content
        )
    }
}
