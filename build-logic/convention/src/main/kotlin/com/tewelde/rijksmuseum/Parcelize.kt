package com.tewelde.rijksmuseum

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget

/**
 * Tells the Kotlin Parcelize plugin to treat `com.tewelde.rijksmuseum.core.common.Parcelize`
 * as a parcelize annotation. Under AGP 9 the android target is created by
 * `com.android.kotlin.multiplatform.library`, not by `androidTarget {}`, so we have to reach
 * it through the targets API and tack the compiler arg onto its compilations.
 */
fun KotlinMultiplatformExtension.configureParcelize() {
    targets.withType(KotlinTarget::class.java).configureEach {
        if (name == "android") {
            compilations.configureEach {
                compileTaskProvider.configure {
                    compilerOptions.freeCompilerArgs.addAll(
                        "-P",
                        "plugin:org.jetbrains.kotlin.parcelize:additionalAnnotation=" +
                            "com.tewelde.rijksmuseum.core.common.Parcelize",
                    )
                }
            }
        }
    }
}
