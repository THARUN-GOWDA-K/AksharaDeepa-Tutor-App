package com.aksharadeepa.tutor.ui.strength

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
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

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showBottomSheet = true }, containerColor = SoftSage) {
                Icon(Icons.Default.AutoAwesome, contentDescription = "Ask AI", tint = Color.White)
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text("Strength Map", style = MaterialTheme.typography.headlineMedium)

            Canvas(modifier = Modifier.fillMaxWidth().height(250.dp).padding(16.dp)) {
                val center = Offset(size.width / 2, size.height / 2)
                val radius = min(size.width, size.height) / 2
                
                val angles = listOf(-Math.PI / 2, Math.PI / 6, 5 * Math.PI / 6)
                angles.forEach { angle ->
                    drawLine(
                        color = Color.LightGray,
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
                    drawPath(path, color = DeepOlive.copy(alpha = 0.5f))
                    drawPath(path, color = DeepOlive, style = Stroke(width = 4f))
                }
            }
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                Text("Science: ${(subjectScores[0]*100).toInt()}%")
                Text("Math: ${(subjectScores[1]*100).toInt()}%")
                Text("Social: ${(subjectScores[2]*100).toInt()}%")
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(subjects.size) { index ->
                    val sub = subjects[index]
                    val subChapters = chapters.filter { it.subject == sub }
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(sub, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
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
                                    Badge(containerColor = badgeColor) { Text(status, modifier = Modifier.padding(4.dp)) }
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
                    Text("Your Personal Study Coach", style = MaterialTheme.typography.titleLarge)
                    OutlinedTextField(
                        value = userQuestion,
                        onValueChange = { userQuestion = it },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        label = { Text("Ask a question about your progress") }
                    )
                    Button(
                        onClick = { viewModel.askAi(userQuestion, chapters, attempts) },
                        modifier = Modifier.fillMaxWidth()
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
