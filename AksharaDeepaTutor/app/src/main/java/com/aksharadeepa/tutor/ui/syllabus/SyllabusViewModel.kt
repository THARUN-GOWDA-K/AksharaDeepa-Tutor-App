package com.aksharadeepa.tutor.ui.syllabus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aksharadeepa.tutor.data.model.Chapter
import com.aksharadeepa.tutor.data.model.DailyProgress
import com.aksharadeepa.tutor.data.repository.ChapterRepository
import com.aksharadeepa.tutor.data.repository.GoalRepository
import com.aksharadeepa.tutor.data.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@HiltViewModel
class SyllabusViewModel @Inject constructor(
    private val chapterRepository: ChapterRepository,
    private val quizRepository: QuizRepository,
    private val goalRepository: GoalRepository
) : ViewModel() {

    private val _selectedSubject = MutableStateFlow("SCIENCE")
    val selectedSubject: StateFlow<String> = _selectedSubject

    val chapters = _selectedSubject
        .flatMapLatest { subject -> chapterRepository.getChaptersBySubject(subject) }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val allChapters = chapterRepository.getAllChapters()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        
    val allAttempts = quizRepository.getAllAttempts()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())



    fun selectSubject(subject: String) {
        _selectedSubject.value = subject
    }

    fun toggleChapterCompletion(chapter: Chapter, isCompleted: Boolean) {
        viewModelScope.launch {
            chapterRepository.updateChapter(chapter.copy(isCompleted = isCompleted))
            updateDailyGoal(isCompleted)
        }
    }

    private suspend fun updateDailyGoal(increment: Boolean) {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val currentProgress = goalRepository.getDailyProgress(today).firstOrNull()
        if (currentProgress != null) {
            val newCount = if (increment) currentProgress.chaptersStudiedCount + 1 else Math.max(0, currentProgress.chaptersStudiedCount - 1)
            goalRepository.updateDailyProgress(currentProgress.copy(
                chaptersStudiedCount = newCount,
                goalMet = newCount >= currentProgress.goalTarget
            ))
        } else {
            if (increment) {
                goalRepository.updateDailyProgress(
                    DailyProgress(
                        date = today,
                        chaptersStudiedCount = 1,
                        goalTarget = 3,
                        goalMet = false
                    )
                )
            }
        }
    }
}
