plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.buildConfig) apply false
    alias(libs.plugins.modulegraph)
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.metro)
}

moduleGraphConfig {
    readmePath.set("./README.md")
    heading = "### Module Graph"
    showFullPath = true
}

metro {
    // Defines whether or not metro is enabled. Useful if you want to gate this behind a dynamic
    // build configuration.
    enabled = true // Default

    // Enable (extremely) verbose debug logging
    debug = false // Default
}