package com.tewelde.rijksmuseum

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Base KMP config shared by every kotlin-multiplatform module in this project.
 *
 * Notably does NOT call `androidTarget {}`. Under AGP 9, the
 * `com.android.kotlin.multiplatform.library` plugin auto-registers the android
 * target when its DSL is configured — see [configureKotlinMultiplatformAndroid].
 */
@OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
internal fun Project.configureKotlinMultiplatformBase(
    extension: KotlinMultiplatformExtension,
) = extension.apply {
    jvmToolchain(17)

    jvm()

    wasmJs {
        browser()
        compilerOptions {
            freeCompilerArgs.add("-Xwasm-kclass-fqn")
        }
    }

    listOf(iosArm64(), iosSimulatorArm64())

    applyDefaultHierarchyTemplate()

    sourceSets.apply {
        commonMain {
            dependencies {
                implementation(libs.findLibrary("kotlinx.coroutines.core").get())
                implementation(libs.findLibrary("kermit").get())
                implementation(libs.findBundle("kotlinInjectAnvil").get())
            }
        }
        androidMain {
            dependencies {
                implementation(libs.findLibrary("kotlinx.coroutines.android").get())
            }
        }
        jvmMain {
            dependencies {
                implementation(libs.findLibrary("kotlinx.coroutines.swing").get())
            }
        }
    }
}
