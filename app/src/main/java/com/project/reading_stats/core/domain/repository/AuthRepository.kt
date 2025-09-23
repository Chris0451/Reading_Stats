package com.project.reading_stats.core.domain.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.reading_stats.core.domain.model.UserProfile
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/*
    Firebase communication:
    - to register and login users
    - to save their profiles in the database
    - to retrieve them from the database using the UID
    - to sign them out
    - to check if the user is logged in
 */
@Singleton
open class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) {

    open suspend fun login(email: String, password: String): Result<UserProfile> {
        return try {
            val res = auth.signInWithEmailAndPassword(email, password).await()
            val uid = res.user?.uid ?: throw IllegalStateException("UID nullo dopo login")
            val doc = db.collection("users").document(uid).get().await()
            val user = doc.toObject(UserProfile::class.java)
                ?: throw IllegalStateException("Profilo utente non trovato")
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    open suspend fun register(profile: UserProfile, password: String): Result<UserProfile> {
        return try {
            val res = auth.createUserWithEmailAndPassword(profile.email, password).await()
            val uid = res.user?.uid ?: throw IllegalStateException("UID nullo dopo register")

            val toSave = profile.copy(uid = uid)
            db.collection("users").document(uid).set(toSave).await()
            Result.success(toSave)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    open suspend fun isEmailTaken(email: String): Boolean {
        val q = db.collection("users").whereEqualTo("email", email).limit(1).get().await()
        return !q.isEmpty
    }

    open suspend fun isUsernameTaken(username: String): Boolean {
        val q = db.collection("users").whereEqualTo("username", username).limit(1).get().await()
        return !q.isEmpty
    }

    fun logout() = auth.signOut()
    fun currentUserId(): String? = auth.currentUser?.uid

    suspend fun getUser(uid: String): UserProfile? {
        val doc = db.collection("users").document(uid).get().await()
        return doc.toObject(UserProfile::class.java)
    }
}