package com.aksharadeepa.tutor.ui.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import com.aksharadeepa.tutor.ui.theme.ErrorRed
import com.aksharadeepa.tutor.ui.theme.SuccessGreen

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

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Text("Quiz Complete!", style = MaterialTheme.typography.headlineMedium)
            Text("Chapter: ${chapter?.chapterName}")
            Text("Score: $score / $total", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            if (aiTips != null) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("✨ AI Study Tips", style = MaterialTheme.typography.titleMedium)
                        Text(aiTips ?: "")
                    }
                }
            } else {
                Button(onClick = { viewModel.getAiStudyTips() }, modifier = Modifier.fillMaxWidth()) {
                    if (isLoadingAi) CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                    else Text("✨ Get AI Study Tips")
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text("Review Answers:", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(questions.size) { index ->
            val question = questions[index]
            val answer = userAnswers.find { it.questionId == question.id }
            val isCorrect = answer?.isCorrect == true
            
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCorrect) SuccessGreen.copy(alpha = 0.1f) else ErrorRed.copy(alpha = 0.1f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Q: ${question.questionText}", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Display the actual text of the options instead of just A/B/C/D
                    val selectedText = when(answer?.selectedOption) {
                        "A" -> question.optionA
                        "B" -> question.optionB
                        "C" -> question.optionC
                        "D" -> question.optionD
                        else -> "None"
                    }
                    val correctText = when(question.correctOption) {
                        "A" -> question.optionA
                        "B" -> question.optionB
                        "C" -> question.optionC
                        "D" -> question.optionD
                        else -> question.correctOption
                    }
                    
                    Text("Your Answer: ${answer?.selectedOption ?: "-"} ($selectedText)", color = if (isCorrect) SuccessGreen else ErrorRed)
                    if (!isCorrect) {
                        Text("Correct Answer: ${question.correctOption} ($correctText)", color = SuccessGreen)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Explanation: ${question.explanation}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onBackToChapters, modifier = Modifier.fillMaxWidth()) {
                Text("Back to Chapters")
            }
        }
    }
}
