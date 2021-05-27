package ru.example.sed.core

import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import retrofit2.HttpException
import retrofit2.Response
import ru.example.sed.dto.ErrorDTO

class CoreUtils(private val moshi: Moshi) {
    companion object {
        const val EXPIRED_PASSWORD_ERROR = "Пароль пользователя просрочен."
    }

    fun parseResponseErrorBody(response: Response<*>?): ErrorDTO? {
        if (response != null) {
            val errorBody = response.errorBody()?.string()

            return if (errorBody != null && errorBody.isNotBlank()) {
                try {
                    moshi.adapter(ErrorDTO::class.java).fromJson(errorBody)
                } catch (e: JsonDataException) {
                    e.printStackTrace()
                    ErrorDTO(code = response.code().toString())
                }
            } else {
                ErrorDTO(code = response.code().toString())
            }
        } else {
            return ErrorDTO(message = "Сетевая ошибка")
        }
    }

    fun parseError(it: Throwable): Throwable {
        if (it is HttpException) {
            val exception = SedHttpException(it.response(), this)

            if (exception.errorDTO?.toString() == EXPIRED_PASSWORD_ERROR) {
                return ExpiredPasswordException()
            }

            return exception
        }

        return it
    }
}

suspend fun <A, B> Iterable<A>.pmap(f: suspend (A) -> B): List<B> = coroutineScope {
    map { async { f(it) } }.awaitAll()
}