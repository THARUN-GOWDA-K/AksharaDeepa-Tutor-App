package com.aksharadeepa.tutor.ui.goal

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aksharadeepa.tutor.ui.theme.*
import com.aksharadeepa.tutor.data.model.Chapter

@Composable
fun DailyGoalScreen(viewModel: GoalViewModel = hiltViewModel(), onTakeQuiz: (Chapter) -> Unit = {}) {
    val dailyProgress by viewModel.dailyProgress.collectAsState()
    val streakData by viewModel.streakData.collectAsState()
    val recommendedChapters by viewModel.recommendedChapters.collectAsState()

    val goalTarget = dailyProgress?.goalTarget ?: 3
    val currentCount = dailyProgress?.chaptersStudiedCount ?: 0
    val isMet = dailyProgress?.goalMet == true
    val progressRatio = if (goalTarget > 0) currentCount.toFloat() / goalTarget else 0f

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isMet) 1.2f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    val headerBrush = Brush.verticalGradient(listOf(SoftSage, SageMist))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(headerBrush)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Daily Goal", style = MaterialTheme.typography.displayLarge)
        Text("Small steps, big mastery.", style = MaterialTheme.typography.bodyMedium, color = TextGray)

        Card(modifier = Modifier.padding(vertical = 16.dp).fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = SurfaceWhite)) {
            Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.Center) {
                Text("🔥 ${streakData?.currentStreak ?: 0} Day Streak", style = MaterialTheme.typography.titleLarge, color = DeepOlive)
            }
        }

        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(200.dp)) {
            CircularProgressIndicator(
                progress = progressRatio,
                modifier = Modifier.fillMaxSize(),
                strokeWidth = 16.dp,
                color = if (isMet) SuccessGreen else AccentTeal,
                trackColor = BorderSoft
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.scale(scale)) {
                Text("$currentCount / $goalTarget", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text("Chapters Today", style = MaterialTheme.typography.bodyMedium, color = TextGray)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (isMet) {
            Text("Goal Met!", style = MaterialTheme.typography.titleLarge, color = SuccessGreen, modifier = Modifier.scale(scale))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = SurfaceWhite)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Set Daily Goal", style = MaterialTheme.typography.titleMedium)
                Slider(
                    value = goalTarget.toFloat(),
                    onValueChange = { viewModel.setGoalTarget(it.toInt()) },
                    valueRange = 1f..10f,
                    steps = 8,
                    colors = SliderDefaults.colors(activeTrackColor = AccentTeal, thumbColor = AccentTeal)
                )
            }
        }

        if (recommendedChapters.isNotEmpty()) {
            Spacer(modifier = Modifier.height(24.dp))
            Text("Recommended Topics Today", style = MaterialTheme.typography.titleMedium)

            LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(recommendedChapters) { chapter ->
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = SurfaceWhite)) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(displaySubject(chapter.subject), style = MaterialTheme.typography.labelLarge, color = subjectAccent(chapter.subject))
                                Text(chapter.chapterName, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                            }
                            Button(
                                onClick = { onTakeQuiz(chapter) },
                                colors = ButtonDefaults.buttonColors(containerColor = subjectAccent(chapter.subject))
                            ) {
                                Text("Take Quiz")
                            }
                        }
                    }
                }
            }
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }

        Text(
            "\"Success is the sum of small efforts, repeated day in and day out.\"",
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
    }
}

private fun displaySubject(subject: String): String = when (subject.uppercase()) {
    "SCIENCE" -> "Science"
    "MATH" -> "Mathematics"
    "SOCIAL" -> "Social Studies"
    else -> subject
}
