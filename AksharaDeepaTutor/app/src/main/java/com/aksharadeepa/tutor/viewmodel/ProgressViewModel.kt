package com.aksharadeepa.tutor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aksharadeepa.tutor.data.local.entities.Chapter
import com.aksharadeepa.tutor.data.local.entities.QuizAttempt
import com.aksharadeepa.tutor.data.repository.ChapterRepository
import com.aksharadeepa.tutor.data.repository.QuizRepository
import com.aksharadeepa.tutor.models.SubjectProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class ProgressViewModel @Inject constructor(
    chapterRepository: ChapterRepository,
    quizRepository: QuizRepository
) : ViewModel() {

    val subjectProgress: StateFlow<List<SubjectProgress>> = combine(
        chapterRepository.getAllChapters(),
        quizRepository.getAllAttempts()
    ) { chapters, attempts ->
        listOf("SCIENCE", "MATH", "SOCIAL").map { subject ->
            buildSubjectProgress(subject, chapters, attempts)
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private fun buildSubjectProgress(
        subject: String,
        chapters: List<Chapter>,
        attempts: List<QuizAttempt>
    ): SubjectProgress {
        val subjectChapters = chapters.filter { it.subject == subject }
        val completed = subjectChapters.count { it.isCompleted }
        val total = subjectChapters.size

        val subjectAttempts = attempts.filter { attempt -> subjectChapters.any { it.id == attempt.chapterId } }
        val totalScore = subjectAttempts.sumOf { it.score }
        val totalQuestions = subjectAttempts.sumOf { it.totalQuestions }
        val quizPercent = if (totalQuestions > 0) {
            ((totalScore.toFloat() / totalQuestions) * 100f).roundToInt()
        } else {
            0
        }

        return SubjectProgress(
            subject = subject,
            completedChapters = completed,
            totalChapters = total,
            quizScorePercent = quizPercent
        )
    }
}
