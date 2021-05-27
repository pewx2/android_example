package ru.example.sed.auth

import retrofit2.http.*
import ru.example.sed.auth.dto.ChangePasswordDTO
import ru.example.sed.auth.dto.UserDTO
import ru.example.sed.dto.TokensDTO

/**
 * API аутентификации
 */
interface AuthApi {
    /**
     * Получить access и refresh токены по логину/паролю.
     *
     * @param username логин пользователя
     * @param password пароль пользователя
     * @param grantType тип аутентификации, по-умолчанию password
     *
     * @return access и refresh токены
     */
    @POST("api/oauth/token")
    @FormUrlEncoded
    suspend fun fetchToken(
        @Query("username") username: String,
        @Query("password") password: String,
        @Field("username") usernameField: String,
        @Field("password") passwordField: String,
        @Query("grant_type") grantType: String = "password",
        @Field("grant_type") grantTypeField: String = "password"
    ): TokensDTO

    /**
     * Обновить access_token по refresh_token
     *
     * @param refreshToken
     * @param grantType тип аутентификации, по-умолчанию refresh_token
     *
     * @return access (всегда новый) и refresh (возможно тот же) токены
     */
    @POST("api/oauth/token")
    @FormUrlEncoded
    suspend fun refreshToken(
        @Query("refresh_token") refreshToken: String,
        @Query("grant_type") grantType: String = "refresh_token",
        @Field("grant_type") grantTypeField: String = "refresh_token",
        @Field("refresh_token") refreshTokenField: String = "refresh_token"
    ): TokensDTO

    /**
     * Получить dto владельца токена
     *
     * @param bearerToken токен пользователя вида "Bearer <token>"
     *
     * @return dto владельца токена
     */
    @GET("api/whoami")
    suspend fun whoami(@Header("Authorization") bearerToken: String): UserDTO

    /**
     * Обновить пароль пользователя
     *
     * @param login логи пользователя
     * @param dto данные для смены пароля
     */
    @POST("api/users/{login}/change_password")
    suspend fun changeUserPassword(
        @Path("login") login: String,
        @Body dto: ChangePasswordDTO
    )
}