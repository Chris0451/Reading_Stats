// app/src/androidTest/java/com/project/reading_stats/AuthFlowTest.kt
package com.project.reading_stats

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.*
import org.junit.Rule
import org.junit.Test

class AuthFlowTest {

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @Test
    fun login_navigatesToHome() {
        // compila email/password
        rule.onNodeWithText("Email").performTextInput("test@example.com")
        rule.onNodeWithText("Password").performTextInput("123456")
        // clic Login
        rule.onNodeWithText("Login").performClick()
        // verifica che compaia la Home (placeholder)
        rule.onNodeWithText("Benvenuto!").assertExists()
    }

    @Test
    fun register_navigatesToHome() {
        // vai a Registrazione
        rule.onNodeWithText("Non hai un account? Registrati").performClick()

        // compila campi
        rule.onNodeWithText("Nome").performTextInput("Mario")
        rule.onNodeWithText("Cognome").performTextInput("Rossi")
        rule.onNodeWithText("Username").performTextInput("mrossi")
        rule.onNodeWithText("Email").performTextInput("mario@example.com")
        rule.onAllNodesWithText("Password")[0].performTextInput("123456") // primo campo password
        rule.onNodeWithText("Conferma password").performTextInput("123456")

        // clic Registrati
        rule.onNodeWithText("Registrati").performClick()

        // verifica Home
        rule.onNodeWithText("Benvenuto!").assertExists()
    }
}
