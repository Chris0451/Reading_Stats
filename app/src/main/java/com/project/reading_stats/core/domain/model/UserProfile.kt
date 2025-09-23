package com.project.reading_stats.core.domain.model

import com.google.firebase.Timestamp

/*
    Data model for the user profile.
 */
data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val surname: String = "",
    val username: String = "",
    val email: String = "",
    val createdAt: Timestamp = Timestamp.Companion.now()
)