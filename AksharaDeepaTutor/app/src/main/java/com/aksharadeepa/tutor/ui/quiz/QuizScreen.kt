package com.aksharadeepa.tutor.ui.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aksharadeepa.tutor.ui.theme.*

@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    onQuizFinished: () -> Unit
) {
    val questions by viewModel.questions.collectAsState()
    val currentIndex by viewModel.currentQuestionIndex.collectAsState()
    val timer by viewModel.timer.collectAsState()
    val quizState by viewModel.quizState.collectAsState()

    if (questions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = SoftSage)
        }
        return
    }

    if (quizState == QuizState.Finished) {
        onQuizFinished()
        return
    }

    val question = questions[currentIndex]

    val headerBrush = Brush.verticalGradient(listOf(SoftSage, SageMist))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(headerBrush)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = timer / 30f,
                    color = AccentTeal,
                    trackColor = BorderSoft,
                    strokeWidth = 6.dp,
                    modifier = Modifier.size(48.dp)
                )
                Text("$timer", style = MaterialTheme.typography.labelLarge)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("Question ${currentIndex + 1}/${questions.size}", style = MaterialTheme.typography.titleMedium)
                Text("Time left", style = MaterialTheme.typography.bodySmall, color = TextGray)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(colors = CardDefaults.cardColors(containerColor = SurfaceWhite)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Quick Check", style = MaterialTheme.typography.labelLarge, color = AccentTeal)
                Text(
                    question.questionText,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        val options = listOf(
            "A" to question.optionA,
            "B" to question.optionB,
            "C" to question.optionC,
            "D" to question.optionD
        )

        options.forEach { (key, text) ->
            val isAnswered = quizState is QuizState.Answered
            val isCorrectOption = key == question.correctOption

            val userAnswer = viewModel.userAnswers.value.find { it.questionId == question.id }
            val isSelectedOption = userAnswer?.selectedOption == key

            val containerColor = if (isAnswered) {
                when {
                    isCorrectOption -> SuccessGreen
                    isSelectedOption -> ErrorRed
                    else -> SurfaceWhite
                }
            } else SurfaceWhite

            val contentColor = if (isAnswered && (isCorrectOption || isSelectedOption)) Color.White else InkDark

            OutlinedButton(
                onClick = { if (!isAnswered) viewModel.submitAnswer(key) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = containerColor,
                    contentColor = contentColor
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSoft)
            ) {
                Text("$key. $text")
            }
        }

        if (quizState is QuizState.Answered) {
            Card(
                modifier = Modifier.padding(top = 16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite)
            ) {
                Text(
                    question.explanation,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Button(
                onClick = { viewModel.nextQuestion() },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = AccentTeal)
            ) {
                Text(if (currentIndex < questions.size - 1) "Next" else "Finish")
            }
        }
    }
}
