plugins {
    alias(libs.plugins.rijksmuseum.kotlinMultiplatform)
    alias(libs.plugins.rijksmuseum.composeMultiplatform)
    alias(libs.plugins.kotlin.serialization)
}


kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.model)

            api(libs.kotlinx.serialization.json)
            api(compose.components.resources)

            implementation(libs.navigation.compose)

            implementation(libs.bundles.circuit)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.tewelde.rijksmuseum.resources"
    generateResClass = always
}