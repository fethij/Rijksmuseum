package com.tewelde.rijksmuseum

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import org.gradle.api.Project

/**
 * AGP 9 / KMP library plugin config. Replaces the old top-level `android {}` block.
 *
 * Namespace is derived from the module path: e.g. `:core:network` → `com.tewelde.rijksmuseum.core.network`.
 * `androidResources` is enabled because Compose Multiplatform resources are routed through it.
 */
internal fun KotlinMultiplatformAndroidLibraryExtension.configureKotlinMultiplatformAndroid(
    project: Project,
) {
    val moduleName = project.path.split(":").drop(2).joinToString(".")
    namespace = if (moduleName.isNotEmpty()) {
        "com.tewelde.rijksmuseum.$moduleName"
    } else {
        "com.tewelde.rijksmuseum"
    }
    compileSdk = project.libs.findVersion("android.compileSdk").get().requiredVersion.toInt()
    minSdk = project.libs.findVersion("android.minSdk").get().requiredVersion.toInt()

    androidResources { enable = true }
}
