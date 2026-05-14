package com.aksharadeepa.tutor.ui.goal

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aksharadeepa.tutor.data.local.entities.Chapter
import com.aksharadeepa.tutor.ui.quiz.QuizViewModel
import com.aksharadeepa.tutor.ui.theme.GlassBackgroundGradient
import com.aksharadeepa.tutor.ui.theme.GlassBorder
import com.aksharadeepa.tutor.ui.theme.GlassCard
import com.aksharadeepa.tutor.ui.theme.GlassPrimaryButton
import com.aksharadeepa.tutor.ui.theme.GlassTrack
import com.aksharadeepa.tutor.ui.theme.GradientEnd
import com.aksharadeepa.tutor.ui.theme.GradientStart
import com.aksharadeepa.tutor.ui.theme.TextMuted
import com.aksharadeepa.tutor.ui.theme.TextPrimary
import com.aksharadeepa.tutor.ui.theme.TextSecondary
import com.aksharadeepa.tutor.ui.theme.subjectAccent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyGoalScreen(
    viewModel: DailyGoalViewModel = hiltViewModel(),
    quizViewModel: QuizViewModel? = null,
    onTakeQuiz: suspend (Chapter) -> Unit = {}
) {
    val dailyProgress by viewModel.dailyProgress.collectAsState()
    val streakData by viewModel.streakData.collectAsState()
    val chaptersCompletedToday by viewModel.chaptersCompletedToday.collectAsState()
    val recommendedChapters by viewModel.recommendedChapters.collectAsState()

    val goalTarget = dailyProgress?.goalTarget ?: 3
    val currentCount = chaptersCompletedToday.size
    val isMet = currentCount >= goalTarget
    val progressRatio = if (goalTarget > 0) currentCount.toFloat() / goalTarget else 0f

    val snackbarHostState = remember { SnackbarHostState() }

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isMet) 1.08f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    val scope = rememberCoroutineScope()

    LaunchedEffect(quizViewModel) {
        quizViewModel?.uiEvents?.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        containerColor = androidx.compose.ui.graphics.Color.Transparent,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(GlassBackgroundGradient)
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text("Daily Goal", style = MaterialTheme.typography.displayLarge, color = TextPrimary)
                Text("Small steps, big mastery.", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
            }

            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "🔥 ${streakData?.currentStreak ?: 0} day streak",
                            style = MaterialTheme.typography.titleLarge,
                            color = TextPrimary
                        )
                    }
                }
            }

            item {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(220.dp)) {
                    GlassCircularProgress(
                        progress = progressRatio,
                        modifier = Modifier.fillMaxSize()
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.scale(scale)) {
                        Text(
                            "$currentCount / $goalTarget",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text("Chapters today", style = MaterialTheme.typography.bodyMedium, color = TextMuted)
                    }
                    if (isMet) {
                        SparkleBurst(modifier = Modifier.fillMaxSize())
                    }
                }
            }

            item {
                if (isMet) {
                    Text(
                        "Goal met!",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary,
                        modifier = Modifier.scale(scale)
                    )
                }
            }

            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Chapters studied today", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                        Spacer(modifier = Modifier.height(8.dp))
                        if (chaptersCompletedToday.isEmpty()) {
                            Text("No chapters completed today yet.", color = TextSecondary)
                        } else {
                            chaptersCompletedToday.forEach { chapter ->
                                Text("• ${chapter.chapterName}", color = TextSecondary)
                            }
                        }
                    }
                }
            }

            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Set Daily Goal", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                        Slider(
                            value = goalTarget.toFloat(),
                            onValueChange = { viewModel.setGoalTarget(it.toInt()) },
                            valueRange = 1f..10f,
                            steps = 8,
                            colors = SliderDefaults.colors(
                                activeTrackColor = GradientEnd,
                                inactiveTrackColor = GlassTrack,
                                thumbColor = GradientEnd
                            )
                        )
                    }
                }
            }

            if (recommendedChapters.isNotEmpty()) {
                item {
                    Text("Recommended topics today", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                }

                items(recommendedChapters.size) { index ->
                    val chapter = recommendedChapters[index]
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    displaySubject(chapter.subject),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = subjectAccent(chapter.subject)
                                )
                                Text(
                                    chapter.chapterName,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            GlassPrimaryButton(
                                onClick = {
                                    scope.launch { onTakeQuiz(chapter) }
                                },
                                modifier = Modifier.width(120.dp)
                            ) {
                                Text("Take Quiz", color = TextPrimary)
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    "\"Success is the sum of small efforts, repeated day in and day out.\"",
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp),
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
private fun GlassCircularProgress(
    progress: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val stroke = 16.dp.toPx()
        drawArc(
            color = GlassTrack,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )
        drawArc(
            brush = Brush.sweepGradient(listOf(GradientStart, GradientEnd)),
            startAngle = -90f,
            sweepAngle = 360f * progress.coerceIn(0f, 1f),
            useCenter = false,
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )
    }
}

@Composable
private fun SparkleBurst(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "")
    val radius by transition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )
    val alpha by transition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2, size.height / 2)
        val maxRadius = size.minDimension / 2 * radius
        val sparkleColor = TextPrimary.copy(alpha = alpha)
        val offsets = listOf(
            Offset(0f, -maxRadius * 0.7f),
            Offset(maxRadius * 0.7f, 0f),
            Offset(0f, maxRadius * 0.7f),
            Offset(-maxRadius * 0.7f, 0f)
        )
        offsets.forEach { offset ->
            drawCircle(
                color = sparkleColor,
                radius = maxRadius * 0.12f,
                center = center + offset
            )
        }
    }
}

private fun displaySubject(subject: String): String = when (subject.uppercase()) {
    "SCIENCE" -> "Science"
    "MATH" -> "Mathematics"
    "SOCIAL" -> "Social Studies"
    else -> subject
}
