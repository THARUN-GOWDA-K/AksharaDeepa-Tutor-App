package com.aksharadeepa.tutor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aksharadeepa.tutor.data.repository.AuthRepository
import com.aksharadeepa.tutor.data.repository.UserSyncRepository
import com.aksharadeepa.tutor.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userSyncRepository: UserSyncRepository
) : ViewModel() {
    private val _authState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val authState: StateFlow<UiState<String>> = _authState

    fun signUp(name: String, email: String, password: String) {
        if (name.isBlank() || email.isBlank() || password.length < 6) {
            _authState.value = UiState.Error("Please fill all fields (min 6 characters password).")
            return
        }
        viewModelScope.launch {
            _authState.value = UiState.Loading
            val result = authRepository.signUp(name.trim(), email.trim(), password)
            _authState.value = result.fold(
                onSuccess = { userId ->
                    viewModelScope.launch { userSyncRepository.syncFromBackend(userId) }
                    UiState.Success(userId)
                },
                onFailure = { UiState.Error(it.message ?: "Sign up failed") }
            )
        }
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = UiState.Error("Email and password are required.")
            return
        }
        viewModelScope.launch {
            _authState.value = UiState.Loading
            val result = authRepository.login(email.trim(), password)
            _authState.value = result.fold(
                onSuccess = { userId ->
                    viewModelScope.launch { userSyncRepository.syncFromBackend(userId) }
                    UiState.Success(userId)
                },
                onFailure = { UiState.Error(it.message ?: "Login failed") }
            )
        }
    }

    fun sendPasswordReset(email: String) {
        if (email.isBlank()) {
            _authState.value = UiState.Error("Email is required.")
            return
        }
        viewModelScope.launch {
            _authState.value = UiState.Loading
            val result = authRepository.sendPasswordReset(email.trim())
            _authState.value = result.fold(
                onSuccess = { UiState.Success("reset") },
                onFailure = { UiState.Error(it.message ?: "Reset failed") }
            )
        }
    }

    fun resetState() {
        _authState.value = UiState.Idle
    }
}
