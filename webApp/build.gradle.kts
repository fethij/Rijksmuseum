import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
}

kotlin {
    jvmToolchain(libs.versions.jvmTarget.get().toInt())

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        wasmJsMain.dependencies {
            implementation(projects.shared)
            implementation(projects.core.common)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(libs.bundles.kotlinInjectAnvil)
            implementation(libs.bundles.circuit)
        }
    }
}

dependencies {
    add("kspWasmJs", libs.kotlinInject.compiler)
    add("kspWasmJs", libs.kotlinInject.anvil.compiler)
}
