package com.project.reading_stats.feature.auth.state
import com.project.reading_stats.core.domain.model.UserProfile
/*
 Modeling the events that can occur in the authentication process.
 */
sealed class AuthUiEvent {
    data class Login(val email: String, val password: String) : AuthUiEvent()
    data class Register(val profile: UserProfile, val password: String, val confirmPassword: String, val onSuccess: () -> Unit) : AuthUiEvent()
    object Logout : AuthUiEvent()
}