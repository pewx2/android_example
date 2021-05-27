package ru.example.sed.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorDTO(
    val error: String? = null,
    @Json(name = "error_description") val description: String? = null,
    val code: String? = null,
    val message: String? = null
) {
    override fun toString() = when {
        description != null -> description
        message != null -> message
        error != null -> error
        code != null -> code
        else -> throw IllegalStateException("")
    }
}