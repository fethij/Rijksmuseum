plugins {
    alias(libs.plugins.rijksmuseum.kotlinMultiplatform)
    alias(libs.plugins.rijksmuseum.composeMultiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.common)
            implementation(libs.coil.compose)
            implementation(libs.zoomimage.compose.coil)
        }
    }
}