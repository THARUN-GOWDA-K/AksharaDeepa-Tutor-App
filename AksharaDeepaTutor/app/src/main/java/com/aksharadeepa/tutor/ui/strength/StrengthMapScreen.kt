package com.aksharadeepa.tutor.ui.strength

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aksharadeepa.tutor.ui.theme.CorrectGreen
import com.aksharadeepa.tutor.ui.theme.GlassBackgroundGradient
import com.aksharadeepa.tutor.ui.theme.GlassBorder
import com.aksharadeepa.tutor.ui.theme.GlassCard
import com.aksharadeepa.tutor.ui.theme.GlassPrimaryButton
import com.aksharadeepa.tutor.ui.theme.GlassSurfaceStrong
import com.aksharadeepa.tutor.ui.theme.GradientEnd
import com.aksharadeepa.tutor.ui.theme.GradientStart
import com.aksharadeepa.tutor.ui.theme.BadgeGray
import com.aksharadeepa.tutor.ui.theme.WarningYellow
import com.aksharadeepa.tutor.ui.theme.TextMuted
import com.aksharadeepa.tutor.ui.theme.TextPrimary
import com.aksharadeepa.tutor.ui.theme.TextSecondary
import com.aksharadeepa.tutor.ui.theme.ErrorRed
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StrengthMapScreen(viewModel: StrengthMapViewModel = hiltViewModel()) {
    val uiState by viewModel.strengthUiState.collectAsState()
    val aiResponse by viewModel.aiResponse.collectAsState()
    val isLoadingAi by viewModel.isLoadingAi.collectAsState()
    val chapters by viewModel.allChapters.collectAsState()
    val attempts by viewModel.allAttempts.collectAsState()

    var showBottomSheet by remember { mutableStateOf(false) }
    var userQuestion by remember { mutableStateOf("") }

    Scaffold(
        containerColor = androidx.compose.ui.graphics.Color.Transparent,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showBottomSheet = true },
                containerColor = GlassSurfaceStrong
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = "Ask AI", tint = TextPrimary)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(GlassBackgroundGradient)
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Strength Map", style = MaterialTheme.typography.displayLarge, color = TextPrimary)
            Text(
                "See where you shine and what to revisit.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))
            val hasAnyData = uiState.subjects.any { it.hasAttempts || it.completionPercent > 0 }
            if (hasAnyData) {
                RadarChart(
                    performanceScores = uiState.performanceScores,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
            } else {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("No analytics yet", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Take a quiz or mark chapters complete to build your strength map.", color = TextSecondary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                uiState.subjects.forEach { subjectStrength ->
                    GlassCard(modifier = Modifier.weight(1f)) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                displaySubject(subjectStrength.subject),
                                style = MaterialTheme.typography.labelLarge,
                                color = TextMuted
                            )
                            Text(
                                if (subjectStrength.hasAttempts) {
                                    "Quiz ${subjectStrength.quizPercent}%"
                                } else {
                                    "Not attempted"
                                },
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary
                            )
                            Text(
                                "Coverage ${subjectStrength.completionPercent}%",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.subjects.size) { index ->
                    val subject = uiState.subjects[index]
                    val subjectChapters = chapters.filter { it.subject == subject.subject }

                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                displaySubject(subject.subject),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "Quiz performance: ${subject.quizPercent}%",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                            Text(
                                "Syllabus coverage: ${subject.completionPercent}%",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                            if (!subject.hasAttempts) {
                                Text(
                                    "Not attempted yet.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextMuted
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            subjectChapters.forEach { chapter ->
                                val lastAtt = attempts.filter { it.chapterId == chapter.id }
                                    .maxByOrNull { it.attemptedAt }
                                val pct = if (lastAtt != null && lastAtt.totalQuestions > 0) {
                                    (lastAtt.score * 100) / lastAtt.totalQuestions
                                } else {
                                    -1
                                }
                                val badgeColor = when {
                                    pct >= 80 -> CorrectGreen
                                    pct in 60..79 -> WarningYellow
                                    pct in 0..59 -> ErrorRed
                                    else -> BadgeGray
                                }
                                val status = when {
                                    pct >= 80 -> "Strong"
                                    pct in 60..79 -> "Developing"
                                    pct in 0..59 -> "Needs work"
                                    else -> "Not attempted"
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(chapter.chapterName, color = TextPrimary)
                                        if (chapter.isCompleted) {
                                            Text("Completed", color = TextMuted, style = MaterialTheme.typography.labelSmall)
                                        }
                                    }
                                    Box(
                                        modifier = Modifier
                                            .background(badgeColor.copy(alpha = 0.25f), shape = RoundedCornerShape(8.dp))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(status, color = TextPrimary, style = MaterialTheme.typography.labelSmall)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false }
        ) {
            Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                Text("AI Study Coach", style = MaterialTheme.typography.titleLarge, color = TextPrimary)
                OutlinedTextField(
                    value = userQuestion,
                    onValueChange = { userQuestion = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    label = { Text("Ask about your progress", color = TextMuted) },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = GlassBorder,
                        unfocusedBorderColor = GlassBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        cursorColor = TextPrimary
                    )
                )
                GlassPrimaryButton(
                    onClick = { viewModel.askAi(userQuestion) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ask", color = TextPrimary)
                }

                if (isLoadingAi) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Thinking...", color = TextSecondary)
                } else if (aiResponse != null) {
                    GlassCard(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                        Text(
                            aiResponse.orEmpty(),
                            modifier = Modifier.padding(16.dp),
                            color = TextSecondary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun RadarChart(
    performanceScores: List<Float>,
    modifier: Modifier = Modifier
) {
    val angles = listOf(-PI / 2, PI / 6, 5 * PI / 6)
    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            val textPadding = 40.dp.toPx()
            val radius = (min(size.width, size.height) / 2) - textPadding

            val gridColor = TextPrimary.copy(alpha = 0.15f)
            val levels = 4
            for (i in 1..levels) {
                drawCircle(
                    color = gridColor,
                    radius = radius * (i / levels.toFloat()),
                    center = center,
                    style = Stroke(width = 1f)
                )
            }

            angles.forEach { angle ->
                drawLine(
                    color = gridColor,
                    start = center,
                    end = Offset(
                        center.x + radius * cos(angle).toFloat(),
                        center.y + radius * sin(angle).toFloat()
                    ),
                    strokeWidth = 2f
                )
            }

            val scores = if (performanceScores.size >= 3) performanceScores else listOf(0f, 0f, 0f)
            val path = Path()
            angles.forEachIndexed { index, angle ->
                val scoreRadius = radius * scores[index].coerceIn(0f, 1f)
                val point = Offset(
                    center.x + scoreRadius * cos(angle).toFloat(),
                    center.y + scoreRadius * sin(angle).toFloat()
                )
                if (index == 0) path.moveTo(point.x, point.y) else path.lineTo(point.x, point.y)
            }
            path.close()
            drawPath(path, color = GradientStart.copy(alpha = 0.40f))
            drawPath(path, color = GradientEnd, style = Stroke(width = 2f))
        }

        Text(
            "Science",
            modifier = Modifier.align(Alignment.TopCenter),
            color = TextPrimary,
            fontSize = 12.sp
        )
        Text(
            "Math",
            modifier = Modifier.align(Alignment.BottomEnd).padding(end = 8.dp),
            color = TextPrimary,
            fontSize = 12.sp
        )
        Text(
            "Social",
            modifier = Modifier.align(Alignment.BottomStart).padding(start = 8.dp),
            color = TextPrimary,
            fontSize = 12.sp
        )
    }
}

private fun displaySubject(subject: String): String = when (subject.uppercase()) {
    "SCIENCE" -> "Science"
    "MATH" -> "Mathematics"
    "SOCIAL" -> "Social Studies"
    else -> subject
}
