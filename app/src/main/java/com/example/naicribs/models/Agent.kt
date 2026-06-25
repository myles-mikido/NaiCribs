package com.example.naicribs.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Agent(
    val id: String = "",
    @SerialName("full_name") val fullName: String = "",
    val email: String = "",
    @SerialName("created_at") val createdAt: String = ""
)