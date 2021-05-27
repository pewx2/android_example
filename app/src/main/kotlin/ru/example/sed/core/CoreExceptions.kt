package ru.example.sed.core

import retrofit2.Response
import ru.example.sed.dto.ErrorDTO

class AccessTokenNotFoundException : Exception()
class RefreshTokenNotFoundException : Exception()
class CredentialsNotFoundException : Exception()
class ExpiredPasswordException : Exception()

class SedHttpException(
    response: Response<*>?,
    coreUtils: CoreUtils
) : Exception() {
    var errorDTO: ErrorDTO? = coreUtils.parseResponseErrorBody(response)

    override fun toString(): String {
        return try {
            errorDTO?.toString() ?: "Ошибка"
        } catch (e: IllegalStateException) {
            "Ошибка"
        }
    }

    fun isAuthError() = errorDTO?.code in setOf("99999", "401", "500")
}

