package com.aksharadeepa.tutor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aksharadeepa.tutor.data.repository.AuthRepository
import com.aksharadeepa.tutor.data.repository.ProfileRepository
import com.aksharadeepa.tutor.models.UserProfile
import com.aksharadeepa.tutor.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {
    private val _profileState = MutableStateFlow<UiState<UserProfile>>(UiState.Idle)
    val profileState: StateFlow<UiState<UserProfile>> = _profileState

    fun loadProfile() {
        val userId = authRepository.currentUserId() ?: return
        viewModelScope.launch {
            _profileState.value = UiState.Loading
            val result = profileRepository.getProfile(userId)
            _profileState.value = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Profile load failed") }
            )
        }
    }

    fun updateProfile(profile: UserProfile) {
        viewModelScope.launch {
            _profileState.value = UiState.Loading
            val result = profileRepository.updateProfile(profile)
            _profileState.value = result.fold(
                onSuccess = { UiState.Success(profile) },
                onFailure = { UiState.Error(it.message ?: "Profile update failed") }
            )
        }
    }

    fun signOut() {
        authRepository.signOut()
    }
}
