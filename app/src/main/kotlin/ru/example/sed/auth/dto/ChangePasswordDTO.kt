package ru.example.sed.auth.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChangePasswordDTO(
    val login: String,
    val newPassword: String,
    val oldPassword: String
)