package com.tewelde.rijksmuseum

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

internal fun Project.configureKotlinMultiplatform(
    extension: KotlinMultiplatformExtension
) = extension.apply {
    jvmToolchain(17)

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    jvm("desktop")

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs { browser() }

    listOf(iosArm64(), iosSimulatorArm64())

    applyDefaultHierarchyTemplate()

    sourceSets.apply {
        commonMain {
            dependencies {
                implementation(libs.findLibrary("kotlinx.coroutines.core").get())
                api(libs.findLibrary("koin.core").get())
                implementation(libs.findLibrary("kermit").get())
            }

            androidMain {
                dependencies {
                    implementation(libs.findLibrary("koin.android").get())
                    implementation(libs.findLibrary("kotlinx.coroutines.android").get())
                }

                jvmMain.dependencies {
                    implementation(libs.findLibrary("kotlinx.coroutines.swing").get())
                }
            }
        }
    }
}