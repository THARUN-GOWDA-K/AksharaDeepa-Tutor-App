package com.aksharadeepa.tutor.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aksharadeepa.tutor.models.UserProfile
import com.aksharadeepa.tutor.ui.theme.*
import com.aksharadeepa.tutor.utils.UiState
import com.aksharadeepa.tutor.viewmodel.ProfileStats
import com.aksharadeepa.tutor.viewmodel.ProfileViewModel
import com.aksharadeepa.tutor.viewmodel.RecentAttempt
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onLogout: () -> Unit = {}
) {
    val profileState by viewModel.profileState.collectAsState()
    val profileStats by viewModel.profileStats.collectAsState()
    val recentAttempts by viewModel.recentAttempts.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GlassBackgroundGradient)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Profile", style = MaterialTheme.typography.displayLarge, color = TextPrimary)
        Text("Manage your learning identity and settings.", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)

        when (val state = profileState) {
            is UiState.Success -> ProfileEditor(
                profile = state.data,
                stats = profileStats,
                recentAttempts = recentAttempts,
                onSave = { updatedProfile -> viewModel.updateProfile(updatedProfile) },
                onLogout = {
                    viewModel.signOut()
                    onLogout()
                }
            )
            is UiState.Error -> Text(state.message, color = TextPrimary)
            UiState.Loading -> Text("Loading profile...", color = TextMuted)
            else -> Unit
        }
    }
}

@Composable
private fun ProfileEditor(
    profile: UserProfile,
    stats: ProfileStats,
    recentAttempts: List<RecentAttempt>,
    onSave: (UserProfile) -> Unit,
    onLogout: () -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var displayName by remember { mutableStateOf(profile.displayName) }
    var school by remember { mutableStateOf(profile.school ?: "") }
    var grade by remember { mutableStateOf(profile.grade) }

    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Surface(
                    shape = CircleShape,
                    color = GlassSurfaceStrong,
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = TextPrimary, modifier = Modifier.padding(16.dp))
                }
                Column {
                    Text(profile.displayName.ifBlank { "Student" }, style = MaterialTheme.typography.titleLarge, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                    if (profile.email.isNotBlank()) {
                        Text(profile.email, color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .background(GlassSurfaceStrong, shape = CircleShape)
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text("🔥 Streak: ${profile.streakCount} days", color = GradientEnd, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Divider(color = GlassBorder)

            if (isEditing) {
                GlassTextField(
                    value = displayName,
                    onValueChange = { displayName = it },
                    placeholder = "Full Name",
                    modifier = Modifier.fillMaxWidth()
                )
                GlassTextField(
                    value = school,
                    onValueChange = { school = it },
                    placeholder = "School Name",
                    modifier = Modifier.fillMaxWidth()
                )
                GlassTextField(
                    value = grade,
                    onValueChange = { grade = it },
                    placeholder = "Grade (e.g. 10)",
                    modifier = Modifier.fillMaxWidth()
                )
                
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    GlassPrimaryButton(
                        onClick = { isEditing = false },
                        modifier = Modifier.weight(1f),
                        enabled = true
                    ) {
                        Text("Cancel", fontWeight = FontWeight.Bold)
                    }
                    GlassPrimaryButton(
                        onClick = { 
                            onSave(profile.copy(displayName = displayName, school = school, grade = grade))
                            isEditing = false
                        },
                        modifier = Modifier.weight(1f),
                        enabled = displayName.isNotBlank()
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Save", fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ProfileDetailRow(label = "Email", value = profile.email.ifBlank { "Not provided" })
                    ProfileDetailRow(label = "School", value = profile.school?.takeIf { it.isNotBlank() } ?: "Not provided")
                    ProfileDetailRow(label = "Grade", value = profile.grade)
                }

                Spacer(modifier = Modifier.height(8.dp))

                GlassPrimaryButton(
                    onClick = { isEditing = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Edit Profile", fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            GlassPrimaryButton(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = androidx.compose.ui.graphics.Color(0xFFFF5252), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Sign Out", color = androidx.compose.ui.graphics.Color(0xFFFF5252), fontWeight = FontWeight.Bold)
            }
        }
    }

    ProfileStatsRow(stats)

    ProfileSectionCard(title = "Learning Snapshot", icon = Icons.Default.Lightbulb) {
        ProfileDetailRow(
            label = "Completion",
            value = "${stats.completedChapters}/${stats.totalChapters} (${stats.completionPercent}%)"
        )
        ProfileDetailRow(
            label = "Strongest subject",
            value = displaySubject(stats.strongestSubject)
        )
        ProfileDetailRow(
            label = "Needs focus",
            value = displaySubject(stats.weakestSubject)
        )
        ProfileDetailRow(
            label = "Average score",
            value = "${stats.averageScorePercent}%"
        )
        ProfileDetailRow(
            label = "Quizzes taken",
            value = stats.quizzesTaken.toString()
        )
    }

    ProfileSectionCard(title = "Recent Activity", icon = Icons.Default.CheckCircle) {
        if (recentAttempts.isEmpty()) {
            Text("No quiz attempts yet.", color = TextSecondary)
        } else {
            recentAttempts.forEachIndexed { index, attempt ->
                if (index > 0) {
                    Divider(color = GlassBorder, modifier = Modifier.padding(vertical = 8.dp))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(attempt.chapterName, color = TextPrimary)
                        Text(formatAttemptDate(attempt.attemptedAt), color = TextMuted, style = MaterialTheme.typography.labelSmall)
                    }
                    Text("${attempt.scorePercent}%", color = GradientEnd, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun ProfileDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = TextMuted, style = MaterialTheme.typography.bodyMedium)
        Text(value, color = TextPrimary, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun ProfileStatsRow(stats: ProfileStats) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        ProfileStatCard(
            label = "Coverage",
            value = "${stats.completionPercent}%",
            subLabel = "${stats.completedChapters}/${stats.totalChapters}",
            modifier = Modifier.weight(1f)
        )
        ProfileStatCard(
            label = "Quizzes",
            value = stats.quizzesTaken.toString(),
            subLabel = "Attempts",
            modifier = Modifier.weight(1f)
        )
        ProfileStatCard(
            label = "Avg Score",
            value = "${stats.averageScorePercent}%",
            subLabel = "Overall",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ProfileStatCard(label: String, value: String, subLabel: String, modifier: Modifier = Modifier) {
    GlassCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, color = TextMuted, style = MaterialTheme.typography.labelMedium)
            Text(value, color = TextPrimary, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(subLabel, color = TextSecondary, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
private fun ProfileSectionCard(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector? = null, content: @Composable ColumnScope.() -> Unit) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (icon != null) {
                    Icon(icon, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(title, color = TextPrimary, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

private fun displaySubject(subject: String?): String {
    return when (subject?.uppercase()) {
        "SCIENCE" -> "Science"
        "MATH" -> "Mathematics"
        "SOCIAL" -> "Social Studies"
        null -> "Not enough data"
        else -> subject
    }
}

private fun formatAttemptDate(timestamp: Long): String {
    if (timestamp <= 0L) return ""
    val formatter = SimpleDateFormat("dd MMM", Locale.getDefault())
    return formatter.format(Date(timestamp))
}
