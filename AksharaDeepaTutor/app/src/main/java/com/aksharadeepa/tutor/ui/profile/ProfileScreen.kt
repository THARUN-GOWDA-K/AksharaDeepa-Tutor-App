package com.aksharadeepa.tutor.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
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
import com.aksharadeepa.tutor.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onLogout: () -> Unit = {}
) {
    val profileState by viewModel.profileState.collectAsState()

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
                    Text("Streak: ${profile.streakCount} days", color = TextAccent, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                }
            }

            HorizontalDivider(color = GlassBorder)

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
                Icon(Icons.Default.ExitToApp, contentDescription = null, tint = androidx.compose.ui.graphics.Color(0xFFFF5252), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Sign Out", color = androidx.compose.ui.graphics.Color(0xFFFF5252), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun ProfileDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = TextMuted, style = MaterialTheme.typography.bodyMedium)
        Text(value, color = TextPrimary, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
    }
}
