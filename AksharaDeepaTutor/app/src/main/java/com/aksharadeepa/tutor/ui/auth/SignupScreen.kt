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
fun SignupScreen(
    onSignupSuccess: () -> Unit,
    onLogin: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val authState by viewModel.authState.collectAsState()
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(authState) {
        if (authState is UiState.Success) {
            viewModel.resetState()
            onSignupSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GlassBackgroundGradient)
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Create Account", style = MaterialTheme.typography.displayLarge, color = TextPrimary)
        Text("Start your learning journey.", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)

        Spacer(modifier = Modifier.height(20.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Sign Up", style = MaterialTheme.typography.titleLarge, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(12.dp))
                GlassTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = "Full Name",
                    modifier = Modifier.fillMaxWidth()
                )
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
                GlassPrimaryButton(onClick = { viewModel.signUp(name, email, password) }) {
                    Text("Create Account", color = TextPrimary)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Already have an account? Sign in",
                    color = TextMuted,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .clickable { onLogin() }
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
            UiState.Loading -> Text("Creating account...", color = TextMuted)
            else -> Unit
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Go back to login",
            color = TextSecondary,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.clickable { onLogin() }
        )
    }
}
