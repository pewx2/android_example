package ru.example.sed.auth

enum class AuthorizationType(var value: String? = null) {
    NONE,
    PINCODE,
    FINGERPRINT
}