package com.project.reading_stats.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.reading_stats.core.domain.model.UserProfile
import com.project.reading_stats.core.domain.repository.AuthRepository
import com.project.reading_stats.feature.auth.state.AuthUiEvent
import com.project.reading_stats.feature.auth.state.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
*
* ViewModel for the authentication process.
*
 */

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {

    // Stato osservabile dalla UI (loading, error)
    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state

    // Dispatcher di eventi dalla UI
    fun handle(event: AuthUiEvent, onSuccess: () -> Unit = {}) {
        when (event) {
            is AuthUiEvent.Login -> login(event.email, event.password, onSuccess)
            is AuthUiEvent.Register -> register(event.profile, event.password, event.confirmPassword, onSuccess)
            AuthUiEvent.Logout -> logout()
        }
    }

    // LOGIN
    fun login(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }
            val result = repo.login(email, password)
            _state.update { it.copy(loading = false, error = result.exceptionOrNull()?.message) }
            if (result.isSuccess) onSuccess()
        }
    }

    // REGISTER (usa direttamente il model + password/confirmPassword)
    fun register(
        profile: UserProfile,
        password: String,
        confirmPassword: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }

            // Validazione minima lato VM
            if (password != confirmPassword) {
                _state.update { it.copy(loading = false, error = "Le password non coincidono.") }
                return@launch
            }

            // Verifiche unicità richieste (email / username)
            if (repo.isEmailTaken(profile.email)) {
                _state.update { it.copy(loading = false, error = "Email già in uso.") }
                return@launch
            }
            if (repo.isUsernameTaken(profile.username)) {
                _state.update { it.copy(loading = false, error = "Username già in uso.") }
                return@launch
            }

            // Registrazione vera e propria
            val result = repo.register(profile, password)
            _state.update { it.copy(loading = false, error = result.exceptionOrNull()?.message) }
            if (result.isSuccess) onSuccess()
        }
    }

    // LOGOUT
    fun logout() {
        repo.logout()
    }
}
