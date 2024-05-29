package com.tewelde.rijksmuseum.core.common.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.withOptions
import org.koin.core.qualifier.named
import org.koin.dsl.module

val commonModule = module {
    single { provideIoDispatcher() } withOptions {
        named(RijksmuseumDispatchers.IO)
    }

    single { provideDefaultDispatcher() } withOptions {
        named(RijksmuseumDispatchers.Default)
    }

    single {
        provideApplicationScope(
            get(named(RijksmuseumDispatchers.Default))
        )
    }

}

expect fun provideIoDispatcher(): CoroutineDispatcher
fun provideDefaultDispatcher() = Dispatchers.Default
fun provideApplicationScope(dispatcher: CoroutineDispatcher): CoroutineScope =
    CoroutineScope(SupervisorJob() + dispatcher)


enum class RijksmuseumDispatchers {
    IO, Default
}