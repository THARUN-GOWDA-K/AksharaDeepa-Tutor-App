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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onSignup: () -> Unit,
    onForgotPassword: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val authState by viewModel.authState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(authState) {
        if (authState is UiState.Success) {
            viewModel.resetState()
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GlassBackgroundGradient)
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Welcome Back", style = MaterialTheme.typography.displayLarge, color = TextPrimary)
        Text("Sign in to continue learning.", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)

        Spacer(modifier = Modifier.height(20.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Login", style = MaterialTheme.typography.titleLarge, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(12.dp))
                GlassTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "Email",
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                Spacer(modifier = Modifier.height(12.dp))
                GlassTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "Password",
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(16.dp))
                GlassPrimaryButton(onClick = { viewModel.login(email, password) }) {
                    Text("Sign In", color = TextPrimary)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Forgot password?",
                    color = TextMuted,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(top = 6.dp)
                )
                Text(
                    "Reset",
                    color = TextPrimary,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .clickable { onForgotPassword() }
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    "Create an account",
                    color = TextPrimary,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .clickable { onSignup() }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (authState) {
            is UiState.Error -> Text(
                text = (authState as UiState.Error).message,
                color = TextPrimary,
                style = MaterialTheme.typography.bodyMedium
            )
            UiState.Loading -> Text("Signing in...", color = TextMuted)
            else -> Unit
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Tap here to reset password",
            color = TextSecondary,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .padding(top = 4.dp)
                .clickable { onForgotPassword() }
        )
        Text(
            "New here? Create an account",
            color = TextSecondary,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .padding(top = 2.dp)
                .clickable { onSignup() }
        )
    }
}
