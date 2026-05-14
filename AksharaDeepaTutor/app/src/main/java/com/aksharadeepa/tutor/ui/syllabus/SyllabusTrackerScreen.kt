package com.aksharadeepa.tutor.ui.syllabus

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aksharadeepa.tutor.ui.theme.GlassBackgroundGradient
import com.aksharadeepa.tutor.ui.theme.GlassBorder
import com.aksharadeepa.tutor.ui.theme.GlassCard
import com.aksharadeepa.tutor.ui.theme.GlassLinearProgress
import com.aksharadeepa.tutor.ui.theme.GlassPrimaryButton
import com.aksharadeepa.tutor.ui.theme.GlassSurfaceStrong
import com.aksharadeepa.tutor.ui.theme.TextMuted
import com.aksharadeepa.tutor.ui.theme.TextPrimary
import com.aksharadeepa.tutor.ui.theme.TextSecondary
import com.aksharadeepa.tutor.ui.theme.subjectAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SyllabusTrackerScreen(viewModel: SyllabusViewModel = hiltViewModel()) {
    val selectedSubject by viewModel.selectedSubject.collectAsState()
    val chapters by viewModel.chapters.collectAsState()
    val allChapters by viewModel.allChapters.collectAsState()
    val allAttempts by viewModel.allAttempts.collectAsState()

    val subjects = listOf("SCIENCE", "MATH", "SOCIAL")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GlassBackgroundGradient)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Syllabus Tracker", style = MaterialTheme.typography.displayLarge, color = TextPrimary)
            Text(
                "Track chapter completion and keep your progress visible.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }

        val completedCount = allChapters.count { it.isCompleted }
        val totalCount = allChapters.size
        val progress = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f

        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Overall Progress", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                    Spacer(modifier = Modifier.weight(1f))
                    Text("${(progress * 100).toInt()}%", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                }
                GlassLinearProgress(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
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
                    label = {
                        Text(
                            displaySubject(subject),
                            color = if (selectedSubject == subject) TextPrimary else TextMuted
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = GlassSurfaceStrong,
                        selectedLabelColor = TextPrimary,
                        containerColor = androidx.compose.ui.graphics.Color.Transparent,
                        labelColor = TextMuted
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = GlassBorder,
                        selectedBorderColor = GlassBorder
                    )
                )
            }
        }

        val subjectChapters = chapters
        val subjectCompleted = subjectChapters.count { it.isCompleted }
        val subjectProgress = if (subjectChapters.isNotEmpty()) {
            subjectCompleted.toFloat() / subjectChapters.size
        } else {
            0f
        }

        GlassLinearProgress(
            progress = subjectProgress,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(subjectChapters) { chapter ->
                val attempt = allAttempts.filter { it.chapterId == chapter.id }.maxByOrNull { it.attemptedAt }
                val scoreText = if (attempt != null) {
                    "${(attempt.score * 100) / attempt.totalQuestions}%"
                } else {
                    "Not attempted"
                }

                GlassCard(modifier = Modifier.fillMaxWidth()) {
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
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                                Text(
                                    displaySubject(chapter.subject),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = subjectAccent(chapter.subject)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                scoreText,
                                style = MaterialTheme.typography.labelLarge,
                                color = TextSecondary
                            )
                        }

                        if (chapter.importantConcepts.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Key Concepts", style = MaterialTheme.typography.labelLarge, color = TextMuted)
                            Text(chapter.importantConcepts, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        GlassPrimaryButton(
                            onClick = { viewModel.toggleChapterCompletion(chapter, !chapter.isCompleted) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                if (chapter.isCompleted) "Completed" else "Mark as Complete",
                                color = TextPrimary
                            )
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
