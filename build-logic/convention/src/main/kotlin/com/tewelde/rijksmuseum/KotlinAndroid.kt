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
    // Build a unique per-module namespace from the gradle path: ":core:network" →
    // "com.tewelde.rijksmuseum.core.network", ":shared" → "com.tewelde.rijksmuseum.shared".
    // AGP 9 enforces android.uniquePackageNames=true so the old fallback to the base
    // namespace would clash with androidApp's `com.tewelde.rijksmuseum`.
    val moduleName = project.path.split(":").filter { it.isNotEmpty() }.joinToString(".")
    namespace = "com.tewelde.rijksmuseum.$moduleName"
    compileSdk = project.libs.findVersion("android.compileSdk").get().requiredVersion.toInt()
    minSdk = project.libs.findVersion("android.minSdk").get().requiredVersion.toInt()

    androidResources { enable = true }
}
