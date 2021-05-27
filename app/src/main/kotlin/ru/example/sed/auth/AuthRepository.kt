package ru.example.sed.auth

import retrofit2.HttpException
import ru.example.sed.auth.dto.ChangePasswordDTO
import ru.example.sed.auth.dto.UserDTO
import ru.example.sed.core.CoreUtils
import ru.example.sed.core.SedHttpException
import ru.example.sed.dto.Token
import ru.example.sed.dto.TokensDTO
import ru.example.sed.dto.toBearer
import ru.example.sed.service.CacheService

class AuthRepository(
    private val authApi: AuthApi,
    private val cacheService: CacheService,
    private val coreUtils: CoreUtils
) {
    private var currentUserDTO: UserDTO? = null

    /**
     * Авторизация пользователя по записанным данным
     *
     * Если записанных данных нет, то бросается CredentialsNotFoundException
     */
    suspend fun authorize(): AuthorizationType {
        val (username, password) = cacheService.getCredentials()
        signIn(username, password)
        return cacheService.getAuthorizationType()
    }

    /**
     * Авторизация пользователя по логину/паролю.
     *
     * Если авторизация прошла успешно, то сохраняются данные для входа и пара токенов .
     *
     * @param username имя пользователя
     * @param password пароль пользователя
     */
    suspend fun signIn(username: String, password: String) {
        try {
            val tokens = authApi.fetchToken(
                username = username,
                password = password,
                usernameField = username,
                passwordField = password
            )
            cacheService.saveCredentials(username, password)
            cacheService.saveTokens(tokens)
        } catch (e: HttpException) {
            throw coreUtils.parseError(e)
        }
    }

    /**
     * Обновление существующих токенов.
     *
     * Если обновление прошло успешно, то сохраняется пара новых токенов
     */
    private suspend fun refreshTokens(): TokensDTO {
        try {
            val refreshToken = cacheService.getRefreshToken()
            val newTokens = authApi.refreshToken(refreshToken)
            cacheService.saveTokens(newTokens)
            return newTokens
        } catch (e: HttpException) {
            throw coreUtils.parseError(e)
        }
    }

    /**
     * Сменить пароль пользователя
     *
     * @param dto данные для изменения пароля
     */
    suspend fun changeUserPassword(dto: ChangePasswordDTO): AuthorizationType {
        try {
            authApi.changeUserPassword(login = dto.login, dto = dto)
            cacheService.saveCredentials(dto.login, dto.newPassword)
            return authorize()
        } catch (e: HttpException) {
            throw coreUtils.parseError(e)
        }
    }

    /**
     * Получить информацию о текущем пользователе
     */
    suspend fun whoAmI(useCache: Boolean = false): UserDTO {
        if (useCache) {
            return currentUserDTO ?: makeAuthorizeRequest {
                val result = authApi.whoami(it)
                currentUserDTO = result
                result
            }
        }

        return makeAuthorizeRequest {
            val result = authApi.whoami(it)
            currentUserDTO = result
            result
        }
    }

    /**
     * Выполнить авторизованный запрос.
     *
     * Если произошла ошибка авторизация (например, невалидный токен), то
     * выполняется обновление токена и попытка выполнить запрос ещё один раз.
     *
     * @param request запрос к апи
     */
    suspend fun <T> makeAuthorizeRequest(request: suspend (Token) -> T): T {
        return try {
            val accessToken = cacheService.getAccessToken()
            request.invoke(accessToken.toBearer())
        } catch (e: HttpException) {
            val error = coreUtils.parseError(e)

            if (error is SedHttpException && error.isAuthError()) {
                val tokens = refreshTokens()
                request.invoke(tokens.accessToken.toBearer())
            } else {
                throw error
            }
        }
    }
}