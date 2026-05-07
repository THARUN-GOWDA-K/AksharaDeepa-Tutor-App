package com.aksharadeepa.tutor.ui.syllabus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aksharadeepa.tutor.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SyllabusTrackerScreen(viewModel: SyllabusViewModel = hiltViewModel()) {
    val selectedSubject by viewModel.selectedSubject.collectAsState()
    val chapters by viewModel.chapters.collectAsState()
    val allChapters by viewModel.allChapters.collectAsState()
    val allAttempts by viewModel.allAttempts.collectAsState()

    val subjects = listOf("SCIENCE", "MATH", "SOCIAL")
    val headerBrush = Brush.verticalGradient(listOf(SoftSage, SageMist))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(headerBrush)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Syllabus Tracker", style = MaterialTheme.typography.displayLarge)
            Text(
                "Track chapter completion and keep your progress visible.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextGray
            )
        }

        val completedCount = allChapters.count { it.isCompleted }
        val totalCount = allChapters.size
        val progress = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Overall Progress", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.weight(1f))
                    Text("${(progress * 100).toInt()}%", style = MaterialTheme.typography.titleMedium)
                }
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    color = SuccessGreen,
                    trackColor = BorderSoft
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            subjects.forEach { subject ->
                FilterChip(
                    selected = selectedSubject == subject,
                    onClick = { viewModel.selectSubject(subject) },
                    label = { Text(displaySubject(subject)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = subjectAccent(subject),
                        selectedLabelColor = SurfaceWhite
                    )
                )
            }
        }

        val subjectChapters = chapters
        val subjectCompleted = subjectChapters.count { it.isCompleted }
        val subjectProgress = if (subjectChapters.isNotEmpty()) subjectCompleted.toFloat() / subjectChapters.size else 0f
        val subjectColor = subjectAccent(selectedSubject)

        LinearProgressIndicator(
            progress = subjectProgress,
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
                .padding(horizontal = 16.dp),
            color = subjectColor,
            trackColor = BorderSoft
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(subjectChapters) { chapter ->
                val attempt = allAttempts.filter { it.chapterId == chapter.id }.maxByOrNull { it.attemptedAt }
                val scoreText = if (attempt != null) "${(attempt.score * 100) / attempt.totalQuestions}%" else "Not attempted"

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    chapter.chapterName,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    displaySubject(chapter.subject),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = subjectAccent(chapter.subject)
                                )
                            }
                            Badge(containerColor = subjectAccent(chapter.subject)) {
                                Text(scoreText, color = SurfaceWhite, modifier = Modifier.padding(6.dp))
                            }
                        }

                        if (chapter.importantConcepts.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Key Concepts", style = MaterialTheme.typography.labelLarge, color = TextGray)
                            Text(chapter.importantConcepts, style = MaterialTheme.typography.bodyMedium)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { viewModel.toggleChapterCompletion(chapter, !chapter.isCompleted) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (chapter.isCompleted) BorderSoft else subjectAccent(chapter.subject),
                                contentColor = if (chapter.isCompleted) InkLight else SurfaceWhite
                            )
                        ) {
                            Text(if (chapter.isCompleted) "Completed" else "Mark as Complete")
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
