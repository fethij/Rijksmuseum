package com.tewelde.rijksmuseum.feature.detail.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import com.tewelde.rijksmuseum.feature.detail.FileUtil

actual val platformModule: Module = module {
    factoryOf(::FileUtil)
}