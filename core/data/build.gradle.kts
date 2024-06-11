plugins {
    alias(libs.plugins.rijksmuseum.kotlinMultiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.common)
            implementation(projects.core.model)
            implementation(projects.core.network)
        }
    }
}
