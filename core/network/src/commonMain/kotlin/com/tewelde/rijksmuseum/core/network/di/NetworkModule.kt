package com.tewelde.rijksmuseum.core.network.di

import com.tewelde.rijksmuseum.BuildConfig
import com.tewelde.rijksmuseum.core.network.RijksMuseumNetworkDataSource
import com.tewelde.rijksmuseum.core.network.ktor.HAS_IMAGE
import com.tewelde.rijksmuseum.core.network.ktor.KtorRijksMuseumNetwork
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
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

const val KEY = "key"

val networkModule = module {
    singleOf(::KtorRijksMuseumNetwork) { bind<RijksMuseumNetworkDataSource>() }
    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            explicitNulls = false
        }
    }
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(get())
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
    }
}