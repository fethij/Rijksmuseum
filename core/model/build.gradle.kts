import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin{
    jvm()
    listOf(iosArm64(), iosSimulatorArm64())
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs { browser() }
}