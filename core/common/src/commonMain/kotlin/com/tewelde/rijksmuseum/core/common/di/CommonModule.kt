package com.tewelde.rijksmuseum.core.common.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.qualifier.named
import org.koin.dsl.module

val commonModule = module {
    single(named(RijksmuseumDispatchers.IO)) { provideIoDispatcher() }
    single(named(RijksmuseumDispatchers.Default)) { Dispatchers.Default }
    single<CoroutineScope> {
        provideApplicationScope(get(named(RijksmuseumDispatchers.Default)))
    }
}

expect fun provideIoDispatcher(): CoroutineDispatcher
fun provideApplicationScope(dispatcher: CoroutineDispatcher): CoroutineScope =
    CoroutineScope(SupervisorJob() + dispatcher)


enum class RijksmuseumDispatchers {
    IO, Default
}