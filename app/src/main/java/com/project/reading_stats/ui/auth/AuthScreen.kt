package com.project.reading_stats.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.reading_stats.ui.theme.Reading_StatsTheme

enum class AuthMode { Login, Register }

data class LoginFormData(
    val email: String,
    val password: String,
    val rememberMe: Boolean
)

data class RegistrationFormData(
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    initialMode: AuthMode = AuthMode.Login,
    onLogin: (LoginFormData) -> Unit = {},
    onRegister: (RegistrationFormData) -> Unit = {}
) {
    var authMode by rememberSaveable { mutableStateOf(initialMode) }
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (authMode == AuthMode.Login) "Bentornato!" else "Crea il tuo account",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (authMode == AuthMode.Login) {
                "Accedi per continuare a monitorare le tue letture."
            } else {
                "Compila i campi per iniziare la tua sfida di lettura."
            },
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 520.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                when (authMode) {
                    AuthMode.Login -> LoginForm(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        onSubmit = {
                            onLogin(it)
                        },
                        onNavigateToRegister = {
                            authMode = AuthMode.Register
                        }
                    )

                    AuthMode.Register -> RegistrationForm(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        onSubmit = {
                            onRegister(it)
                        },
                        onNavigateToLogin = {
                            authMode = AuthMode.Login
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun LoginForm(
    modifier: Modifier = Modifier,
    onSubmit: (LoginFormData) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var rememberMe by rememberSaveable { mutableStateOf(false) }
    var showPassword by rememberSaveable { mutableStateOf(false) }

    val isFormValid = email.isNotBlank() && password.isNotBlank()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Password") },
            singleLine = true,
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    if (isFormValid) {
                        onSubmit(
                            LoginFormData(
                                email = email.trim(),
                                password = password,
                                rememberMe = rememberMe
                            )
                        )
                    }
                }
            ),
            trailingIcon = {
                TextButton(
                    onClick = { showPassword = !showPassword },
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    Text(text = if (showPassword) "Nascondi" else "Mostra")
                }
            }
        )

        FilledTonalButton(
            onClick = { rememberMe = !rememberMe },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = if (rememberMe) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                },
                contentColor = if (rememberMe) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        ) {
            Text(text = if (rememberMe) "Accesso memorizzato" else "Memorizza l'accesso")
        }

        Button(
            onClick = {
                focusManager.clearFocus()
                onSubmit(
                    LoginFormData(
                        email = email.trim(),
                        password = password,
                        rememberMe = rememberMe
                    )
                )
            },
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Accedi")
        }

        TextButton(
            onClick = onNavigateToRegister,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Non hai un account? Registrati")
        }
    }
}

@Composable
private fun RegistrationForm(
    modifier: Modifier = Modifier,
    onSubmit: (RegistrationFormData) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var showConfirmPassword by rememberSaveable { mutableStateOf(false) }

    val passwordsMismatch = password.isNotEmpty() && confirmPassword.isNotEmpty() && password != confirmPassword
    val isFormValid = firstName.isNotBlank() &&
        lastName.isNotBlank() &&
        username.isNotBlank() &&
        email.isNotBlank() &&
        password.isNotBlank() &&
        confirmPassword.isNotBlank() &&
        !passwordsMismatch

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Nome") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Cognome") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Username") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Password") },
            singleLine = true,
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            trailingIcon = {
                TextButton(
                    onClick = { showPassword = !showPassword },
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    Text(text = if (showPassword) "Nascondi" else "Mostra")
                }
            }
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Conferma password") },
            singleLine = true,
            isError = passwordsMismatch,
            supportingText = if (passwordsMismatch) {
                { Text(text = "Le password non coincidono") }
            } else {
                null
            },
            visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    if (isFormValid) {
                        onSubmit(
                            RegistrationFormData(
                                firstName = firstName.trim(),
                                lastName = lastName.trim(),
                                username = username.trim(),
                                email = email.trim(),
                                password = password,
                                confirmPassword = confirmPassword
                            )
                        )
                    }
                }
            ),
            trailingIcon = {
                TextButton(
                    onClick = { showConfirmPassword = !showConfirmPassword },
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    Text(text = if (showConfirmPassword) "Nascondi" else "Mostra")
                }
            }
        )

        Button(
            onClick = {
                focusManager.clearFocus()
                onSubmit(
                    RegistrationFormData(
                        firstName = firstName.trim(),
                        lastName = lastName.trim(),
                        username = username.trim(),
                        email = email.trim(),
                        password = password,
                        confirmPassword = confirmPassword
                    )
                )
            },
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Registrati")
        }

        TextButton(
            onClick = onNavigateToLogin,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Hai già un account? Accedi")
        }
    }
}

@Preview(showBackground = true, name = "Login")
@Composable
private fun AuthScreenLoginPreview() {
    Reading_StatsTheme {
        Surface {
            AuthScreen()
        }
    }
}

@Preview(showBackground = true, name = "Registrazione")
@Composable
private fun AuthScreenRegistrationPreview() {
    Reading_StatsTheme {
        Surface {
            AuthScreen(initialMode = AuthMode.Register)
        }
    }
}
