plugins {
    alias(libs.plugins.rijksmuseum.kotlinMultiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.common)
            implementation(projects.core.model)
            api(projects.core.network)
        }
    }
}