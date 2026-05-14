package com.aksharadeepa.tutor.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aksharadeepa.tutor.ui.theme.GlassBackgroundGradient
import com.aksharadeepa.tutor.ui.theme.GlassCard
import com.aksharadeepa.tutor.ui.theme.GlassPrimaryButton
import com.aksharadeepa.tutor.ui.theme.TextMuted
import com.aksharadeepa.tutor.ui.theme.TextPrimary
import com.aksharadeepa.tutor.ui.theme.TextSecondary

@Composable
fun HomeScreen(
    onStartQuiz: () -> Unit,
    onViewSyllabus: () -> Unit,
    onViewProgress: () -> Unit,
    onViewGoals: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GlassBackgroundGradient)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text("Good evening", style = MaterialTheme.typography.headlineMedium, color = TextPrimary)
            Text("Ready for your next session?", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        }

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Daily Focus", style = MaterialTheme.typography.titleLarge, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Finish 2 chapters and take 1 quiz today.", color = TextSecondary)
                Spacer(modifier = Modifier.height(12.dp))
                GlassPrimaryButton(onClick = onViewGoals) {
                    Text("View Daily Goals", color = TextPrimary)
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            GlassCard(modifier = Modifier.weight(1f)) {
                Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = TextPrimary)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Syllabus", color = TextPrimary, style = MaterialTheme.typography.titleMedium)
                    Text("Track chapters", color = TextMuted, style = MaterialTheme.typography.bodySmall)
                }
            }
            GlassCard(modifier = Modifier.weight(1f)) {
                Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Lightbulb, contentDescription = null, tint = TextPrimary)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Quiz", color = TextPrimary, style = MaterialTheme.typography.titleMedium)
                    Text("5-question sets", color = TextMuted, style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = TextPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("AI Coach", color = TextPrimary, style = MaterialTheme.typography.titleMedium)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Get tips after each quiz and see your weakest topics.", color = TextSecondary)
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            GlassPrimaryButton(onClick = onStartQuiz, modifier = Modifier.weight(1f)) {
                Text("Start Quiz", color = TextPrimary)
            }
            GlassPrimaryButton(onClick = onViewSyllabus, modifier = Modifier.weight(1f)) {
                Text("View Syllabus", color = TextPrimary)
            }
        }

        GlassPrimaryButton(onClick = onViewProgress, modifier = Modifier.fillMaxWidth()) {
            Text("Progress Analytics", color = TextPrimary)
        }
    }
}
