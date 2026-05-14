package com.aksharadeepa.tutor.ui.quiz

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aksharadeepa.tutor.ui.theme.CorrectGreen
import com.aksharadeepa.tutor.ui.theme.ErrorRed
import com.aksharadeepa.tutor.ui.theme.GlassBackgroundGradient
import com.aksharadeepa.tutor.ui.theme.GlassBorder
import com.aksharadeepa.tutor.ui.theme.GlassCard
import com.aksharadeepa.tutor.ui.theme.GlassPrimaryButton
import com.aksharadeepa.tutor.ui.theme.GlassSurface
import com.aksharadeepa.tutor.ui.theme.GlassTrack
import com.aksharadeepa.tutor.ui.theme.GradientEnd
import com.aksharadeepa.tutor.ui.theme.TextMuted
import com.aksharadeepa.tutor.ui.theme.TextPrimary
import com.aksharadeepa.tutor.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    onQuizFinished: () -> Unit
) {
    val questions by viewModel.questions.collectAsState()
    val currentIndex by viewModel.currentQuestionIndex.collectAsState()
    val timer by viewModel.timer.collectAsState()
    val quizState by viewModel.quizState.collectAsState()
    val userAnswers by viewModel.userAnswers.collectAsState()

    if (questions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = GradientEnd)
        }
        return
    }

    if (quizState == QuizState.Finished) {
        onQuizFinished()
        return
    }

    if (currentIndex !in questions.indices) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(GlassBackgroundGradient),
            contentAlignment = Alignment.Center
        ) {
            GlassCard(modifier = Modifier.padding(16.dp)) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Question unavailable.", color = TextPrimary, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(12.dp))
                    GlassPrimaryButton(onClick = onQuizFinished) {
                        Text("Back", color = TextPrimary)
                    }
                }
            }
        }
        return
    }

    val question = questions[currentIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GlassBackgroundGradient)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = timer / 30f,
                    color = GradientEnd,
                    trackColor = GlassTrack,
                    strokeWidth = 6.dp,
                    modifier = Modifier.size(48.dp)
                )
                Text("$timer", style = MaterialTheme.typography.labelLarge, color = TextPrimary)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    "Question ${currentIndex + 1}/${questions.size}",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Text("Time left", style = MaterialTheme.typography.bodySmall, color = TextMuted)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Quick Check", style = MaterialTheme.typography.labelLarge, color = TextMuted)
                Text(
                    question.questionText,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
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
            val userAnswer = userAnswers.find { it.questionId == question.id }
            val isSelectedOption = userAnswer?.selectedOption == key

            val containerColor = when {
                isAnswered && isCorrectOption -> CorrectGreen.copy(alpha = 0.25f)
                isAnswered && isSelectedOption -> ErrorRed.copy(alpha = 0.25f)
                else -> GlassSurface
            }
            val borderColor = when {
                isAnswered && isCorrectOption -> CorrectGreen
                isAnswered && isSelectedOption -> ErrorRed
                else -> GlassBorder
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = containerColor),
                border = BorderStroke(1.dp, borderColor),
                onClick = { if (!isAnswered) viewModel.submitAnswer(key) }
            ) {
                Text(
                    "$key. $text",
                    modifier = Modifier.padding(16.dp),
                    color = TextPrimary
                )
            }
        }

        if (quizState is QuizState.Answered) {
            GlassCard(modifier = Modifier.padding(top = 16.dp)) {
                Text(
                    question.explanation,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
            GlassPrimaryButton(
                onClick = { viewModel.nextQuestion() },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(if (currentIndex < questions.size - 1) "Next" else "Finish", color = TextPrimary)
            }
        }
    }
}
