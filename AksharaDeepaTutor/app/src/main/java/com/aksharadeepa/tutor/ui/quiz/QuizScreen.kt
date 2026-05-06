package com.aksharadeepa.tutor.ui.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aksharadeepa.tutor.ui.theme.ErrorRed
import com.aksharadeepa.tutor.ui.theme.SuccessGreen

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
        Text("Loading...")
        return
    }

    if (quizState == QuizState.Finished) {
        onQuizFinished()
        return
    }

    val question = questions[currentIndex]

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LinearProgressIndicator(
            progress = timer / 30f,
            modifier = Modifier.fillMaxWidth()
        )
        Text("Time Left: $timer s", modifier = Modifier.padding(top = 8.dp))

        Text("Question ${currentIndex + 1}/${questions.size}", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
        Text(question.questionText, style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(top = 16.dp))

        Spacer(modifier = Modifier.height(24.dp))

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

            val buttonColor = if (isAnswered) {
                if (isCorrectOption) SuccessGreen
                else if (isSelectedOption) ErrorRed
                else MaterialTheme.colorScheme.surfaceVariant
            } else MaterialTheme.colorScheme.surfaceVariant

            Button(
                onClick = { if (!isAnswered) viewModel.submitAnswer(key) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
            ) {
                Text("$key: $text", color = if (isAnswered) Color.White else MaterialTheme.colorScheme.onSurface)
            }
        }

        if (quizState is QuizState.Answered) {
            Text(question.explanation, modifier = Modifier.padding(top = 16.dp))
            Button(onClick = { viewModel.nextQuestion() }, modifier = Modifier.padding(top = 16.dp).fillMaxWidth()) {
                Text(if (currentIndex < questions.size - 1) "Next" else "Finish")
            }
        }
    }
}
