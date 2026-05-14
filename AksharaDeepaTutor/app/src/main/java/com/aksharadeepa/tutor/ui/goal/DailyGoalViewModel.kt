package com.aksharadeepa.tutor.ui.goal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aksharadeepa.tutor.data.local.entities.Chapter
import com.aksharadeepa.tutor.data.local.entities.DailyProgress
import com.aksharadeepa.tutor.data.local.entities.StreakData
import com.aksharadeepa.tutor.data.repository.ChapterRepository
import com.aksharadeepa.tutor.data.repository.GoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DailyGoalViewModel @Inject constructor(
    private val goalRepository: GoalRepository,
    private val chapterRepository: ChapterRepository
) : ViewModel() {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val todayStr = dateFormat.format(Date())

    val dailyProgress = goalRepository.getDailyProgress(todayStr)
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val streakData = goalRepository.getStreakData()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val chaptersCompletedToday = chapterRepository.getChaptersCompletedOnDate(todayStr)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val recommendedChapters = chapterRepository.getAllChapters()
        .map { chapters ->
            val science = chapters.firstOrNull { it.subject == "SCIENCE" && !it.isCompleted }
            val math = chapters.firstOrNull { it.subject == "MATH" && !it.isCompleted }
            val social = chapters.firstOrNull { it.subject == "SOCIAL" && !it.isCompleted }
            listOfNotNull(science, math, social)
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        viewModelScope.launch { ensureDailyProgress() }
        viewModelScope.launch { refreshStreakOnStartup() }

        viewModelScope.launch {
            chaptersCompletedToday.collect { chapters ->
                syncDailyProgressFromChapters(chapters)
            }
        }

        viewModelScope.launch {
            dailyProgress.collect { progress ->
                if (progress?.goalMet == true) {
                    updateStreakForToday(progress)
                }
            }
        }
    }

    fun setGoalTarget(target: Int) {
        viewModelScope.launch {
            val current = dailyProgress.value
            val count = chaptersCompletedToday.value.size
            val goalMet = count >= target
            if (current != null) {
                goalRepository.updateDailyProgress(
                    current.copy(goalTarget = target, goalMet = goalMet)
                )
            } else {
                goalRepository.updateDailyProgress(
                    DailyProgress(
                        date = todayStr,
                        chaptersStudiedCount = count,
                        goalTarget = target,
                        goalMet = goalMet
                    )
                )
            }
        }
    }

    private suspend fun ensureDailyProgress() {
        val progress = goalRepository.getDailyProgress(todayStr).firstOrNull()
        if (progress == null) {
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

    private suspend fun syncDailyProgressFromChapters(chapters: List<Chapter>) {
        val count = chapters.size
        val current = dailyProgress.value
        val target = current?.goalTarget ?: 3
        val goalMet = count >= target
        if (current == null) {
            goalRepository.updateDailyProgress(
                DailyProgress(
                    date = todayStr,
                    chaptersStudiedCount = count,
                    goalTarget = target,
                    goalMet = goalMet
                )
            )
        } else if (current.chaptersStudiedCount != count || current.goalMet != goalMet) {
            goalRepository.updateDailyProgress(
                current.copy(
                    chaptersStudiedCount = count,
                    goalMet = goalMet
                )
            )
        }
    }

    private suspend fun refreshStreakOnStartup() {
        val streak = goalRepository.getStreakData().firstOrNull()
            ?: StreakData(currentStreak = 0, lastGoalMetDate = "").also {
                goalRepository.updateStreakData(it)
            }
        val yesterday = getYesterdayString()
        val yesterdayProgress = goalRepository.getDailyProgress(yesterday).firstOrNull()
        if (yesterdayProgress?.goalMet != true && streak.currentStreak != 0) {
            if (streak.lastGoalMetDate != todayStr) {
                goalRepository.updateStreakData(
                    streak.copy(currentStreak = 0, lastGoalMetDate = streak.lastGoalMetDate)
                )
            }
        }
    }

    private suspend fun updateStreakForToday(progress: DailyProgress) {
        val streak = goalRepository.getStreakData().firstOrNull()
            ?: StreakData(currentStreak = 0, lastGoalMetDate = "").also {
                goalRepository.updateStreakData(it)
            }
        if (streak.lastGoalMetDate == todayStr) return

        val yesterday = getYesterdayString()
        val yesterdayMet = goalRepository.getDailyProgress(yesterday).firstOrNull()?.goalMet == true
        val newStreak = if (yesterdayMet) streak.currentStreak + 1 else 1
        goalRepository.updateStreakData(
            streak.copy(currentStreak = newStreak, lastGoalMetDate = todayStr)
        )
    }

    private fun getYesterdayString(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        return dateFormat.format(calendar.time)
    }
}
