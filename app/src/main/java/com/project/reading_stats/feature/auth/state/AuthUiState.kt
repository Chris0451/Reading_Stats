package com.project.reading_stats.feature.auth.state

import com.project.reading_stats.core.domain.model.UserProfile

/*
* Data model for the state of the authentication process.
* */
data class AuthUiState(
    val loading: Boolean = false,
    val user: UserProfile? = null, /*If not null, the user is authenticated*/
    val error: String? = null /*message errors*/
)