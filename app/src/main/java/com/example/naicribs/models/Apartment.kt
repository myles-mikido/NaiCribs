package com.example.naicribs.models

data class Apartment(
    val id: String = "",
    val agentId: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val location: String = "",

    val latitude: Double? = null,
    val longitude: Double? = null,

    val houseType: String = "",
    val contactNumber: String = "",
    val email: String = "",
    val imageUrls: List<String> = emptyList(),

    // Area tags added by the agent e.g. ["Near CBD", "Gated", "Westlands"]
    // These power customer search/filter without needing Algolia/full-text search
    val tags: List<String> = emptyList(),

    @get:com.google.firebase.firestore.Exclude
    val createdAt: com.google.firebase.Timestamp? = null
)