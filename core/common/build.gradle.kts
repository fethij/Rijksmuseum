plugins {
    alias(libs.plugins.rijksmuseum.kotlinMultiplatform)
    alias(libs.plugins.rijksmuseum.composeMultiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}


kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.kotlinx.serialization.json)
            api(compose.components.resources)

            implementation(libs.koin.compose)
            implementation(libs.koin.composeVM)

            implementation(libs.navigation.compose)

            implementation(libs.bundles.kotlinInjectAnvil)
            implementation(libs.bundles.circuit)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.tewelde.rijksmuseum.resources"
    generateResClass = always
}


dependencies {
    ksp(libs.kotlinInject.compiler)
    ksp(libs.kotlinInject.anvil.compiler)
}