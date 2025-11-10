plugins {
    alias(libs.plugins.rijksmuseum.kotlinMultiplatform)
    alias(libs.plugins.rijksmuseum.composeMultiplatform)
    alias(libs.plugins.rijksmuseum.featureMultiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.common)
            implementation(projects.core.navigation)

            implementation(projects.core.model)
            api(projects.core.domain)
            implementation(projects.core.designsystem)

            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(libs.coil.compose)

            implementation(libs.bundles.circuit)
        }
    }
}