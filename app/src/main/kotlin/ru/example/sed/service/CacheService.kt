package ru.example.sed.service

import android.content.Context
import ru.example.sed.BuildConfig
import ru.example.sed.auth.AuthorizationType
import ru.example.sed.core.AccessTokenNotFoundException
import ru.example.sed.core.CredentialsNotFoundException
import ru.example.sed.core.RefreshTokenNotFoundException
import ru.example.sed.dto.Token
import ru.example.sed.dto.TokensDTO

class CacheService(private val context: Context) {
    companion object {
        private const val TOKENS = "TOKENS"
        private const val ACCESS_TOKEN = "ACCESS_TOKEN"
        private const val REFRESH_TOKEN = "REFRESH_TOKEN"

        private const val SERVERS = "SERVERS"
        private const val ACTIVE_SERVER = "ACTIVE_SERVER"

        private const val CREDENTIALS = "CREDENTIALS"
        private const val USERNAME = "USERNAME"
        private const val PASSWORD = "PASSWORD"

        private const val AUTHORIZATION_TYPE = "AUTHORIZATION_TYPE"
        private const val AUTHORIZATION_VALUE = "AUTHORIZATION_VALUE"
    }

    private val defaultServers = setOf(BuildConfig.SED_API_SERVER)

    fun setAuthorizationType(type: AuthorizationType) {
        val sharedPref = context.getSharedPreferences(AUTHORIZATION_TYPE, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(AUTHORIZATION_TYPE, type.name)
            type.value?.let { putString(AUTHORIZATION_VALUE, it) }

            commit()
        }
    }

    fun getAuthorizationType(): AuthorizationType {
        val sharedPref = context.getSharedPreferences(AUTHORIZATION_TYPE, Context.MODE_PRIVATE)
        return sharedPref?.let {
            val authorizationValue = it.getString(AUTHORIZATION_VALUE, null)
            val authorizationType = it.getString(AUTHORIZATION_TYPE, AuthorizationType.NONE.name)!!

            AuthorizationType.valueOf(authorizationType).apply { value = authorizationValue }
        } ?: AuthorizationType.NONE
    }

    fun clearAuthorizationType() {
        val sharedPref = context.getSharedPreferences(AUTHORIZATION_TYPE, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear()
            commit()
        }
    }

    fun saveCredentials(username: String, password: String) {
        val sharedPref = context.getSharedPreferences(CREDENTIALS, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(USERNAME, username)
            putString(PASSWORD, password)
            commit()
        }
    }

    fun getCredentials(): Pair<String, String> {
        val sharedPref = context.getSharedPreferences(CREDENTIALS, Context.MODE_PRIVATE)

        val username = sharedPref.getString(USERNAME, null)
        val password = sharedPref.getString(PASSWORD, null)

        if (username != null && password != null) {
            return Pair(username, password)
        }

        throw CredentialsNotFoundException()
    }

    fun clearCredentials() {
        val sharedPref = context.getSharedPreferences(CREDENTIALS, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear()
            commit()
        }
    }

    fun saveTokens(dto: TokensDTO) {
        val sharedPref = context.getSharedPreferences(TOKENS, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(ACCESS_TOKEN, dto.accessToken)
            putString(REFRESH_TOKEN, dto.refreshToken)
            commit()
        }
    }

    fun getAccessToken(): Token {
        return context
            .getSharedPreferences(TOKENS, Context.MODE_PRIVATE)
            .getString(ACCESS_TOKEN, null) ?: throw AccessTokenNotFoundException()
    }

    fun getRefreshToken(): Token {
        return context
            .getSharedPreferences(TOKENS, Context.MODE_PRIVATE)
            .getString(REFRESH_TOKEN, null) ?: throw RefreshTokenNotFoundException()
    }

    fun clearTokens() {
        val sharedPref = context.getSharedPreferences(TOKENS, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear()
            commit()
        }
    }

    fun addServer(host: String) {
        val sharedPref = context.getSharedPreferences(SERVERS, Context.MODE_PRIVATE)
        val servers = sharedPref
            .getStringSet(SERVERS, defaultServers)
            ?.toMutableSet() ?: defaultServers.toMutableSet()

        with(sharedPref.edit()) {
            servers.add(host)
            putStringSet(SERVERS, servers)
            commit()
        }
    }

    fun getServers(): Set<String> {
        return context
            .getSharedPreferences(SERVERS, Context.MODE_PRIVATE)
            .getStringSet(SERVERS, defaultServers)!!
    }

    fun setActiveServer(server: String) {
        val sharedPref = context.getSharedPreferences(SERVERS, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(ACTIVE_SERVER, server)
            commit()
        }
    }

    fun getActiveServer(): String {
        return context
            .getSharedPreferences(SERVERS, Context.MODE_PRIVATE)
            .getString(ACTIVE_SERVER, BuildConfig.SED_API_SERVER)!!
    }

    fun wipe() {
        clearAuthorizationType()
        clearCredentials()
        clearTokens()
    }
}