package com.aksharadeepa.tutor.ui.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aksharadeepa.tutor.data.model.Chapter
import com.aksharadeepa.tutor.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizModeScreen(
    viewModel: QuizViewModel = hiltViewModel(),
    onStartQuiz: (Chapter) -> Unit
) {
    val chapters by viewModel.allChapters.collectAsState()
    val attempts by viewModel.allAttempts.collectAsState()

    val subjects = listOf("SCIENCE", "MATH", "SOCIAL")
    var selectedSubject by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("SCIENCE") }

    val headerBrush = Brush.verticalGradient(listOf(SoftSage, SageMist))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(headerBrush)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Quiz Mode", style = MaterialTheme.typography.displayLarge)
            Text(
                "Daily 5-question quizzes for each chapter.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextGray
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            subjects.forEach { subject ->
                FilterChip(
                    selected = selectedSubject == subject,
                    onClick = { selectedSubject = subject },
                    label = { Text(displaySubject(subject)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = subjectAccent(subject),
                        selectedLabelColor = SurfaceWhite
                    )
                )
            }
        }

        val filteredChapters = chapters.filter { it.subject == selectedSubject }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredChapters) { chapter ->
                val attempt = attempts.filter { it.chapterId == chapter.id }.maxByOrNull { it.attemptedAt }
                val scoreText = if (attempt != null) "Last Score: ${(attempt.score * 100) / attempt.totalQuestions}%" else "No attempts yet"

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(chapter.chapterName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                            Text(scoreText, style = MaterialTheme.typography.bodyMedium, color = TextGray)
                        }
                        Button(
                            onClick = {
                                viewModel.startQuiz(chapter)
                                onStartQuiz(chapter)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = subjectAccent(chapter.subject))
                        ) {
                            Text("Start")
                        }
                    }
                }
            }
        }
    }
}

private fun displaySubject(subject: String): String = when (subject.uppercase()) {
    "SCIENCE" -> "Science"
    "MATH" -> "Mathematics"
    "SOCIAL" -> "Social Studies"
    else -> subject
}
