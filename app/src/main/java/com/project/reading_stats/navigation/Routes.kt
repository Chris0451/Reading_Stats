package com.project.reading_stats.navigation

sealed class Routes(val route: String) {
    data object AuthGraph : Routes("auth_graph")
    data object Login : Routes("auth/login")
    data object Register : Routes("auth/register")
    data object Home : Routes("home")
}