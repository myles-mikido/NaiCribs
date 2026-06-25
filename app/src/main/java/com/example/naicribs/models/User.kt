package com.example.naicribs.models

data class User(
    val id: String = "",
    val fullName: String = "",
    val role: String = "",
    // Stores apartment IDs saved/favourited by this customer
    val favoriteIds: List<String> = emptyList(),
    val createdAt: com.google.firebase.Timestamp? = null
)