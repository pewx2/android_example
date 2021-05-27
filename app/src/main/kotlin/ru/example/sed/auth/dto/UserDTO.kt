package ru.example.sed.auth.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class UserDTO(
    @Json(name = "userId") val id: String,
    @Json(name = "userFirstName") val firstName: String,
    @Json(name = "userMiddleName") val middleName: String?,
    @Json(name = "userLastName") val lastName: String,
    val postName: String,
    val defaultEmployee: DefaultEmployee?
) {
    val fullName: String = if (middleName != null) {
        "$lastName ${firstName.toUpperCase(Locale.ROOT).first()}. ${
            middleName.toUpperCase(Locale.ROOT).first()
        }.".trim()
    } else {
        "$lastName ${firstName.toUpperCase(Locale.ROOT).first()}.".trim()
    }

    val organizationId: String?
        get() = defaultEmployee?.organization?.id

    @JsonClass(generateAdapter = true)
    data class DefaultEmployee(
        val id: String,
        val organization: OrganizationDTO
    )
}