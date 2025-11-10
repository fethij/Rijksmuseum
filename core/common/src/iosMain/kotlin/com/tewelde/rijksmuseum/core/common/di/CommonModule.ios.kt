package com.tewelde.rijksmuseum.core.common.di

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

actual fun providePlatformIoDispatcher() = Dispatchers.IO