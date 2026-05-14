package com.aksharadeepa.tutor.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aksharadeepa.tutor.ui.auth.ForgotPasswordScreen
import com.aksharadeepa.tutor.ui.auth.LoginScreen
import com.aksharadeepa.tutor.ui.auth.SignupScreen
import com.aksharadeepa.tutor.ui.goal.DailyGoalScreen
import com.aksharadeepa.tutor.ui.home.HomeScreen
import com.aksharadeepa.tutor.ui.profile.ProfileScreen
import com.aksharadeepa.tutor.ui.progress.ProgressScreen
import com.aksharadeepa.tutor.ui.quiz.QuizModeScreen
import com.aksharadeepa.tutor.ui.quiz.QuizScreen
import com.aksharadeepa.tutor.ui.quiz.QuizSummaryScreen
import com.aksharadeepa.tutor.ui.quiz.QuizViewModel
import com.aksharadeepa.tutor.ui.splash.SplashScreen
import com.aksharadeepa.tutor.ui.strength.StrengthMapScreen
import com.aksharadeepa.tutor.ui.syllabus.SyllabusTrackerScreen
import com.aksharadeepa.tutor.ui.theme.GlassIconPill
import com.aksharadeepa.tutor.ui.theme.GlassNavBorder
import com.aksharadeepa.tutor.ui.theme.GlassNavBar
import com.aksharadeepa.tutor.ui.theme.TextFaint
import com.aksharadeepa.tutor.ui.theme.TextPrimary

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val items = listOf(
        Screen.Home,
        Screen.Syllabus,
        Screen.Quiz,
        Screen.Analytics,
        Screen.Profile
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            val hideBottomBar = currentDestination?.route in listOf(
                Screen.Splash.route,
                Screen.Login.route,
                Screen.Signup.route,
                Screen.ForgotPassword.route,
                Screen.Progress.route,
                Screen.Goal.route,
                Screen.QuizPlay.route,
                Screen.QuizSummary.route
            )

            if (!hideBottomBar) {
                Column {
                    Divider(color = GlassNavBorder, thickness = 0.5.dp)
                    NavigationBar(
                        containerColor = GlassNavBar,
                        tonalElevation = 0.dp
                    ) {
                        items.forEach { screen ->
                            val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                            NavigationBarItem(
                                icon = {
                                    if (selected) {
                                        GlassIconPill {
                                            androidx.compose.material3.Icon(
                                                screen.icon,
                                                contentDescription = null,
                                                tint = TextPrimary
                                            )
                                        }
                                    } else {
                                        androidx.compose.material3.Icon(
                                            screen.icon,
                                            contentDescription = null,
                                            tint = TextFaint
                                        )
                                    }
                                },
                                label = {
                                    Text(
                                        screen.title,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (selected) TextPrimary else TextFaint
                                    )
                                },
                                selected = selected,
                                onClick = {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = TextPrimary,
                                    selectedTextColor = TextPrimary,
                                    unselectedIconColor = TextFaint,
                                    unselectedTextColor = TextFaint,
                                    indicatorColor = androidx.compose.ui.graphics.Color.Transparent
                                )
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        val sharedQuizViewModel = hiltViewModel<QuizViewModel>()

        NavHost(navController, startDestination = Screen.Splash.route, Modifier.padding(innerPadding)) {
            composable(Screen.Splash.route) {
                SplashScreen(onFinished = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                })
            }
            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onSignup = { navController.navigate(Screen.Signup.route) },
                    onForgotPassword = { navController.navigate(Screen.ForgotPassword.route) }
                )
            }
            composable(Screen.Signup.route) {
                SignupScreen(
                    onSignupSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Signup.route) { inclusive = true }
                        }
                    },
                    onLogin = { navController.navigate(Screen.Login.route) }
                )
            }
            composable(Screen.ForgotPassword.route) {
                ForgotPasswordScreen(
                    onBackToLogin = { navController.navigate(Screen.Login.route) }
                )
            }
            composable(Screen.Home.route) {
                HomeScreen(
                    onStartQuiz = { navController.navigate(Screen.Quiz.route) },
                    onViewSyllabus = { navController.navigate(Screen.Syllabus.route) },
                    onViewProgress = { navController.navigate(Screen.Progress.route) },
                    onViewGoals = { navController.navigate(Screen.Goal.route) }
                )
            }
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
            composable(Screen.Analytics.route) { StrengthMapScreen() }
            composable(Screen.Progress.route) { ProgressScreen() }
            composable(Screen.Profile.route) { 
                ProfileScreen(
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                ) 
            }
            composable(Screen.Goal.route) {
                DailyGoalScreen(
                    quizViewModel = sharedQuizViewModel,
                    onTakeQuiz = { chapter ->
                        val started = sharedQuizViewModel.startQuiz(chapter)
                        if (started) {
                            navController.navigate(Screen.QuizPlay.route)
                        }
                    }
                )
            }
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Splash : Screen("splash", "", Icons.Filled.Home)
    object Login : Screen("login", "", Icons.Filled.Home)
    object Signup : Screen("signup", "", Icons.Filled.Home)
    object ForgotPassword : Screen("forgot_password", "", Icons.Filled.Home)
    object Home : Screen("home", "Home", Icons.Filled.Home)
    object Syllabus : Screen("syllabus", "Syllabus", Icons.Filled.Checklist)
    object Quiz : Screen("quiz", "Quiz", Icons.Filled.Lightbulb)
    object QuizPlay : Screen("quiz_play", "Quiz Play", Icons.Filled.Lightbulb)
    object QuizSummary : Screen("quiz_summary", "Summary", Icons.Filled.Lightbulb)
    object Analytics : Screen("analytics", "Analytics", Icons.Filled.Assessment)
    object Progress : Screen("progress", "Progress", Icons.Filled.Assessment)
    object Profile : Screen("profile", "Profile", Icons.Filled.Person)
    object Goal : Screen("goal", "Goal", Icons.Filled.Flag)
}
