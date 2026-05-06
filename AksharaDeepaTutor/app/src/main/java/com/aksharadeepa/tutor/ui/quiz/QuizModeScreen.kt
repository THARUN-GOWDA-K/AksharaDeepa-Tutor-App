package com.aksharadeepa.tutor.ui.quiz

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
import com.aksharadeepa.tutor.data.model.Chapter

@Composable
fun QuizModeScreen(
    viewModel: QuizViewModel = hiltViewModel(),
    onStartQuiz: (Chapter) -> Unit
) {
    val chapters by viewModel.allChapters.collectAsState()
    val attempts by viewModel.allAttempts.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(chapters) { chapter ->
            val attempt = attempts.filter { it.chapterId == chapter.id }.maxByOrNull { it.attemptedAt }
            val scoreText = if (attempt != null) "Last Score: ${(attempt.score * 100) / attempt.totalQuestions}%" else "No attempts"

            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(chapter.chapterName, style = MaterialTheme.typography.bodyLarge)
                        Text(scoreText, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Button(onClick = {
                        viewModel.startQuiz(chapter)
                        onStartQuiz(chapter)
                    }) {
                        Text("Start Quiz")
                    }
                }
            }
        }
    }
}
