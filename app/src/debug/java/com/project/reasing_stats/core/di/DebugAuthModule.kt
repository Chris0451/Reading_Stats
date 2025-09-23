// app/src/debug/java/com/project/reading_stats/core/di/DebugAuthModule.kt
package com.project.reading_stats.core.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.reading_stats.core.domain.model.UserProfile
import com.project.reading_stats.core.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Questo modulo sostituisce quello reale SOLO in debug.
@Module
@InstallIn(SingletonComponent::class)
object DebugAuthModule {

    @Provides @Singleton
    fun provideFakeAuth(): FirebaseAuth = FirebaseAuth.getInstance() // non usato dal fake, ma richiesto

    @Provides @Singleton
    fun provideFakeDb(): FirebaseFirestore = FirebaseFirestore.getInstance() // idem

    @Provides @Singleton
    fun provideAuthRepository(): AuthRepository = object : AuthRepository(
        auth = FirebaseAuth.getInstance(),
        db = FirebaseFirestore.getInstance()
    ) {
        override suspend fun login(email: String, password: String) =
            Result.success(UserProfile(uid = "uid123", name = "Mario", surname = "Rossi", username = "mrossi", email = email))

        override suspend fun register(profile: UserProfile, password: String) =
            Result.success(profile.copy(uid = "uid_new"))

        override suspend fun isEmailTaken(email: String) = false
        override suspend fun isUsernameTaken(username: String) = false
    }
}
