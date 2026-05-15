package com.aksharadeepa.tutor.ui.syllabus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aksharadeepa.tutor.data.local.entities.Chapter
import com.aksharadeepa.tutor.data.repository.AuthRepository
import com.aksharadeepa.tutor.data.repository.ChapterRepository
import com.aksharadeepa.tutor.data.repository.ProgressRepository
import com.aksharadeepa.tutor.data.repository.QuizRepository
import com.aksharadeepa.tutor.data.remote.dto.ProgressUpdateRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
@HiltViewModel
class SyllabusViewModel @Inject constructor(
    private val chapterRepository: ChapterRepository,
    private val quizRepository: QuizRepository,
    private val progressRepository: ProgressRepository,
    private val authRepository: AuthRepository
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
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val completionDate = if (isCompleted) today else null
            
            val updatedChapter = chapter.copy(
                isCompleted = isCompleted,
                completionDate = completionDate
            )
            chapterRepository.updateChapter(updatedChapter)
            
            val updatedChaptersList = allChapters.value.map { 
                if (it.id == chapter.id) updatedChapter else it 
            }
            syncProgressForSubject(chapter.subject, updatedChaptersList)
        }
    }

    private suspend fun syncProgressForSubject(subject: String, chapters: List<Chapter>) {
        val userId = authRepository.currentUserId() ?: return
        val subjectChapters = chapters.filter { it.subject == subject }
        val completedChapters = subjectChapters.filter { it.isCompleted }
        val completedIds = completedChapters.map { it.id }
        val completionDates = completedChapters.mapNotNull { chapter ->
            chapter.completionDate?.let { chapter.id.toString() to it }
        }.toMap()

        val request = ProgressUpdateRequest(
            userId = userId,
            subject = subject,
            completedChapters = completedChapters.size,
            totalChapters = subjectChapters.size,
            updatedAt = System.currentTimeMillis(),
            completedChapterIds = completedIds,
            completionDates = completionDates
        )

        progressRepository.syncProgress(request)
    }
}
