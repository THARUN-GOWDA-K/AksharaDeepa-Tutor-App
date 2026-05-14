package com.aksharadeepa.tutor.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aksharadeepa.tutor.ui.theme.GlassBackgroundGradient
import com.aksharadeepa.tutor.ui.theme.GlassCard
import com.aksharadeepa.tutor.ui.theme.GlassPrimaryButton
import com.aksharadeepa.tutor.ui.theme.GlassTextField
import com.aksharadeepa.tutor.ui.theme.TextMuted
import com.aksharadeepa.tutor.ui.theme.TextPrimary
import com.aksharadeepa.tutor.ui.theme.TextSecondary
import com.aksharadeepa.tutor.utils.UiState
import com.aksharadeepa.tutor.viewmodel.AuthViewModel

@Composable
fun ForgotPasswordScreen(
    onBackToLogin: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val authState by viewModel.authState.collectAsState()
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GlassBackgroundGradient)
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Reset Password", style = MaterialTheme.typography.displayLarge, color = TextPrimary)
        Text("We will email you a reset link.", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)

        Spacer(modifier = Modifier.height(20.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                GlassTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "Email",
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                Spacer(modifier = Modifier.height(16.dp))
                GlassPrimaryButton(onClick = { viewModel.sendPasswordReset(email) }) {
                    Text("Send Reset Link", color = TextPrimary)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (authState) {
            is UiState.Error -> Text(
                text = (authState as UiState.Error).message,
                color = TextPrimary,
                style = MaterialTheme.typography.bodyMedium
            )
            UiState.Loading -> Text("Sending email...", color = TextMuted)
            is UiState.Success -> Text("Reset email sent.", color = TextPrimary)
            else -> Unit
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Back to login",
            color = TextSecondary,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.clickable { onBackToLogin() }
        )
    }
}
