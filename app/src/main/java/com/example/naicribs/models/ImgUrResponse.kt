package com.example.naicribs.models

data class ImgurResponse(
    val data: Data,
    val success: Boolean,
    val status: Int
) {
    data class Data(
        val id: String,
        val link: String
    )
}
