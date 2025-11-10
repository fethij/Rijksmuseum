package com.tewelde.rijksmuseum.core.common.di

import kotlinx.coroutines.CoroutineDispatcher

expect fun provideIoDispatcher(): CoroutineDispatcher

enum class RijksmuseumDispatchers {
    IO, Default
}