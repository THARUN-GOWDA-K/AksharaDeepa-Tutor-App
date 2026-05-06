package com.aksharadeepa.tutor.ui.syllabus

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    Column(modifier = Modifier.fillMaxSize()) {
        val completedCount = allChapters.count { it.isCompleted }
        val totalCount = allChapters.size
        val progress = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f

        Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Overall Progress", style = MaterialTheme.typography.titleMedium)
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    color = SuccessGreen
                )
                Text("${(progress * 100).toInt()}% Completed", modifier = Modifier.padding(top = 4.dp))
            }
        }

        TabRow(selectedTabIndex = subjects.indexOf(selectedSubject)) {
            subjects.forEachIndexed { index, subject ->
                Tab(
                    selected = selectedSubject == subject,
                    onClick = { viewModel.selectSubject(subject) },
                    text = { Text(subject) }
                )
            }
        }

        val subjectChapters = chapters
        val subjectCompleted = subjectChapters.count { it.isCompleted }
        val subjectProgress = if (subjectChapters.isNotEmpty()) subjectCompleted.toFloat() / subjectChapters.size else 0f

        LinearProgressIndicator(
            progress = subjectProgress,
            modifier = Modifier.fillMaxWidth().height(4.dp),
            color = PrimaryBlue
        )

        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            items(subjectChapters) { chapter ->
                val attempt = allAttempts.filter { it.chapterId == chapter.id }.maxByOrNull { it.attemptedAt }
                val scoreText = if (attempt != null) "${(attempt.score * 100) / attempt.totalQuestions}%" else "Not attempted"

                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (chapter.isCompleted) SuccessGreen.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = chapter.isCompleted,
                            onCheckedChange = { viewModel.toggleChapterCompletion(chapter, it) }
                        )
                        Column(modifier = Modifier.padding(start = 8.dp)) {
                            Text(chapter.chapterName, style = MaterialTheme.typography.bodyLarge)
                            Badge(containerColor = PrimaryBlue) {
                                Text(scoreText, color = SurfaceWhite, modifier = Modifier.padding(4.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
