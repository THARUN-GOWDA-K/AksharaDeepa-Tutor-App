package com.aksharadeepa.tutor.ui.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Quiz Complete!", style = MaterialTheme.typography.headlineMedium)
        Text("Chapter: ${chapter?.chapterName}")
        Text("Score: $score / $total", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(24.dp))

        if (aiTips != null) {
            Card {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("✨ AI Study Tips", style = MaterialTheme.typography.titleMedium)
                    Text(aiTips ?: "")
                }
            }
        } else {
            Button(onClick = { viewModel.getAiStudyTips() }) {
                if (isLoadingAi) CircularProgressIndicator(modifier = Modifier.size(24.dp))
                else Text("✨ Get AI Study Tips")
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(onClick = onBackToChapters, modifier = Modifier.fillMaxWidth()) {
            Text("Back to Chapters")
        }
    }
}
