package com.aksharadeepa.tutor.ui.splash

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.School
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aksharadeepa.tutor.ui.theme.GlassBackgroundGradient
import com.aksharadeepa.tutor.ui.theme.TextPrimary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onFinished: () -> Unit,
    @DrawableRes logoResId: Int? = null
) {
    val alpha by animateFloatAsState(targetValue = 1f, label = "")

    LaunchedEffect(Unit) {
        delay(1200)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GlassBackgroundGradient),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.alpha(alpha)
        ) {
            if (logoResId != null) {
                Icon(
                    painter = painterResource(logoResId),
                    contentDescription = null,
                    tint = TextPrimary,
                    modifier = Modifier.size(120.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Rounded.School,
                    contentDescription = null,
                    tint = TextPrimary,
                    modifier = Modifier.size(96.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Akshara Deepa",
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Tutor",
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary
            )
        }
    }
}
