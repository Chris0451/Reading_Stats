package com.project.reading_stats.data.model

/**
 * Modello di dominio per rappresentare un utente salvato su Firebase/Firestore.
 *
 * La presenza di valori di default consente la deserializzazione automatica da parte
 * di Firebase che richiede un costruttore vuoto.
 */
data class FirebaseUser(
    val uid: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val username: String = "",
    val email: String = "",
    val rememberMe: Boolean = false,
    val photoUrl: String = "",
    val createdAt: Long = 0L
) {
    val fullName: String
        get() = listOf(firstName, lastName)
            .filter { it.isNotBlank() }
            .joinToString(separator = " ")

    fun toMap(): Map<String, Any?> = mapOf(
        FIELD_UID to uid,
        FIELD_FIRST_NAME to firstName,
        FIELD_LAST_NAME to lastName,
        FIELD_USERNAME to username,
        FIELD_EMAIL to email,
        FIELD_REMEMBER_ME to rememberMe,
        FIELD_PHOTO_URL to photoUrl,
        FIELD_CREATED_AT to createdAt
    )

    companion object {
        const val FIELD_UID = "uid"
        const val FIELD_FIRST_NAME = "firstName"
        const val FIELD_LAST_NAME = "lastName"
        const val FIELD_USERNAME = "username"
        const val FIELD_EMAIL = "email"
        const val FIELD_REMEMBER_ME = "rememberMe"
        const val FIELD_PHOTO_URL = "photoUrl"
        const val FIELD_CREATED_AT = "createdAt"

        fun fromMap(data: Map<String, Any?>): FirebaseUser = FirebaseUser(
            uid = data[FIELD_UID] as? String ?: "",
            firstName = data[FIELD_FIRST_NAME] as? String ?: "",
            lastName = data[FIELD_LAST_NAME] as? String ?: "",
            username = data[FIELD_USERNAME] as? String ?: "",
            email = data[FIELD_EMAIL] as? String ?: "",
            rememberMe = data[FIELD_REMEMBER_ME] as? Boolean ?: false,
            photoUrl = data[FIELD_PHOTO_URL] as? String ?: "",
            createdAt = (data[FIELD_CREATED_AT] as? Number)?.toLong() ?: 0L
        )
    }
}
