package com.aksharadeepa.tutor.ui.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aksharadeepa.tutor.data.local.entities.Chapter
import com.aksharadeepa.tutor.ui.theme.GlassBackgroundGradient
import com.aksharadeepa.tutor.ui.theme.GlassBorder
import com.aksharadeepa.tutor.ui.theme.GlassCard
import com.aksharadeepa.tutor.ui.theme.GlassPrimaryButton
import com.aksharadeepa.tutor.ui.theme.GlassSurfaceStrong
import com.aksharadeepa.tutor.ui.theme.TextMuted
import com.aksharadeepa.tutor.ui.theme.TextPrimary
import com.aksharadeepa.tutor.ui.theme.TextSecondary
import com.aksharadeepa.tutor.ui.theme.subjectAccent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizModeScreen(
    viewModel: QuizViewModel = hiltViewModel(),
    onStartQuiz: (Chapter) -> Unit
) {
    val chapters by viewModel.allChapters.collectAsState()
    val attempts by viewModel.allAttempts.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val subjects = listOf("SCIENCE", "MATH", "SOCIAL")
    var selectedSubject by remember { mutableStateOf("SCIENCE") }

    LaunchedEffect(viewModel) {
        viewModel.uiEvents.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        containerColor = androidx.compose.ui.graphics.Color.Transparent,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(GlassBackgroundGradient)
                .padding(padding)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Quiz Mode",
                    style = MaterialTheme.typography.displayLarge,
                    color = TextPrimary
                )
                Text(
                    "Daily 5-question quizzes for each chapter.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
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

            val filteredChapters = chapters.filter { it.subject == selectedSubject }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredChapters) { chapter ->
                    val attempt = attempts.filter { it.chapterId == chapter.id }.maxByOrNull { it.attemptedAt }
                    val scoreText = if (attempt != null) {
                        "Last Score: ${(attempt.score * 100) / attempt.totalQuestions}%"
                    } else {
                        "No attempts yet"
                    }

                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
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
                                    scoreText,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextSecondary
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            GlassPrimaryButton(
                                onClick = {
                                    scope.launch {
                                        val started = viewModel.startQuiz(chapter)
                                        if (started) {
                                            onStartQuiz(chapter)
                                        }
                                    }
                                },
                                modifier = Modifier.width(110.dp)
                            ) {
                                Text("Start", color = TextPrimary)
                            }
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
