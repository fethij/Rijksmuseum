plugins {
    alias(libs.plugins.rijksmuseum.kotlinMultiplatform)
    alias(libs.plugins.rijksmuseum.composeMultiplatform)
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.tewelde.rijksmuseum.resources"
    generateResClass = always
}
