package com.tewelde.rijksmuseum.core.common.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

expect fun provideIoDispatcher(): CoroutineDispatcher

enum class RijksmuseumDispatchers {
    IO, Default
}