package com.project.reading_stats.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.project.reading_stats.feature.auth.ui.authGraph
import androidx.compose.material3.Text

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.AuthGraph.route,
        modifier = modifier
    ) {
        authGraph(
            navController = navController,
            onAuthenticated = {
                navController.navigate(Routes.Home.route) {
                    popUpTo(Routes.AuthGraph.route) { inclusive = true }
                    launchSingleTop = true
                }
            }
        )

        // Placeholder Home (stai pure libero di sostituirlo con la tua schermata reale)
        composable(Routes.Home.route) {
            Text("Benvenuto! Autenticazione riuscita.")
        }
    }
}