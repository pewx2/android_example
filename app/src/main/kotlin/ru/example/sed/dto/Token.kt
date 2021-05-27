package ru.example.sed.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

typealias Token = String

fun Token.toBearer() = "Bearer $this"

@JsonClass(generateAdapter = true)
data class TokensDTO(
    @Json(name = "access_token") val accessToken: Token,
    @Json(name = "refresh_token") val refreshToken: Token
)