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
import me.tatarka.inject.annotations.Provides
import org.koin.core.qualifier.named
import org.koin.dsl.module
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

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
    fun provideHttpClient(json: Json): HttpClient = HttpClient {
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

    // TODO named
//    @Provides
//    fun provideHttpClient(): HttpClient
}

val networkModule = module {
//    single<RijksMuseumNetworkDataSource> {
//        KtorRijksMuseumNetwork(
//            rijksmuseumClient = get(named(BuildConfig.APP_NAME)),
//            client = get()
//        )
//    }

//    single {
//        Json {
//            ignoreUnknownKeys = true
//            isLenient = true
//            explicitNulls = false
//            prettyPrint = true
//        }
//    }

//    single(named(BuildConfig.APP_NAME)) {
//        HttpClient {
//            install(ContentNegotiation) {
//                json(get())
//            }
//            install(HttpCache)
//            install(Logging) {
//                level = LogLevel.ALL
//            }
//            install(DefaultRequest) {
//                url {
//                    protocol = URLProtocol.HTTPS
//                    host = RIJKSMUSEUM_HOST
//                    path(RIJKSMUSEUM_PATH)
//                    parameters.append(KEY, BuildConfig.RIJKSMUSEUM_API_KEY)
//                    parameters.append(HAS_IMAGE, true.toString())
//                }
//                header(HttpHeaders.ContentType, ContentType.Application.Json)
//            }
//        }
//    }

//    single {
//        HttpClient()
//    }
}