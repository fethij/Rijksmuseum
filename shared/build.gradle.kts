plugins {
    alias(libs.plugins.rijksmuseum.kotlinMultiplatform)
    alias(libs.plugins.rijksmuseum.composeMultiplatform)
    alias(libs.plugins.rijksmuseum.featureMultiplatform)
}

kotlin {
    listOf(iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            optimized = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            // api() so platform app modules (androidApp/desktopApp/webApp) see all the
            // @CircuitInject / @ContributesBinding classes when their KSP processors
            // generate the merged DI graph.
            api(projects.feature.arts)
            api(projects.feature.detail)
            api(projects.core.navigation)
            api(projects.core.permissions)

            api(compose.material3)
            api(compose.components.resources)
            api(compose.components.uiToolingPreview)

            api(libs.ktor.client.core)
            api(libs.coil.compose)
            api(libs.coil.network.ktor)

            api(libs.filekit.core)

            api(libs.bundles.circuit)
            api(libs.bundles.kotlinInjectAnvil)
        }

        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }

        jvmMain.dependencies {
            implementation(libs.ktor.client.java)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}
