package com.tewelde.rijksmuseum

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

fun KotlinMultiplatformExtension.configureParcelize() {
    androidTarget {
        compilerOptions {
            freeCompilerArgs.addAll(
                "-P",
                "plugin:org.jetbrains.kotlin.parcelize:additionalAnnotation=com.tewelde.rijksmuseum.core.common.Parcelize",
            )
        }
    }
}
