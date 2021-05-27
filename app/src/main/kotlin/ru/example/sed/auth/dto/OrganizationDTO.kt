package ru.example.sed.auth.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OrganizationDTO(
    val id: String,
    val name: String
)