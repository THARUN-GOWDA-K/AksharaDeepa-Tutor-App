package com.aksharadeepa.tutor.ui.goal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aksharadeepa.tutor.data.model.DailyProgress
import com.aksharadeepa.tutor.data.model.StreakData
import com.aksharadeepa.tutor.data.repository.ChapterRepository
import com.aksharadeepa.tutor.data.repository.GoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class GoalViewModel @Inject constructor(
    private val goalRepository: GoalRepository,
    private val chapterRepository: ChapterRepository
) : ViewModel() {

    private val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    val dailyProgress = goalRepository.getDailyProgress(todayStr)
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val streakData = goalRepository.getStreakData()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val completedChaptersToday = chapterRepository.getAllChapters()
        .map { list -> list.filter { it.isCompleted }.size }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)
        
    val recommendedChapters = chapterRepository.getAllChapters()
        .map { chapters ->
            val science = chapters.firstOrNull { it.subject == "SCIENCE" && !it.isCompleted }
            val math = chapters.firstOrNull { it.subject == "MATH" && !it.isCompleted }
            val social = chapters.firstOrNull { it.subject == "SOCIAL" && !it.isCompleted }
            listOfNotNull(science, math, social)
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        
    init {
        viewModelScope.launch {
            val progress = goalRepository.getDailyProgress(todayStr).firstOrNull()
            if (progress == null) {
                // Check if streak is broken
                val streak = goalRepository.getStreakData().firstOrNull()
                if (streak != null && streak.lastGoalMetDate.isNotEmpty() && streak.lastGoalMetDate != todayStr) {
                    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val lastDate = format.parse(streak.lastGoalMetDate)
                    val todayDate = format.parse(todayStr)
                    if (lastDate != null && todayDate != null) {
                        val diffDays = (todayDate.time - lastDate.time) / (1000 * 60 * 60 * 24)
                        if (diffDays > 1) {
                            goalRepository.updateStreakData(StreakData(currentStreak = 0, lastGoalMetDate = streak.lastGoalMetDate))
                        }
                    }
                }

                goalRepository.updateDailyProgress(
                    DailyProgress(
                        date = todayStr,
                        chaptersStudiedCount = 0,
                        goalTarget = 3,
                        goalMet = false
                    )
                )
            }
        }
        
        viewModelScope.launch {
            dailyProgress.collect { progress ->
                if (progress?.goalMet == true) {
                    val streak = goalRepository.getStreakData().firstOrNull()
                    if (streak != null && streak.lastGoalMetDate != todayStr) {
                        goalRepository.updateStreakData(
                            StreakData(currentStreak = streak.currentStreak + 1, lastGoalMetDate = todayStr)
                        )
                    }
                }
            }
        }
    }

    fun setGoalTarget(target: Int) {
        viewModelScope.launch {
            val progress = dailyProgress.value
            if (progress != null) {
                goalRepository.updateDailyProgress(
                    progress.copy(
                        goalTarget = target,
                        goalMet = progress.chaptersStudiedCount >= target
                    )
                )
            }
        }
    }
}
