package com.aksharadeepa.tutor.ui.strength

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aksharadeepa.tutor.ui.theme.*
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StrengthMapScreen(viewModel: StrengthViewModel = hiltViewModel()) {
    val chapters by viewModel.allChapters.collectAsState()
    val attempts by viewModel.allAttempts.collectAsState()
    val aiResponse by viewModel.aiResponse.collectAsState()
    val isLoadingAi by viewModel.isLoadingAi.collectAsState()

    var showBottomSheet by remember { mutableStateOf(false) }
    var userQuestion by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val subjects = listOf("SCIENCE", "MATH", "SOCIAL")
    val subjectScores = subjects.map { sub ->
        val subChapters = chapters.filter { it.subject == sub }
        val subAttempts = attempts.filter { att -> subChapters.any { it.id == att.chapterId } }
        val totalScore = subAttempts.sumOf { it.score }
        val maxScore = subAttempts.sumOf { it.totalQuestions }
        if (maxScore > 0) (totalScore.toFloat() / maxScore) else 0f
    }

    val headerBrush = Brush.verticalGradient(listOf(SoftSage, SageMist))

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showBottomSheet = true }, containerColor = AccentTeal) {
                Icon(Icons.Default.AutoAwesome, contentDescription = "Ask AI", tint = Color.White)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(headerBrush)
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Strength Map", style = MaterialTheme.typography.displayLarge)
            Text("See where you shine and what to revisit.", style = MaterialTheme.typography.bodyMedium, color = TextGray)

            Canvas(modifier = Modifier.fillMaxWidth().height(250.dp).padding(16.dp)) {
                val center = Offset(size.width / 2, size.height / 2)
                val radius = min(size.width, size.height) / 2

                val angles = listOf(-Math.PI / 2, Math.PI / 6, 5 * Math.PI / 6)
                angles.forEach { angle ->
                    drawLine(
                        color = BorderSoft,
                        start = center,
                        end = Offset(
                            center.x + radius * cos(angle).toFloat(),
                            center.y + radius * sin(angle).toFloat()
                        ),
                        strokeWidth = 2f
                    )
                }

                if (subjectScores.any { it > 0 }) {
                    val path = Path()
                    angles.forEachIndexed { index, angle ->
                        val scoreRadius = radius * subjectScores[index]
                        val point = Offset(
                            center.x + scoreRadius * cos(angle).toFloat(),
                            center.y + scoreRadius * sin(angle).toFloat()
                        )
                        if (index == 0) path.moveTo(point.x, point.y)
                        else path.lineTo(point.x, point.y)
                    }
                    path.close()
                    drawPath(path, color = AccentTeal.copy(alpha = 0.3f))
                    drawPath(path, color = AccentTeal, style = Stroke(width = 4f))
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatCard("Science", subjectScores[0], Modifier.weight(1f))
                StatCard("Math", subjectScores[1], Modifier.weight(1f))
                StatCard("Social", subjectScores[2], Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(subjects.size) { index ->
                    val sub = subjects[index]
                    val subChapters = chapters.filter { it.subject == sub }
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = SurfaceWhite)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(displaySubject(sub), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            subChapters.forEach { ch ->
                                val lastAtt = attempts.filter { it.chapterId == ch.id }.maxByOrNull { it.attemptedAt }
                                val pct = if (lastAtt != null) (lastAtt.score * 100) / lastAtt.totalQuestions else -1
                                val badgeColor = when {
                                    pct >= 80 -> SuccessGreen
                                    pct in 60..79 -> WarningYellow
                                    pct in 0..59 -> ErrorRed
                                    else -> GrayBadge
                                }
                                val status = when {
                                    pct >= 80 -> "Strong"
                                    pct in 60..79 -> "Developing"
                                    pct in 0..59 -> "Needs Work"
                                    else -> "Not attempted"
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(ch.chapterName, modifier = Modifier.weight(1f))
                                    Badge(containerColor = badgeColor) { Text(status, modifier = Modifier.padding(4.dp), color = SurfaceWhite) }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState
            ) {
                Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                    Text("AI Study Coach", style = MaterialTheme.typography.titleLarge)
                    OutlinedTextField(
                        value = userQuestion,
                        onValueChange = { userQuestion = it },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        label = { Text("Ask about your progress") }
                    )
                    Button(
                        onClick = { viewModel.askAi(userQuestion, chapters, attempts) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = AccentTeal)
                    ) {
                        Text("Ask")
                    }
                    if (isLoadingAi) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp))
                    } else if (aiResponse != null) {
                        Card(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                            Text(aiResponse ?: "", modifier = Modifier.padding(16.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
private fun StatCard(label: String, score: Float, modifier: Modifier = Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = SurfaceWhite)) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, style = MaterialTheme.typography.labelLarge, color = TextGray)
            Text("${(score * 100).toInt()}%", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
    }
}

private fun displaySubject(subject: String): String = when (subject.uppercase()) {
    "SCIENCE" -> "Science"
    "MATH" -> "Mathematics"
    "SOCIAL" -> "Social Studies"
    else -> subject
}
