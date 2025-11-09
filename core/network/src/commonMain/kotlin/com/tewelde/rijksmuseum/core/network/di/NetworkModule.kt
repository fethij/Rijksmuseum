package com.tewelde.rijksmuseum.core.network.di

import com.tewelde.rijksmuseum.BuildConfig
import com.tewelde.rijksmuseum.core.network.di.qualifier.Named
import com.tewelde.rijksmuseum.core.network.di.qualifier.RijksmuseumClients
import com.tewelde.rijksmuseum.core.network.ktor.HAS_IMAGE
import com.tewelde.rijksmuseum.core.network.ktor.RIJKSMUSEUM_HOST
import com.tewelde.rijksmuseum.core.network.ktor.RIJKSMUSEUM_PATH
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

const val KEY = "key"


@ContributesTo(AppScope::class)
interface NetworkModule {

    @Provides
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        explicitNulls = false
        prettyPrint = true
    }

    @Provides
    @Named(RijksmuseumClients.AUTHORIZED)
    @SingleIn(AppScope::class)
    fun provideAuthorizedHttpClient(json: Json): HttpClient = HttpClient {
        install(ContentNegotiation) {
            json(json)
        }
        install(HttpCache)
        install(Logging) {
            level = LogLevel.ALL
        }
        install(DefaultRequest) {
            url {
                protocol = URLProtocol.HTTPS
                host = RIJKSMUSEUM_HOST
                path(RIJKSMUSEUM_PATH)
                parameters.append(KEY, BuildConfig.RIJKSMUSEUM_API_KEY)
                parameters.append(HAS_IMAGE, true.toString())
            }
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
    }

    @Provides
    @Named(RijksmuseumClients.UNAUTHORIZED)
    @SingleIn(AppScope::class)
    fun provideUnauthorizedHttpClient(): HttpClient = HttpClient {
//        install(HttpCache)
//        install(Logging) {
//            level = LogLevel.ALL
//        }
    }
}