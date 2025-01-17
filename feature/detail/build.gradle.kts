plugins {
    alias(libs.plugins.rijksmuseum.kotlinMultiplatform)
    alias(libs.plugins.rijksmuseum.composeMultiplatform)
}


kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.common)
            implementation(projects.core.model)
            implementation(projects.core.permissions)
            implementation(projects.core.domain)
            implementation(projects.core.permissions)
            implementation(projects.core.designsystem)

            implementation(libs.koin.compose)
            implementation(libs.koin.composeVM)

            implementation(libs.navigation.compose)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(libs.filekit.core)

            implementation(libs.coil.compose)

            implementation(libs.bundles.circuit)
        }
    }
}