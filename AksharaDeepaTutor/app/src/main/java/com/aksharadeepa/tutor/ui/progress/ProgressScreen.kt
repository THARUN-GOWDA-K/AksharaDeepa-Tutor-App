package com.aksharadeepa.tutor.ui.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aksharadeepa.tutor.models.SubjectProgress
import com.aksharadeepa.tutor.ui.theme.GlassBackgroundGradient
import com.aksharadeepa.tutor.ui.theme.GlassCard
import com.aksharadeepa.tutor.ui.theme.GlassLinearProgress
import com.aksharadeepa.tutor.ui.theme.TextPrimary
import com.aksharadeepa.tutor.ui.theme.TextSecondary
import com.aksharadeepa.tutor.ui.theme.subjectAccent
import com.aksharadeepa.tutor.viewmodel.ProgressViewModel

@Composable
fun ProgressScreen(viewModel: ProgressViewModel = hiltViewModel()) {
    val subjects by viewModel.subjectProgress.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GlassBackgroundGradient)
            .padding(16.dp)
    ) {
        Text("Progress", style = MaterialTheme.typography.displayLarge, color = TextPrimary)
        Text("Track syllabus coverage and quiz accuracy.", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(subjects) { subject ->
                SubjectProgressCard(subject)
            }
        }
    }
}

@Composable
private fun SubjectProgressCard(progress: SubjectProgress) {
    val completion = if (progress.totalChapters > 0) {
        progress.completedChapters.toFloat() / progress.totalChapters
    } else {
        0f
    }

    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                progress.subject.lowercase().replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.titleLarge,
                color = subjectAccent(progress.subject),
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Coverage: ${progress.completedChapters}/${progress.totalChapters}",
                color = TextSecondary
            )
            GlassLinearProgress(progress = completion, modifier = Modifier.fillMaxWidth().padding(top = 6.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text("Quiz accuracy: ${progress.quizScorePercent}%", color = TextSecondary)
        }
    }
}
