package com.aksharadeepa.tutor.ui.quiz

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aksharadeepa.tutor.ui.theme.CorrectGreen
import com.aksharadeepa.tutor.ui.theme.ErrorRed
import com.aksharadeepa.tutor.ui.theme.GlassBackgroundGradient
import com.aksharadeepa.tutor.ui.theme.GlassCard
import com.aksharadeepa.tutor.ui.theme.GlassPrimaryButton
import com.aksharadeepa.tutor.ui.theme.GlassShimmerCard
import com.aksharadeepa.tutor.ui.theme.TextMuted
import com.aksharadeepa.tutor.ui.theme.TextPrimary
import com.aksharadeepa.tutor.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizSummaryScreen(
    viewModel: QuizViewModel,
    onBackToChapters: () -> Unit
) {
    val chapter by viewModel.currentChapter.collectAsState()
    val questions by viewModel.questions.collectAsState()
    val userAnswers by viewModel.userAnswers.collectAsState()
    val aiTipsUiState by viewModel.aiTipsUiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val score = userAnswers.count { it.isCorrect }
    val total = questions.size

    LaunchedEffect(viewModel) {
        viewModel.uiEvents.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        containerColor = androidx.compose.ui.graphics.Color.Transparent,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(GlassBackgroundGradient)
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        "Quiz Summary",
                        style = MaterialTheme.typography.displayLarge,
                        color = TextPrimary
                    )
                    Text(
                        "Chapter: ${chapter?.chapterName ?: ""}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }

                item {
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Your Score",
                                style = MaterialTheme.typography.labelLarge,
                                color = TextMuted
                            )
                            Text(
                                "$score / $total",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                    }
                }

                item {
                    when (val state = aiTipsUiState) {
                        is AiTipsUiState.Loading -> {
                            GlassShimmerCard(modifier = Modifier.fillMaxWidth(), height = 110.dp)
                        }
                        is AiTipsUiState.Success -> {
                            GlassCard(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        "AI Study Tips",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        state.tips,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextSecondary
                                    )
                                }
                            }
                        }
                        is AiTipsUiState.Error -> {
                            GlassCard(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        "AI Study Tips",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        state.message,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextSecondary
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    GlassPrimaryButton(
                                        onClick = { viewModel.getAiStudyTips() },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Try Again", color = TextPrimary)
                                    }
                                }
                            }
                        }
                        AiTipsUiState.Idle -> {
                            GlassPrimaryButton(
                                onClick = { viewModel.getAiStudyTips() },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Get AI Study Tips", color = TextPrimary)
                            }
                        }
                    }
                }

                item {
                    Text(
                        "Review Answers",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary
                    )
                }

                items(questions.size) { index ->
                    val question = questions[index]
                    val answer = userAnswers.find { it.questionId == question.id }
                    val isCorrect = answer?.isCorrect == true

                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Q: ${question.questionText}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            val selectedText = when (answer?.selectedOption) {
                                "A" -> question.optionA
                                "B" -> question.optionB
                                "C" -> question.optionC
                                "D" -> question.optionD
                                else -> "None"
                            }
                            val correctText = when (question.correctOption) {
                                "A" -> question.optionA
                                "B" -> question.optionB
                                "C" -> question.optionC
                                "D" -> question.optionD
                                else -> question.correctOption
                            }

                            val statusColor = if (isCorrect) CorrectGreen else ErrorRed
                            val statusLabel = if (isCorrect) "Correct" else "Needs Review"

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Your Answer: ${answer?.selectedOption ?: "-"} ($selectedText)",
                                    color = TextSecondary
                                )
                                Box(
                                    modifier = Modifier
                                        .background(statusColor.copy(alpha = 0.25f), shape = RoundedCornerShape(6.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        statusLabel,
                                        color = TextPrimary,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                            if (!isCorrect) {
                                Text(
                                    "Correct Answer: ${question.correctOption} ($correctText)",
                                    color = TextSecondary
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "Explanation: ${question.explanation}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    GlassPrimaryButton(
                        onClick = onBackToChapters,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Back to Chapters", color = TextPrimary)
                    }
                }
            }
        }
    }
}
