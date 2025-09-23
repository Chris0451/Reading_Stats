// app/src/main/java/com/project/reading_stats/feature/auth/ui/AuthNavigation.kt
package com.project.reading_stats.feature.auth.ui

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.project.reading_stats.core.domain.model.UserProfile
import com.project.reading_stats.feature.auth.AuthViewModel
import com.project.reading_stats.navigation.Routes

fun NavGraphBuilder.authGraph(
    navController: NavController,
    onAuthenticated: () -> Unit
) {
    navigation(startDestination = Routes.Login.route, route = Routes.AuthGraph.route) {

        composable(Routes.Login.route) {
            val vm: AuthViewModel = hiltViewModel()
            val ui = vm.state.collectAsStateWithLifecycle()

            LoginScreen(
                onSubmit = { email, password -> vm.login(email, password) { onAuthenticated() } },
                onGoToRegister = { navController.navigate(Routes.Register.route) },
                loading = ui.value.loading,
                errorText = ui.value.error
            )
        }

        composable(Routes.Register.route) {
            val vm: AuthViewModel = hiltViewModel()
            val ui = vm.state.collectAsStateWithLifecycle()

            RegisterScreen(
                onSubmit = { profile: UserProfile, password: String, confirmPassword: String ->
                    vm.register(profile, password, confirmPassword) { onAuthenticated() }
                },
                onGoToLogin = { navController.navigate(Routes.Login.route) },
                loading = ui.value.loading,
                errorText = ui.value.error
            )
        }
    }
}
