package com.example.naicribs.data

import com.example.naicribs.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db   = FirebaseFirestore.getInstance()

    suspend fun login(email: String, password: String): Result<Unit> = runCatching {
        auth.signInWithEmailAndPassword(email, password).await()
        Unit
    }

    suspend fun register(
        email: String, password: String, fullName: String, role: String
    ): Result<Unit> = runCatching {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val userId = result.user?.uid ?: error("No user ID after registration")
        db.collection("profiles").document(userId)
            .set(User(id = userId, fullName = fullName, role = role)).await()
        Unit
    }

    suspend fun logout(): Result<Unit> = runCatching { auth.signOut() }

    suspend fun resetPassword(email: String): Result<Unit> = runCatching {
        auth.sendPasswordResetEmail(email).await()
    }

    fun getCurrentUserId(): String? = auth.currentUser?.uid

    suspend fun getCurrentUserProfile(): User? {
        val id = getCurrentUserId() ?: return null
        return runCatching {
            db.collection("profiles").document(id).get().await()
                .toObject(User::class.java)
        }.getOrNull()
    }

    suspend fun updateUserRole(role: String): Result<Unit> = runCatching {
        val id = getCurrentUserId() ?: error("Not logged in")
        db.collection("profiles").document(id).update("role", role).await()
        Unit
    }

    // ── Saved / favourite listings ────────────────────────────────────────────
    suspend fun toggleFavorite(apartmentId: String): Result<Boolean> = runCatching {
        val id = getCurrentUserId() ?: error("Not logged in")
        val doc  = db.collection("profiles").document(id).get().await()
        val user = doc.toObject(User::class.java) ?: error("Profile not found")
        val isSaved = apartmentId in user.favoriteIds
        if (isSaved) {
            db.collection("profiles").document(id)
                .update("favoriteIds", FieldValue.arrayRemove(apartmentId)).await()
        } else {
            db.collection("profiles").document(id)
                .update("favoriteIds", FieldValue.arrayUnion(apartmentId)).await()
        }
        !isSaved   // returns true if it's NOW saved, false if removed
    }

    suspend fun getFavoriteIds(): List<String> {
        val id = getCurrentUserId() ?: return emptyList()
        return runCatching {
            db.collection("profiles").document(id).get().await()
                .toObject(User::class.java)?.favoriteIds ?: emptyList()
        }.getOrDefault(emptyList())
    }

    fun isLoggedIn(): Boolean = auth.currentUser != null
}