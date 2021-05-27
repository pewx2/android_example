package ru.example.sed.core

import okhttp3.Interceptor
import okhttp3.Response
import ru.example.sed.service.CacheService
import java.net.URI

class ServerSelectionInterceptor(
    private val cacheService: CacheService
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        val server: URI = URI.create(cacheService.getActiveServer())

        val newUrlBuilder = request.url
            .newBuilder()
            .host(server.host)
            .scheme(server.scheme)

        if (server.port != -1) {
            newUrlBuilder.port(server.port)
        }

        request = request.newBuilder()
            .url(newUrlBuilder.build())
            .build()

        return chain.proceed(request)
    }
}