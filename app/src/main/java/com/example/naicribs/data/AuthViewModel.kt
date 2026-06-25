package com.example.naicribs.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.naicribs.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val currentUser: User? = null
)

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _state.value = AuthUiState(errorMessage = "Please fill in all fields")
            return
        }
        viewModelScope.launch {
            _state.value = AuthUiState(isLoading = true)
            val result = repository.login(email.trim(), password)
            if (result.isSuccess) {
                val user = repository.getCurrentUserProfile()
                _state.value = AuthUiState(isSuccess = true, currentUser = user)
            } else {
                _state.value = AuthUiState(
                    errorMessage = result.exceptionOrNull()?.message ?: "Login failed. Check your credentials."
                )
            }
        }
    }

    fun register(email: String, password: String, fullName: String, role: String) {
        if (email.isBlank() || password.isBlank() || fullName.isBlank()) {
            _state.value = AuthUiState(errorMessage = "Please fill in all fields")
            return
        }
        if (password.length < 6) {
            _state.value = AuthUiState(errorMessage = "Password must be at least 6 characters")
            return
        }
        viewModelScope.launch {
            _state.value = AuthUiState(isLoading = true)
            val result = repository.register(email.trim(), password, fullName.trim(), role)
            _state.value = if (result.isSuccess) {
                AuthUiState(isSuccess = true)
            } else {
                AuthUiState(errorMessage = result.exceptionOrNull()?.message ?: "Registration failed")
            }
        }
    }

    // Called from RoleSelectionScreen once the user picks Agent or Customer.
    // Updates the "role" field on their Firestore profile document.
    fun setUserRole(role: String) {
        viewModelScope.launch {
            repository.updateUserRole(role)
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _state.value = AuthUiState()
        }
    }

    fun resetPassword(email: String) {
        if (email.isBlank()) {
            _state.value = AuthUiState(errorMessage = "Please enter your email")
            return
        }
        viewModelScope.launch {
            _state.value = AuthUiState(isLoading = true)
            val result = repository.resetPassword(email.trim())
            _state.value = if (result.isSuccess) {
                AuthUiState(isSuccess = true)
            } else {
                AuthUiState(errorMessage = result.exceptionOrNull()?.message ?: "Failed to send reset email")
            }
        }
    }

    fun clearError() { _state.value = _state.value.copy(errorMessage = null, isSuccess = false) }

    fun getCurrentUserId() = repository.getCurrentUserId()
    fun isLoggedIn() = repository.isLoggedIn()
}