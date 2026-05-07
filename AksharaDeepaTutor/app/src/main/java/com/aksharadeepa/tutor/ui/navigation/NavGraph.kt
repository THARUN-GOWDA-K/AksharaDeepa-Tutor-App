package com.aksharadeepa.tutor.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.aksharadeepa.tutor.ui.goal.DailyGoalScreen
import com.aksharadeepa.tutor.ui.quiz.QuizModeScreen
import com.aksharadeepa.tutor.ui.quiz.QuizScreen
import com.aksharadeepa.tutor.ui.quiz.QuizSummaryScreen
import com.aksharadeepa.tutor.ui.strength.StrengthMapScreen
import com.aksharadeepa.tutor.ui.syllabus.SyllabusTrackerScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.aksharadeepa.tutor.ui.quiz.QuizViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val items = listOf(
        Screen.Syllabus,
        Screen.Quiz,
        Screen.Strength,
        Screen.Goal
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            val hideBottomBar = currentDestination?.route in listOf(Screen.QuizPlay.route, Screen.QuizSummary.route)

            if (!hideBottomBar) {
                NavigationBar {
                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = null) },
                            label = { Text(screen.title) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        val sharedQuizViewModel = hiltViewModel<QuizViewModel>()

        NavHost(navController, startDestination = Screen.Syllabus.route, Modifier.padding(innerPadding)) {
            composable(Screen.Syllabus.route) { SyllabusTrackerScreen() }
            composable(Screen.Quiz.route) { 
                QuizModeScreen(sharedQuizViewModel) {
                    navController.navigate(Screen.QuizPlay.route)
                }
            }
            composable(Screen.QuizPlay.route) {
                QuizScreen(sharedQuizViewModel) {
                    navController.navigate(Screen.QuizSummary.route) {
                        popUpTo(Screen.QuizPlay.route) { inclusive = true }
                    }
                }
            }
            composable(Screen.QuizSummary.route) {
                QuizSummaryScreen(sharedQuizViewModel) {
                    navController.navigate(Screen.Quiz.route) {
                        popUpTo(Screen.Quiz.route) { inclusive = true }
                    }
                }
            }
            composable(Screen.Strength.route) { StrengthMapScreen() }
            composable(Screen.Goal.route) { 
                DailyGoalScreen(onTakeQuiz = { chapter ->
                    sharedQuizViewModel.startQuiz(chapter)
                    navController.navigate(Screen.QuizPlay.route)
                }) 
            }
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Syllabus : Screen("syllabus", "Syllabus", Icons.Filled.Checklist)
    object Quiz : Screen("quiz", "Quiz", Icons.Filled.Lightbulb)
    object QuizPlay : Screen("quiz_play", "Quiz Play", Icons.Filled.Lightbulb)
    object QuizSummary : Screen("quiz_summary", "Summary", Icons.Filled.Lightbulb)
    object Strength : Screen("strength", "Strength", Icons.Filled.Assessment)
    object Goal : Screen("goal", "Goal", Icons.Filled.Flag)
}
