// app/src/main/java/com/project/reading_stats/feature/auth/ui/AuthComponents.kt
package com.project.reading_stats.feature.auth.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.project.reading_stats.core.domain.model.UserProfile

/* ---------- COMPONENTI DI AUTENTICAZIONE ---------- */

@Composable
fun AuthHeader(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title, style = MaterialTheme.typography.headlineMedium)
        if (!subtitle.isNullOrBlank()) {
            Spacer(Modifier.height(4.dp))
            Text(
                subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    imeAction: ImeAction = ImeAction.Next,
    isError: Boolean = false,
    supportingText: String? = null,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = leadingIcon,
        singleLine = true,
        isError = isError,
        supportingText = { if (!supportingText.isNullOrBlank()) Text(supportingText) },
        keyboardOptions = keyboardOptions.copy(imeAction = imeAction),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun EmailField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    supportingText: String? = null,
    imeAction: ImeAction = ImeAction.Next
) {
    AuthTextField(
        value = value,
        onValueChange = onValueChange,
        label = "Email",
        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        imeAction = imeAction,
        isError = isError,
        supportingText = supportingText
    )
}

@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "Password",
    isError: Boolean = false,
    supportingText: String? = null,
    imeAction: ImeAction = ImeAction.Done,
    onDone: (() -> Unit)? = null
) {
    var visible by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
        singleLine = true,
        isError = isError,
        supportingText = { if (!supportingText.isNullOrBlank()) Text(supportingText) },
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val text = if (visible) "Nascondi" else "Mostra"
            TextButton(onClick = { visible = !visible }) { Text(text) }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = imeAction),
        keyboardActions = KeyboardActions(onDone = { onDone?.invoke() }),
        modifier = Modifier.fillMaxWidth()
    )
}

/* ---------- FORM DI LOGIN ---------- */

@Composable
fun LoginForm(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    submitEnabled: Boolean,
    onSubmit: () -> Unit,
    onGoToRegister: () -> Unit,
    errorText: String? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        AuthHeader("Accedi", "Entra con le tue credenziali")

        if (!errorText.isNullOrBlank()) {
            Text(errorText, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(8.dp))
        }

        EmailField(value = email, onValueChange = onEmailChange)
        Spacer(Modifier.height(12.dp))
        PasswordField(
            value = password,
            onValueChange = onPasswordChange,
            onDone = onSubmit
        )

        Spacer(Modifier.height(20.dp))
        Button(
            onClick = onSubmit,
            enabled = submitEnabled,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Login") }

        Spacer(Modifier.height(12.dp))
        TextButton(
            onClick = onGoToRegister,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) { Text("Non hai un account? Registrati") }
    }
}

/* ---------- FORM DI REGISTRAZIONE (usa UserProfile) ---------- */

@Composable
fun RegisterForm(
    profile: UserProfile,
    password: String,
    confirmPassword: String,
    onProfileChange: (UserProfile) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    submitEnabled: Boolean,
    onSubmit: (UserProfile, String, String) -> Unit,
    onGoToLogin: () -> Unit,
    errorText: String? = null,
    modifier: Modifier = Modifier
) {
    val name = profile.name
    val surname = profile.surname
    val username = profile.username
    val email = profile.email

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        AuthHeader("Crea Account", "Registrati per iniziare")

        if (!errorText.isNullOrBlank()) {
            Text(errorText, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(8.dp))
        }

        AuthTextField(
            value = name,
            onValueChange = { onProfileChange(profile.copy(name = it)) },
            label = "Nome",
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
        )
        Spacer(Modifier.height(12.dp))
        AuthTextField(
            value = surname,
            onValueChange = { onProfileChange(profile.copy(surname = it)) },
            label = "Cognome",
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
        )
        Spacer(Modifier.height(12.dp))
        AuthTextField(
            value = username,
            onValueChange = { onProfileChange(profile.copy(username = it)) },
            label = "Username",
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
        )
        Spacer(Modifier.height(12.dp))
        EmailField(
            value = email,
            onValueChange = { onProfileChange(profile.copy(email = it)) }
        )
        Spacer(Modifier.height(12.dp))
        PasswordField(
            value = password,
            onValueChange = onPasswordChange,
            label = "Password",
            imeAction = ImeAction.Next
        )
        Spacer(Modifier.height(12.dp))
        PasswordField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = "Conferma password",
            onDone = { onSubmit(profile, password, confirmPassword) }
        )

        val mismatch = password.isNotBlank() && confirmPassword.isNotBlank() && password != confirmPassword
        if (mismatch) {
            Spacer(Modifier.height(8.dp))
            Text("Le password non coincidono.", color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(20.dp))
        Button(
            onClick = { onSubmit(profile, password, confirmPassword) },
            enabled = submitEnabled && !mismatch,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Registrati") }

        Spacer(Modifier.height(12.dp))
        TextButton(
            onClick = onGoToLogin,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) { Text("Hai già un account? Login") }
    }
}
