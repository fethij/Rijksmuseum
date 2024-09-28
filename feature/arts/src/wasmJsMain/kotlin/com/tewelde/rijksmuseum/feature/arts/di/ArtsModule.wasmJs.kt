package com.tewelde.rijksmuseum.feature.arts.di

import FileUtil
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

actual val platformModule: Module = module {
    factoryOf(::FileUtil)
}