// app/src/main/java/com/project/reading_stats/feature/auth/ui/AuthScreen.kt
package com.project.reading_stats.feature.auth.ui

import androidx.compose.runtime.*
import com.project.reading_stats.core.domain.model.UserProfile

@Composable
fun LoginScreen(
    onSubmit: (email: String, password: String) -> Unit,
    onGoToRegister: () -> Unit,
    loading: Boolean,
    errorText: String?
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val submitEnabled = email.isNotBlank() && password.length >= 6 && !loading

    LoginForm(
        email = email,
        password = password,
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        submitEnabled = submitEnabled,
        onSubmit = { if (submitEnabled) onSubmit(email, password) },
        onGoToRegister = onGoToRegister,
        errorText = errorText
    )
}

@Composable
fun RegisterScreen(
    onSubmit: (profile: UserProfile, password: String, confirmPassword: String) -> Unit,
    onGoToLogin: () -> Unit,
    loading: Boolean,
    errorText: String?
) {
    var profile by remember { mutableStateOf(UserProfile(uid = "", name = "", surname = "", username = "", email = "")) }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val mismatch = password.isNotBlank() && confirmPassword.isNotBlank() && password != confirmPassword
    val submitEnabled =
        profile.name.isNotBlank() &&
                profile.surname.isNotBlank() &&
                profile.username.isNotBlank() &&
                profile.email.isNotBlank() &&
                password.length >= 6 &&
                !mismatch &&
                !loading

    RegisterForm(
        profile = profile,
        password = password,
        confirmPassword = confirmPassword,
        onProfileChange = { profile = it },
        onPasswordChange = { password = it },
        onConfirmPasswordChange = { confirmPassword = it },
        submitEnabled = submitEnabled,
        onSubmit = { p, pwd, cPwd -> if (submitEnabled) onSubmit(p, pwd, cPwd) },
        onGoToLogin = onGoToLogin,
        errorText = errorText
    )
}
