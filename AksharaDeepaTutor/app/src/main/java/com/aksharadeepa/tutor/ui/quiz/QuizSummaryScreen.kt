package com.aksharadeepa.tutor.ui.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aksharadeepa.tutor.ui.theme.*

@Composable
fun QuizSummaryScreen(
    viewModel: QuizViewModel,
    onBackToChapters: () -> Unit
) {
    val chapter by viewModel.currentChapter.collectAsState()
    val questions by viewModel.questions.collectAsState()
    val userAnswers by viewModel.userAnswers.collectAsState()
    val aiTips by viewModel.aiTips.collectAsState()
    val isLoadingAi by viewModel.isLoadingAi.collectAsState()

    val score = userAnswers.count { it.isCorrect }
    val total = questions.size

    val headerBrush = Brush.verticalGradient(listOf(SoftSage, SageMist))

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(headerBrush)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Quiz Summary", style = MaterialTheme.typography.displayLarge)
            Text("Chapter: ${chapter?.chapterName ?: ""}", style = MaterialTheme.typography.bodyMedium, color = TextGray)
        }

        item {
            Card(colors = CardDefaults.cardColors(containerColor = SurfaceWhite)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Your Score", style = MaterialTheme.typography.labelLarge, color = AccentTeal)
                    Text("$score / $total", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                }
            }
        }

        item {
            if (aiTips != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("AI Study Tips", style = MaterialTheme.typography.titleMedium)
                        Text(aiTips ?: "", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            } else {
                Button(
                    onClick = { viewModel.getAiStudyTips() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentTeal)
                ) {
                    if (isLoadingAi) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Get AI Study Tips")
                    }
                }
            }
        }

        item {
            Text("Review Answers", style = MaterialTheme.typography.titleLarge)
        }

        items(questions.size) { index ->
            val question = questions[index]
            val answer = userAnswers.find { it.questionId == question.id }
            val isCorrect = answer?.isCorrect == true
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Q: ${question.questionText}", style = MaterialTheme.typography.bodyLarge)
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

                    val statusColor = if (isCorrect) SuccessGreen else ErrorRed
                    val statusLabel = if (isCorrect) "Correct" else "Needs Review"

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Your Answer: ${answer?.selectedOption ?: "-"} ($selectedText)",
                            color = statusColor
                        )
                        Box(
                            modifier = Modifier.background(statusColor, shape = RoundedCornerShape(6.dp)).padding(6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(statusLabel, color = SurfaceWhite, style = MaterialTheme.typography.labelSmall)
                        }
                    }
                    if (!isCorrect) {
                        Text("Correct Answer: ${question.correctOption} ($correctText)", color = SuccessGreen)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Explanation: ${question.explanation}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onBackToChapters,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = DeepOlive)
            ) {
                Text("Back to Chapters")
            }
        }
    }
}
